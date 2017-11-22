package com.example.user.soundrecorder;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    Button btnRecord,btnStop,btnPlay,btnSPR;
    String nama_file = null;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    String randomAudioFileName = "abcdefghijklmnopqrstuvwxyz";
    Random random;
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRecord = (Button)findViewById(R.id.btn_record);
        btnStop = (Button)findViewById(R.id.btn_stop);
        btnSPR = (Button)findViewById(R.id.btn_spr);
        btnPlay = (Button)findViewById(R.id.btn_play);

        btnStop.setEnabled(false);
        btnPlay.setEnabled(false);
        btnSPR.setEnabled(false);

        random = new Random();

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    nama_file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                            CreateRandomFileName(5) + "AudioRecorder.mp3";
                    MediaRecordReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    btnRecord.setEnabled(false);
                    btnStop.setEnabled(true);
                    Toast.makeText(MainActivity.this, "Recording Mulai Nih :D", Toast.LENGTH_SHORT).show();

                } else

                {
                    RequestPermossion();
                }
            }

        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                btnStop.setEnabled(false);
                btnPlay.setEnabled(true);
                btnRecord.setEnabled(true);
                btnSPR.setEnabled(false);

                Toast.makeText(MainActivity.this, "Record Selesai Yaaa :*", Toast.LENGTH_SHORT).show();

            }
        });

        btnSPR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStop.setEnabled(false);
                btnRecord.setEnabled(true);
                btnSPR.setEnabled(false);
                btnPlay.setEnabled(true);

                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecordReady();
                }

            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStop.setEnabled(false);
                btnRecord.setEnabled(false);
                btnSPR.setEnabled(true);
                mediaPlayer = new MediaPlayer();
                try{
                    mediaPlayer.setDataSource(nama_file);
                    mediaPlayer.prepare();
                }catch (IOException e){
                    e.printStackTrace();
                }
                mediaPlayer.start();
                Toast.makeText(MainActivity.this, "Recordingnya Mulai Cuy", Toast.LENGTH_SHORT).show();

            }
        });
        }
        public void MediaRecordReady(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setOutputFile(nama_file);
    }

    public String CreateRandomFileName(int string){
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string){
            stringBuilder.append(randomAudioFileName.charAt(random.nextInt(randomAudioFileName.length())));
            i++;
        }
        return stringBuilder.toString();
    }
    private void RequestPermossion(){
        ActivityCompat.requestPermissions(MainActivity.this, new
        String[]{WRITE_EXTERNAL_STORAGE,RECORD_AUDIO},RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case RequestPermissionCode:
                if (grantResults.length>0){
                    boolean storePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean recordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(storePermission && recordPermission){
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Permission", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
}
