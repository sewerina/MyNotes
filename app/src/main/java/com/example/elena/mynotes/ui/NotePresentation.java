package com.example.elena.mynotes.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.elena.mynotes.R;
import com.example.elena.mynotes.database.entities.NoteEntity;
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

//        mCreationDateTv.setText(mNoteEntity.creationDate.getDate());
//        mEditedDateTv.setText(mNoteEntity.modifiedDate.getDate());
        mDescriptionTv.setText(mNoteEntity.description);
    }
}