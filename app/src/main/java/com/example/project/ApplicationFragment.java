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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ApplicationFragment extends Fragment {
    TextInputEditText name, id, mg, reason;
    String namestr, idstr, mgstr, reasonstr;
    Button apply;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_apply, container, false);
        name = root.findViewById(R.id.nameField);
        mg = root.findViewById(R.id.classField);
        id = root.findViewById(R.id.studentIDfield);
        reason = root.findViewById(R.id.reason);
        apply = root.findViewById(R.id.apply);
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
                idstr = id.getText().toString().trim();
                mgstr = mg.getText().toString().trim();
                reasonstr = reason.getText().toString().trim();
                if(namestr.length() > 0 && idstr.length() > 0 && mgstr.length() > 0 && reasonstr.length() > 0)
                    apply.setEnabled(true);
                else
                    apply.setEnabled(false);
            }
        };
        name.addTextChangedListener(tw);
        mg.addTextChangedListener(tw);
        id.addTextChangedListener(tw);
        reason.addTextChangedListener(tw);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(namestr, mgstr, idstr, reasonstr);
            }
        });
        return root;
    }
    public void sendEmail(String namee, String mgg, String idd, String reasonn){
        String[] TO = {"h1710137@nushigh.edu.sg"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Scrabble Club Application: " + namee);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "To Scrabble Club ExCo:\n\nI am " + namee + " (" + idd + ") from class " + mgg + ". and I am applying for Scrabble Club because " + reasonn);
        try{
            startActivity(Intent.createChooser(emailIntent, "Choose an email client"));
        }
        catch(android.content.ActivityNotFoundException e){
            Toast.makeText(getContext(), "No email client found", Toast.LENGTH_LONG).show();
        }

    }
}
