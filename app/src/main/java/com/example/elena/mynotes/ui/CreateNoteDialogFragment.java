package com.example.elena.mynotes.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
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

public class CreateNoteDialogFragment extends DialogFragment {

    private static String TAG = "NoteDialog";
    private final static String CATEGORY_ID = "com.example.elena.mynotes.ui.CreateNoteDialogFragment.categoryId";

    @BindView(R.id.et_note)
    EditText mEditText;

    private int mCategoryId;

    private static CreateNoteDialogFragment newInstance(int categoryId) {
        CreateNoteDialogFragment dialogFragment = new CreateNoteDialogFragment();
        Bundle args = new Bundle();
        args.putInt(CATEGORY_ID, categoryId);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    public static void showDialog(FragmentManager fragmentManager, int categoryId) {
        newInstance(categoryId).show(fragmentManager, TAG);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_create_note, null);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            mCategoryId = getArguments().getInt(CATEGORY_ID);
        }

        dialogBuilder.setTitle("Create new note")
                .setView(view)
                .setPositiveButtonIcon(activity.getResources().getDrawable(R.drawable.ic_positive_btn))
                .setPositiveButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String noteDescription = mEditText.getText().toString();

                        if (!noteDescription.isEmpty()) {
                            NoteEntity noteEntity = new NoteEntity();
                            noteEntity.categoryId = mCategoryId;
                            noteEntity.description = noteDescription;
                            Date now = new Date();
                            noteEntity.creationDate = now;
                            noteEntity.modifiedDate = now;

                            MyNotesDao dao = MyNotesApp.getDatabase().myNotesDao();
                            dao.createNote(noteEntity);

                            if (activity instanceof CategoryActivity) {
                                ((CategoryActivity)activity).refresh();
                            }

                        } else {
                            Toast.makeText(activity, "Enter your new note!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        return dialogBuilder.create();
    }
}
