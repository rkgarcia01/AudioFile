package com.example.audiofileproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    private static int MICROPHONE_PERMISSION_CODE = 200;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaplayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isMicrophonePresent())
        {
            getMicrophonePermission();
        }

        firestore = FirebaseFirestore.getInstance();

        Map<String,Object> users = new HashMap<>();
        users.put("firstname", "Easy");
        users.put("lastname", "TUTO");
        users.put("description", "Subscribe");

        firestore.collection("users").add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_LONG).show();
            }
        });


    }
    public void btnRecordPressed(View v)
    {

        try
        {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(getRecordingFilePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(this, "Recording Has Started", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //mediaRecorder = new MediaRecorder();
        //mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        //mediaRecorder.setOutputFile(getRecordingFilePath());
        //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //mediaRecorder.prepare();
        //mediaRecorder.start();
    }

    public void btnStopPressed(View v)
    {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        Toast.makeText(this, "Recording Has Stopped", Toast.LENGTH_LONG).show();
    }

    public void btnPlayPressed(View v)
    {
        try
        {
            mediaplayer = new MediaPlayer();
            mediaplayer.setDataSource(getRecordingFilePath());
            mediaplayer.prepare();
            mediaplayer.start();
            Toast.makeText(this, "Recording Is Playing", Toast.LENGTH_LONG).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        //mediaplayer = new MediaPlayer();
        //mediaplayer.setDataSource(getRecordingFilePath());
        //mediaplayer.prepare();
        //mediaplayer.start();

    }
//  This method checks to see if the phone has Microphone Feature -> returns true else false
    private boolean isMicrophonePresent()
    {
        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE))
        {
            return true;
        }
        else
        {
            return false;
        }

    }
//  If the request to "RECORD_AUDIO" is denied ("PERMISSION_DENIED"), then we will "requestPermissions" to the user to record the audio with the static int"MICROPHONE_PERMISSION_CODE"
    private void getMicrophonePermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
        }
    }

    private String getRecordingFilePath()
    {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "testRecordingFile" + ".mp3");
        return file.getPath();

    }
}