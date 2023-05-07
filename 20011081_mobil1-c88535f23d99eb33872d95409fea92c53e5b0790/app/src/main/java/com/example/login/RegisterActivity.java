package com.example.login;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    private EditText editFirstName, editLastName, editEnterYear, editGradYear, editEmail, editPassword;
    private String txtFirstName, txtLastName, txtEnterYear, txtGradYear, txtEmail, txtPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private ImageView imgProfile;
    private FirebaseUser mUser;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editFirstName = (EditText)findViewById(R.id.first_name);
        editLastName = (EditText)findViewById(R.id.last_name);
        editEnterYear = (EditText)findViewById(R.id.enteryear);
        editGradYear = (EditText)findViewById(R.id.gradyear);
        editEmail = (EditText)findViewById(R.id.email);
        editPassword = (EditText)findViewById(R.id.password);
        imgProfile = (ImageView)findViewById(R.id.imgView);

        // Have an account? Text click handled.
        TextView haveAnAccount = (TextView) findViewById(R.id.haveanaccount);

        haveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            }
        });

        //For authorization.
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public void register(View v){
        txtFirstName = editFirstName.getText().toString();
        txtLastName = editLastName.getText().toString();
        txtEnterYear = editEnterYear.getText().toString();
        txtGradYear = editGradYear.getText().toString();
        txtEmail = editEmail.getText().toString();
        txtPassword = editPassword.getText().toString();

        // We are checking if all the fields are filled or not. All fields are mandatory to fill.
        if(!TextUtils.isEmpty(txtEmail) && !TextUtils.isEmpty(txtPassword) &&
           !TextUtils.isEmpty(txtFirstName) && !TextUtils.isEmpty(txtLastName) &&
           !TextUtils.isEmpty(txtEnterYear) && !TextUtils.isEmpty(txtGradYear) ){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mUser = mAuth.getCurrentUser();
                        FirebaseDatabase.getInstance().getReference("user/" + mUser.getUid()).setValue(new User(" ", txtFirstName, txtLastName, txtEnterYear, txtGradYear, txtEmail, txtPassword, " ", " ", " ", " ", " ", " "))
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            uploadImage();
                                            Toast.makeText(RegisterActivity.this, "Register is successful", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else{
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(RegisterActivity.this, "Please fill the mandatory fields!", Toast.LENGTH_SHORT).show();
        }
    }

    //Pick image from gallery.
    int PERMISSION_OK_CODE = 1, PERMISSION_NOT_CODE = 0;
    public void pickImage(View v){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // Permission not granted.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_NOT_CODE);
        }else{
            //Permission granted.
            Intent selectPict = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(selectPict, PERMISSION_OK_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_NOT_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent selectPict = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(selectPict, PERMISSION_OK_CODE);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private Uri imgUri;
    private Bitmap imgPicked;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PERMISSION_OK_CODE){
            if(resultCode == RESULT_OK && data != null){
                imgUri = data.getData();
                try {
                    imgPicked = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);
                    imgProfile.setImageBitmap(imgPicked);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    // UploadImage method
    private void uploadImage()
    {
        if (imgUri != null) {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog= new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){
                                    updateProfilePicture(task.getResult().toString());
                                }
                            }
                        });
                        Toast.makeText(RegisterActivity.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }

    //Upload that image into database after pulling it from storage.
    private void updateProfilePicture(String url){
        FirebaseDatabase.getInstance().getReference("user/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/profilePicture").setValue(url);
    }
}
