package iv.widget;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.json.JSONObject;

import android.content.Context;

public class RemoteFetch {

    private static final String WEATHER_UNDERGROUND_CURRENT_WEATHER =
            "http://api.wunderground.com/api/KEY/conditions/q/STATE/CITY.json";
    private static final String WEATHER_UNDERGROUND_YESTERDAY =
            "http://api.wunderground.com/api/KEY/yesterday/q/STATE/CITY.json";

    public static JSONObject getCurrentWeather(Context context, String state, String city) {
        //https://www.wunderground.com/weather/api/d/docs?d=data/conditions&MR=1
        String adj = WEATHER_UNDERGROUND_CURRENT_WEATHER
                .replace("KEY", "" + R.string.weatherUnderground)
                .replace("STATE", state)
                .replace("CITY", city);
        //DEBUG
        //
        System.out.println(adj);
        //
        try {
            URL url = new URL(String.format(WEATHER_UNDERGROUND_CURRENT_WEATHER));
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null) {
                json.append(tmp).append("\n");
            }
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            return data;
        } catch (Exception e) {
            return null;
        }
    }

    public static JSONObject getPastWeather(Context context, String state, String city) {
        //https://www.wunderground.com/weather/api/d/docs?d=data/yesterday
        String adj = WEATHER_UNDERGROUND_YESTERDAY
                .replace("KEY", "" + R.string.weatherUnderground)
                .replace("STATE", state)
                .replace("CITY", city);
        try {
            URL url = new URL(String.format(WEATHER_UNDERGROUND_YESTERDAY));
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null) {
                json.append(tmp).append("\n");
            }
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            return data;

        } catch (Exception e) {
            return null;
        }
    }
}
