package com.example.elena.mynotes.ui.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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
import com.example.elena.mynotes.R;
import com.example.elena.mynotes.database.entities.NoteEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    public static final String EXTRA_CATEGORY_ID = "com.example.elena.mynotes.ui.categoryId";

    @BindView(R.id.recyclerView_notes)
    RecyclerView mRecyclerView;

    @BindView(R.id.fab_add_note)
    FloatingActionButton mFab;

    private NoteAdapter mAdapter;

    private ActionBar mActionBar;

    private int mCategoryId;

    private NotesViewModel mViewModel;

    public static Intent newIntent(Context context, int categoryId) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ButterKnife.bind(this);

        mViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
        mViewModel.notes.observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> noteEntities) {
                mAdapter.update(noteEntities);
            }
        });
        mViewModel.imageName.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String imageName) {
                int id = getResources().getIdentifier(imageName, "drawable", getPackageName());
                mActionBar.setIcon(id);
            }
        });
        mViewModel.categoryName.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String categoryName) {
                setTitle(categoryName);
            }
        });

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(true);

        mCategoryId = getIntent().getIntExtra(EXTRA_CATEGORY_ID, -1);
        mViewModel.load(mCategoryId);

        mAdapter = new NoteAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNoteDialogFragment.showDialog(getSupportFragmentManager());
            }
        });
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
                UpdateCategoryDialogFragment.showDialog(getSupportFragmentManager());
                return true;

            case R.id.sort_notes:
                if (mViewModel.switchNotesOrder()) {
                    item.setIcon(R.drawable.ic_sort);
                    item.setTitle(R.string.sort);
                } else {
                    item.setIcon(R.drawable.ic_unsorted);
                    item.setTitle(R.string.unsorted);
                }
                return true;

            case R.id.delete_category:
                mViewModel.deleteCategory();
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
        private List<NoteEntity> mNotes = new ArrayList<>();

        public NoteAdapter() {
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
