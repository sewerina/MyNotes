package com.example.elena.mynotes.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.elena.mynotes.MyNotesApp;
import com.example.elena.mynotes.R;
import com.example.elena.mynotes.database.MyNotesDao;
import com.example.elena.mynotes.database.entities.CategoryEntity;
import com.example.elena.mynotes.database.entities.NoteEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    public static final String EXTRA_CATEGORY_NAME = "com.example.elena.mynotes.ui.categoryName";
    public static final String EXTRA_CATEGORY_ID = "com.example.elena.mynotes.ui.categoryId";

    @BindView(R.id.recyclerView_notes)
    RecyclerView mRecyclerView;

    @BindView(R.id.fab_add_note)
    FloatingActionButton mFab;

    private NoteAdapter mAdapter;
    private MyNotesDao mMyNotesDao;
    private int mCategoryId;
    private ActionBar mActionBar;

    private boolean mIsSort = false;

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

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(true);

        String name = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        setTitle(name);

        mCategoryId = getIntent().getIntExtra(EXTRA_CATEGORY_ID, -1);

        mMyNotesDao = MyNotesApp.getDatabase().myNotesDao();

        List<CategoryEntity> list = mMyNotesDao.categoryById(mCategoryId);
        if (!list.isEmpty()) {
            CategoryEntity categoryEntity = list.get(0);
            String imageName = categoryEntity.imageName;
            int id = getResources().getIdentifier(imageName, "drawable", getPackageName());
            mActionBar.setIcon(id);

        }

//        for(int k = 1; k <= 5; k++) {
//            NoteEntity noteEntity = new NoteEntity();
//            noteEntity.categoryId = mCategoryId;
//            noteEntity.id = 11 * k + mCategoryId;
//            noteEntity.description = "Something";
//            mMyNotesDao.createNote(noteEntity);
//        }

        List<NoteEntity> noteEntities = mMyNotesDao.getNotesByCategoryId(mCategoryId);
        mAdapter = new NoteAdapter(noteEntities);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNoteDialogFragment.showDialog(getSupportFragmentManager(), mCategoryId);
            }
        });
    }

    public void refreshAdapter() {
        List<NoteEntity> notes = mIsSort ? mMyNotesDao.getSortedNotesByCategoryId(mCategoryId) : mMyNotesDao.getNotesByCategoryId(mCategoryId);
        mAdapter.update(notes);
    }

    public void refreshToolbar() {
        CategoryEntity categoryEntity = mMyNotesDao.categoryById(mCategoryId).get(0);
        setTitle(categoryEntity.name);
        String imageName = categoryEntity.imageName;
        int id = getResources().getIdentifier(imageName, "drawable", getPackageName());
        mActionBar.setIcon(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_category:
                UpdateCategoryDialogFragment.showDialog(getSupportFragmentManager(), mCategoryId);
                return true;

            case R.id.sort_notes:
                mIsSort = !mIsSort;
                if (mIsSort) {
                    item.setIcon(R.drawable.ic_sort);
                    item.setTitle(R.string.sort);
                } else {
                    item.setIcon(R.drawable.ic_unsorted);
                    item.setTitle(R.string.unsorted);
                }
                refreshAdapter();
                return true;

            case R.id.delete_category:
                mMyNotesDao.deleteNotesByCategoryId(mCategoryId);
                refreshAdapter();
                mMyNotesDao.deleteCategory(mCategoryId);
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private NotePresentation mNotePresentation;

        public NoteHolder(@NonNull NotePresentation presentation) {
            super(presentation.view());
            this.itemView.setOnClickListener(this);
            mNotePresentation = presentation;
        }

        public void bind(NoteEntity noteEntity) {
            mNotePresentation.bind(noteEntity);
        }

        @Override
        public void onClick(View v) {
            mNotePresentation.showDialogForUpdateNote(getSupportFragmentManager());
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

        public void update(List<NoteEntity> notesByCategoryId) {
            mNotes.clear();
            mNotes.addAll(notesByCategoryId);
            notifyDataSetChanged();
        }
    }

}
