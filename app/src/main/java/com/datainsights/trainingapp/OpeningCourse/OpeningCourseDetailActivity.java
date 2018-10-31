package com.datainsights.trainingapp.OpeningCourse;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.datainsights.trainingapp.FileUtility;
import com.datainsights.trainingapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class OpeningCourseDetailActivity extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback{
    Button btnOpeningCourseDetailBack;
    Spinner spCourseList;
    String[] courseData = new String[]{"Oracle","Java Programming Language","Professional Android development"};
    CardView et_opening_course_Duration,et_opening_course_StartDate,et_opening_course_EndDate;
    TextView etStartDate,etDuration,etEndDate;
    int mYear,mMonth,mDay,mHour,mMinute;
    ImageView iv_opening_course_detail;
    CircleImageView cv_opening_course_detail;
    private static final int SELECT_PHOTO_GALLERY = 5;
    private static final int PERMISSION_REQUEST_CAMERA = 15;
    private static final int SELECT_PHOTO_CAMERA = 10;
    private final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 25;
    Bitmap imageBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_course_detail);
        btnOpeningCourseDetailBack = findViewById(R.id.btn_opening_course_detail_back);
        spCourseList = findViewById(R.id.sp_course_list);
        et_opening_course_Duration = findViewById(R.id.et_opening_course_duration);
        et_opening_course_StartDate = findViewById(R.id.et_opening_course_startDate);
        et_opening_course_EndDate = findViewById(R.id.et_opening_course_endDate);
        etStartDate = findViewById(R.id.et_startDate);
        etEndDate = findViewById(R.id.et_endDate);
        etDuration = findViewById(R.id.et_duration);
        iv_opening_course_detail = findViewById(R.id.iv_opening_course_detail);
        cv_opening_course_detail = findViewById(R.id.cv_opening_course_detail);
        etStartDate.setOnClickListener(this);
        etEndDate.setOnClickListener(this);
        etDuration.setOnClickListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spinner_course_item,R.id.tv_courseData,courseData);
        spCourseList.setAdapter(adapter);

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
               /* AlertDialog.Builder builderSingle = new AlertDialog.Builder(OpeningCourseDetailActivity.this);
               // builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle("Add Photo!");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(OpeningCourseDetailActivity.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("Take Photo");
                arrayAdapter.add("Choose from Gallery");
                arrayAdapter.add("Cancel");
               // arrayAdapter.add("Umang");
              //  arrayAdapter.add("Gatti");

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(OpeningCourseDetailActivity.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();*/



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



                   /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(intent, 1);*/
                   /* File f = new File(Environment.getExternalStorageDirectory().toString());

                    for (File temp : f.listFiles()) {

                        if (temp.getName().equals("temp.jpg")) {

                            f = temp;

                            break;

                        }

                    }

                    try {

                        Bitmap bitmap;

                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();



                        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),

                                bitmapOptions);



                        viewImage.setImageBitmap(bitmap);



                        String path = android.os.Environment

                                .getExternalStorageDirectory()

                                + File.separator

                                + "Phoenix" + File.separator + "default";

                        f.delete();

                        OutputStream outFile = null;

                        File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                        try {

                            outFile = new FileOutputStream(file);

                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);

                            outFile.flush();

                            outFile.close();

                        } catch (FileNotFoundException e) {

                            e.printStackTrace();

                        } catch (IOException e) {

                            e.printStackTrace();

                        } catch (Exception e) {

                            e.printStackTrace();

                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }
*/
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
 /*   private void imageFileWrite() throws IOException{
        String filePath = FileUtility.getPhotoFolderPath();
        String fileName = String.valueOf(System.currentTimeMillis()).concat(".jpg");
        Pair<Boolean,String> returnResult = FileUtility.writeImageFile(filePath, fileName,imageBitmap);
        if (returnResult.first) {
            imageRVAdapter.addNewFile(new File(filePath,fileName));
        }
    }*/
 @Override
 public void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     switch (requestCode){
         case SELECT_PHOTO_CAMERA:
             if(resultCode == Activity.RESULT_OK){
                 try {
                     Bundle extras = data.getExtras();
                     imageBitmap = (Bitmap) extras.get("data");
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

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            etEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view == etDuration) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            etDuration.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}
