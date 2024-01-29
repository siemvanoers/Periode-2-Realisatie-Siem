package com.realisatie.realisatiesiem.classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Day {
    private Date date;
    private Exercises exercises;
    private Meals meals;
    private Sleep sleep;

    public Day(Date date, Exercises exercises, Meals meals, Sleep sleep) {
        this.date = date;
        this.exercises = exercises;
        this.meals = meals;
        this.sleep = sleep;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Exercises getExercises() {
        return exercises;
    }

    public void setExercises(Exercises exercises) {
        this.exercises = exercises;
    }

    public Meals getMeals() {
        return meals;
    }

    public void setMeals(Meals meals) {
        this.meals = meals;
    }

    public Sleep getSleep() {
        return sleep;
    }

    public void setSleep(Sleep sleep) {
        this.sleep = sleep;
    }
}
