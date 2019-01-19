package com.example.elena.mynotes.ui.notes;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class UpdateCategoryDialogFragment extends DialogFragment {
    private static final String TAG = "UpdateCategoryDialog";

    @BindView(R.id.et_categoryName)
    EditText mEditText;

    @BindView(R.id.btn_select_icon)
    Button mButton;

    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;

    boolean mIsClick = false;

    private NotesViewModel mViewModel;

    private static UpdateCategoryDialogFragment newInstance() {
        return new UpdateCategoryDialogFragment();
    }

    public static void showDialog(FragmentManager fragmentManager) {
        newInstance().show(fragmentManager, TAG);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        mViewModel = ViewModelProviders.of(activity).get(NotesViewModel.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_category, null);
        ButterKnife.bind(this, view);

        String lastCategoryName = mViewModel.categoryName.getValue();
        String lastCategoryIcon = mViewModel.imageName.getValue();

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

        builder.setTitle(getString(R.string.dialog_title_updateCategory))
                .setView(view)
                .setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newCategoryName = mEditText.getText().toString();
                        String newCategoryIcon = lastCategoryIcon;
                        int choiceId = mRadioGroup.getCheckedRadioButtonId();
                        if (choiceId > 0) {
                            RadioButton choiceRadioButton = view.findViewById(choiceId);
                            newCategoryIcon = choiceRadioButton.getTag().toString();
                        }

                        mViewModel.updateCategory(newCategoryName, newCategoryIcon);
                    }
                });

        return builder.create();
    }

}
