package com.example.notes_mackevich6.datas;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.notes_mackevich6.domains.models.Note;

import java.util.ArrayList;

public class NotesContext {

    public static ArrayList<Note> AllNotes() {
        ArrayList<Note> allNotes = new ArrayList<>();
        Cursor cursor = DbContext.sqLiteDatabase.query("Notes", null, null, null, null, null, "Id DESC");

        if(cursor.moveToFirst() == false) {
            cursor.close();
            return allNotes;
        }

        do {
            Note note = new Note();
            note.id = cursor.getInt(0);
            note.title = cursor.getString(1);
            note.text = cursor.getString(2);
            note.date = cursor.getString(3);
            note.color = cursor.getString(4);
            note.isFavorite = cursor.getInt(5) == 1;
            allNotes.add(note);
        } while(cursor.moveToNext());

        cursor.close();
        return allNotes;
    }

    public static void Save(Note note, boolean update) {
        ContentValues CV = new ContentValues();
        CV.put("Title", note.title);
        CV.put("Text", note.text);
        CV.put("Date", note.date);
        CV.put("Color", note.color);
        CV.put("IsFavorite", note.isFavorite ? 1 : 0);

        if(!update) {
            long id = DbContext.sqLiteDatabase.insert("Notes", null, CV);
            note.id = (int) id;
        } else {
            DbContext.sqLiteDatabase.update("Notes", CV, "Id = ?", new String[] {String.valueOf(note.id)});
        }
    }

    public static void Delete(Note note) {
        DbContext.sqLiteDatabase.delete("Notes", "Id = ?", new String[] {String.valueOf(note.id)});
    }

    public static ArrayList<Note> isFavoriteNotes() {
        ArrayList<Note> FavoriteNotes = new ArrayList<>();
        Cursor cursor = DbContext.sqLiteDatabase.query("Notes", null, "IsFavorite = 1", null, null ,null, "Id DESC");
        if(cursor.moveToFirst() == false) {
            cursor.close();
            return FavoriteNotes;
        }
f
        do {
            Note note = new Note();
            note.id = cursor.getInt(0);
            note.title = cursor.getString(1);
            note.text = cursor.getString(2);
            note.date = cursor.getString(3);
            note.color = cursor.getString(4);
            note.isFavorite = true;
            FavoriteNotes.add(note);
        } while (cursor.moveToNext());

        cursor.close();
        return FavoriteNotes;
    }
}