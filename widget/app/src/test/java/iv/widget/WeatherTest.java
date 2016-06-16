package iv.widget;

import android.test.mock.MockApplication;
import android.util.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.PrintWriter;

public class WeatherTest {
    @Test
    public void getCurrentHappy() throws Exception {
        //happy
        RemoteFetch rf = new RemoteFetch();
        JSONObject j = rf.getCurrentWeather(null, "VA", "Richmond");
        Assert.assertTrue(j != null);
    }

    @Test
    public void getPastHappy() throws Exception {
        //happy
        RemoteFetch rf = new RemoteFetch();
        JSONObject j = rf.getPastWeather(null, "VA", "Richmond");
        Assert.assertTrue(j != null);
    }

    @Test
    public void getCurrentSad() throws Exception {
        //sad, bad city & state
        RemoteFetch rf = new RemoteFetch();
        JSONObject j = rf.getCurrentWeather(null, "s", "a");
        Assert.assertTrue(j != null);
    }

    @Test
    public void getPastSad() throws Exception {
        //sad, bad city & state
        RemoteFetch rf = new RemoteFetch();
        JSONObject j = rf.getPastWeather(null, "s", "a");
        Assert.assertTrue(j != null);
    }

    @Test
    public void parseTestHappy() {
        Main m = new Main();
        JSONObject tdy = Mockito.mock(JSONObject.class);
        JSONObject yst = Mockito.mock(JSONObject.class);
        Weather result = null;
        try {
            Mockito.when(tdy.getInt(Mockito.anyString())).thenReturn(100);
            Mockito.when(yst.getInt(Mockito.anyString())).thenReturn(50);
            result = m.parseWeather(tdy, yst);
        } catch (JSONException je) {
            //eh
            Assert.fail();
        }
        Assert.assertEquals(result, Weather.HOT);
    }
}
