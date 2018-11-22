package com.datainsights.trainingapp.StudentRegistration;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.datainsights.trainingapp.BaseActivity;
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

public class StudentDetailActivity extends BaseActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback,InsertCallback{
    Bitmap imageBitmap;
    Button btnBack, btnRegister,btnEdit,btnCancel;
    Uri contentURI;
    Uri downloadUri;
    CircleImageView cvStudentProfile;
    private static final int SELECT_PHOTO_GALLERY = 5;
    private static final int PERMISSION_REQUEST_CAMERA = 15;
    private static final int SELECT_PHOTO_CAMERA = 10;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 25;
    EditText etStudentName, etFirstPhone, etSecondPhone, etEmail;
    private RadioGroup mGenderGroup;
    FirebaseStorage storage;
    StorageReference storageReference;
    String stName = "", stEmail = "";
    String selectedGender = "";
    String fileName = "";
    String filePath = "";
    RadioButton rbFemale, rbMale;
    StudentData studentUpdate;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        btnBack = findViewById(R.id.btn_back);
        etStudentName = findViewById(R.id.et_studentName);
        etFirstPhone = findViewById(R.id.et_firstPhone);
        etSecondPhone = findViewById(R.id.et_secondPhone);
        mGenderGroup = findViewById(R.id.gender_radio_group);
        etEmail = findViewById(R.id.et_email);
        btnRegister = findViewById(R.id.btn_register);
        btnEdit = findViewById(R.id.btn_edit);
        btnCancel = findViewById(R.id.btn_cancel);
        cvStudentProfile = findViewById(R.id.cv_studentProfile);
        rbFemale = findViewById(R.id.radio_female);
        rbMale = findViewById(R.id.radio_male);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cvStudentProfile.setOnClickListener(this);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveStudentRegisterData();
            }
        });
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("images");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            studentUpdate = (StudentData) extras.getSerializable("StudentObj");
            if (studentUpdate != null) {
                etStudentName.setText(studentUpdate.getName());
                etFirstPhone.setText(String.valueOf(studentUpdate.getFirstPhone()));
                etSecondPhone.setText(String.valueOf(studentUpdate.getSecondPhone()));
                etEmail.setText(studentUpdate.getEmail());
                selectedGender = studentUpdate.getGender();
                setUIEnable(false);
                btnRegister.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.ic_error);
                requestOptions.error(R.drawable.ic_error);

                Glide.with(this)
                        .setDefaultRequestOptions(requestOptions)
                        .load(studentUpdate.getProfileImageURL())
                        .into(cvStudentProfile);
                if (selectedGender.equalsIgnoreCase("Male")) {
                    rbMale.setChecked(true);
                } else if (selectedGender.equalsIgnoreCase("Female")) {
                    rbFemale.setChecked(true);
                }
                btnEdit.setOnClickListener(this);
                btnCancel.setOnClickListener(this);
            }
        }
    }

    private void setUIEnable(boolean enable){
        etFirstPhone.setEnabled(enable);
        etSecondPhone.setEnabled(enable);
        etEmail.setEnabled(enable);
        etStudentName.setEnabled(enable);
        cvStudentProfile.setClickable(enable);
        mGenderGroup.setClickable(enable);
        rbFemale.setClickable(enable);
        rbMale.setClickable(enable);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cv_studentProfile:
                selectImage();
                break;
            case R.id.btn_cancel:
                btnCancel.setVisibility(View.GONE);
                btnRegister.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
                setUIEnable(false);
                break;
            case R.id.btn_edit:
                setUIEnable(true);
                btnEdit.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
                btnRegister.setVisibility(View.VISIBLE);
                btnRegister.setText("Update");
                break;
        }
    }

    private void saveStudentRegisterData() {

        stName = etStudentName.getText().toString();
        stEmail = etEmail.getText().toString();
        if (TextUtils.isEmpty(stName)) {
            etStudentName.setError("Please fill student name");
            etStudentName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(etFirstPhone.getText())) {
            etFirstPhone.setError("Please type a first phone");
            etFirstPhone.requestFocus();
            return;
        } else if (!(android.util.Patterns.PHONE.matcher(String.valueOf(etFirstPhone.getText())).matches())) {
            etFirstPhone.setError("Invalid First Phone");
            etFirstPhone.requestFocus();
            return;
        }
//        if (TextUtils.isEmpty(etSecondPhone.getText())) {
//            etSecondPhone.setError("Please type a second phone");
//            etSecondPhone.requestFocus();
//            return;
//        } else if (!(android.util.Patterns.PHONE.matcher(etSecondPhone.getText()).matches())) {
//            etSecondPhone.setError("Invalid Second Phone");
//            etSecondPhone.requestFocus();
//            return;
//        }
        if (TextUtils.isEmpty(stEmail)) {
            etEmail.setError("Please type a mail");
            etEmail.requestFocus();
            return;
        } else if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(stEmail).matches())) {
            etEmail.setError("Invalid mail format");
            etEmail.requestFocus();
            return;
        }
        int checkId = mGenderGroup.getCheckedRadioButtonId();
        selectedGender = "";
        switch (checkId) {
            case R.id.radio_male:
                selectedGender = "Male";
                break;
            case R.id.radio_female:
                selectedGender = "Female";
                break;
        }
        uploadImage();

    }

    private void updateToFirebase(){
        studentUpdate.setName(etStudentName.getText().toString());
        studentUpdate.setFirstPhone(Long.parseLong(etFirstPhone.getText().toString()));
        studentUpdate.setSecondPhone(Long.parseLong(etSecondPhone.getText().toString()));
        studentUpdate.setEmail(etEmail.getText().toString());
        studentUpdate.setGender(selectedGender);
        if (downloadUri != null)
            studentUpdate.setProfileImageURL(downloadUri.toString());
        StorageHelper.getStorageService().updateStudentData(studentUpdate, this);
    }

    private void saveOrUpdateToFirebase(){
        if(studentUpdate == null){
            savetoFirebase();
        }else{
            updateToFirebase();
        }
    }

    private void savetoFirebase() {

        StudentData stdata = new StudentData();
        stdata.setName(etStudentName.getText().toString());
        stdata.setFirstPhone(Long.parseLong(etFirstPhone.getText().toString()));
        stdata.setSecondPhone(Long.parseLong(etSecondPhone.getText().toString()));
        stdata.setEmail(etEmail.getText().toString());
        stdata.setGender(selectedGender);
        if (downloadUri != null)
            stdata.setProfileImageURL(downloadUri.toString());
        StorageHelper.getStorageService().insertStudentData(stdata,this);
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
    private void uploadImage() {
        uploadingDialog(true);
        if (contentURI != null) {
            final StorageReference ref = storageReference.child("images/student_" + System.currentTimeMillis() + ".jpg");
            ref.putFile(contentURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUri = uri;
                                    saveOrUpdateToFirebase();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    uploadingDialog(false);
                                    Toast.makeText(getApplicationContext(), "Image upload Fail", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            uploadingDialog(false);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int)(100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            uploadLoading.setProgress(progress);
                        }
                    });
        } else {
            saveOrUpdateToFirebase();
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentDetailActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    if (!isPermissionCameraGranted()) {
                        ActivityCompat.requestPermissions(StudentDetailActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                PERMISSION_REQUEST_CAMERA);


                    } else {
                        actionCamera();

                    }
                } else if (options[item].equals("Choose from Gallery"))

                {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, SELECT_PHOTO_GALLERY);

                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    private void actionCamera() {
        Intent galleryIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(galleryIntent, SELECT_PHOTO_CAMERA);
    }

    private boolean isPermissionCameraGranted() {
        return ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
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
                            ActivityCompat.requestPermissions(StudentDetailActivity.this,
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
                                ActivityCompat.requestPermissions(StudentDetailActivity.this,
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
        fileName = String.valueOf("student_" + System.currentTimeMillis()).concat(".jpg");
        Pair<Boolean, String> returnResult = FileUtility.writeImageFile(filePath, fileName, imageBitmap);

        Uri uri = Uri.fromFile(new File(filePath, fileName));

        if (returnResult.first) {
            Picasso.with(this)
                    .load(uri)
                    .error(R.drawable.ic_error)
                    .into(cvStudentProfile);
        }
    }

    private boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(getApplicationContext(),
                permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onSuccess(String msg) {
        uploadingDialog(false);
        hideKeyboard(StudentDetailActivity.this);
        finish();
    }

    @Override
    public void onFailure(String msg) {
        uploadingDialog(false);
        Toast.makeText(StudentDetailActivity.this, "Fail Data insert", Toast.LENGTH_SHORT).show();
    }
}
