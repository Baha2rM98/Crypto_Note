package ir.Baha2rMirzazadeh.crypto;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import es.dmoral.toasty.Toasty;
import ir.Baha2rMirzazadeh.crypto.note.Note;
import ir.Baha2rMirzazadeh.crypto.view.NotePasswordWatcher;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.RelativeLayout;
import com.rey.material.widget.TextView;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class EditActivity extends AppCompatActivity {

    private final static boolean EDIT_MODE = true;
    private final static boolean SAVE_MODE = false;

    private Note currentNote;
    private EditText titleEdit;
    private EditText descriptionEdit;
    private EditText passwordEdit;
    private EditText textEdit;
    private Button noteActionButton;
    private TextView titleTextView;
    private boolean mode = SAVE_MODE;
    private ArrayList<File> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        findViews();
        getNote();
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(getActionbarView());
    }

    private void findViews() {
        titleEdit = findViewById(R.id.edit_note_title);
        descriptionEdit = findViewById(R.id.edit_note_description);
        passwordEdit = findViewById(R.id.edit_note_password);
        passwordEdit.addTextChangedListener(new NotePasswordWatcher());
        textEdit = findViewById(R.id.edit_note_text);
    }

    private void getNote() {
        if (getIntent().getExtras() == null) {
            Toasty.error(this, "Something went wrong!", Toasty.LENGTH_SHORT).show();
            finish();
        }
        currentNote = (Note) getIntent().getExtras().get("note");
        noteList = (ArrayList<File>) getIntent().getExtras().get("allNotes");
        if (currentNote == null) {
            Toasty.error(this, "Something went wrong!", Toasty.LENGTH_SHORT).show();
            finish();
        }
        if (!currentNote.isEncrypted()) {
            passwordEdit.setVisibility(View.GONE);
        }
        titleEdit.setText(currentNote.getTitle());
        descriptionEdit.setText(currentNote.getDescription());
        passwordEdit.setText(currentNote.getPassword());
        textEdit.setText(currentNote.getText());
    }

    private RelativeLayout getActionbarView() {
        RelativeLayout layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.edit_toolbar, null);
        titleTextView = layout.findViewById(R.id.title_text_view);
        titleTextView.setText("Edit note: " + currentNote.getTitle());
        noteActionButton = layout.findViewById(R.id.note_action_btn);
        noteActionButton.setOnClickListener(_1 -> noteAction());
        return layout;
    }

    private void noteAction() {
        mode = !mode;
        if (mode == SAVE_MODE) {
            if (passwordEdit.getVisibility() == View.VISIBLE && passwordEdit.getText().length() > 16) {
                Toasty.error(this, "Password is too long!", Toasty.LENGTH_SHORT).show();
            } else if (checkNoteName(titleEdit.getText().toString())) {
                Toasty.error(this, "Note exists, choose an other note title", Toasty.LENGTH_SHORT).show();
            } else {
                noteActionButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_icon));
                titleEdit.setEnabled(false);
                descriptionEdit.setEnabled(false);
                passwordEdit.setEnabled(false);
                textEdit.setEnabled(false);
                save();
            }
        }
        if (mode == EDIT_MODE) {
            noteActionButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.save_icon));
            titleEdit.setEnabled(true);
            descriptionEdit.setEnabled(true);
            passwordEdit.setEnabled(true);
            textEdit.setEnabled(true);
        }
    }

    private boolean checkNoteName(String name) {
        for (File file : noteList) {
            if (file.getName().equals(name) && !file.equals(currentNote.getPath()))
                return true;
        }
        return false;
    }

    private void save() {
        currentNote.getPath().delete();
        currentNote.setTitle(titleEdit.getText().toString());
        currentNote.setPath(new File(currentNote.getPath().getParent(), currentNote.getTitle()));
        currentNote.setDescription(descriptionEdit.getText().toString());
        currentNote.setText(textEdit.getText().toString());
        if (currentNote.isEncrypted()) {
            currentNote.setPassword(passwordEdit.getText().toString());
        }
        try {
            currentNote.save(this);
            titleTextView.setText("Edit note: " + currentNote.getTitle());
            Toasty.success(this, "Note saved successfully!", Toasty.LENGTH_SHORT).show();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (mode == SAVE_MODE) {
            setResult(StartActivity.EDIT_RESULT);
            super.onBackPressed();
        }
        if (mode == EDIT_MODE) {
            Toasty.warning(this, "Please save note", Toasty.LENGTH_SHORT).show();
        }
    }
}
