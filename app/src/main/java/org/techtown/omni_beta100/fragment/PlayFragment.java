package org.techtown.omni_beta100.fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.techtown.omni_beta100.Interface.onBackPressedListener;
import org.techtown.omni_beta100.R;
import org.techtown.omni_beta100.activity.RecordActivity;
import org.techtown.omni_beta100.util.MusicDto;

public class PlayFragment extends Fragment implements onBackPressedListener {

    public static PlayFragment newInstance() {
        return new PlayFragment();
    }

    //
    View view;
    boolean isPlaying = false;

    //private ArrayList<MusicDto> list;
    public static MediaPlayer mediaPlayer;
    private TextView title;
    private ImageView album;
    private SeekBar bar_play;
    private TextView t1,t2;
    private Button bt_speed, bt_term, bt_count;

    public static final int INIT = 0; //처음
    public static final int RUN = 1; //실행중
    public static final int PAUSE = 2; // 정지

    public static int repeat=0;

    public static int status = INIT;

    public static int i = 1; //재생 버튼 경우를 바꾸기위한 변수
    private int playcount = 0;

    public static int val_speed = 0;
    public static int val_term = 0;
    public static int val_counter = 0;

    public float speed, term;
    public int counter;

     ImageButton bt_play;

    private int bar_moving_value;

    private long basetime, rec_time;

    //private ContentResolver res;

    //private int position;


    private int playbackPosition =0; //중지될때 위치
    //private AudioManager audioManager;//볼륨조절 위해서


