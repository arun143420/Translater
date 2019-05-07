package com.example.arun.googlesignin;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class Search extends AppCompatActivity {

    TextView translated_text;
    Button add_btn, srch_btn;
    Boolean flag = false;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    ImageView mic_button;
    DatabaseReference databaseReference;
    Switch dogri_switch, kashmiri_switch;
    String language = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        translated_text = findViewById(R.id.translated_text);
        translated_text.setVisibility(View.INVISIBLE);
        dogri_switch = findViewById(R.id.dogri_switch);
        kashmiri_switch = findViewById(R.id.kashmiri_switch);



        mic_button = findViewById(R.id.mic_button);

        mic_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                askSpeechInput();
            }
        });



        add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(Search.this, Store.class);
                startActivity(it);
            }
        });




        dogri_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){
                    dogri_switch.setChecked(true);
                    kashmiri_switch.setChecked(false);
                    language = "dogri";
                }


            }
        });

        kashmiri_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){
                    dogri_switch.setChecked(false);
                    kashmiri_switch.setChecked(true);
                    language = "kashmiri";
                }


            }
        });










    }


    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hi speak something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    translated_text.setText(result.get(0));



                    databaseReference = FirebaseDatabase.getInstance().getReference("words");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                            if (translated_text.getText().toString().trim().toLowerCase().equals(postSnapshot.child("english").getValue())) {
                                translated_text.setVisibility(View.VISIBLE);
                                MediaPlayer mp=new MediaPlayer();

                                if (language.equals("dogri")) {
                                    translated_text.setText(postSnapshot.child("dogri").getValue().toString());


                                    try{
                                        mp.setDataSource(postSnapshot.child("dogri_audio").getValue().toString());//Write your location here
                                        mp.prepare();
                                        mp.start();

                                    }catch(Exception e){e.printStackTrace();}




                                }else if (language.equals("kashmiri")){

                                    translated_text.setText(postSnapshot.child("kashmiri").getValue().toString());
                                    try{
                                        mp.setDataSource(postSnapshot.child("kashmiri_audio").getValue().toString());//Write your location here
                                        mp.prepare();
                                        mp.start();

                                    }catch(Exception e){e.printStackTrace();}
                                }
                                flag = true;

                            }

                            }
                            if (flag) {
                                Toast.makeText(Search.this, "Word Found", Toast.LENGTH_LONG).show();


                            } else {
                                translated_text.setText("Try Again.............");
                                Toast.makeText(Search.this, "Not Found", Toast.LENGTH_LONG).show();


                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



























                }
                break;
            }

        }


    }
}
