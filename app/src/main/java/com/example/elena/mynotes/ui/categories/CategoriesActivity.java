package com.example.elena.mynotes.ui.categories;

import android.content.Intent;
import android.os.Bundle;
import com.example.elena.mynotes.R;
import com.example.elena.mynotes.database.entities.CategoryEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView_categories)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.fab_add_category)
    FloatingActionButton mFab;

    @BindView(R.id.tv_emptyCategoryList)
    TextView mEmptyListTV;

    private CategoryAdapter mAdapter;
    private CategoriesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        ButterKnife.bind(this);

        mViewModel = ViewModelProviders.of(this).get(CategoriesViewModel.class);
        mViewModel.categories.observe(this, new Observer<List<CategoryEntity>>() {
            @Override
            public void onChanged(List<CategoryEntity> categoryEntities) {
                if (categoryEntities.isEmpty()) {
                    mEmptyListTV.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    mEmptyListTV.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                mAdapter.update(categoryEntities);
            }
        });

        mAdapter = new CategoryAdapter();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false));
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

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.loadCategories();
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
        private final List<CategoryEntity> mCategories = new ArrayList<>();

        public CategoryAdapter() {
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
