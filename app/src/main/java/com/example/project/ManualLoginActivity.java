package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ManualLoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextWatcher tw = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Button login = findViewById(R.id.login);
                EditText et4 = findViewById(R.id.username);
                EditText et5 = findViewById(R.id.password);
                if(!et4.getText().toString().equals("") && !et5.getText().toString().equals(""))
                    login.setEnabled(true);
                else
                    login.setEnabled(false);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };
        EditText et4 = findViewById(R.id.username);
        EditText et5 = findViewById(R.id.password);
        et4.addTextChangedListener(tw);
        et5.addTextChangedListener(tw);
    }
    public void handleRegister(View view){
        Intent intent = new Intent(ManualLoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
    public void handlelogin(View view){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        EditText et = findViewById(R.id.username);
        EditText pw = findViewById(R.id.password);
        if(et.getText().toString().trim().equals("admin") && pw.getText().toString().trim().equals("1")){
            Toast toast3 = Toast.makeText(getApplicationContext(), "Admin login successful!", Toast.LENGTH_LONG);
            toast3.show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("name", "Admin");
            intent.putExtra("status", "Administrator");
            startActivity(intent);
            return;
        }
        String student_id_or_username = et.getText().toString().trim();
        String docName;
        User accbyname = User.findAccountByName(student_id_or_username);
        User accbyid = User.findAccountByStudentID(student_id_or_username);
        if (accbyname == null){
            if (accbyid== null){
                Toast.makeText(getApplicationContext(), "Invalid login!", Toast.LENGTH_LONG).show();
                return;
            }
            else{
                docName = accbyid.getName();
            }
        }
        else{
            docName = accbyname.getName();
        }
        try {
            Task<DocumentSnapshot> documentTask = db.collection("projectusers").document(docName).get();
            while (!documentTask.isComplete()) {}
            if (documentTask.isSuccessful()) {
                DocumentSnapshot snapshot = documentTask.getResult();
                String password = snapshot.get("password", String.class);
                if (pw.getText().toString().equals(password)) {
                    Toast toast3 = Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG);
                    toast3.show();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("name", docName);
                    intent.putExtra("status", "User");
                    startActivity(intent);
                }
                else{
                    Toast toast4 = Toast.makeText(getApplicationContext(), "Invalid login!", Toast.LENGTH_LONG);
                    toast4.show();
                    return;
                }
            }
        }
        catch(Exception e){
            Toast toast5 = Toast.makeText(getApplicationContext(), "Invalid login!", Toast.LENGTH_LONG);
            toast5.show();
            Log.w(TAG, "Error writing document", e);
        }
    }
}
