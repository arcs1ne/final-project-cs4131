package com.example.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class QuizFragment extends Fragment {
    int[] retina = {0,1,7,3,3,2,5,3,1,2,1,6,3,2,2,4,0,3,11,6,4,0,2,0,0,0};
    int[] satire = {3,4,4,10,2,1,8,2,2,0,2,6,7,11,3,8,0,4,5,8,0,4,4,0,0,0};
    int[] satine = {2,4,3,7,1,3,11,2,1,2,3,9,6,4,1,4,0,11,6,3,2,3,4,2,0,2};
    String valid4Q = "AQUA\tQATS\tQUAD\tQUAI\tQUEY\tQUIN\tQUIT\tQUOD\n" +
            "QAID\tQOPH\tQUAG\tQUAY\tQUID\tQUIP\tQUIZ\tSUQS";
    String valid4J = "AJAR\tJADE\tJARS\tJEEP\tJEST\tJIGS\tJOBS\tJOLT\tJUBE\tJURY\n" +
            "AJEE\tJAGG\tJATO\tJEER\tJETE\tJILL\tJOCK\tJOSH\tJUDO\tJUST\n" +
            "DJIN\tJAGS\tJAUK\tJEES\tJETS\tJILT\tJOES\tJOSS\tJUGA\tJUTE\n" +
            "DOJO\tJAIL\tJAUP\tJEEZ\tJEUX\tJIMP\tJOEY\tJOTA\tJUGS\tJUTS\n" +
            "FUJI\tJAKE\tJAVA\tJEFE\tJEWS\tJINK\tJOGS\tJOTS\tJUJU\tMOJO\n" +
            "HADJ\tJAMB\tJAWS\tJEHU\tJIAO\tJINN\tJOHN\tJOUK\tJUKE\tPUJA\n" +
            "HAJI\tJAMS\tJAYS\tJELL\tJIBB\tJINS\tJOIN\tJOWL\tJUMP\tRAJA\n" +
            "HAJJ\tJANE\tJAZZ\tJEON\tJIBE\tJINX\tJOKE\tJOWS\tJUNK\tSOJA\n" +
            "JABS\tJAPE\tJEAN\tJERK\tJIBS\tJISM\tJOKY\tJOYS\tJUPE\n" +
            "JACK\tJARL\tJEED\tJESS\tJIFF\tJIVE\tJOLE\tJUBA\tJURA";
    String valid4X = "APEX\tAXLE\tDEXY\tEXIT\tFLEX\tJEUX\tMOXA\tOXES\tSEXY\n" +
            "AXAL\tAXON\tDOUX\tEXON\tFLUX\tJINX\tNEXT\tOXID\tTAXA\n" +
            "AXED\tBOXY\tDOXY\tEXPO\tFOXY\tLUXE\tNIXE\tOXIM\tTAXI\n" +
            "AXEL\tCALX\tEAUX\tFALX\tHOAX\tLYNX\tNIXY\tPIXY\tTEXT\n" +
            "AXES\tCOAX\tEXAM\tFAUX\tIBEX\tMAXI\tONYX\tPREX\tVEXT\n" +
            "AXIL\tCOXA\tEXEC\tFIXT\tILEX\tMINX\tORYX\tROUX\tWAXY\n" +
            "AXIS\tCRUX\tEXES\tFLAX\tIXIA\tMIXT\tOXEN\tSEXT\tXYST\n";
    String letters = "abcdefghikl;mnopqrstuvwxyz";
    HashMap<String, String> def = WordCheckerFragment.definitionMap;
    String type;
    AutoCompleteTextView e;
    List<String> quizArray = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_quiz, container, false);
        String[] QUIZ_ITEMS = getContext().getResources().getStringArray(R.array.quiz_options);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(),R.layout.dropdown_menu_popup_item,QUIZ_ITEMS);
        e =  root.findViewById(R.id.filled_exposed_dropdown);
        e.setAdapter(adapter);
        Button b = root.findViewById(R.id.start);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = e.getText().toString();
                switch (type){
                    case "2 Letter Words": initializeNumLetter(2);break;
                    case "3 Letter Words": initializeNumLetter(3);break;
                    case "4 Letter Words with J": initialize4Letter('j');break;
                    case "4 Letter Words with Q": initialize4Letter('q');break;
                    case "4 Letter Words with X": initialize4Letter('x');break;
                    case "RETINA- Bingo Stem": initializeBingoStem("RETINA");break;
                    case "SATINE- Bingo Stem": initializeBingoStem("SATINE");break;
                    case "SATIRE- Bingo Stem": initializeBingoStem("SATIRE");break;
                    default:
                        Toast.makeText(getContext(), "Quiz not found", Toast.LENGTH_LONG);
                }
            }
        });
        // 2s, 3s, 4J, 4Q, 4X, SATIRE, SATINE, RETINA
        // 2/3 format: string together 3 single character strings, ask if user if it's valid
        // 4JQX format: give user 4 letters, ask user to scramble
        // bingo format: give user a letter, ask user to list all valid words
        return root;
    }
    public void initializeNumLetter(int num){
        for(int i = 0; i < 26; i++) quizArray.add(letters.substring(i,i+1));
        switch (num){
            case 2:
                for(int i = 0; i < 26; i++) quizArray.add(letters.substring(i,i+1));
                quizArray.add("a");quizArray.add("a");quizArray.add("a");
                quizArray.add("e"); quizArray.add("e"); quizArray.add("e");
                quizArray.add("i");quizArray.add("i");quizArray.add("i");
                quizArray.add("o");quizArray.add("o");quizArray.add("o");
                break;
            case 3:
                quizArray.add("a");quizArray.add("a");quizArray.add("a");quizArray.add("a");quizArray.add("a");
                quizArray.add("e"); quizArray.add("e"); quizArray.add("e"); quizArray.add("e"); quizArray.add("e");
                quizArray.add("i");quizArray.add("i");quizArray.add("i");quizArray.add("i");quizArray.add("i");
                quizArray.add("o");quizArray.add("o");quizArray.add("o");quizArray.add("o");quizArray.add("o");
                break;
        }
    }
    public void initialize4Letter(char c){
        switch(c){
            case 'j':
                String[] j4 = valid4J.split("\\t");
                quizArray = Arrays.asList(j4);
                break;
            case 'q':
                String[] q4 = valid4Q.split("\\t");
                quizArray = Arrays.asList(q4);
                break;
            case 'x':
                String[] x4 = valid4X.split("\\t");
                quizArray = Arrays.asList(x4);
                break;
        }

    }
    public void initializeBingoStem(String s){
        switch (s){
            case "RETINA":break;
            case "SATIRE": break;
            case "SATINE": break;
        }
    }
}
