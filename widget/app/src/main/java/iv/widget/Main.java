package iv.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class Main extends AppWidgetProvider {
    private final int UNIVERSAL_DEGREE_DIF = 8;
    private String weatherCond = "unavailable";

    @Override
    public void onUpdate(Context context,
                         AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // start new thread to get weather data
        //need to get loc
        updateWeatherData(context, "VA", "Richmond");
        final int N = appWidgetIds.length;
        // Perform this loop procedure for each App Widget
        // that belongs to this provider
        // In case there are multiple widgets
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
            remoteViews.setTextViewText(R.id.date, DateFetcher.getProperDate());
            remoteViews.setTextViewText(R.id.weatherDesc, weatherCond);

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
                RemoteFetch rf = new RemoteFetch();
                JSONArray both = rf.doInBackground(state, city);
                JSONObject today = null;
                JSONObject yesterday = null;
                try {
                    today = both.getJSONObject(0);
                    yesterday = both.getJSONObject(1);
                } catch (JSONException je) {
                    //just wait
                }
                if (today == null || yesterday == null) {
                    Toast.makeText(context, "could'nt find that city", Toast.LENGTH_LONG).show();
                    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
                    remoteViews.setTextViewText(R.id.weatherDesc, "unavailable");
                } else {
                    Toast.makeText(context, "finding weather", Toast.LENGTH_LONG).show();
                    weatherCond = parseWeather(today, yesterday).toString();
                }
            }
        }.start();
    }

    public Weather parseWeather(JSONObject today, JSONObject yesterday) {
        int tempDif = 0;
        try {
            int tempf = (int) today.getDouble("temp_f");
            int yestf = 0;
            // here we have gotten a list of weather updates
            // at different times through the day.
            // so, lets get the array of observations and find
            // the one closest to the current hour
            JSONArray observations = yesterday.getJSONArray("observations");
            String realTime = DateFetcher.get24Hour();
            boolean done = false;
            for (int i = 0; i < observations.length() && !done; i++) {
                JSONObject time = observations.getJSONObject(i);
                if (time != null) {
                    JSONObject date = time.getJSONObject("date");
                    if (realTime.equals(date.getString("hour"))) {
                        yestf = (int) time.getDouble("tempi");
                        done = true;
                    }
                }
            }
            tempDif = tempf - yestf;
        } catch (JSONException je) {
            //bad json file, frowny face
            je.printStackTrace();
        }
        //parse tempdif
        // negative means today is colder
        // pos means today is warmer
        Weather current = null;
        if (tempDif > UNIVERSAL_DEGREE_DIF) {
            current = Weather.HOT;
        } else if (tempDif > 0) {
            current = Weather.WARM;
        } else if (tempDif == 0) {
            current = Weather.SAME;
        } else if (tempDif < UNIVERSAL_DEGREE_DIF) {
            current = Weather.COLD;
        } else if (tempDif < 0) {
            current = Weather.CHILLY;
        }
        return current;
    }

}
