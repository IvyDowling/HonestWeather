package iv.widget;

import org.junit.Test;
import org.junit.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTest {

    @Test
    public void year() throws Exception {
        Calendar c = new GregorianCalendar();
        c.setTime(new Date());
        Assert.assertEquals(DateFetcher.getYear(), c.get(Calendar.YEAR));
    }

    @Test
    public void month() throws Exception {
        Calendar c = new GregorianCalendar();
        c.setTime(new Date());
        Assert.assertEquals(DateFetcher.getMonth(), (c.get(Calendar.MONTH) + 1));
    }

    @Test
    public void day() throws Exception {
        Calendar c = new GregorianCalendar();
        c.setTime(new Date());
        Assert.assertEquals(DateFetcher.getDay(), c.get(Calendar.DAY_OF_MONTH));
    }
}