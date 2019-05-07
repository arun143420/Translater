package com.example.arun.googlesignin;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Store extends AppCompatActivity {
    EditText english_txt, dogri_txt, kashmiri_txt;
    Button store_btn, dogri_btn, kashmiri_btn;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference mStorage = storage.getReference();
    WordsBean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        english_txt = findViewById(R.id.english_txt);
        dogri_txt = findViewById(R.id.dogri_txt);
        kashmiri_txt = findViewById(R.id.kashmiri_txt);
        dogri_btn = findViewById(R.id.dogri_btn);
        kashmiri_btn = findViewById(R.id.kashmiri_btn);


        store_btn = findViewById(R.id.store_btn);

        store_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bean = new WordsBean();
                bean.setDogri(dogri_txt.getText().toString().trim().toLowerCase());
                bean.setEnglish(english_txt.getText().toString().trim().toLowerCase());
                bean.setKashmiri(dogri_txt.getText().toString().trim().toLowerCase());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("words");
                bean.setId(reference.push().getKey());
                reference.child(bean.getId()).setValue(bean)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dogri_txt.setText("");
                                english_txt.setText("");
                                kashmiri_txt.setText("");
                                Toast.makeText(Store.this,"Words Added",Toast.LENGTH_LONG).show();

                            }
                        });


            }
        });

        dogri_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_upload = new Intent();
                intent_upload.setType("audio/*");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_upload,1);

            }
        });


        kashmiri_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_upload = new Intent();
                intent_upload.setType("audio/*");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_upload,2);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        if(requestCode == 1){

            if(resultCode == RESULT_OK){

                //the selected audio.
                Uri resultUri = data.getData();


                final String file_name = System.currentTimeMillis() + ".mp3";
                final StorageReference filepath = mStorage.child("audio/dogri/"+file_name);



                filepath.putFile(resultUri)//Compress Image goes here
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                final StorageReference ref = mStorage.child("audio/dogri/" + file_name);
                                ref.getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onSuccess(final Uri downloadUri) {
                                                bean.setDogri_audio(downloadUri.toString());

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }

                        });



            }
        }

        else if (requestCode == 2){
            if(resultCode == RESULT_OK){

                //the selected audio.
                Uri resultUri = data.getData();


                final String file_name = System.currentTimeMillis() + ".mp3";
                final StorageReference filepath = mStorage.child("audio/kashmiri/"+file_name);



                filepath.putFile(resultUri)//Compress Image goes here
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                final StorageReference ref = mStorage.child("audio/kashmiri/" + file_name);
                                ref.getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onSuccess(final Uri downloadUri) {
                                                bean.setKashmiri_audio(downloadUri.toString());

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }

                        });

            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
