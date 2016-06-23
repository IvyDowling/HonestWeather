package iv.widget;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;

public class RemoteFetch extends AsyncTask<String, Void, JSONArray> {

    private final String WEATHER_UNDERGROUND_CURRENT_WEATHER =
            "http://api.wunderground.com/api/KEY/conditions/q/STATE/CITY.json";
    private final String WEATHER_UNDERGROUND_YESTERDAY =
            "http://api.wunderground.com/api/KEY/yesterday/q/STATE/CITY.json";

    private static JSONObject current;
    private static JSONObject yesterday;

    public JSONObject getCurrentWeather(String state, String city) {
        //https://www.wunderground.com/weather/api/d/docs?d=data/conditions&MR=1
        String adj = WEATHER_UNDERGROUND_CURRENT_WEATHER
                .replace("KEY", "" + R.string.weatherUnderground)
                .replace("STATE", state)
                .replace("CITY", city);

        try {
            URL url = new URL(String.format(WEATHER_UNDERGROUND_CURRENT_WEATHER));
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuffer json = new StringBuffer(10000);
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

    public JSONObject getPastWeather(String state, String city) {
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

            StringBuffer json = new StringBuffer(10000);
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

    public static JSONObject getCurrent() {
        return current;
    }

    public static JSONObject getYesterday() {
        return yesterday;
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        //[0] -> STATE
        //[1]-> CITY
        current = this.getCurrentWeather(params[0], params[1]);
        yesterday = this.getPastWeather(params[0], params[1]);
        JSONArray combo = new JSONArray();
        combo.put(current);
        combo.put(yesterday);
        return combo;
    }
}
