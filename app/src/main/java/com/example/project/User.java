package com.example.project;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class User {
    private String name, student_id;
    private int rating;
    private Uri PFP_URI;
    private boolean isAdmin;
    private static ArrayList<User> userArrayList = initArrayList();

    public User(String name, String student_id, Uri PFP_URI, int rating, boolean isAdmin) {
        this.name = name;
        this.student_id = student_id;
        this.PFP_URI = PFP_URI;
        this.rating = rating;
        this.isAdmin = false;
    }

    public String getName() {
        return name;
    }
    public String getStudent_id(){return student_id;}
    public Uri getPFP() {
        return PFP_URI;
    }
    public int getRating() {
        return rating;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String toString() {
        return "Name:" + getName() + "\nStudent ID:" + student_id + "\nURI:" + PFP_URI;
    }
    public static ArrayList<User> initArrayList() {
        userArrayList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String name = doc.get("name", String.class);
                    String URI = doc.get("PFP_URI", String.class);
                    String student_id = doc.get("student_id", String.class);
                    int rating = doc.get("rating", Integer.class);
                    if (URI != null)
                        addToArrayList(userArrayList, new User(name, student_id, Uri.parse(URI),rating,false));
                    else
                        addToArrayList(userArrayList, new User(name, student_id, null, rating,false));
                }
            }
        });
        return userArrayList;
    }
    public static ArrayList<User> addToArrayList(ArrayList<User> userArrayList1, User acc){
        userArrayList1.add(acc);
        userArrayList = userArrayList1;
        return userArrayList1;
    }
    public static ArrayList<User> getUserArrayList(){
        return userArrayList;
    }
    public static User findAccountByName(String name){
        for (User a: userArrayList) {
            if (a.getName().equals(name))
                return a;
        }
        return null;
    }
    public static User findAccountByStudentID(String student_id){
        for (User a: userArrayList) {
            if (a.getStudent_id().equals(student_id))
                return a;
        }
        return null;
    }

}

