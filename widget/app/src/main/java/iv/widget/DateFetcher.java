package iv.widget;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateFetcher {

    public static String getDateDDMMYYY() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        //month starts at 0 in Calendar class
        month = month + 1;
        return "" + day + "-" + month + "-" + year;
    }

    public static String getProperDate() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String monthName = "";
        switch (month) {
            case 0:
                monthName = "January";
                break;
            case 1:
                monthName = "February";
                break;
            case 2:
                monthName = "March";
                break;
            case 3:
                monthName = "April";
                break;
            case 4:
                monthName = "May";
                break;
            case 5:
                monthName = "June";
                break;
            case 6:
                monthName = "July";
                break;
            case 7:
                monthName = "August";
                break;
            case 8:
                monthName = "September";
                break;
            case 9:
                monthName = "October";
                break;
            case 10:
                monthName = "November";
                break;
            case 11:
                monthName = "December";
                break;
        }
        return monthName + " " + day + ", " + year;
    }

    public static int getYear() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        return cal.get(Calendar.YEAR);
    }

    public static int getMonth() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        return cal.get(Calendar.MONTH);
    }

    public static int getDay() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        return cal.get(Calendar.DAY_OF_MONTH);
    }

}
