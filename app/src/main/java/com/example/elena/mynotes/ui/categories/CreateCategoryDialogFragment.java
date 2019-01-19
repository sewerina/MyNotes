package com.example.elena.mynotes.ui.categories;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.elena.mynotes.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateCategoryDialogFragment extends DialogFragment {
    private static final String TAG = "CreateCategoryDialog";

    @BindView(R.id.et_categoryName)
    EditText mEditText;

    @BindView(R.id.btn_select_icon)
    Button mButton;

    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;

    boolean mIsClick = false;

    private CategoriesViewModel mViewModel;

    private static CreateCategoryDialogFragment newInstance() {
        CreateCategoryDialogFragment dialogFragment = new CreateCategoryDialogFragment();
        return dialogFragment;
    }

    public static void showDialog(FragmentManager fragmentManager) {
        newInstance().show(fragmentManager, TAG);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        mViewModel = ViewModelProviders.of(activity).get(CategoriesViewModel.class);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_category, null);
        ButterKnife.bind(this, view);

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

        dialogBuilder.setTitle(getString(R.string.dialog_title_createCategory))
                .setView(view)
                .setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = mEditText.getText().toString();
                        String icon = "city";

                        int choiceId = mRadioGroup.getCheckedRadioButtonId();

                        if (choiceId > 0) {
                            RadioButton choiceRadioButton = view.findViewById(choiceId);
                            icon = choiceRadioButton.getTag().toString();
                        }

                        if (!name.isEmpty()) {
                            mViewModel.createCategory(name, icon);
                        } else {
                            Toast.makeText(activity, "Enter correct a category name!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return dialogBuilder.create();
    }
}
