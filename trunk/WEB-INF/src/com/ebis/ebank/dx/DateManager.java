package com.ebis.ebank.dx;

public class DateManager {
    private static String date = null;
    private static DateManager manager = new DateManager();
    private DateManager() {
    }
    public static DateManager getInstance() {
        return manager;
    }
    public synchronized void setDate(String date) {
        if ( date != null && date.length() == 8 )
            this.date = date;
    }
    public String getDate() {
        return date;
    }
}