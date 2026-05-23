package com.example.notes_mackevich6.datas;

import android.content.Context;

import com.example.notes_mackevich6.domains.models.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RepoNotes {
    public static ArrayList<Note> Notes = new ArrayList<>();

    public static void Save(Context context) {
        String json = new Gson().toJson(Notes);
        context.getSharedPreferences("notes_db", Context.MODE_PRIVATE)
                .edit()
                .putString("data", json)ва
                .apply();
    }

    public static void Load(Context context) {
        String json = context.getSharedPreferences("notes_db", Context.MODE_PRIVATE)
                .getString("data", null);
        if(json != null) {
            Type type = new TypeToken<ArrayList<Note>>(){}.getType();
            Notes = new Gson().fromJson(json, type);
        }
    }
}
