package org.techtown.omni_beta100.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import org.techtown.omni_beta100.R;
import org.techtown.omni_beta100.activity.RecordActivity;
import org.techtown.omni_beta100.util.ListenDialog;

import java.io.File;
import java.io.IOException;


public class RecordFragment extends Fragment /*implements AutoPermissionsListener */ {

    public static RecordFragment newInstance() {
        return new RecordFragment();
    }


    View view;

    ImageButton bt_stop;

    //private String filePath, filename;
    int position = 0;
    public String filename;

    private MediaRecorder recorder;


    //public static final int INIT = 0; //처음
    //public static final int RUN = 1; // 실행중
    //public static final int PAUSE = 2; // 정지

    //public static int status = INIT;
    private long basetime, rec_time;

    TextView timer;
    //SeekBar play_rec_bar;
    boolean isPlaying = false;

    MediaPlayer rec_mp;

    //TextView seek_start, seek_end;

    class MyThread extends Thread {
        @Override
        public void run() {
            while (isPlaying) {
                ListenDialog.play_rec_bar.setProgress(rec_mp.getCurrentPosition());
            }
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_record, container, false);


        permissionCheck();

        timer = (TextView) view.findViewById(R.id.timer);

        //File sdcard = Environment.getExternalStorageDirectory();
        File sdcard = getContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC + File.separator
        + "omni_record");

        //String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + "omni_record"))
        File file = new File(sdcard, "omni_record.mp3");
        filename = file.getAbsolutePath();
        Log.d("getActivity()", "저장할 파일 명 : " + filename);

        recordAudio();


        bt_stop = (ImageButton) view.findViewById(R.id.bt_stop);

        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("녹음 정지가 안되는거네??");
                stopRecording();

                new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                        .setMessage("녹음을 들어보시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                final ListenDialog listenDialog = new ListenDialog(getActivity());
                                listenDialog.show();



                                startPlay();
                                //play_rec_bar = ListenDialog.play_rec_bar;
                                //seek_start = ListenDialog.seek_start;
                                //seek_end = ListenDialog.seek_end;

                               /* View innerView = getLayoutInflater().inflate(R.layout.alert_seekbar, null);
                                AlertDialog.Builder adialog = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

                                adialog.setView(innerView);
                                play_rec_bar = (SeekBar) innerView.findViewById(R.id.play_seek);
                                seek_start = (TextView) innerView.findViewById(R.id.seek_start);
                                seek_end = (TextView) innerView.findViewById(R.id.seek_end);
                                startPlay();


                                adialog.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ((RecordActivity) getActivity()).replaceFragment(PlayFragment.newInstance());

                                    }
                                });


                                adialog.setNegativeButton("다시듣기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {



                                    }
                                });
                                AlertDialog alert = adialog.create();
                                //alert.setTitle("재생");
                                alert.show();

                                alert.getButton(AlertDialog.BUTTON_NEGATIVE)
                                        .setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startPlay();
                                            }
                                        });*/



                                ListenDialog.bt_close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((RecordActivity) getActivity()).replaceFragment(PlayFragment.newInstance());
                                        listenDialog.dismiss();
                                    }
                                });

                                ListenDialog.bt_re_listen.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(isPlaying==true){
                                            stopPlay();
                                        }

                                        startPlay();
                                    }
                                });

                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ((RecordActivity) getActivity()).replaceFragment(PlayFragment.newInstance());
                            }
                        }).show();
            }
        });


        return view;
    }

    private void recordAudio() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(filename);

        try {
            recorder.prepare();
            recorder.start();
            Toast.makeText(getActivity(), "녹음 시작", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        basetime = SystemClock.elapsedRealtime();

        handler.sendEmptyMessage(0);
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            //Toast.makeText(getActivity(),"녹음중지", Toast.LENGTH_LONG).show();
        }

        ContentValues values = new ContentValues(10);
        values.put(MediaStore.MediaColumns.TITLE, "Recorded");
        values.put(MediaStore.Audio.Media.ALBUM, "Audio Album");
        values.put(MediaStore.Audio.Media.ARTIST, "Mike");
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, "Recorded Audio");
        values.put(MediaStore.Audio.Media.IS_RINGTONE, 1);
        values.put(MediaStore.Audio.Media.IS_MUSIC, 1);
        values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.DATA, filename);

        Uri audioUri = getActivity().getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);

        handler.removeMessages(0);
        rec_time = SystemClock.elapsedRealtime();

        timer.setText("00:00");

        basetime = 0;
        rec_time = 0;
    }

    private void startPlay() {

        killMediaPlayer();

        try {
            rec_mp = new MediaPlayer();
            rec_mp.setDataSource("file://" + filename);
            rec_mp.prepare();
            rec_mp.start();

            int a = rec_mp.getDuration();
            ListenDialog.play_rec_bar.setMax(a);
            isPlaying = true;

            int m = a / 60000;
            int s = (a % 60000) / 1000;
            ListenDialog.seek_end.setText((m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s));


            //


            new MyThread().start();


        } catch (Exception e) {
            e.printStackTrace();
        }

        ListenDialog.play_rec_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int m = progress / 60000;
                int s = (progress % 60000) / 1000;

                ListenDialog.seek_start.setText((m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s));

                if (seekBar.getMax() == progress) {
                    isPlaying = false;
                    rec_mp.pause();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                isPlaying = false;
                rec_mp.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                isPlaying = true;
                int playposition = seekBar.getProgress();
                rec_mp.seekTo(playposition);
                rec_mp.start();
                new MyThread().start();
            }

        });

        rec_mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlay();
            }
        });


    }

    public void stopPlay(){
        if(rec_mp != null)
        {
            isPlaying = false;
            rec_mp.stop();
            rec_mp.release();

            ListenDialog.play_rec_bar.setProgress(0);

        }
    }

    private String getTime() {
        long nowTime = SystemClock.elapsedRealtime();
        long overTime = nowTime - basetime;

        long m = overTime / 1000 / 60;
        long s = (overTime / 1000) % 60;
        long ms = overTime % 1000;
        String recTime = String.format("%02d:%02d", m, s);

        return recTime;
    }
  /* private String sendTimetoTotal(){
        long nowTime = SystemClock.elapsedRealtime();
        long overTime = nowTime - basetime;

        long m = overTime/1000/60;
        long s = (overTime/1000)%60;
        long ms = overTime % 1000;
        String recTime = String.format("%02분:%02초", m, s);

        return recTime;
    }*/

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            timer.setText(getTime());

            handler.sendEmptyMessage(0);
        }
    };

    public void permissionCheck() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO}, 1);
        }
    }


    private void killMediaPlayer() {
        if (rec_mp != null) {
            try {
                rec_mp.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


