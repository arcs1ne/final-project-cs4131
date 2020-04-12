package com.example.project;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;

public class LessonDialog extends DialogFragment {

    public static final String TAG = "example_dialog";
    static Lesson mles;
    private Toolbar toolbar;

    public static LessonDialog display(FragmentManager fragmentManager, Lesson les) {
        LessonDialog lessonDialog = new LessonDialog();
        mles = les;
        lessonDialog.show(fragmentManager, TAG);
        return lessonDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.lesson_dialog, container, false);
        TextInputLayout til1  = view.findViewById(R.id.til1);
        TextInputLayout til2 = view.findViewById(R.id.til2);
        TextInputLayout til3 = view.findViewById(R.id.til3);
        TextInputEditText content1 = view.findViewById(R.id.mcont1);
        TextInputEditText content2 = view.findViewById(R.id.mcont2);
        TextInputEditText content3 = view.findViewById(R.id.mcont3);

        til1.setHint(mles.getH1());
        content1.setText(mles.getContentMap().get(mles.getH1()));
        til2.setHint(mles.getH2());
        content2.setText(mles.getContentMap().get(mles.getH2()));
        if(mles.getContentMap().get(mles.getH2()) == null){
            til2.setVisibility(View.INVISIBLE);
            til3.setVisibility(View.INVISIBLE);
        }
        content3.setText(mles.getContentMap().get(mles.getH3()));
        til3.setHint(mles.getH3());
        if(mles.getContentMap().get(mles.getH3()) == null){
            til3.setVisibility(View.INVISIBLE);
        }
        toolbar = view.findViewById(R.id.toolbar);

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LessonDialog.this.dismiss();
            }
        });
        toolbar.setTitle("Lesson " + mles.getNumber() + ": " + mles.getTitle());
    }
}