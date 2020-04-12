package com.example.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import java.util.regex;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {
    Uri FILE_URI;
    ArrayList<User> userArrayList = User.initArrayList();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextWatcher tw = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                TextInputEditText et4 = findViewById(R.id.studentIDfield);
                TextInputEditText et5 = findViewById(R.id.nameField);
                TextInputEditText et6 = findViewById(R.id.pwField);
                TextInputEditText et7 = findViewById(R.id.cfmpwd);
                Button b = findViewById(R.id.reg);
                if(!et4.getText().toString().equals("") && !et5.getText().toString().equals("")&& !et7.getText().toString().equals("")&& !et6.getText().toString().equals(""))
                    b.setEnabled(true);
                else{
                    b.setEnabled(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };

        TextInputEditText et4 = findViewById(R.id.studentIDfield);
        TextInputEditText et5 = findViewById(R.id.nameField);
        TextInputEditText et6 = findViewById(R.id.pwField);
        TextInputEditText et7 = findViewById(R.id.cfmpwd);
        et4.addTextChangedListener(tw);
        et5.addTextChangedListener(tw);
        et6.addTextChangedListener(tw);
        et7.addTextChangedListener(tw);
    }
    /*
    public void choosePfp(View view){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                1);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    if (data!=null) {
                        ImageView imageR = findViewById(R.id.pfp);
                        FILE_URI = data.getData();
                        imageR.setImageURI(FILE_URI);
                    }
                }
        }
    }

     */
    public void handleFinaliseRegister(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> field = new HashMap<String, Object>();
        TextInputEditText et = findViewById(R.id.nameField);
        String name = et.getText().toString().trim();
        TextInputEditText sid = findViewById(R.id.studentIDfield);
        String student_id = sid.getText().toString().trim();
        TextInputEditText pw = findViewById(R.id.pwField);
        String password = pw.getText().toString().trim();
        TextInputEditText cpw = findViewById(R.id.cfmpwd);
        String confirmed = cpw.getText().toString().trim();
        if (!password.equals(confirmed)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Both password fields do not match", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        if(password.length() == 0 || name.length() == 0 || student_id.length() == 0){
            Toast toast = Toast.makeText(getApplicationContext(), "All fields must be filled up!", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        else if(!student_id.matches("h[0-9]{7,7}")){
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid student ID!", Toast.LENGTH_LONG);
            toast.show();
        }
        if(password.length()<8){
            Toast toast = Toast.makeText(getApplicationContext(), "Password must be 8 digits or more!", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        field.put("name", name);
        field.put("student_id", student_id);
        field.put("password", password);
        field.put("rating", 0);
        /*
        // STORAGE [START]
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        Uri file = FILE_URI;
        StorageReference pfpRef = storageRef.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = pfpRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(), "Unsuccessful upload", Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
        // STORAGE [END]
         */
        /*
        if (FILE_URI != null) {field.put("PFP_URI", FILE_URI.toString());}
        else{field.put("PFP_URI", "");}
        Log.d("Register died", name + " " + student_id + " " + FILE_URI);
         */
        userArrayList.add(new User(name,student_id,false));
        db.collection("projectusers").document(name).set(field);
        Toast toast = Toast.makeText(getApplicationContext(), "Successfully registered!", Toast.LENGTH_LONG);
        toast.show();
        Intent intent = new Intent(this, ManualLoginActivity.class);
        startActivity(intent);
    }
}
