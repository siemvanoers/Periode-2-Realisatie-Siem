package com.realisatie.realisatiesiem.classes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Sleep {

    private String quality;
    private int duration;

    public Sleep(String quality, int duration) {
        this.quality = quality;
        this.duration = duration;
    }

    public Sleep(ResultSet rs) throws SQLException {
        this.quality = rs.getString("quality");
        this.duration = rs.getInt("duration");
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
}
