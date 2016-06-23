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
        JSONObject tdy = Mockito.mock(JSONObject.class);
        JSONObject yst = Mockito.mock(JSONObject.class);
        //inner mocks
        JSONArray ystArray = Mockito.mock(JSONArray.class);
        JSONObject ystArrayObject = Mockito.mock(JSONObject.class);
        JSONObject dateObject = Mockito.mock(JSONObject.class);
        Weather result = null;
        try {
            //get current time for the hour-match
            SimpleDateFormat sdf = new SimpleDateFormat("HH");
            String time = sdf.format(new Date());
            //setup date mock
            Mockito.when(dateObject.getString(Mockito.anyString())).thenReturn(time);
            //setup array object mock
            Mockito.when(ystArrayObject.getDouble(Mockito.anyString())).thenReturn(50.0);
            Mockito.when(ystArrayObject.getJSONObject(Mockito.anyString())).thenReturn(dateObject);
            //setup array
            Mockito.when(ystArray.getJSONObject(Mockito.anyByte())).thenReturn(ystArrayObject);
            Mockito.when(ystArray.length()).thenReturn(100);//infinity
            //outer mock
            Mockito.when(tdy.getDouble(Mockito.anyString())).thenReturn(100.0);
            Mockito.when(yst.getJSONArray(Mockito.anyString())).thenReturn(ystArray);
            result = m.parseWeather(tdy, yst);
        } catch (JSONException je) {
            Assert.fail();
        }
        Assert.assertEquals(result, Weather.HOT);
    }


    @Test
    public void parseTestFileExamples() {
        BufferedReader cond = null;
        BufferedReader yest = null;
        try {
            cond = new BufferedReader(new FileReader("./.conditions.json"));
            yest = new BufferedReader(new FileReader("./.yesterday.json"));
        } catch (FileNotFoundException fnf) {
            Assert.fail();
        }
        String yestContents = "";
        String condContents = "";
        try {
            while (yest.ready()) {
                yestContents += yest.readLine();
            }
            while (cond.ready()) {
                condContents += cond.readLine();
            }
        } catch (IOException io) {
            Assert.fail();
        }
        JSONObject tdy = null;
        JSONObject yst = null;
        try {
            tdy = new JSONObject(condContents);
            yst = new JSONObject(yestContents);
        } catch (JSONException je) {
            Assert.fail();
        }
        Weather result = m.parseWeather(tdy, yst);
        System.out.println(result);
        Assert.assertTrue(result != null);
    }
}
