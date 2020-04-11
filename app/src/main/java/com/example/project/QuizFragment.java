package com.example.project;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.primitives.Chars;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class QuizFragment extends Fragment {
    // Corresponding to the array index, the alphabet and the bingo stem form that amount of words
    int[] retina = {0,1,7,3,3,2,5,3,1,2,1,6,3,2,2,4,0,3,11,6,4,0,2,0,0,0};
    int[] satire = {3,4,4,10,2,1,8,2,2,0,2,6,7,11,3,8,0,4,5,8,0,4,4,0,0,0};
    int[] satine = {2,4,3,7,1,3,11,2,1,2,3,9,6,4,1,4,0,11,6,3,2,3,4,2,0,2};
    // Initialize valid 4 letter JQX hooks
    String valid4Q = "AQUA\tQATS\tQUAD\tQUAI\tQUEY\tQUIN\tQUIT\tQUOD\t" +
            "QAID\tQOPH\tQUAG\tQUAY\tQUID\tQUIP\tQUIZ\tSUQS";

    String valid4J = "AJAR\tJADE\tJARS\tJEEP\tJEST\tJIGS\tJOBS\tJOLT\tJUBE\tJURY\t" +
            "AJEE\tJAGG\tJATO\tJEER\tJETE\tJILL\tJOCK\tJOSH\tJUDO\tJUST\t" +
            "DJIN\tJAGS\tJAUK\tJEES\tJETS\tJILT\tJOES\tJOSS\tJUGA\tJUTE\t" +
            "DOJO\tJAIL\tJAUP\tJEEZ\tJEUX\tJIMP\tJOEY\tJOTA\tJUGS\tJUTS\t" +
            "FUJI\tJAKE\tJAVA\tJEFE\tJEWS\tJINK\tJOGS\tJOTS\tJUJU\tMOJO\t" +
            "HADJ\tJAMB\tJAWS\tJEHU\tJIAO\tJINN\tJOHN\tJOUK\tJUKE\tPUJA\t" +
            "HAJI\tJAMS\tJAYS\tJELL\tJIBB\tJINS\tJOIN\tJOWL\tJUMP\tRAJA\t" +
            "HAJJ\tJANE\tJAZZ\tJEON\tJIBE\tJINX\tJOKE\tJOWS\tJUNK\tSOJA\t" +
            "JABS\tJAPE\tJEAN\tJERK\tJIBS\tJISM\tJOKY\tJOYS\tJUPE\t" +
            "JACK\tJARL\tJEED\tJESS\tJIFF\tJIVE\tJOLE\tJUBA\tJURA";
    String valid4X = "APEX\tAXLE\tDEXY\tEXIT\tFLEX\tJEUX\tMOXA\tOXES\tSEXY\t" +
            "AXAL\tAXON\tDOUX\tEXON\tFLUX\tJINX\tNEXT\tOXID\tTAXA\t" +
            "AXED\tBOXY\tDOXY\tEXPO\tFOXY\tLUXE\tNIXE\tOXIM\tTAXI\t" +
            "AXEL\tCALX\tEAUX\tFALX\tHOAX\tLYNX\tNIXY\tPIXY\tTEXT\t" +
            "AXES\tCOAX\tEXAM\tFAUX\tIBEX\tMAXI\tONYX\tPREX\tVEXT\t" +
            "AXIL\tCOXA\tEXEC\tFIXT\tILEX\tMINX\tORYX\tROUX\tWAXY\t" +
            "AXIS\tCRUX\tEXES\tFLAX\tIXIA\tMIXT\tOXEN\tSEXT\tXYST";
    String letters = "abcdefghijklmnopqrstuvwxyz", lets2 = "", stem="", typedsofar = "";
    int currentType, lets =2, score = 0;
    int i5 = 0,  j5 = 0;
    HashMap<String, String> def = LoginActivity.getDefinitionMap();
    // initializing views
    LinearLayout a, p;
    Button b,c,d,m, n, o;
    AutoCompleteTextView e;
    TextInputEditText f;
    TextView g,h,i, j, k;
    TextInputLayout l;
    boolean isWord, hasLet, notTyped;
    List<String> typedBingoes = new ArrayList<>();
    List<String> quizArray = new ArrayList<>();
    List<Integer> numArray = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_quiz, container, false);
        String[] QUIZ_ITEMS = getContext().getResources().getStringArray(R.array.quiz_options);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(),R.layout.dropdown_menu_popup_item,QUIZ_ITEMS);
        // finding views by id
        a = root.findViewById(R.id.yesno); // LinearLayout containing Yes and No buttons
        b = root.findViewById(R.id.start); // Start quiz button
        c = root.findViewById(R.id.yes); // Yes button
        d = root.findViewById(R.id.no); // No button
        e =  root.findViewById(R.id.filled_exposed_dropdown); // Spinner text
        f = root.findViewById(R.id.word); // EditText
        g = root.findViewById(R.id.title); // Question
        h = root.findViewById(R.id.question); // Word
        i = root.findViewById(R.id.typedsofar); // Bingo words entered
        j = root.findViewById(R.id.result); // Correct or wrong
        k = root.findViewById(R.id.score); // Tally score
        l = root.findViewById(R.id.til); // Text input layout containing EditText
        m = root.findViewById(R.id.next); // Next question button
        n = root.findViewById(R.id.submit); // Submit current input
        o = root.findViewById(R.id.skip);  // Skip current question
        p = root.findViewById(R.id.submitskip); // LinearLayout containing Submit and Skip buttons
        // setting listeners
        e.setAdapter(adapter);
        View.OnClickListener ocl2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWord = def.containsKey(h.getText().toString().trim());
                switch(v.getId()) {
                    case R.id.yes:
                        if (isWord) {
                            j.setText("Correct!");
                            score ++;
                            k.setText("Score: " + score);
                            j.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                        } else {
                            j.setText("Wrong!");
                            j.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                        }
                        break;
                    case R.id.no:
                        if (!isWord) {
                            j.setText("Correct!");
                            score ++;
                            k.setText("Score: " + score);
                            j.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                        } else {
                            j.setText("Wrong!");
                            j.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                        }
                }
                m.setEnabled(true);
            }
        };
        c.setOnClickListener(ocl2);
        d.setOnClickListener(ocl2);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f.setText("");
                j.setText("");
            }
        });
        f.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(f.getText().toString().trim().length() == 0)
                    n.setEnabled(false);
                else
                    n.setEnabled(true);
            }
        });
        o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextquestion();
            }
        });
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextquestion();
            }
        });
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wrd = f.getText().toString().toLowerCase().trim();
                isWord = def.containsKey(wrd);
                if (currentType == 1) {
                    hasLet = isAnagram(wrd, h.getText().toString().toLowerCase());
                    if (isWord && hasLet) {
                        j.setText("Correct!");
                        j.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                        f.setEnabled(false);
                        m.setEnabled(true);
                        n.setEnabled(false);
                        o.setEnabled(false);
                        score++;
                        k.setText("Score: " + score);
                    } else {
                        j.setText("Wrong!");
                        j.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                        n.setEnabled(true);
                        o.setEnabled(true);
                    }
                }
                else{
                    String qstr = h.getText().toString().replace("+","").toLowerCase();
                    hasLet = isAnagram(qstr, wrd);
                    notTyped = typedBingoes.contains(wrd);
                    if(notTyped){
                        j.setText("You have already typed this!");
                        return;
                    }
                    if (isWord && hasLet) {
                        j.setText("Correct!");
                        j.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                        score++;
                        if (score == j5) {
                            f.setEnabled(false);
                            m.setEnabled(true);
                            n.setEnabled(false);
                            o.setEnabled(false);
                        }
                        k.setText("Score: " + score + " / " + j5);
                        if (!typedsofar.equals("")){
                            typedsofar += ", ";
                        }
                        typedsofar += wrd;
                        typedBingoes.add(wrd);
                        i.setText("Bingoes typed so far: " + typedsofar);
                    } else {
                        j.setText("Wrong!");
                        j.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                        n.setEnabled(true);
                        o.setEnabled(true);
                    }
                }

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score = 0;
                if (b.getText().toString().toLowerCase().trim().equals("start quiz")) {
                    String type = e.getText().toString();
                    switch (type) {
                        case "2 Letter Words":
                            initializeNumLetter(2);
                            currentType = 0;
                            lets = 2;
                            break;
                        case "3 Letter Words":
                            initializeNumLetter(3);
                            currentType = 0;
                            lets = 3;
                            break;
                        case "4 Letter Words with J":
                            initialize4Letter('j');
                            currentType = 1;
                            lets2 = "j";
                            break;
                        case "4 Letter Words with Q":
                            initialize4Letter('q');
                            currentType = 1;
                            lets2 = "q";
                            break;
                        case "4 Letter Words with X":
                            initialize4Letter('x');
                            currentType = 1;
                            lets2 = "x";
                            break;
                        case "RETINA- Bingo Stem":
                            initializeBingoStem("RETINA");
                            currentType = 2;
                            stem = "RETINA";
                            break;
                        case "SATINE- Bingo Stem":
                            initializeBingoStem("SATINE");
                            currentType = 2;
                            stem = "SATINE";
                            break;
                        case "SATIRE- Bingo Stem":
                            initializeBingoStem("SATIRE");
                            currentType = 2;
                            stem = "SATIRE";
                            break;
                        default:
                            Toast.makeText(getContext(), "Quiz not found", Toast.LENGTH_LONG).show();
                            return;
                    }
                    e.setEnabled(false);
                    b.setText("End quiz");
                    nextquestion();
                }
                else{
                    score = 0;
                    typedsofar = "";
                    typedBingoes = new ArrayList<>();
                    numArray = new ArrayList<>();
                    quizArray = new ArrayList<>();
                    a.setVisibility(View.INVISIBLE);
                    b.setText("Start quiz");
                    e.setEnabled(true);
                    g.setVisibility(View.INVISIBLE);
                    h.setVisibility(View.INVISIBLE);
                    i.setVisibility(View.INVISIBLE);
                    j.setVisibility(View.INVISIBLE);
                    k.setVisibility(View.INVISIBLE);
                    k.setText("Score: 0");
                    l.setVisibility(View.INVISIBLE);
                    m.setVisibility(View.INVISIBLE);
                    p.setVisibility(View.INVISIBLE);
                }
            }
        });
        // 2s, 3s, 4J, 4Q, 4X, SATIRE, SATINE, RETINA
        // 2/3 format: string together 3 single character strings, ask if user if it's valid
        // 4JQX format: give user 4 letters, ask user to scramble
        // bingo format: give user a letter, ask user to list all valid words
        // initializing all required views

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
            case "RETINA":
                for (int i : retina) {
                    numArray.add(i);
                }
                break;
            case "SATIRE":
                for (int i : satire) {
                    numArray.add(i);
                }
                break;
            case "SATINE":
                for (int i : satine) {
                    numArray.add(i);
                }
                break;
        }
    }
    public void nextquestion(){
        String qn;
        switch(currentType){
            case 0:
                a.setVisibility(View.VISIBLE);
                g.setVisibility(View.VISIBLE);
                h.setVisibility(View.VISIBLE);
                j.setVisibility(View.VISIBLE);
                k.setVisibility(View.VISIBLE);
                m.setVisibility(View.VISIBLE);
                Random r1 = new Random();
                int i1 = r1.nextInt(quizArray.size());
                int i2 = r1.nextInt(quizArray.size());
                qn = quizArray.get(i1) + quizArray.get(i2);
                if (lets == 3){
                    int i3 = r1.nextInt(quizArray.size());
                    qn += quizArray.get(i3);
                }
                g.setText("Is this a valid " + lets + " letter word?");
                h.setText(qn);
                j.setText("");
                m.setEnabled(false);
                break;
            case 1:
                g.setVisibility(View.VISIBLE);
                h.setVisibility(View.VISIBLE);
                j.setVisibility(View.VISIBLE);
                l.setVisibility(View.VISIBLE);
                k.setVisibility(View.VISIBLE);
                m.setVisibility(View.VISIBLE);
                p.setVisibility(View.VISIBLE);
                Random r4 = new Random();
                int i4 = r4.nextInt(quizArray.size());
                qn = quizArray.get(i4);
                g.setText("Unscramble the following letters.");
                h.setText(scramble(qn));
                j.setText("");
                f.setEnabled(true);
                f.setText("");
                m.setEnabled(false);
                n.setEnabled(true);
                o.setEnabled(true);
                break;
            case 2:
                g.setVisibility(View.VISIBLE);
                h.setVisibility(View.VISIBLE);
                i.setVisibility(View.VISIBLE);
                j.setVisibility(View.VISIBLE);
                k.setVisibility(View.VISIBLE);
                l.setVisibility(View.VISIBLE);
                m.setVisibility(View.VISIBLE);
                p.setVisibility(View.VISIBLE);
                j5 = 0;
                while (j5 == 0) {
                    Random r5 = new Random();
                    i5 = r5.nextInt(numArray.size());
                    j5 = numArray.get(i5);
                }
                f.setText("");
                f.setEnabled(true);
                g.setText("Type all possible words formed with the given stem and letter.");
                h.setText(stem + "+" + getCharForNumber(i5));
                i.setText("Bingoes typed so far: none");
                j.setText("");
                k.setText("Score: 0 / " + j5);
                m.setEnabled(false);
                n.setEnabled(true);
                o.setEnabled(true);
                break;
        }
    }
    public String scramble(String s) {
        List<Character> chars = Chars.asList(s.toCharArray());
        Collections.shuffle(chars);
        return new String(Chars.toArray(chars));
    }
    public boolean isAnagram(String str1, String str2){
        char[] c1 = str1.trim().toCharArray();
        char[] c2 = str2.trim().toCharArray();
        Arrays.sort(c1);
        Arrays.sort(c2);
        for (int z = 0; z < c1.length; z++){
            if(c1[z] != c2[z]){
                return false;
            }
        }
        return true;
    }
    private String getCharForNumber(int i) {
        return i > -1 && i < 26 ? String.valueOf((char)(i + 'A' )) : null;
    }
}
