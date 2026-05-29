package com.example.notes_mackevich6.presentations;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notes_mackevich6.R;
import com.example.notes_mackevich6.datas.DbContext;
import com.example.notes_mackevich6.datas.NotesContext;
import com.example.notes_mackevich6.datas.RepoNotes;
import com.example.notes_mackevich6.domains.models.Note;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class NotesActivity extends AppCompatActivity {

    GridLayout itemsParent;
    View bthAddNotes;
    EditText etSearch;
    DbContext dbContext;
    ArrayList<Note> currentNotes = new ArrayList<>();
    Button bthAll, bthFavorite;
    boolean showOnlyFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes);

        bthAddNotes = findViewById(R.id.btn_add_notes);
        itemsParent = findViewById(R.id.gl_notes);
        etSearch = findViewById(R.id.et_search);
        bthAll = findViewById(R.id.btn_all);
        bthFavorite = findViewById(R.id.btn_favorites);

        bthAll.setOnClickListener(v -> {
            showOnlyFavorite = false;
            loadNotesFromDatabase();
        });

        bthFavorite.setOnClickListener(v -> {
            showOnlyFavorite = true;
            loadNotesFromDatabase();
        });

        bthAddNotes.setOnClickListener(v -> {
            Intent intentActivityNote = new Intent(this, NoteActivity.class);
            startActivity(intentActivityNote);
        });

        etSearch.setOnKeyListener(SearchListner);

        dbContext = new DbContext(this);

        loadNotesFromDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotesFromDatabase();
    }

    private void loadNotesFromDatabase() {
        if(showOnlyFavorite) {
            currentNotes = NotesContext.isFavoriteNotes();
        } else
            currentNotes = NotesContext.AllNotes();

        RepoNotes.Notes = currentNotes;
        RepoNotes.Save(this);
        displayNotes(currentNotes);
    }

    public void displayNotes(ArrayList<Note> notes) {
        itemsParent.removeAllViews();

        for(int i = 0; i < notes.size(); i++) {
            View item_notes = LayoutInflater.from(this).inflate(R.layout.item_note, itemsParent, false);

            TextView tvTitle = item_notes.findViewById(R.id.tv_title);
            TextView tvText = item_notes.findViewById(R.id.tv_text);
            TextView tvDate = item_notes.findViewById(R.id.tv_date);
            ImageView ivFavorite = item_notes.findViewById(R.id.iv_favorite);

            tvTitle.setText(notes.get(i).title);
            tvText.setText(notes.get(i).text);
            tvDate.setText(notes.get(i).date);

            applyNoteColor(item_notes, notes.get(i).color);

            if(notes.get(i).isFavorite) {
                ivFavorite.setImageResource(R.drawable.select_ic_favorite);
            } else {
                ivFavorite.setImageResource(R.drawable.ic_favorite);
            }

            int Position = i;
            int noteId = notes.get(i).id;

            item_notes.setOnClickListener(v -> {
                Intent intentActivityNote = new Intent(this, NoteActivity.class);
                intentActivityNote.putExtra("position", Position);
                startActivity(intentActivityNote);
            });

            itemsParent.addView(item_notes);
        }
    }

    View.OnKeyListener SearchListner = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            String Search = etSearch.getText().toString();
            ArrayList<Note> FindNotes = currentNotes.stream().filter(
                    item -> item.text.toLowerCase().contains(Search.toLowerCase()) ||
                            item.title.toLowerCase().contains(Search.toLowerCase())
            ).collect(Collectors.toCollection(ArrayList::new));

            displayNotes(FindNotes);
            return false;
        }
    };

    private void applyNoteColor(View itemView, String colorHex) {
        if (colorHex == null || colorHex.isEmpty()) return;

        LinearLayout strip = itemView.findViewById(R.id.strip_color);
        LinearLayout bg = itemView.findViewById(R.id.bg_color);

        if (strip != null) {
            strip.setBackgroundColor(Color.parseColor(colorHex));
        }
        if (bg != null) {
            String transparent = "#30" + colorHex.substring(1);
            bg.setBackgroundColor(Color.parseColor(transparent));
        }
    }
}