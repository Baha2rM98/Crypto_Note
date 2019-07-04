package ir.Baha2rMirzazadeh.crypto.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import co.dift.ui.SwipeToAction;
import ir.Baha2rMirzazadeh.crypto.R;
import ir.Baha2rMirzazadeh.crypto.note.Note;

public class NoteViewHolder extends SwipeToAction.ViewHolder<Note> {
    private TextView title;
    private TextView description;
    private ImageView encryption;

    public NoteViewHolder(View v) {
        super(v);
        title = v.findViewById(R.id.note_item_title);
        description = v.findViewById(R.id.note_item_desc);
        encryption = v.findViewById(R.id.note_item_encrypt);
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getDescription() {
        return description;
    }

    public ImageView getEncryption() {
        return encryption;
    }
}
