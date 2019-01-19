package com.example.elena.mynotes.ui.notes;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.elena.mynotes.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateNoteDialogFragment extends DialogFragment {
    private static String TAG = "NoteDialog";

    private NotesViewModel mViewModel;

    @BindView(R.id.et_note)
    EditText mEditText;

    private static CreateNoteDialogFragment newInstance() {
        return new CreateNoteDialogFragment();
    }

    public static void showDialog(FragmentManager fragmentManager) {
        newInstance().show(fragmentManager, TAG);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        mViewModel = ViewModelProviders.of(activity).get(NotesViewModel.class);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_note, null);
        ButterKnife.bind(this, view);

        dialogBuilder.setTitle(getString(R.string.dialog_title_createNote))
                .setView(view)
                .setPositiveButtonIcon(activity.getResources().getDrawable(R.drawable.ic_positive_btn))
                .setPositiveButton(getString(R.string.btn_createNoteDialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String noteDescription = mEditText.getText().toString();
                        if (!noteDescription.isEmpty()) {
                            mViewModel.createNote(noteDescription);
                        } else {
                            Toast.makeText(activity, "Enter your new note!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return dialogBuilder.create();
    }
}
