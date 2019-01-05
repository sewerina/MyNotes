package com.example.elena.mynotes.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.elena.mynotes.MyNotesApp;
import com.example.elena.mynotes.R;
import com.example.elena.mynotes.database.MyNotesDao;
import com.example.elena.mynotes.database.entities.CategoryEntity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateCategoryDialogFragment extends DialogFragment {
    private static final String TAG = "UpdateCategoryDialog";
    private static final String CATEGORY_ID = "com.example.elena.mynotes.ui.UpdateCategoryDialogFragment.categoryId";

    @BindView(R.id.et_categoryName)
    EditText mEditText;

    @BindView(R.id.btn_select_icon)
    Button mButton;

    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;

    boolean mIsClick = false;

    private int mCategoryId;
    private MyNotesDao mDao;

    private static UpdateCategoryDialogFragment newInstance(int categoryId) {
        UpdateCategoryDialogFragment dialogFragment = new UpdateCategoryDialogFragment();
        Bundle args = new Bundle();
        args.putInt(CATEGORY_ID, categoryId);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    public static void showDialog(FragmentManager fragmentManager, int categoryId) {
        newInstance(categoryId).show(fragmentManager, TAG);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_category, null);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            mCategoryId = getArguments().getInt(CATEGORY_ID);
        }

        mDao = MyNotesApp.getDatabase().myNotesDao();
        CategoryEntity categoryEntity = mDao.categoryById(mCategoryId).get(0);
        String lastCategoryName = categoryEntity.name;
        String lastCategoryIcon = categoryEntity.imageName;

        mEditText.setText(lastCategoryName);
        RadioButton checkedRadioButton = mRadioGroup.findViewWithTag(lastCategoryIcon);
        checkedRadioButton.setChecked(true);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsClick = !mIsClick;
                if (mIsClick) {
                    mRadioGroup.setVisibility(View.VISIBLE);
                } else {
                    mRadioGroup.setVisibility(View.GONE);
                }
            }
        });

        builder.setTitle("Update your category")
                .setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newCategoryName = mEditText.getText().toString();
                        String newCategoryIcon = lastCategoryIcon;
                        int choiceId = mRadioGroup.getCheckedRadioButtonId();
                        if (choiceId > 0) {
                            RadioButton choiceRadioButton = view.findViewById(choiceId);
                            newCategoryIcon = choiceRadioButton.getTag().toString();
                        }

                        if (!newCategoryName.isEmpty() && !newCategoryName.equals(lastCategoryName)) {
                            categoryEntity.name = newCategoryName;
                        }
                        if (!newCategoryIcon.equals(lastCategoryIcon)) {
                            categoryEntity.imageName = newCategoryIcon;
                        }
                        mDao.updateCategory(categoryEntity);

                        if (activity instanceof CategoryActivity) {
                            ((CategoryActivity) activity).refreshToolbar();
                        }
                    }
                });

        return builder.create();
    }

}
