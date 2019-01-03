package com.example.elena.mynotes.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.example.elena.mynotes.MyNotesApp;
import com.example.elena.mynotes.R;
import com.example.elena.mynotes.database.MyNotesDao;
import com.example.elena.mynotes.database.entities.NoteEntity;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private static final String EXTRA_CATEGORY_NAME = "com.example.elena.mynotes.ui.categoryName";
    private static final String EXTRA_CATEGORY_ID = "com.example.elena.mynotes.ui.categoryId";

    @BindView(R.id.recyclerView_notes)
    RecyclerView mRecyclerView;

    private NoteAdapter mAdapter;
    private MyNotesDao mMyNotesDao;

    public static Intent newIntent(Context context, String name, int id) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(EXTRA_CATEGORY_NAME, name);
        intent.putExtra(EXTRA_CATEGORY_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ButterKnife.bind(this);

        String name = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        setTitle(name);

        int categoryId = getIntent().getIntExtra(EXTRA_CATEGORY_ID, 0);

        mMyNotesDao = MyNotesApp.getDatabase().myNotesDao();

        for(int k = 1; k <= 5; k++) {
            NoteEntity noteEntity = new NoteEntity();
            noteEntity.categoryId = categoryId;
            noteEntity.id = 11 * k;
            noteEntity.description = "Something";
            mMyNotesDao.createNote(noteEntity);
        }

        List<NoteEntity> noteEntities = mMyNotesDao.getAllNotes();
        mAdapter = new NoteAdapter(noteEntities);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private class NoteHolder extends RecyclerView.ViewHolder {
        private NotePresentation mNotePresentation;

        public NoteHolder(@NonNull NotePresentation presentation) {
            super(presentation.view());
            mNotePresentation = presentation;
        }

        public void bind(NoteEntity noteEntity) {
            mNotePresentation.bind(noteEntity);
        }
    }

    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {
        private List<NoteEntity> mNotes;

        public NoteAdapter(List<NoteEntity> notes) {
            mNotes = notes;
        }

        @NonNull
        @Override
        public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new NoteHolder(new NotePresentation(parent));
        }

        @Override
        public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
            NoteEntity noteEntity = mNotes.get(position);
            holder.bind(noteEntity);
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }
    }
}
