package com.example.elena.mynotes.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.elena.mynotes.MyNotesApp;
import com.example.elena.mynotes.R;
import com.example.elena.mynotes.database.MyNotesDao;
import com.example.elena.mynotes.database.entities.CategoryEntity;
import com.example.elena.mynotes.model.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView_categories)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.fab_add_category)
    FloatingActionButton mFab;

    private MyNotesDao mMyNotesDao;
    private CategoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        ButterKnife.bind(this);

        mMyNotesDao = MyNotesApp.getDatabase().myNotesDao();

//        for (int i = 1; i <= 10; i++) {
//            CategoryEntity entity = new CategoryEntity();
//            entity.id = i;
//            entity.name = String.valueOf(i);
//            mMyNotesDao.createCategory(entity);
//        }

        List<CategoryEntity> categories = mMyNotesDao.getAllCategories();
        mAdapter = new CategoryAdapter(categories);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(mAdapter);

        setSupportActionBar(mToolbar);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Add new category", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                CreateCategoryDialogFragment.showDialog(getSupportFragmentManager());
            }
        });
    }

    public void refresh(){
        mAdapter.update(mMyNotesDao.getAllCategories());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CategoryPresentation mCategoryPresentation;

        public CategoryHolder(@NonNull CategoryPresentation categoryPresentation) {
            super(categoryPresentation.view());
            this.itemView.setOnClickListener(this);
            mCategoryPresentation = categoryPresentation;
        }

        public void bind(CategoryEntity categoryEntity) {
            mCategoryPresentation.bind(categoryEntity);
        }

        @Override
        public void onClick(View v) {
            Intent intent = mCategoryPresentation.navigateToCategoryActivity();
            startActivity(intent);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
        private List<CategoryEntity> mCategories;

        public CategoryAdapter(List<CategoryEntity> categories) {
            mCategories = categories;
        }

        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CategoryHolder(new CategoryPresentation(parent));
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
            CategoryEntity categoryEntity = mCategories.get(position);
            holder.bind(categoryEntity);
        }

        @Override
        public int getItemCount() {
            return mCategories.size();
        }

        public void update(List<CategoryEntity> categories) {
            mCategories.clear();
            mCategories.addAll(categories);
            notifyDataSetChanged();
        }
    }

}
