package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LessonFragment extends Fragment {
    static LessonsViewAdapter lva;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lesson, container, false);
        ExtendedFloatingActionButton efab = root.findViewById(R.id.addlesson);
        // recycler view
        final RecyclerView tv = root.findViewById(R.id.lessonsRV);
        lva = new LessonsViewAdapter(this.getContext(), Lesson.getLessonArrayList());
        tv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        tv.setAdapter(lva);
        efab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AppCompatActivity a = (AppCompatActivity)getContext();
                AddLessonDialog.display(a.getSupportFragmentManager());
            }
        });
        return root;
    }
}
