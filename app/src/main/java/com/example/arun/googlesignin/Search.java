package com.example.arun.googlesignin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Search extends AppCompatActivity {
    EditText english;
    TextView dogri;
    Button add_btn, srch_btn;
    Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        english = findViewById(R.id.edit_english);
        dogri = findViewById(R.id.dogri_text);
        dogri.setVisibility(View.INVISIBLE);

        srch_btn = findViewById(R.id.srch_btn);

        srch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("words");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                            if (english.getText().toString().trim().toLowerCase().equals(postSnapshot.child("english").getValue())) {
                                dogri.setVisibility(View.VISIBLE);
                                dogri.setText(postSnapshot.child("dogri").getValue().toString());
                                flag = true;

                            }

                        }
                        if (flag){
                            Toast.makeText(Search.this,"Word Found",Toast.LENGTH_LONG).show();


                        }else {
                            Toast.makeText(Search.this,"Word Not Found",Toast.LENGTH_LONG).show();
                            Toast.makeText(Search.this,"Try Again",Toast.LENGTH_LONG).show();


                        }
                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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
    }
}
