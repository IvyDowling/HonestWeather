package iv.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

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


        }
    }

    private void updateWeatherData(final Context context, final String state, final String city) {
        new Thread() {
            public void run() {
                final JSONObject today = RemoteFetch.getCurrentWeather(context, state, city);
                final JSONObject yesterday = RemoteFetch.getPastWeather(context, state, city);
                if (today == null) {
                    Toast.makeText(context, "couldnt find that city", Toast.LENGTH_LONG).show();
                } else {
                    parseWeather(context, today, yesterday);
                }
            }
        }.start();
    }

    private void parseWeather(final Context context, JSONObject today, JSONObject yesterday) {
        //get current temp, and yesterday's temp.
        int tempdif = 0;
        try {
            int tempf = today.getInt("temp_f");
            //this one is tricky,
            // here we have gotten a list of weather updates
            // at different times through the day.
            int yestf = yesterday.getInt("tempi");
            tempdif = tempf - yestf;
        } catch (JSONException je) {
            //bad json file, frowny face
        }
        //parse tempdif
        // negative means today is colder
        // pos means today is warmer
        Weather current = Weather.NICE;
        if (tempdif > UNIVERSAL_DEGREE_DIF) {
            current = Weather.HOT;
        } else if (tempdif > 0) {
            current = Weather.WARM;
        } else if (tempdif == 0) {
            //nice
        } else if (tempdif < UNIVERSAL_DEGREE_DIF) {
            current = Weather.COLD;
        } else if (tempdif < 0) {
            current = Weather.CHILLY;
        }
        //change widget view to reflect "current"
        //

    }

}
