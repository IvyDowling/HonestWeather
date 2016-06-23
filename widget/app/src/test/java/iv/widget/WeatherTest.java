package iv.widget;

import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WeatherTest {

    private static Main m = new Main();

    @Test
    public void getCurrentHappy() throws Exception {
        //happy
        RemoteFetch rf = new RemoteFetch();
        JSONObject j = rf.getCurrentWeather("VA", "Richmond");
        Assert.assertTrue(j != null);
    }

    @Test
    public void getPastHappy() throws Exception {
        //happy
        RemoteFetch rf = new RemoteFetch();
        JSONObject j = rf.getPastWeather("VA", "Richmond");
        Assert.assertTrue(j != null);
    }

    @Test
    public void getCurrentSad() throws Exception {
        //sad, bad city & state
        RemoteFetch rf = new RemoteFetch();
        JSONObject j = rf.getCurrentWeather("s", "a");
        Assert.assertTrue(j != null);
    }

    @Test
    public void getPastSad() throws Exception {
        //sad, bad city & state
        RemoteFetch rf = new RemoteFetch();
        JSONObject j = rf.getPastWeather("s", "a");
        Assert.assertTrue(j != null);
    }

    @Test
    public void parseTestAbstract() {
        m.getClass();
        JSONObject tdy = Mockito.mock(JSONObject.class);
        JSONObject yst = Mockito.mock(JSONObject.class);
        //inner mocks
        JSONArray ystArray = Mockito.mock(JSONArray.class);
        JSONObject ystArrayObject = Mockito.mock(JSONObject.class);
        JSONObject dateObject = Mockito.mock(JSONObject.class);
        Weather result;
        //get current time for the hour-match
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String time = sdf.format(new Date());
        try {
            //setup date mock
            Mockito.when(dateObject.getString(Mockito.anyString())).thenReturn(time);
        } catch (JSONException je) {
            Assert.fail(je.getMessage());
        }
        try {
            //setup array object mock
            Mockito.when(ystArrayObject.getDouble(Mockito.anyString())).thenReturn(50.0);
            Mockito.when(ystArrayObject.getJSONObject(Mockito.anyString())).thenReturn(dateObject);
        } catch (JSONException je) {
            Assert.fail(je.getMessage());
        }
        try {
            //setup array
            Mockito.when(ystArray.getJSONObject(0)).thenReturn(ystArrayObject);
            Mockito.when(ystArray.length()).thenReturn(100);//infinity
        } catch (JSONException je) {
            Assert.fail(je.getMessage());
        }
        try {
            //outer mock
            Mockito.when(tdy.getDouble(Mockito.anyString())).thenReturn(100.0);
            Mockito.when(yst.getJSONArray(Mockito.anyString())).thenReturn(ystArray);
        } catch (JSONException je) {
            Assert.fail(je.toString());
        }
        result = m.parseWeather(tdy, yst);
        Assert.assertEquals(Weather.HOT, result);
    }
}
