package com.crackeddish.theblog.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.crackeddish.theblog.Model.Blog;
import com.crackeddish.theblog.R;
import com.google.android.gms.actions.ItemListIntents;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {
    private ImageButton mPostImage;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubmitButton;
    private DatabaseReference mPostDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog mProgress;
    private Uri mImageUri;
    private StorageReference mStorage;

    private static final int GALLERY_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        mProgress=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mStorage= FirebaseStorage.getInstance().getReference();
        mUser=mAuth.getCurrentUser();
        mPostDatabase= FirebaseDatabase.getInstance().getReference().child("MBlog");
        mPostImage=(ImageButton)findViewById(R.id.imageButton);
        mPostTitle=findViewById(R.id.postTitleEt);
        mPostDesc=findViewById(R.id.descriptionEt);
        mSubmitButton=findViewById(R.id.submitPost);
        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_CODE && resultCode==RESULT_OK){
            mImageUri=data.getData();
            mPostImage.setImageURI(mImageUri);
        }
    }

    private void startPosting() {
        mProgress.setMessage("Posting...");
        mProgress.show();

        final String titleVal=mPostTitle.getText().toString().trim();
        final String descVal=mPostDesc.getText().toString().trim();
        if (!TextUtils.isEmpty(titleVal) && mImageUri!=null){
            //start uploading
            StorageReference filePath=mStorage.child("MBlogImages").child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl=taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost=mPostDatabase.push();
                    Map<String,String> dataToSave=new HashMap<>();
                    dataToSave.put("title",titleVal);
                    dataToSave.put("desc",descVal);
                    dataToSave.put("image",downloadUrl.toString());
                    dataToSave.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
                    dataToSave.put("userid",mUser.getUid());
                    newPost.setValue(dataToSave);
                    mProgress.dismiss();
                    startActivity(new Intent(AddPostActivity.this,PostListActivity.class));
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPostActivity.this,"Failed  to post.",Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
