package com.example.petcareapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ReportLostActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private EditText edtOpisReport,edtInfoReport;
    private Button btnMapaReport,btnDoneRegister;
    private String imageIdentifier,uploadedImageLink;
    private String imageDownloadLink,downloadUrl;
    private Bitmap bitmap;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private ArrayList<String> userList;
    private ArrayAdapter adapter;

    private ArrayList<String> uids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_lost);

        mAuth = FirebaseAuth.getInstance();
        edtInfoReport = findViewById(R.id.edtInfoReport);
        edtOpisReport = findViewById(R.id.edtOpisReport);
        imageView = findViewById(R.id.imgViewReport);
        imageView.setOnClickListener(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        btnDoneRegister = findViewById(R.id.btnDoneRegister);
        btnMapaReport = findViewById(R.id.btnLokacijaReport);

        btnDoneRegister.setOnClickListener(this);
        btnMapaReport.setOnClickListener(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLokacijaReport) {
            Intent intent = new Intent(ReportLostActivity.this,MapsActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.btnDoneRegister) {
            uploadImageToServer();
            uploadToDatabase();
        }

        if (v.getId() == R.id.imgViewReport) {
            selectImage();
        }
    }

    private void selectImage() {
        if (Build.VERSION.SDK_INT < 23) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1000);

        } if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1000);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1000);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            Uri chosenImage = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),chosenImage);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private void uploadImageToServer() {

        if (bitmap != null) {
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();










            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
            byte[] data = baos.toByteArray();

            imageIdentifier = UUID.randomUUID()  + ".png";
            //final StorageReference ref = storageReference.child("images").child(imageIdentifier);
            //
            final UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("images").child(imageIdentifier).putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ReportLostActivity.this,"FAILED UPLOAD OF IMAGE",Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(ReportLostActivity.this,"Succesfull UPLOAD OF IMAGE",Toast.LENGTH_SHORT).show();

                   // final StorageReference ref = storageReference.child("images").child(imageIdentifier);
                   // uploadTask = ref.putFile(file);
//                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                        @Override
//                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                            if (!task.isSuccessful()) {
//                                throw task.getException();
//                            }
//
//                            // Continue with the task to get the download URL
//                            return FirebaseStorage.getInstance().getReference().getDownloadUrl();
//                        }
//                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//                            if (task.isSuccessful()) {
//                                imageDownloadLink = task.getResult().toString();
//                            } else {
//                                // Handle failures
//                                // ...
//                            }
//                        }
//                    });

                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            try {
                                uploadedImageLink = task.getResult().toString();
                                Toast.makeText(ReportLostActivity.this,"IMAGE LINK",Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    });

//                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//                            imageDownloadLink = task.getResult().toString();
//                            Toast.makeText(ReportLostActivity.this,"Succesfull Download LINK",Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
//                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                        @Override
//                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                            if (!task.isSuccessful()) {
//                                throw task.getException();
//                            }
//
//                            // Continue with the task to get the download URL
//                            return ref.getDownloadUrl();
//                        }
//                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//                            if (task.isSuccessful()) {
//                                imageDownloadLink = task.getResult().toString();
//                            } else {
//                                // Handle failures
//                                // ...
//                            }
//                        }
//                    });



//                    FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                            uids.add(snapshot.getKey());
//                            String username = (String) snapshot.child("username").getValue();
//                            userList.add(username);
//                            // adapter.notifyDataSetChanged();
//
//
//                        }
//
//                        @Override
//                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                        }
//
//                        @Override
//                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });

//                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//                            if (task.isSuccessful()) {
//                                imageDownloadLink = task.getResult().toString();
//                            } else {
//                                Toast.makeText(ReportLostActivity.this,"FAILED IMAGE LINK",Toast.LENGTH_SHORT).show();
//
//                            }
//                        }
//                    });
                }
            });
        } else {
            Toast.makeText(ReportLostActivity.this,"PUT IMAGE FIRST",Toast.LENGTH_SHORT).show();
        }

    }


    private void uploadToDatabase() {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Opis",edtOpisReport.getText().toString() + "");
        hashMap.put("Info",edtInfoReport.getText().toString() + "");
        hashMap.put("FromWhom", mAuth.getCurrentUser().getDisplayName() + "");
        hashMap.put("imageIdentifier",imageIdentifier + "");
        hashMap.put("imageDownloadLink",uploadedImageLink + "");


//        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getDisplayName().toString() +"").child("usersPosts").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(ReportLostActivity.this,"DATA SENT TO SERVER",Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(ReportLostActivity.this,"DATAFAILLELELLDEL SENT TO SERVER",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        firebaseFirestore.collection("userPosts").add(hashMap);
    }
}