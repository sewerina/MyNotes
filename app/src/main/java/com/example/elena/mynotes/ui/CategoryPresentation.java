package com.example.elena.mynotes.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.elena.mynotes.R;
import com.example.elena.mynotes.database.entities.CategoryEntity;
import com.example.elena.mynotes.model.Category;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryPresentation {
    @BindView(R.id.iv_category)
    ImageView mImageView;

    @BindView(R.id.tv_categoryName)
    TextView mTextView;

    private ViewGroup mViewGroup;
    private View mView;

    private CategoryEntity mCategory;

    public CategoryPresentation(ViewGroup viewGroup) {
        mViewGroup = viewGroup;
    }

    public View view () {
        if (mView == null) {
            mView = LayoutInflater.from(mViewGroup.getContext()).inflate(R.layout.category_list_item, mViewGroup, false);
        }
        ButterKnife.bind(this, mView);
        return mView;
    }

    public void bind(CategoryEntity categoryEntity) {
        String imageName = categoryEntity.imageName;
        Resources resources = mViewGroup.getContext().getResources();
        int id = resources.getIdentifier(imageName, "drawable",
                mViewGroup.getContext().getPackageName());

        mImageView.setImageResource(id);
        mImageView.setTag(imageName);
        mTextView.setText(categoryEntity.name);

        mCategory = categoryEntity;
    }

    public Intent navigateToCategoryActivity() {
        Intent intent = null;
        if (mCategory != null) {
            intent = CategoryActivity.newIntent(mViewGroup.getContext(), mCategory.name, mCategory.id);
        }
        // TODO: do not return null
        return intent;
    }
}
