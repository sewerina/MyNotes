package com.example.elena.mynotes.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.example.elena.mynotes.R;

public class CategoryActivity extends AppCompatActivity {
    private static final String EXTRA_CATEGORY_NAME = "com.example.elena.mynotes.ui.categoryName";
    private static final String EXTRA_CATEGORY_ID = "com.example.elena.mynotes.ui.categoryId";

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

        String name = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        setTitle(name);
    }
}
