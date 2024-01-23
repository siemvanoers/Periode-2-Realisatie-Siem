package com.realisatie.realisatiesiem.classes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Meals {
    private String name;
    private String type;
    private int calories;
    private int proteins;
    private int fats;
    private int carbs;

    public Meals(String name, String type, int calories, int proteins, int fats, int carbs) {
        this.name = name;
        this.type = type;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbs = carbs;
    }

    public Meals(ResultSet rs) throws SQLException {
        this.name = rs.getString("name");
        this.type = rs.getString("type");
        this.calories = rs.getInt("calories");
        this.proteins = rs.getInt("protein");
        this.fats = rs.getInt("fats");
        this.carbs = rs.getInt("carbs");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProteins() {
        return proteins;
    }

    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    public int getFats() {
        return fats;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }
}
