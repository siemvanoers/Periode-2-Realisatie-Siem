package com.realisatie.realisatiesiem.classes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Exercises {
    private String name;
    private String muscleGroup;
    private int sets;
    private int reps;

    public Exercises(String name, String muscleGroup, int sets, int reps) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.sets = sets;
        this.reps = reps;
    }

    public Exercises(ResultSet rs) throws SQLException {
        this.name = rs.getString("name");
        this.muscleGroup = rs.getString("muscle_group");
        this.sets = rs.getInt("sets");
        this.reps = rs.getInt("reps");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }
}
