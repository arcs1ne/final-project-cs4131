package com.example.project;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddLessonDialog extends DialogFragment {
    static ArrayList<Lesson> lessonArrayList = Lesson.getLessonArrayList();

    public static final String TAG = "example_dialog";
    private Toolbar toolbar;

    public static AddLessonDialog display(FragmentManager fragmentManager) {
        AddLessonDialog lessonDialog = new AddLessonDialog();
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
        View view = inflater.inflate(R.layout.addlesson_dialog, container, false);
        toolbar = view.findViewById(R.id.toolbar);

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddLessonDialog.this.dismiss();
            }
        });
        toolbar.setTitle("Add lesson");
        toolbar.inflateMenu(R.menu.addlesson);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                HashMap<String,String> temp = new HashMap<>();
                Map<String, Object> field = new HashMap<String, Object>();
                TextInputEditText title = view.findViewById(R.id.title);
                TextInputEditText h1 = view.findViewById(R.id.h1);
                TextInputEditText h2 = view.findViewById(R.id.h2);
                TextInputEditText h3 = view.findViewById(R.id.h3);
                TextInputEditText c1 = view.findViewById(R.id.c1);
                TextInputEditText c2 = view.findViewById(R.id.c2);
                TextInputEditText c3 = view.findViewById(R.id.c3);
                field.put("number", Lesson.getLessonArrayList().size()+1);
                field.put("title", title.getText().toString().trim());
                try {
                    field.put("head1", h1.getText().toString().trim());
                    field.put("cont1", c1.getText().toString().trim());
                    field.put("head2", h2.getText().toString().trim());
                    field.put("cont2", c2.getText().toString().trim());
                    field.put("head3", h3.getText().toString().trim());
                    field.put("cont3", c3.getText().toString().trim());
                    temp.put("head1", h1.getText().toString().trim());
                    temp.put("cont1", c1.getText().toString().trim());
                    temp.put("head2", h2.getText().toString().trim());
                    temp.put("cont2", c2.getText().toString().trim());
                    temp.put("head3", h3.getText().toString().trim());
                    temp.put("cont3", c3.getText().toString().trim());
                }
                catch (NullPointerException e) {
                    if(h1.getText() == null || c1.getText()== null)
                        Toast.makeText(getContext(), "At least one pair of heading and content needs to be filled", Toast.LENGTH_SHORT).show();
                    else if (h2.getText() == null || c2.getText() == null) {
                        field.put("head2", null);
                        field.put("cont2", null);
                        field.put("head3", null);
                        field.put("cont3",null);
                        temp.put("head2", null);
                        temp.put("cont2", null);
                        temp.put("head3", null);
                        temp.put("cont3",null);

                    }
                    else {
                        field.put("head3", null);
                        field.put("cont3",null);
                        temp.put("head3", null);
                        temp.put("cont3",null);
                    }
                }
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("lessons").document().set(field);
                if(h2.getText() == null) {
                    Lesson.addToArrayList(lessonArrayList, new Lesson(Lesson.getLessonArrayList().size() + 1, title.getText().toString().trim(), temp, h1.getText().toString().trim(), null, null));
                }
                else if(h3.getText() == null) {
                    Lesson.addToArrayList(lessonArrayList, new Lesson(Lesson.getLessonArrayList().size() + 1, title.getText().toString().trim(), temp, h1.getText().toString().trim(), h2.getText().toString().trim(), null));
                }
                else {
                    Lesson.addToArrayList(lessonArrayList, new Lesson(Lesson.getLessonArrayList().size() + 1, title.getText().toString().trim(), temp, h1.getText().toString().trim(), h2.getText().toString().trim(), h3.getText().toString().trim()));
                }
                LessonFragment.lva.notifyDataSetChanged();
                AddLessonDialog.this.dismiss();
                return true;
            }
        });
    }
}