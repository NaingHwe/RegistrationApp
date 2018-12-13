package com.datainsights.trainingapp.OpeningCourse;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.datainsights.trainingapp.BaseActivity;
import com.datainsights.trainingapp.CourseEntry.CourseData;
import com.datainsights.trainingapp.FileUtility;
import com.datainsights.trainingapp.R;
import com.datainsights.trainingapp.Storage.InsertCallback;
import com.datainsights.trainingapp.Storage.StorageHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static junit.framework.Assert.assertEquals;

public class OpeningCourseDetailActivity extends BaseActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback,InsertCallback {
    Button btnOpeningCourseDetailBack;
    Spinner spCourseList;
    Uri downloadUri;
    StorageReference storageReference;
    CardView et_opening_course_Duration,et_opening_course_StartDate,et_opening_course_EndDate,et_opening_course_starttime,et_opening_course_endtime;
    TextView etStartDate,etEndDate,etEndTime,etStartTime;
    int mYear,mMonth,mDay,mHour,mMinute;
    ImageView iv_opening_course_detail;
    CircleImageView cv_opening_course_detail;
    private static final int SELECT_PHOTO_GALLERY = 5;
    private static final int PERMISSION_REQUEST_CAMERA = 15;
    private static final int SELECT_PHOTO_CAMERA = 10;
    private final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 25;
    Bitmap imageBitmap;
    FirebaseStorage storage;
    OpeningCourseData openCourseData;
    EditText etFee,etDuration;
    ArrayList<String> courseListArray = new ArrayList<>();
    ArrayList<CourseData> courseListArrayObj = new ArrayList<>();
    String item,courseImageUrl;
    Button btnOpeningSave,btnOpenCourseEdit,btnOpenCourseCancel;
    Uri contentURI;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_course_detail);
        btnOpeningCourseDetailBack = findViewById(R.id.btn_opening_course_detail_back);
        spCourseList = findViewById(R.id.sp_course_list);
        et_opening_course_Duration = findViewById(R.id.et_opening_course_duration);
        et_opening_course_StartDate = findViewById(R.id.et_opening_course_startDate);
        et_opening_course_EndDate = findViewById(R.id.et_opening_course_endDate);
        et_opening_course_starttime = findViewById(R.id.et_opening_course_starttime);
        et_opening_course_endtime = findViewById(R.id.et_opening_course_endtime);

        etStartDate = findViewById(R.id.et_startDate);
        etEndDate = findViewById(R.id.et_endDate);
        etDuration = findViewById(R.id.et_duration);
        etStartTime =findViewById(R.id.et_startTime);
        etEndTime = findViewById(R.id.et_endTime);
        etFee = findViewById(R.id.et_fee);
        iv_opening_course_detail = findViewById(R.id.iv_opening_course_detail);
        cv_opening_course_detail = findViewById(R.id.cv_opening_course_detail);
        btnOpeningSave = findViewById(R.id.btn_opening_save);
        btnOpenCourseEdit = findViewById(R.id.btn_open_course_edit);
        btnOpenCourseCancel = findViewById(R.id.btn_open_course_cancel);
        etStartDate.setOnClickListener(this);
        etEndDate.setOnClickListener(this);
        etStartTime.setOnClickListener(this);
        etEndTime.setOnClickListener(this);
        btnOpeningSave.setOnClickListener(this);
        btnOpenCourseEdit.setOnClickListener(this);
        btnOpenCourseCancel.setOnClickListener(this);

        btnOpeningCourseDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cv_opening_course_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();

            }
        });

        etFee.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                uploadImage();
                    return true;
                }
                return false;
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("opening_course_images");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            openCourseData = (OpeningCourseData) extras.getSerializable("OpeningCourseObj");
            if (openCourseData != null) {
               // spinner.setText(openCourseData.getOpeningCourseTitle());
                etDuration.setText(openCourseData.getOpeningCourseDuration());
                etDuration.setEnabled(false);
                etStartDate.setText(openCourseData.getOpeningCourseStartDate());
                etStartDate.setEnabled(false);
                etEndDate.setText(openCourseData.getOpeningCourseEndDate());
                etEndDate.setEnabled(false);
                etFee.setText(String.valueOf(openCourseData.getOpeningCourseFee()));
                etFee.setEnabled(false);
                etStartTime.setText(openCourseData.getOpeningCourseStartTime());
                etStartTime.setEnabled(false);
                etEndTime.setText(openCourseData.getOpeningCourseEndTime());
                etEndTime.setEnabled(false);
                spCourseList.setEnabled(false);
                btnOpenCourseEdit.setVisibility(View.VISIBLE);
                btnOpeningSave.setVisibility(View.GONE);
                cv_opening_course_detail.setEnabled(false);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.course);
                requestOptions.error(R.drawable.course);

                Glide.with(this)
                        .setDefaultRequestOptions(requestOptions)
                        .load(openCourseData.getOpeningCourseImageURL())
                        .into(cv_opening_course_detail);

            }
        }


        spCourseList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                item = (String)parent.getItemAtPosition(pos);
                for (CourseData courseDataObj : courseListArrayObj) {

                    if(item.equalsIgnoreCase(courseDataObj.getCourseTitle())){
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.course);
                        requestOptions.error(R.drawable.course);
                        courseImageUrl = courseDataObj.getCourseImageURL();
                        Glide.with(getApplicationContext())
                                .setDefaultRequestOptions(requestOptions)
                                .load(courseDataObj.getCourseImageURL())
                                .into(cv_opening_course_detail);
                        break;

                    }
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
                //  System.out.println("Nothing item ");
            }
        });






    }
    private void selectImage() {



        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };



        AlertDialog.Builder builder = new AlertDialog.Builder(OpeningCourseDetailActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    if(!isPermissionCameraGranted()){
                        ActivityCompat.requestPermissions(OpeningCourseDetailActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                PERMISSION_REQUEST_CAMERA);


                    }else{
                        actionCamera();

                    }



                }

                else if (options[item].equals("Choose from Gallery"))

                {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent,SELECT_PHOTO_GALLERY);


                  /*  Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);*/



                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }
 public Uri getImageUri(Context inContext, Bitmap inImage) {
     ByteArrayOutputStream bytes = new ByteArrayOutputStream();
     inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
     String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
     return Uri.parse(path);
 }
 @Override
 public void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     switch (requestCode){
         case SELECT_PHOTO_CAMERA:
             if(resultCode == Activity.RESULT_OK){
                 try {
                     Bundle extras = data.getExtras();
                     if (extras != null) {
                         imageBitmap = (Bitmap) extras.get("data");
                     }
                    // imageBitmap = (Bitmap) extras.get("data");
                     contentURI = getImageUri(getApplicationContext(), imageBitmap);
                     if(!isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                         ActivityCompat.requestPermissions(OpeningCourseDetailActivity.this,
                                 new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                 PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                     }else{
                         imageFileWrite();
                     }

                 } catch (IOException e) {
                     e.printStackTrace();
                 }

             }

             break;
         case SELECT_PHOTO_GALLERY:
             if(resultCode == Activity.RESULT_OK) {
                 if (data != null) {
                     Uri contentURI = data.getData();
                     try {
                         imageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), contentURI);
                         if(!isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                             ActivityCompat.requestPermissions(OpeningCourseDetailActivity.this,
                                     new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                     PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                         }else{
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
    private void imageFileWrite() throws IOException{
        String filePath = FileUtility.getPhotoFolderPath();
        String fileName = String.valueOf(System.currentTimeMillis()).concat(".jpg");
        String finalPhoto = filePath.concat(fileName);
        Pair<Boolean,String> returnResult = FileUtility.writeImageFile(filePath, fileName,imageBitmap);
        System.out.println("file path = "+finalPhoto);
        Uri uri = Uri.fromFile(new File(filePath,fileName));
        if (returnResult.first) {
            Picasso.with(this)
                    .load(uri)
                    .error(R.drawable.ic_launcher_background)
                    .into(cv_opening_course_detail);
          //  imageRVAdapter.addNewFile(new File(filePath,fileName));
        }
    }
    private boolean isPermissionGranted(String permission){
        return ContextCompat.checkSelfPermission(getApplicationContext(),
                permission)
                == PackageManager.PERMISSION_GRANTED;
    }
    private void actionCamera(){
        Intent galleryIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(galleryIntent,SELECT_PHOTO_CAMERA);
    }
    private boolean isPermissionCameraGranted(){
        return ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onClick(View view) {
        if (view == etStartDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            etStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                           /* if(!etEndDate.getText().toString().equalsIgnoreCase("") && !etStartTime.getText().toString().equalsIgnoreCase("")
                                    && !etEndTime.getText().toString().equalsIgnoreCase("") && (parseInt(etEndDate.getText().toString())>parseInt(etStartDate.getText().toString())))
                            {
                                try {
                                    calculateDuration();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
*/

                        }
                    }, mYear, mMonth, mDay);
           datePickerDialog.show();
        }
        if (view == etEndDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            etEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                           /* if(!etStartDate.getText().toString().equalsIgnoreCase("") && !etStartTime.getText().toString().equalsIgnoreCase("")
                                    && !etEndTime.getText().toString().equalsIgnoreCase("") && (parseInt(etEndDate.getText().toString())>parseInt(etStartDate.getText().toString())))
                            {
                                try {
                                    calculateDuration();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }*/

                           // calculateDuration();
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
       /* if (view == etDuration) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            etDuration.setText(hourOfDay + ":" + minute);





                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }*/
        if (view == etStartTime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            String AM_PM ;
                            if(hourOfDay < 12) {
                                AM_PM = "AM";
                            } else {
                                AM_PM = "PM";
                            }
                            etStartTime.setText(hourOfDay + ":" + minute+ " "+ AM_PM);

                          /*  if(!etStartDate.getText().toString().equalsIgnoreCase("") && !etEndDate.getText().toString().equalsIgnoreCase("")
                                    && !etEndTime.getText().toString().equalsIgnoreCase("") && (parseInt(etEndDate.getText().toString())>parseInt(etStartDate.getText().toString())))
                            {
                                try {
                                    calculateDuration();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }*/



                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (view == etEndTime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            String AM_PM ;
                            if(hourOfDay < 12) {
                                AM_PM = "AM";
                            } else {
                                AM_PM = "PM";
                            }
                            etEndTime.setText(hourOfDay + ":" + minute+" "+AM_PM);

                           /* if(!etStartDate.getText().toString().equalsIgnoreCase("") && !etEndDate.getText().toString().equalsIgnoreCase("")
                                    && !etStartTime.getText().toString().equalsIgnoreCase("") && (parseInt(etEndDate.getText().toString())>parseInt(etStartDate.getText().toString())))
                            {
                                try {
                                    calculateDuration();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }*/



                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if(view == btnOpeningSave)
        {
            saveFunction();
        }
        if(view == btnOpenCourseEdit){
            setUIEnable(true);
            btnOpenCourseEdit.setVisibility(View.GONE);
            btnOpeningSave.setVisibility(View.VISIBLE);
            btnOpeningSave.setText("Update");
            btnOpenCourseCancel.setVisibility(View.VISIBLE);

        }
        if(view == btnOpenCourseCancel)
        {
            btnOpeningSave.setVisibility(View.GONE);
            btnOpenCourseCancel.setVisibility(View.GONE);
            btnOpenCourseEdit.setVisibility(View.VISIBLE);
            setUIEnable(false);
        }


    }
    private void setUIEnable(boolean enable){
        spCourseList.setEnabled(enable);
        etStartDate.setEnabled(enable);
        etEndDate.setEnabled(enable);
        etStartTime.setEnabled(enable);
        etEndTime.setEnabled(enable);
        etDuration.setEnabled(enable);
        etFee.setEnabled(enable);
        cv_opening_course_detail.setEnabled(enable);


    }
private void calculateDuration() throws ParseException {


    String startdate = etStartDate.getText().toString();
    String enddate = etEndDate.getText().toString();
    String[] sdate1 = startdate.split("-");
    String sdate_day = sdate1[0];
    String sdate_month = sdate1[1];
    String sdate_year = sdate1[2];

    String[] edate1 = enddate.split("-");
    String edate_day = edate1[0];
    String edate_month = edate1[1];
    String edate_year = edate1[2];

    Calendar first = Calendar.getInstance() ;
    Calendar second = Calendar.getInstance();

    DateFormat df = new java.text.SimpleDateFormat("hh:mm");
    Date date1 = df.parse(etStartTime.getText().toString());
    Date date2 = df.parse(etEndTime.getText().toString());
    int timediff = (int) (date2.getTime() - date1.getTime());
    System.out.println("timediff = "+timediff);
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        System.out.println("O ----------------");
        first = new Calendar.Builder().setDate(parseInt(sdate_year), parseInt(sdate_month)+1, parseInt(sdate_day)).set(Calendar.AM_PM, 0).set(Calendar.HOUR, 8).build();
        second = new Calendar.Builder().setDate(parseInt(edate_year), parseInt(edate_month)+1, parseInt(edate_day)).set(Calendar.AM_PM, 0).set(Calendar.HOUR, 8).build();

    }else{
        System.out.println("not O ----------------");
        first.set(Calendar.DAY_OF_MONTH,parseInt(sdate_day));
        first.set(Calendar.MONTH,parseInt(sdate_month)+1); // 0-11 so 1 less
        first.set(Calendar.YEAR, parseInt(sdate_year));
       // first.set(Calendar.AM_PM, 0);
        first.set(Calendar.HOUR, timediff);


        second.set(Calendar.DAY_OF_MONTH,parseInt(edate_day));
        second.set(Calendar.MONTH,parseInt(edate_month)+1); // 0-11 so 1 less
        second.set(Calendar.YEAR, parseInt(edate_year));
       // second.set(Calendar.AM_PM, 0);
        second.set(Calendar.HOUR, timediff);
    }



    int numberOfDays = 0;
    long numberOfHours = 0;
//Get number of full days
    while(first.get(Calendar.DATE) != second.get(Calendar.DATE)){
        if(Calendar.SATURDAY != first.get(Calendar.DAY_OF_WEEK)
                && Calendar.SUNDAY != first.get(Calendar.DAY_OF_WEEK)){
            numberOfDays++;
        }
        first.roll(Calendar.DATE, true);
    }
//Get number of hours in the remaining day
    numberOfHours = TimeUnit.MILLISECONDS
            .toHours(second.getTimeInMillis() - first.getTimeInMillis());

    System.out.println("Number of Days = "+numberOfDays+",Number of Hours = "+numberOfHours);
    System.out.println("Difference = " +
            ( numberOfDays * 24 + numberOfHours ) + " hour(s)");
}

   @Override
    protected void onPostResume() {
        super.onPostResume();

       FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
       DatabaseReference refcourse = firebaseDatabase.getReference("courses");
       courseListArray = new ArrayList<>();

       refcourse.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int index = 0,count=0;
               for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                   CourseData courseDataObj=postSnapshot.getValue(CourseData.class);
                   //System.out.println("name data = "+name);
                   courseListArrayObj.add(courseDataObj);

                   if(courseDataObj != null){
                       String name=courseDataObj.getCourseTitle();
                       System.out.println("name data = "+name);
                       courseListArray.add(name);
                   }

                   if (openCourseData != null) {
                      if(openCourseData.getOpeningCourseTitle().equalsIgnoreCase(courseDataObj.getCourseTitle()))
                      { count = index;} }
                   index++;
               }
               ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_course_item, R.id.tv_courseData, courseListArray);
               spCourseList.setAdapter(adapter);
               spCourseList.setSelection(count);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

    @SuppressLint("SetTextI18n")
    private void saveFunction(){
     String chketDuration = etDuration.getText().toString();
     String chketStartDate = etStartDate.getText().toString();
     String chketEndDate = etEndDate.getText().toString();
        if(item.equals("") || item == null)
     {
         Toast.makeText(this,"Please fill course",Toast.LENGTH_SHORT).show();

     }else if(chketDuration.equals("")){
            etDuration.setError("Please fill duration.");
            etDuration.requestFocus();
     }else if(chketStartDate.equals(""))
     {
        etStartDate.setText("Please fill start date.");
        etStartDate.requestFocus();
     }else if(chketEndDate.equals(""))
        {
            etEndDate.setText("Please fill end date.");
            etEndDate.requestFocus();
        }else{
            uploadImage();
           // Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show();
        }


    }
    private void uploadImage() {
        uploadingDialog(true);
        if (contentURI != null) {

            final StorageReference ref = storageReference.child("opening_course_images/opening_course_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
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
                                    Toast.makeText(getApplicationContext(), "download Fail", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            uploadingDialog(false);
                            Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            uploadLoading.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        } else {
            saveOrUpdateToFirebase();
        }
    }
    private void saveOrUpdateToFirebase(){
        if(openCourseData == null){
            savetoFirebase();
        }else{
            updateToFirebase();
        }
    }
    private void savetoFirebase(){
        OpeningCourseData openCourseData = new OpeningCourseData();
        openCourseData.setOpeningCourseTitle(item);
        openCourseData.setOpeningCourseDuration(etDuration.getText().toString());
        openCourseData.setOpeningCourseStartDate(etStartDate.getText().toString());
        openCourseData.setOpeningCourseEndDate(etEndDate.getText().toString());
        openCourseData.setOpeningCourseFee(parseDouble(etFee.getText().toString()));
        openCourseData.setOpeningCourseStartTime(etStartTime.getText().toString());
        openCourseData.setOpeningCourseEndTime(etEndTime.getText().toString());
        if (downloadUri != null)
            openCourseData.setOpeningCourseImageURL(downloadUri.toString());
        else if(courseImageUrl != null || !courseImageUrl.equals(""))
            openCourseData.setOpeningCourseImageURL(courseImageUrl);

        StorageHelper.getStorageService().insertOpeningCourseData(openCourseData,this);
    }
    private void updateToFirebase(){

        openCourseData.setOpeningCourseTitle(item);
        openCourseData.setOpeningCourseDuration(etDuration.getText().toString());
        openCourseData.setOpeningCourseStartDate(etStartDate.getText().toString());
        openCourseData.setOpeningCourseEndDate(etEndDate.getText().toString());
        openCourseData.setOpeningCourseFee(parseDouble(etFee.getText().toString()));
        openCourseData.setOpeningCourseStartTime(etStartTime.getText().toString());
        openCourseData.setOpeningCourseEndTime(etEndTime.getText().toString());

        if (downloadUri != null)
            openCourseData.setOpeningCourseImageURL(downloadUri.toString());
        else if(courseImageUrl != null || !courseImageUrl.equals(""))
            openCourseData.setOpeningCourseImageURL(courseImageUrl);
        StorageHelper.getStorageService().updateOpeningCourseData(openCourseData, this);

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
    public void onSuccess(String msg) {
        uploadingDialog(false);
        hideKeyboard(OpeningCourseDetailActivity.this);
        finish();
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(OpeningCourseDetailActivity.this, "Fail Data insert", Toast.LENGTH_SHORT).show();
    }
}
