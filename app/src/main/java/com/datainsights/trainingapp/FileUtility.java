package com.datainsights.trainingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileUtility {

    public static File getInternalStorage(Context context){
        return context.getFilesDir();
    }

    public static File getExternalStorage(){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath().concat("/").concat("TrainingApp"));
        if(!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public static String getPhotoFolderPath(){
        File file = new File(getExternalStorage().getAbsolutePath().concat("/").concat("photo"));
        if(!file.exists()){
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    private static boolean isExternalStorageAvailable(){
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static String getAvailableInternalMemorySize(Context context) {
        File path = getInternalStorage(context);
        return formatSize(path.getFreeSpace());
    }

    public static String getUsedInternalMemorySize(Context context) {
        File path = getInternalStorage(context);
        return formatSize(path.getTotalSpace() - path.getFreeSpace());
    }

    public static String getTotalInternalMemorySize(Context context) {
        File path = getInternalStorage(context);
        return formatSize(path.getTotalSpace());
    }

    public static String getAvailableExternalMemorySize() {
        if (isExternalStorageAvailable()) {
            File path = getExternalStorage();
            return formatSize(path.getFreeSpace());
        } else {
            return "-";
        }
    }

    public static String getUsedExternalMemorySize() {
        if (isExternalStorageAvailable()) {
            File path = getExternalStorage();
            return formatSize(path.getTotalSpace() - path.getFreeSpace());
        } else {
            return "-";
        }
    }

    public static String getTotalExternalMemorySize() {
        if (isExternalStorageAvailable()) {
            File path = getExternalStorage();
            return formatSize(path.getTotalSpace());
        } else {
            return "-";
        }
    }

    private static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = " KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = " MB";
                size /= 1024;
                if (size >= 1024) {
                    suffix = " GB";
                    size /= 1024;
                }
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File[] getAllPhotoFiles(){
        return new File(getPhotoFolderPath()).listFiles();
    }

    public static Pair<Boolean,String> writeImageFile(String filePath, String fileNameWithExtension,Bitmap bitmap)throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File file = new File(filePath,fileNameWithExtension);
        if(file.exists()){
            return new Pair<>(false,"File already exists");
        }
        file.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes.toByteArray());
        outputStream.close();
        return new Pair<>(true,"Successful save");
    }

    public static Pair<Boolean,String> writeFile(String filePath, String fileNameWithExtension, Object content) throws IOException{
        File file = new File(filePath,fileNameWithExtension);
        if(file.exists()){
            return new Pair<>(false,"File already exists");
        }
        file.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(content.toString().getBytes());
        outputStream.close();
        return new Pair<>(true,"Successful save");
    }

    public static String readTextFile(String filePath,String fileName){
        File file = new File(filePath,fileName);
        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return text.toString();
    }
}
