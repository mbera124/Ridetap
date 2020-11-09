package com.example.ridetap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ridetap.model.Cars;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class AddCar extends AppCompatActivity {
    private Button btnChoose, btnsave;
    private ImageView imageView;
    private EditText etcontact, etprice,etmodel,etlocation;

    private Uri filePath;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    // Folder path for Firebase Storage.
    String Storage_Path = "https://console.firebase.google.com/u/0/project/dictionary-96874/storage/dictionary-96874.appspot.com/files~2Fimages";

    // Root Database Name for Firebase Database.
    String Database_Path = "Addcar";

    private final int PICK_IMAGE_REQUEST = 71;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        btnChoose = findViewById(R.id.btnChoose);
        btnsave = findViewById(R.id.btnsave);
        etcontact = findViewById(R.id.contact);
        etprice = findViewById(R.id.price);
        etlocation = findViewById(R.id.location);
        etmodel = findViewById(R.id.model);
        imageView = findViewById(R.id.imgView);


        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Addcar");


        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();

            }
        });
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String ImageURL = "";
                                    ImageURL = uri.toString();
                                    String TempImageContact = etcontact.getText().toString().trim();
                                    String TempImagePrice = etprice.getText().toString().trim();
                                    String TempImageModel= etmodel.getText().toString().trim();
                                    String TempImageLocation = etlocation.getText().toString().trim();

                                    Cars cars = new Cars(ImageURL,TempImageModel,TempImageLocation,TempImagePrice,TempImageContact);
                                    databaseReference.push().setValue(cars);
                                }
                            });


                            progressDialog.dismiss();
                            Toast.makeText(AddCar.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddCar.this, Home.class));
//                            finish();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddCar.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploading " + ((int) progress) + "%...");
                        }
                    });
        }
    }

}


