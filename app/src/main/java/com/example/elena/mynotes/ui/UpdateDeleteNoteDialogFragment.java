package com.example.elena.mynotes.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.example.elena.mynotes.MyNotesApp;
import com.example.elena.mynotes.R;
import com.example.elena.mynotes.database.MyNotesDao;
import com.example.elena.mynotes.database.entities.NoteEntity;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateDeleteNoteDialogFragment extends DialogFragment {
    private static final String TAG = "UpdateNoteDialog";
    private static final String NOTE_ID = "com.example.elena.mynotes.ui.UpdateDeleteNoteDialogFragment.noteId";

    @BindView(R.id.et_note)
    EditText mEditText;

    private MyNotesDao mDao;

    private int mNoteId;

    private static UpdateDeleteNoteDialogFragment newInstance(int noteId) {
        UpdateDeleteNoteDialogFragment dialogFragment = new UpdateDeleteNoteDialogFragment();
        Bundle args = new Bundle();
        args.putInt(NOTE_ID, noteId);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    public static void showDialog(FragmentManager fragmentManager, int noteId) {
        newInstance(noteId).show(fragmentManager, TAG);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_note, null);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            mNoteId = getArguments().getInt(NOTE_ID);
        }

        mDao = MyNotesApp.getDatabase().myNotesDao();
        NoteEntity noteEntity = mDao.noteById(mNoteId).get(0);
        String lastNoteDescription = noteEntity.description;
        mEditText.setText(lastNoteDescription);

        dialogBuilder.setTitle(getString(R.string.dialog_title_updateDeleteNote))
                .setView(view)
                .setNegativeButton(getString(R.string.btn_delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDao.deleteNote(mNoteId);

                        if (activity instanceof CategoryActivity) {
                            ((CategoryActivity) activity).refreshAdapter();
                        }
                    }
                })
                .setPositiveButton(getString(R.string.btn_update), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newNoteDescription = mEditText.getText().toString();
                        if (!newNoteDescription.equals(lastNoteDescription)) {
                            noteEntity.description = newNoteDescription;
                            noteEntity.modifiedDate = new Date();
                            mDao.updateNote(noteEntity);

                            if (activity instanceof CategoryActivity) {
                                ((CategoryActivity) activity).refreshAdapter();
                            }
                        }
                    }
                });

        return dialogBuilder.create();
    }

}
