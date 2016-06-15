package iv.widget;

import android.test.mock.MockApplication;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

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
}
