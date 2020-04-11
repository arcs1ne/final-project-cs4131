package com.example.project;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

public class WordCheckerFragment extends Fragment {
    static Button b;
    static TextInputEditText e;
    static TextView v;
    static String s;
    HashMap<String,String>dm = LoginActivity.getDefinitionMap();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_checker, container, false);
        //

    //
        b = root.findViewById(R.id.button);
        e = root.findViewById(R.id.checkTextField);
        v = root.findViewById(R.id.definition);
        TextWatcher tw = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable r) {
                s = e.getText().toString().trim();
                //if(e.getCurrentTextColor() == ContextCompat.getColor(getContext(), R.color.green) || e.getCurrentTextColor() == ContextCompat.getColor(getContext(), R.color.red) ){
                //    e.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                //    e.setText("");
               // }
                if (s.length() > 1 && s.length() < 16) {
                    b.setEnabled(true);
                } else {
                    b.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        e.addTextChangedListener(tw);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv) {
                e.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                e.setText("");
                v.setText("");
                b.setEnabled(true);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidity(e.getText().toString());
            }
        });
        // INIT WORDS AND DEFINTIONS


            /*
            final InputStream file = getContext().getAssets().open("csw19_1.txt");
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            while(line.trim() != null){
                line = reader.readLine();
                String parts[] = line.split(" ", 2);
                definitionMap.put(parts[0], parts[1]);

             */
        return root;
    }

    public boolean checkValidity(String s) {
        boolean contains = dm.containsKey(s.toLowerCase().trim());
        if (contains) {
            String definition = dm.get(s);
            v.setText(definition);
            e.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            e.setText(e.getText().toString() + " is a word");
        } else {
            e.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            e.setText(e.getText().toString() + " is not a word");
        }
        b.setEnabled(false);
        return (contains);
    }
    //public HashMap<String, String> getDefinitionMap(){}

}

