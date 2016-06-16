package iv.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Main extends AppWidgetProvider {
    private static final int UNIVERSAL_DEGREE_DIF = 8;

    @Override
    public void onUpdate(Context context,
                         AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // start new thread to get weather data
        //need to get loc
        updateWeatherData(context, "VA", "Richmond");
        //
        final int N = appWidgetIds.length;
        // Perform this loop procedure for each App Widget
        // that belongs to this provider
        // In case there are multiple widgets
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
            remoteViews.setTextViewText(R.id.date, DateFetcher.getProperDate());

            Intent intent = new Intent(context, Main.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.weatherDesc, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    public void updateWeatherData(final Context context, final String state, final String city) {
        new Thread() {
            public void run() {
                final JSONObject today = RemoteFetch.getCurrentWeather(state, city);
                final JSONObject yesterday = RemoteFetch.getPastWeather(state, city);
                if (today == null || yesterday == null) {
                    Toast.makeText(context, "couldnt find that city", Toast.LENGTH_LONG).show();
                } else {
                    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
                    remoteViews.setTextViewText(R.id.weatherDesc, parseWeather(today, yesterday).toString());
                }
            }
        }.start();
    }

    public Weather parseWeather(JSONObject today, JSONObject yesterday) {
        int tempdif = 0;
        System.out.println(today.toString());
        System.out.println(yesterday.toString());
        try {
            JSONObject est = today.getJSONObject("estimated");
            int tempf = est.getInt("temp_f");
            //this one is tricky,
            // here we have gotten a list of weather updates
            // at different times through the day.
            JSONObject hist = yesterday.getJSONObject("history");
            int yestf = hist.getInt("tempi");
            tempdif = tempf - yestf;
        } catch (JSONException je) {
            //bad json file, frowny face
            je.printStackTrace();
        }
        //parse tempdif
        // negative means today is colder
        // pos means today is warmer
        Weather current = null;
        if (tempdif > UNIVERSAL_DEGREE_DIF) {
            current = Weather.HOT;
        } else if (tempdif > 0) {
            current = Weather.WARM;
        } else if (tempdif == 0) {
            current = Weather.SAME;
        } else if (tempdif < UNIVERSAL_DEGREE_DIF) {
            current = Weather.COLD;
        } else if (tempdif < 0) {
            current = Weather.CHILLY;
        }
        //
        return current;
    }

}