    class MyThread extends Thread {
        @Override
        public void run() {
            while (isPlaying) {
                bar_play.setProgress(mediaPlayer.getCurrentPosition());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_play, container, false);

        bt_play = (ImageButton) view.findViewById(R.id.bt_play);
        bar_play = (SeekBar)view.findViewById(R.id.bar_play);

        t1 = (TextView)view.findViewById(R.id.t1);
        t2 = (TextView)view.findViewById(R.id.t2);

        bt_speed = (Button)view.findViewById(R.id.bt_x2);
        bt_term = (Button)view.findViewById(R.id.bt_term);
        bt_count = (Button)view.findViewById(R.id.bt_count);
        //Button bt_re_start = (Button) view.findViewById(R.id.bt_re_start);









        bt_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(val_speed==0) {
                    bt_speed.setText("x0.75");
                    val_speed=1;
                }
                else if(val_speed==1)
                {
                    bt_speed.setText("x0.5");
                    val_speed = 2;
                }
                else if(val_speed==2)
                {
                    bt_speed.setText("x2.0");
                    val_speed =3;
                }
                else if(val_speed==3){
                    bt_speed.setText("x1.5");
                    val_speed =4;
                }
                else if(val_speed==4){
                    bt_speed.setText("x1.0");
                    val_speed =0;
                }

            }
        });

        bt_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(val_term==0) {
                    bt_term.setText("1.0");
                    val_term=1;
                }
                else if(val_term==1)
                {
                    bt_term.setText("2.0");
                    val_term = 2;
                }
                else if(val_term==2)
                {
                    bt_term.setText("0.5");
                    val_term =0;
                }


            }
        });

        bt_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(val_counter==0){
                    bt_count.setText("3회");
                    val_counter= 1;
                }
                else if(val_counter==1){
                    bt_count.setText("5회");
                    val_counter= 2;
                }
                else if(val_counter==2){
                    bt_count.setText("∞");
                    val_counter= 3;

                }
                else if(val_counter==3){
                    bt_count.setText("1회");
                    val_counter =0;

                }
            }
        });




        bt_play.setOnClickListener(new View.OnClickListener() {



            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {






               // setPlayspeed(0.5f);




                //일시정지 후 다시 재생
                    if (i == 1) {
                        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                            mediaPlayer.seekTo(playbackPosition);
                            mediaPlayer.start();
                            isPlaying = true;
                            new MyThread().start();
                            //Toast.makeText(getActivity(), "Restart", Toast.LENGTH_SHORT).show();
                            bt_play.setImageResource(R.drawable.icon_pause2);
                            i = 0;
                        } else {
                            try {
                                if (mediaPlayer != null) {
                                    mediaPlayer.stop();
                                    mediaPlayer.release();
                                    mediaPlayer = null;
                                    bar_play.setProgress(0);
                                }
                                //playAudio();


                                speed = getSpeed();
                                term = getTerm();
                                counter = getCounter();

                                System.out.println(counter + "first");


                                System.out.println("반복 횟수 : " + counter);
                                System.out.println("반복 간격 : " + term);


                                //Toast.makeText(getActivity(), "first play", Toast.LENGTH_LONG).show();
                                repeat = 0;

                                    playMusic(((RecordActivity) getActivity()).getList().get(((RecordActivity) getActivity()).getPosition()));




                                bt_speed.setEnabled(false);
                                bt_term.setEnabled(false);
                                bt_count.setEnabled(false);



                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            bt_play.setImageResource(R.drawable.icon_pause2);
                            i = 0;

                        }
                    } else if (i == 0) {  //일시정지
                        if (mediaPlayer != null) {
                            //현재 재생위치 저장
                            playbackPosition = mediaPlayer.getCurrentPosition();
                            mediaPlayer.pause();
                            isPlaying = false;
                            //Toast.makeText(getActivity(), "Pause", Toast.LENGTH_LONG).show();
                            bt_play.setImageResource(R.drawable.icon_centerplay);
                            i = 1;


                            //stopPlay();
                            //isPlaying = false;
                            //Toast.makeText(getActivity(), "Stop!!!!", Toast.LENGTH_LONG).show();
                            //bt_play.setImageResource(R.drawable.icon_centerplay);
                            //i = 1;
                            //bar_play.setProgress(0);
                        }
                    } else {
                    /*if (mediaPlayer != null && !mediaPlayer.isPlaying()) {

                        mediaPlayer.seekTo(playbackPosition);
                        mediaPlayer.start();
                        isPlaying = true;
                        new MyThread().start();
                        Toast.makeText(getActivity(), "Restart", Toast.LENGTH_LONG).show();
                        bt_play.setImageResource(R.drawable.icon_pause2);
                        i = 0;*/
                    }

                }

                /*stopPlay();
                Toast.makeText(getActivity(), "정지",
                        Toast.LENGTH_LONG).show();*/
                //     }
                // });

       /* bt_re_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mediaPlayer.stop(); // 음악이 재생될때만 멈추도록 설정해주기! 안그러면 오류뜨더라.
                //mediaPlayer = null;

                ((RecordActivity)getActivity()).replaceFragment(RecordFragment.newInstance());





            }*/

        });

        bt_play.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                stopPlay();

                bt_speed.setText("x1.0");
                val_speed =0;

                bt_term.setText("0.5");
                val_term =0;

                bt_count.setText("1회");
                val_counter =0;

                ((RecordActivity) getActivity()).replaceFragment(RecordFragment.newInstance());
                return true;
            }
        });


        bar_play.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                    int m = progress / 60000;
                    int s = (progress % 60000) / 1000;

                    t1.setText((m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s));

                /*if(seekBar.getMax() == progress){
                    stopPlay();
                }*/


            }

            //드래그 시작할때
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                if(isPlaying == true){
                    try {

                        bar_moving_value =0;
                        isPlaying = false;
                        mediaPlayer.pause();

                    }
                    catch(Exception e){

                    }
                }else{
                    try {

                        bar_moving_value=1;
                        isPlaying = false;
                        mediaPlayer.pause();

                    }
                    catch(Exception e){

                    }
                }
            }


                //드래그 멈추고 손을 땔때
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


               /* try {

                    isPlaying = true;
                    int playposition = seekBar.getProgress();
                    mediaPlayer.seekTo(playposition);
                    mediaPlayer.start();
                    new MyThread().start();
                }
                catch(Exception e){
                    Toast.makeText(getActivity(), "재생 버튼을 먼저 누르세요.", Toast.LENGTH_LONG).show();
                    bar_play.setProgress(0);
                }*/


                if(bar_moving_value==0){
                    try {

                        System.out.println("요기 들어와야함ㄴㄴ");
                        isPlaying = true;
                        int playposition = seekBar.getProgress();
                        mediaPlayer.seekTo(playposition);
                        mediaPlayer.start();
                        new MyThread().start();
                    }
                    catch(Exception e){
                        Toast.makeText(getActivity(), "재생 버튼을 먼저 누르세요.", Toast.LENGTH_LONG).show();
                        bar_play.setProgress(0);
                    }
                }else{
                    try {

                        System.out.println("요기 들어와야함");

                        int playposition = seekBar.getProgress();
                        mediaPlayer.seekTo(playposition);
                        playbackPosition=playposition;
                        // mediaPlayer.start();
                        new MyThread().start();
                    }
                    catch(Exception e){
                        Toast.makeText(getActivity(), "재생 버튼을 먼저 누르세요.", Toast.LENGTH_LONG).show();
                        bar_play.setProgress(0);
                    }
                }

            }
        });



        return view;
    }

    public void realPlay(){

    }



    public void playMusic(final MusicDto musicDto) {

        final Handler hand = new Handler();
        killMediaPlayer();
        try {
            //bar_play.setProgress(0);
            if(mediaPlayer != null) {
                mediaPlayer.reset();
            }
            Uri musicURI = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, ""+musicDto.getId());


            mediaPlayer = MediaPlayer.create(getActivity(), musicURI);
           // mediaPlayer.setDataSource(getActivity(), musicURI);
           // mediaPlayer.prepare();
            //mediaPlayer.setLooping(true);


            setPlayspeed(speed);

            mediaPlayer.start();
            status = RUN;

            int a = mediaPlayer.getDuration();
            bar_play.setMax(a);
            isPlaying = true;

            int m = a/60000;
            int s = (a%60000)/1000;
            t2.setText((m<10?"0" + m : m ) + ":" + (s  <10 ? "0" + s : s));

           new MyThread().start();
            repeat++;

           mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
               @Override
               public void onCompletion(MediaPlayer mp) {
                   stopPlay();
                   System.out.println("-----------------------------------반복 된 횟수 : " + repeat);
                   status = PAUSE;
                   try {
                       //mediaPlayer.release();
                   }catch(Exception e){
                       e.printStackTrace();
                   }

                   System.out.println("-----------------------------------이후 반복 된 횟수 : " + repeat);
                   System.out.println("counter은 ??? : " + counter);

                   if(val_counter == 1 && repeat<3){

                           bt_speed.setEnabled(false);
                           bt_term.setEnabled(false);
                           bt_count.setEnabled(false);
                           System.out.println("요기안들어오낭");
                           hand.postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   playMusic(musicDto);
                                   i=0;
                                   bt_play.setImageResource(R.drawable.icon_pause2);
                               }
                           }, (long)term*1000);


                   }

                   else if(val_counter == 2 && repeat <5){

                       bt_speed.setEnabled(false);
                       bt_term.setEnabled(false);
                       bt_count.setEnabled(false);
                           System.out.println("횟수5번에 들어옴");
                       hand.postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               playMusic(musicDto);
                               i=0;
                               bt_play.setImageResource(R.drawable.icon_pause2);
                           }
                       }, (long)term*1000);

                   }
                   else if(val_counter == 3 && repeat < 10000){
                       bt_speed.setEnabled(false);
                       bt_term.setEnabled(false);
                       bt_count.setEnabled(false);
                       System.out.println("횟수무한에 들어옴");

                       hand.postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               playMusic(musicDto);
                               i=0;
                               bt_play.setImageResource(R.drawable.icon_pause2);
                           }
                       }, (long)term*1000);
                   }

               }
           });

        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("SimplePlayer", e.getMessage());
        }
    }



    //미디어를 재생하는 사용자 정의 메소드
    private void playAudio() throws Exception {

        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.toeic11);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();

    }

    @Override
    //액티비티가 화면에서 제거될 때 호출되는 메서드
    public void onDestroy() {
        killMediaPlayer();
        super.onDestroy();
    }


    private void killMediaPlayer() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setPlayspeed(float sp){


        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(sp));
    }



    public float getSpeed(){
        float speed;
        String s = bt_speed.getText().toString().substring(1,4);
        speed = Float.parseFloat(s);

        return speed;
    }
    public float getTerm(){
        float term;
        String s = bt_term.getText().toString();
        term = Float.parseFloat(s);

        return term;
    }
    public int getCounter(){
        int counter;
        String s = bt_count.getText().toString().substring(0,1);
        if(s.equals("∞"))
        {
            counter = 0;
        }
        else
        counter = Integer.parseInt(s);


        return counter;

    }

    public void stopPlay(){
        if(mediaPlayer != null)
        {
            isPlaying = false;
            mediaPlayer.stop();
            mediaPlayer.release();
            System.out.println("release완료");
           /* try {
                mediaPlayer.release();
            }catch(Exception e){
                e.printStackTrace();
            }*/

            bt_speed.setEnabled(true);
            bt_term.setEnabled(true);
            bt_count.setEnabled(true);
            bt_play.setImageResource(R.drawable.icon_centerplay);

            i = 1;
            bar_play.setProgress(0);

            mediaPlayer = null;



        }
        else{

        }
    }

    public void onBackPressed() {
        stopPlay();

        bt_speed.setText("x1.0");
        val_speed =0;

        bt_term.setText("0.5");
        val_term =0;

        bt_count.setText("1회");
        val_counter =0;

        Log.d("Playfragment", "fragment에서 onbackpress호출");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Playfragment", "Playfragment종료");
        //stopPlay();
    }
}


