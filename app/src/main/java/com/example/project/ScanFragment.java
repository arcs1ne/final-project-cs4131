package com.example.project;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_LONG;

public class ScanFragment extends Fragment {
    private ImageView i;
    private FloatingActionButton c, u, n;
    private TextView t,s;
    private TextInputEditText d;
    String resultText;
    Uri imageUri, photoURI;
    FirebaseVisionImage image;
    static final int REQUEST_IMAGE_CAPTURE = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scan, container, false);
        s = root.findViewById(R.id.word);
        i = root.findViewById(R.id.image);
        t = root.findViewById(R.id.noImage);
        c = root.findViewById(R.id.takePic);
        u = root.findViewById(R.id.uploadPic);
        n = root.findViewById(R.id.send);
        d = root.findViewById(R.id.changableText);
        d.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {   }
            @Override
            public void afterTextChanged(Editable s) {
                resultText = d.getText().toString().toLowerCase().trim();
            }
        });
        View.OnClickListener o = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.takePic:
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // Ensure that there's a camera activity to handle the intent
                        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                            // Create the File where the photo should go
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File
                                Log.e("TAG", ex.getMessage());
                            }
                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                photoURI = FileProvider.getUriForFile(getContext(),
                                        "com.example.project.fileprovider",
                                        photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                            }
                        }
                        break;
                    case R.id.uploadPic:
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                                3);

                        break;
                    case R.id.send:
                        d.setEnabled(false);
                        new FetchAPI().execute();

                }
            }
        };
        c.setOnClickListener(o);
        u.setOnClickListener(o);
        n.setOnClickListener(o);
        return root;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String path;
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE://take picture
                if (resultCode == RESULT_OK) {
                    try {
                        image = FirebaseVisionImage.fromFilePath(getContext(), photoURI);
                        scan();
                        i.setImageURI(photoURI);
                        if (photoURI != null){
                            t.setText("");
                            s.setText("");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    try {
                        photoURI = data.getData();
                        image = FirebaseVisionImage.fromFilePath(getContext(), photoURI);
                        scan();
                        i.setImageURI(photoURI);
                        if (photoURI != null){
                            t.setText("");
                            s.setText("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
    String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    public void scan(){
        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        if (image == null){
            Toast.makeText(getContext(), "No image uploaded!", LENGTH_LONG).show();
            return;
        }
        textRecognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                try{
                    resultText = firebaseVisionText.getText();
                    resultText = resultText.replaceAll("[^a-zA-Z]","").toLowerCase().trim();
                    Log.d("SCAN", resultText);
                    d.setText(resultText);
                    d.setEnabled(true);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Make sure your image consists of only the rack!", Toast.LENGTH_SHORT).show();;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class FetchAPI extends AsyncTask<Void, Void, Void> {
        String data = "";
        String dataParsed = "";
        @Override
        protected Void doInBackground(Void... urls) {
            try {
                URL url = new URL("http://www.anagramica.com/best/:" + resultText);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        data += line;
                    }
                    bufferedReader.close();
                    JSONObject JO = new JSONObject(data);
                    JSONArray best = JO.getJSONArray("best");
                    // if word exists
                    if (best.length() != 0) {
                        // get all best words
                        for (int i = 0; i < best.length(); i++){
                            if (i > 0){
                                dataParsed+= ", ";
                            }
                            dataParsed += best.getString(i);
                        }
                        // set best words in text view
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                s.setText("Best words: " + dataParsed);
                            }
                        });
                    }
                    else {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                s.setText("No word could be formed!");
                            }
                        });
                    }
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        s.setText("There was an error processing the image.");
                    }
                });
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
            return null;
        }
    }
}
