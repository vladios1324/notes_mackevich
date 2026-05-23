package com.example.notes_mackevich6.presentations;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.notes_mackevich6.R;
import com.example.notes_mackevich6.datas.DbContext;
import com.example.notes_mackevich6.datas.NotesContext;
import com.example.notes_mackevich6.datas.RepoNotes;
import com.example.notes_mackevich6.domains.models.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoteActivity extends AppCompatActivity {
    Note note;
    EditText etTitle, etText;
    TextView tvDate;
    View bthSelectColor, bthBack, bthTrash;
    DbContext dbContext;
    int notePosition = -1;
    ImageView bthFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note);

        Date DateNow = new Date();
        SimpleDateFormat FormatForDateNow = new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");

        bthSelectColor = findViewById(R.id.bth_select_color);
        bthBack = findViewById(R.id.bth_back);
        bthTrash = findViewById(R.id.bth_trash);
        etTitle = findViewById(R.id.et_title);
        etText = findViewById(R.id.et_text);
        tvDate = findViewById(R.id.tv_date);
        bthFavorite = findViewById(R.id.bth_favorite);

        dbContext = new DbContext(this);

        Bundle arguments = getIntent().getExtras();
        if(arguments != null) {
            notePosition = arguments.getInt("position");
            ArrayList<Note> notes = NotesContext.AllNotes();
            if(notePosition >= 0 && notePosition < notes.size()) {
                note = notes.get(notePosition);

                etTitle.setText(note.title);
                etText.setText(note.text);

                if (note.color != null && !note.color.isEmpty()) {
                    ((CardView) bthSelectColor).setCardBackgroundColor(Color.parseColor(note.color));
                    String transparent = "#30" + note.color.substring(1);
                    ConstraintLayout main = findViewById(R.id.main);
                    if (main != null) {
                        main.setBackgroundColor(Color.parseColor(transparent));
                    }
                }
            }
        }

        if(note == null) {
            bthTrash.setVisibility(View.GONE);
        }

        if(note != null && note.isFavorite) {
            bthFavorite.setImageResource(R.drawable.select_ic_favorite);
        } else {
            bthFavorite.setImageResource(R.drawable.ic_favorite);
        }

        bthFavorite.setOnClickListener(v -> {
            if(note != null) {
                note.isFavorite = !note.isFavorite;
                if(note.isFavorite) {
                    bthFavorite.setImageResource(R.drawable.select_ic_favorite);
                    Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show();
                } else {
                    bthFavorite.setImageResource(R.drawable.ic_favorite);
                    Toast.makeText(this, "Удалено из избранного", Toast.LENGTH_SHORT).show();
                }

                NotesContext.Save(note, true);
                RepoNotes.Notes = NotesContext.AllNotes();
                RepoNotes.Save(this);
            }
        });

        tvDate.setText("Отредактированно: " + FormatForDateNow.format(DateNow));

        bthSelectColor.setOnClickListener(v -> {
            showColorPicker();
        });

        bthBack.setOnClickListener(v -> {
            String Title = etTitle.getText().toString();
            String Text = etText.getText().toString();

            if(Text.replace(" ", "").replace("\r", "").replace("\n", "").isEmpty()) {
                Toast.makeText(this, "Нечего сохранять", Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                boolean isUpdate = (note != null && note.id != 0);

                if(note == null) {
                    note = new Note();
                    note.color = "#2071F9";
                }

                note.title = Title;
                note.text = Text;
                note.date = FormatForDateNow.format(DateNow);

                NotesContext.Save(note, isUpdate);

                RepoNotes.Notes = NotesContext.AllNotes();
                RepoNotes.Save(NoteActivity.this);
            }
            finish();
        });

        bthTrash.setOnClickListener(v -> {
            if (note != null) {
                NotesContext.Delete(note);
                RepoNotes.Notes = NotesContext.AllNotes();
                RepoNotes.Save(NoteActivity.this);
                Toast.makeText(this, "Заметка удалена", Toast.LENGTH_SHORT).show();
            }
            finish();
        });
    }

    private void showColorPicker() {
        String[] colors = {"#2071F9", "#4CAF50", "#FF9800", "#E91E63", "#9C27B0", "#00BCD4"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите цвет");

        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(3);

        for (String color : colors) {
            View btn = new View(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 120;
            params.height = 120;
            params.setMargins(8, 8, 8, 8);
            btn.setLayoutParams(params);
            btn.setBackgroundColor(Color.parseColor(color));
            btn.setClickable(true);

            btn.setOnClickListener(v -> {
                if (note == null) {
                    note = new Note();
                    note.color = "#2071F9";
                }
                note.color = color;

                ((CardView) bthSelectColor).setCardBackgroundColor(Color.parseColor(color));
                String transparent = "#30" + color.substring(1);
                ConstraintLayout main = findViewById(R.id.main);
                if (main != null) {
                    main.setBackgroundColor(Color.parseColor(transparent));
                }

                boolean isUpdate = (note.id != 0);
                NotesContext.Save(note, isUpdate);
                RepoNotes.Notes = NotesContext.AllNotes();
                RepoNotes.Save(NoteActivity.this);

                Toast.makeText(this, "Цвет применён", Toast.LENGTH_SHORT).show();
            });

            gridLayout.addView(btn);
        }

        builder.setView(gridLayout);
        builder.show();
    }
}