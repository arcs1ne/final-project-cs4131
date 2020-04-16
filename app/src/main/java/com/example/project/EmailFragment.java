package com.example.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class EmailFragment extends Fragment {
    String namestr, classstr, subjectstr, bodystr;
    Button send;
    TextInputEditText name, classs, subject, body;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_email, container, false);
        name = root.findViewById(R.id.name2);
       classs = root.findViewById(R.id.class2);
        subject = root.findViewById(R.id.email2);
       body = root.findViewById(R.id.body2);
        send = root.findViewById(R.id.send);
        TextWatcher tw = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                namestr = name.getText().toString().trim();
                classstr = classs.getText().toString().trim();
                subjectstr = subject.getText().toString().trim();
                bodystr = body.getText().toString().trim();
               if(namestr.length() > 0 && classstr.length() > 0 && subjectstr.length() > 0 && bodystr.length() > 0)
                    send.setEnabled(true);
                else
                    send.setEnabled(false);
            }
        };
        name.addTextChangedListener(tw);
        classs.addTextChangedListener(tw);
        subject.addTextChangedListener(tw);
        body.addTextChangedListener(tw);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(namestr, classstr, subjectstr, bodystr);
            }
        });
        return root;
    }
    public void sendEmail(String name, String classs, String subject, String body){
        String[] TO = {"h1710137@nushigh.edu.sg"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "To the developers:\n\nI am " + name + " from class " + classs + ". " + body);
        try{
            startActivity(Intent.createChooser(emailIntent, "Choose an email client"));
        }
        catch(android.content.ActivityNotFoundException e){
            Toast.makeText(getContext(), "No email client found", Toast.LENGTH_LONG).show();
        }

    }
}
