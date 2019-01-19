package com.example.elena.mynotes.ui.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.elena.mynotes.R;
import com.example.elena.mynotes.database.entities.NoteEntity;

import java.text.DateFormat;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotePresentation {
    @BindView(R.id.tv_creationDate)
    TextView mCreationDateTv;

    @BindView(R.id.tv_editedDate)
    TextView mEditedDateTv;

    @BindView(R.id.tv_description)
    TextView mDescriptionTv;

    private ViewGroup mViewGroup;
    private View mView;

    private NoteEntity mNoteEntity;

    public NotePresentation(ViewGroup viewGroup) {
        mViewGroup = viewGroup;
    }

    public View view() {
        if (mView == null) {
            mView = LayoutInflater.from(mViewGroup.getContext()).inflate(R.layout.note_list_item, mViewGroup, false);
        }
        ButterKnife.bind(this, mView);
        return mView;
    }

    public void bind(NoteEntity noteEntity) {
        mNoteEntity = noteEntity;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

        String creationDate = dateFormat.format(mNoteEntity.creationDate);
        mCreationDateTv.setText(creationDate);

        String editedDate = dateFormat.format(mNoteEntity.modifiedDate);
        mEditedDateTv.setText(editedDate);

        mDescriptionTv.setText(mNoteEntity.description);
    }

    public void showDialogForUpdateNote(FragmentManager fragmentManager) {
        UpdateDeleteNoteDialogFragment.showDialog(fragmentManager, mNoteEntity.id);
    }
}
