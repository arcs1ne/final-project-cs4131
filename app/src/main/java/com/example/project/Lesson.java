package com.example.project;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Lesson implements Comparable<Lesson> {
    private String title, h1, h2, h3;
    private int number;
    private HashMap<String, String> contentMap = new HashMap<>();
    private static ArrayList<Lesson> lessonArrayList = initArrayList();

    public Lesson (int number, String title, HashMap<String,String> content, String head1, String head2, String head3){
        this.title = title;
        this.number = number;
        this.contentMap = content;
        this.h1 = head1;
        this.h2 = head2;
        this.h3 = head3;
    }

    public HashMap<String, String> getContentMap() {
        return contentMap;
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getH1() {
        return h1;
    }

    public String getH2() {
        return h2;
    }

    public String getH3() {
        return h3;
    }

    public String toString() {
        return "Number:" + getNumber() + "\nTitle:" + getTitle() + "\nContent:" + getContentMap();
    }
    public static ArrayList<Lesson> initArrayList() {
        lessonArrayList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> recordingTask = db.collection("lessons").get();
        while(!recordingTask.isComplete()){}
        if(recordingTask.isSuccessful()) {
            QuerySnapshot snapshot = recordingTask.getResult();
            for (QueryDocumentSnapshot doc : snapshot) {
                int number = doc.get("number", Integer.class);
                String title = doc.get("title", String.class);
                String head1 = doc.get("head1", String.class);
                String cont1 = doc.get("cont1", String.class);
                String head2 = doc.get("head2", String.class);
                String cont2 = doc.get("cont2", String.class);
                String head3 = doc.get("head3", String.class);
                String cont3 = doc.get("cont3", String.class);
                HashMap<String, String> temp = new HashMap<>();
                temp.put(head1, cont1);
                temp.put(head2, cont2);
                temp.put(head3, cont3);
                addToArrayList(lessonArrayList, new Lesson(number, title, temp, head1, head2, head3));
            }
            Collections.sort(lessonArrayList);
        }
        return lessonArrayList;
    }
    public static ArrayList<Lesson> addToArrayList(ArrayList<Lesson> lessonArrayList1, Lesson les){
        lessonArrayList1.add(les);
        lessonArrayList = lessonArrayList1;
        return lessonArrayList;
    }
    public static ArrayList<Lesson> getLessonArrayList(){
        return lessonArrayList;
    }
    @Override
    public int compareTo(Lesson l) {
        return (this.getNumber() < l.getNumber() ? -1 : (this.getNumber() == l.getNumber() ? 0 : 1));
    }


}

