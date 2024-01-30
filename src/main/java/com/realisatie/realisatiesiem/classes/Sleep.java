package com.realisatie.realisatiesiem.classes;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Sleep {

    private String quality;
    private int duration;
    private Date sleepDate;

    public Sleep(String quality, int duration, Date sleepDate) {
        this.quality = quality;
        this.duration = duration;
        this.sleepDate = sleepDate;
    }

    public Sleep(ResultSet rs) throws SQLException {
        this.quality = rs.getString("quality");
        this.duration = rs.getInt("duration");
        this.sleepDate = rs.getDate("sleep_date");
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    public Date getSleepDate() {
        return sleepDate;
    }

    public void setSleepDate(Date sleepDate) {
        this.sleepDate = sleepDate;
    }
}
