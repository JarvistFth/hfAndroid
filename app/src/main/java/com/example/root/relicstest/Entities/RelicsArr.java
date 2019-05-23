package com.example.root.relicstest.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RelicsArr {
    @SerializedName("")
    private List<Relics> data;


    @Override
    public String toString() {
        return "data = " + data;
    }
}
