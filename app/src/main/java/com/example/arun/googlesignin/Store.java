package com.example.arun.googlesignin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Store extends AppCompatActivity {
    EditText etext1, etext2;
    String text1,text2;
    Button store_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        etext1 = findViewById(R.id.text1);
        etext2 = findViewById(R.id.text2);
        store_btn = findViewById(R.id.store_btn);

        store_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WordsBean bean = new WordsBean();
                bean.setDogri(etext1.getText().toString().trim().toLowerCase());
                bean.setEnglish(etext2.getText().toString().trim().toLowerCase());

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("words");
                bean.setId(reference.push().getKey());
                reference.child(bean.getId()).setValue(bean)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                etext1.setText("");
                                etext2.setText("");
                                Toast.makeText(Store.this,"Words Added",Toast.LENGTH_LONG).show();

                            }
                        });


            }
        });

    }
}
