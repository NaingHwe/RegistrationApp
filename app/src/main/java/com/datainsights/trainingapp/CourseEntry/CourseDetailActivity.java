package com.datainsights.trainingapp.CourseEntry;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.datainsights.trainingapp.FileUtility;
import com.datainsights.trainingapp.R;
import com.datainsights.trainingapp.Storage.InsertCallback;
import com.datainsights.trainingapp.Storage.StorageHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseDetailActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnCourseDetailBack,btnCourseSave;
    CircleImageView cvCourse;
    Bitmap imageBitmap;
    String fileName = "";
    String filePath = "";
    Uri contentURI;
    Uri downloadUri;
    EditText etCourseTitle,etCourseCode,etCourseDescription;

    String courseTitle,courseCode,courseDescription;
    FirebaseStorage storage;
    StorageReference storageReference;
    private static final int SELECT_PHOTO_GALLERY = 5;
    private static final int PERMISSION_REQUEST_CAMERA = 15;
    private static final int SELECT_PHOTO_CAMERA = 10;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 25;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        btnCourseDetailBack = findViewById(R.id.btn_course_detail_back);
        cvCourse = findViewById(R.id.cv_course);
        etCourseTitle = findViewById(R.id.et_course_title);
        etCourseCode = findViewById(R.id.et_course_code);
        etCourseDescription = findViewById(R.id.et_course_description);
        btnCourseSave = findViewById(R.id.btn_course_save);
        cvCourse.setOnClickListener(this);
        btnCourseSave.setOnClickListener(this);
        btnCourseDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("course_images");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CourseData value = (CourseData) extras.getSerializable("CourseObj");
            if (value != null) {
                etCourseTitle.setText(value.getCourseTitle());
                etCourseCode.setText(value.getCourseCode());
                etCourseDescription.setText(value.getCourseDescription());

                etCourseTitle.setEnabled(false);
                etCourseCode.setEnabled(false);
                etCourseDescription.setEnabled(false);

                btnCourseSave.setVisibility(View.GONE);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.course);
                requestOptions.error(R.drawable.course);

                Glide.with(this)
                        .setDefaultRequestOptions(requestOptions)
                        .load(value.getCourseImageURL())
                        .into(cvCourse);

            }
        }

    }


    private void selectImage() {
        System.out.println("selectImage");

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CourseDetailActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    if (!isPermissionCameraGranted()) {
                        ActivityCompat.requestPermissions(CourseDetailActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                PERMISSION_REQUEST_CAMERA);


                    } else {
                        actionCamera();

                    }
                } else if (options[item].equals("Choose from Gallery"))

                {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, SELECT_PHOTO_GALLERY);

                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }
    private boolean isPermissionCameraGranted() {
        return ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }
    private void actionCamera() {
        Intent galleryIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(galleryIntent, SELECT_PHOTO_CAMERA);
    }
    private boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(getApplicationContext(),
                permission)
                == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onClick(View view) {

       switch(view.getId())
       {
           case R.id.btn_course_save:
               saveCourseFunction();
               break;
           case R.id.cv_course:
               selectImage();
               break;

       }




    }
    public void saveCourseFunction(){
        System.out.println("uploadImage func");

        courseTitle = etCourseTitle.getText().toString();
        courseCode = etCourseCode.getText().toString();
        courseDescription = etCourseDescription.getText().toString();
        if (TextUtils.isEmpty(courseTitle)) {
            etCourseTitle.setError("Please fill course title");
            etCourseTitle.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(courseCode)) {
            etCourseCode.setError("Please fill course code");
            etCourseCode.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(courseDescription)) {
            etCourseDescription.setError("Please fill course description");
            etCourseDescription.requestFocus();
            return;
        }

        uploadImage();
    }
    private void uploadImage() {
        if (contentURI != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = storageReference.child("course_images/course_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
            ref.putFile(contentURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUri = uri;
                                    savetoFirebase();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(getApplicationContext(), "download Fail", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            savetoFirebase();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        } else {
            savetoFirebase();
        }
    }
    private void savetoFirebase() {

        CourseData courseData = new CourseData();
        courseData.setCourseTitle(etCourseTitle.getText().toString());
        courseData.setCourseCode(etCourseCode.getText().toString());
        courseData.setCourseDescription(etCourseDescription.getText().toString());
        if (downloadUri != null)
            courseData.setCourseImageURL(downloadUri.toString());
        StorageHelper.getStorageService().insertCourseData(courseData, new InsertCallback() {
            @Override
            public void onSuccess(String msg) {
                //   Toast.makeText(StudentDetailActivity.this, "Success Data insert", Toast.LENGTH_SHORT).show();
                hideKeyboard(CourseDetailActivity.this);
                finish();
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(CourseDetailActivity.this, "Fail Data insert", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            imageBitmap = (Bitmap) extras.get("data");
                        }

                        contentURI = getImageUri(getApplicationContext(), imageBitmap);
                        if (!isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            ActivityCompat.requestPermissions(CourseDetailActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        } else {
                            imageFileWrite();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                break;
            case SELECT_PHOTO_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        contentURI = data.getData();

                        try {
                            imageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), contentURI);
                            if (!isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                ActivityCompat.requestPermissions(CourseDetailActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                            } else {
                                imageFileWrite();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                break;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void imageFileWrite() throws IOException {
        filePath = FileUtility.getPhotoFolderPath();
        fileName = String.valueOf("course_" + System.currentTimeMillis()).concat(".jpg");
        Pair<Boolean, String> returnResult = FileUtility.writeImageFile(filePath, fileName, imageBitmap);

        Uri uri = Uri.fromFile(new File(filePath, fileName));

        if (returnResult.first) {
            Picasso.with(this)
                    .load(uri)
                    .error(R.drawable.ic_error)
                    .into(cvCourse);
        }
    }
}
