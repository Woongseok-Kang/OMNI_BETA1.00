package org.techtown.omni_beta100.AsyncTask;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import org.techtown.omni_beta100.util.MusicDto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.grpc.Context;

public class MyAsyncTask extends AsyncTask<Void, Void,Void> {
    String path;
    String title;
    String projectId;
    String bucketName;
    MusicDto music;
    Cursor cursor;
    InputStream stream;
    Storage storage;
    GoogleCredentials credentials;

    public MyAsyncTask(String projectId, String bucketName, MusicDto music, Cursor cursor, GoogleCredentials credentials) throws IOException {
      //  this.path = path;
      //  this.title = title;
        this.projectId = projectId;
        this.bucketName = bucketName;
        this.music = music;
        this.cursor = cursor;
        this.stream = stream;
        this.credentials = credentials;
        storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().getService();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        String path = cursor.getString( cursor.getColumnIndex( "_data" ) );
        String title = music.getTitle();
        Log.d("path : ",path);
        Log.d("title : ",title);
        try {
            UploadFile(projectId,bucketName,title,path,stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public void UploadFile(String projectId, String bucketName, String objectName, String filePath,InputStream stream)throws IOException
    {

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));  //오류 수정
    }


}
