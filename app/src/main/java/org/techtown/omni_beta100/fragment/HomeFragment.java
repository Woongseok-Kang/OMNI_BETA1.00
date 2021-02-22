package org.techtown.omni_beta100.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.techtown.omni_beta100.MainActivity;
import org.techtown.omni_beta100.R;
import org.techtown.omni_beta100.adapter.MySelectAdapter;
import org.techtown.omni_beta100.service.MyService;
import org.techtown.omni_beta100.service.TimeService;
import org.techtown.omni_beta100.util.Edit_item;
import org.techtown.omni_beta100.util.Edit_playlist_item;
import org.techtown.omni_beta100.util.MusicDto;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    MainActivity mainActivity;
    public static Button bt_time_start, bt_time_reset;
    public static TextView text_study_time;

    public static final int INIT = 0;//처음
    public static final int RUN = 1;//실행중
    public static final int PAUSE = 2;//정지

    public static int status = INIT;

    static private long baseTime, pauseTime;

    static  private long baseTime_save, overTime, applyTime; //서비스 안될 시 여기 열기!!

    public static TextView study_text;
    public static TextView text_empty;

    public static SwipeMenuListView recent_list;
    public static MySelectAdapter recent_adapter;


    public static ArrayList<MusicDto> recent_file;
    public static ArrayList<ArrayList<Edit_item>> recent_script;




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)getActivity();
        System.out.println("onAttach----------------------------------------------------------------------------");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;

        //Intent intent = new Intent(getActivity(), TimeService.class);
        //getActivity().stopService(intent);

        System.out.println("onDetach------------------------------------------------------------------------------------");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("oncreawte 호출-----------------------------------------------------------------------------------------");
    }

    @Override
    public void onResume() {
        super.onResume();
       /* baseTime = 0;
        pauseTime = 0;
        //공부 시간 그대로
        SharedPreferences sf = getActivity().getSharedPreferences("sFile", Context.MODE_PRIVATE);
        String text = sf.getString("text", "00 : 00 : 00");

        //Status 그대로
        SharedPreferences sf2 =getActivity().getSharedPreferences("sFile2", Context.MODE_PRIVATE);
        int stat = sf2.getInt("stat", INIT);

        SharedPreferences sf3 =getActivity().getSharedPreferences("sFile3", Context.MODE_PRIVATE);
        long time_base = sf3.getLong("base", 0);


        status = stat;
        baseTime_save = time_base;
        applyTime = baseTime_save;*/ //여기도 열어


        System.out.println("onREsume---------------------------------------------------------------------------------------------------");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        //baseTime = 0;
        //pauseTime = 0;
        //공부 시간 그대로
       /* SharedPreferences sf = getActivity().getSharedPreferences("sFile", Context.MODE_PRIVATE);
        String text = sf.getString("text", "00 : 00 : 00");

        //Status 그대로
        SharedPreferences sf2 =getActivity().getSharedPreferences("sFile2", Context.MODE_PRIVATE);
        int stat = sf2.getInt("stat", INIT);

        SharedPreferences sf3 =getActivity().getSharedPreferences("sFile3", Context.MODE_PRIVATE);
        long time_base = sf3.getLong("base", 0);

        //SharedPreferences sf4 =getActivity().getSharedPreferences("sFile4", Context.MODE_PRIVATE);
        //long time_pause = sf4.getLong("pause", 0);


        status = stat;
        baseTime_save = time_base;
        //pauseTime = time_pause;

        applyTime = baseTime_save;


        System.out.println("------------------------------------시작시-----------------------------------");
        System.out.println("applyTime :" + applyTime);
        System.out.println("basetime : " +  baseTime);
        System.out.println("overtime : " + overTime);
        System.out.println("base_timesave : " + baseTime_save);

        System.out.println("상태 : " + status);
        System.out.println(baseTime_save);
        System.out.println(text);*/ //여기도 열어





        View v =  inflater.inflate(R.layout.fragment_home,container,false);

        bt_time_start = (Button) v.findViewById(R.id.button_time_start);
        bt_time_reset = (Button) v.findViewById(R.id.button_time_reset);
        text_study_time = (TextView) v.findViewById(R.id.text_study_time);

        study_text = (TextView)v.findViewById(R.id.text_study);
        text_empty = (TextView)v.findViewById(R.id.textView123) ;

        recent_list = (SwipeMenuListView)v.findViewById(R.id.recent_musiclist);


        recent_file = ReadRecentMusic();
        recent_script = ReadRecentData();


        recent_adapter = new MySelectAdapter(getActivity(), recent_file);
        recent_list.setAdapter(recent_adapter);



        SharedPreferences sf = getActivity().getSharedPreferences("study_text", Context.MODE_PRIVATE);
        String st_text = sf.getString("name_study", "Today's Guest님의 공부시간");

       // System.out.println(st_text);
        study_text.setText(st_text);


        if(recent_file.size()==0){
            text_empty.setVisibility(View.VISIBLE);
        }
        else{
            text_empty.setVisibility(View.GONE);
        }

        bt_time_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertbuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                        .setTitle("학습기록 초기화")
                        .setMessage("학습 시간과 학습 문장 모두 초기화가 됩니다. 정말 초기화 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                HomeFragment.bt_time_start.setText("시작");

                                HomeFragment.text_study_time.setText("00 : 00 : 00");
                                MypageFragment.bt_studyshare.setText(String.format("오늘 학습량\n00분 00문장"));
                                SpannableString spannableString1 = new SpannableString(MypageFragment.bt_studyshare.getText().toString());
                                spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 6, 15,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                MypageFragment.bt_studyshare.setText(spannableString1);

                                MypageFragment.studyed_list.clear();
                                MyService.setBaseTime(0);
                                MyService.setPauseTime(0);
                                MyService.setBaseTime_save(0);
                                MyService.setStatus(INIT);
                                MyService.setApplyTime(0);



                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {




                            }
                        });

                AlertDialog alertDialog = alertbuilder.create();
                alertDialog.show();


            }
        });

        //getActivity().startService(new Intent(getActivity().getBaseContext(), TimeService.class));



        /*bt_time_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), TimeService.class);
                getActivity().startService(intent);
            }
        });*/

        /*bt_time_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                bt_time_start.setText("시작");

                text_study_time.setText("00 : 00 : 00");
                MypageFragment.bt_studyshare.setText(String.format("오늘 학습량\n00분 00문장"));
                SpannableString spannableString1 = new SpannableString(MypageFragment.bt_studyshare.getText().toString());
                spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 6, 15,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                MypageFragment.bt_studyshare.setText(spannableString1);

                TimeService.setBaseTime(0);
                TimeService.setPauseTime(0);
                TimeService.setBaseTime_save(0);
                TimeService.setStatus(INIT);
                TimeService.setApplyTime(0);


                ///여기 고치기 stopservice 겹쳐서 자꾸 이상한 시간. 2020.12.19 저녁
                Intent intent = new Intent(getActivity(), TimeService.class);
                getActivity().stopService(intent);

            }
        });*/

       /* bt_time_start = (Button) v.findViewById(R.id.button_time_start);
        bt_time_reset = (Button) v.findViewById(R.id.button_time_reset);

        text_study_time = (TextView) v.findViewById(R.id.text_study_time);
        text_study_time.setText(text);*/ //여기도 열어...

       /* MypageFragment.bt_studyshare.setText("오늘 학습량\n" + text.substring(5,12) + "분 00문장");
        SpannableString spannableString1 = new SpannableString(MypageFragment.bt_studyshare.getText().toString());
        spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 6, 15,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        MypageFragment.bt_studyshare.setText(spannableString1);*/

        /*long min = Long.parseLong(text.substring(5, 7));
        long hour = Long.parseLong(text.substring(0,2));

        long real_min = min + hour*60;

        //System.out.println("--------------------------분으로는 :~~~~~~~~~~~~~~```" + real_min);

        if(real_min>0)
        {
            MypageFragment.bt_studyshare.setText(String.format("오늘 학습량\n%02d분 00문장", real_min));
            SpannableString spannableString1 = new SpannableString(MypageFragment.bt_studyshare.getText().toString());
            spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 6, 15,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            MypageFragment.bt_studyshare.setText(spannableString1);
        }
        else if(real_min>=100)
        {
            MypageFragment.bt_studyshare.setText(String.format("오늘 학습량\n%03d분 00문장", real_min));
            SpannableString spannableString1 = new SpannableString(MypageFragment.bt_studyshare.getText().toString());
            spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 6, 16,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            MypageFragment.bt_studyshare.setText(spannableString1);
        }
        else if(real_min>=1000)
        {
            MypageFragment.bt_studyshare.setText(String.format("오늘 학습량\n%02d분 00문장", real_min));
            SpannableString spannableString1 = new SpannableString(MypageFragment.bt_studyshare.getText().toString());
            spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 6, 17,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            MypageFragment.bt_studyshare.setText(spannableString1);
        }


        if(status == PAUSE)
        {
            bt_time_start.setText("다시시작");
        }

       bt_time_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_time();
            }
        });

        bt_time_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_time_start.setText("시작");

                text_study_time.setText("00 : 00 : 00");
                MypageFragment.bt_studyshare.setText(String.format("오늘 학습량\n00분 00문장"));
                SpannableString spannableString1 = new SpannableString(MypageFragment.bt_studyshare.getText().toString());
                spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 6, 15,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                MypageFragment.bt_studyshare.setText(spannableString1);

                baseTime = 0;
                pauseTime = 0;
                baseTime_save = 0;
                status = INIT;
                applyTime = 0;
            }
        });*/ // 여기도 열어




        return v;
    }

    /*private void start_time(){
        switch(status){
            case INIT:
                //어플리케이션이 실행되고 나서 실제로 경과된 시간
                baseTime = SystemClock.elapsedRealtime();

                //핸들러 실행
                handler.sendEmptyMessage(0);
                //MypageFragment.handler.sendEmptyMessage(0);
                bt_time_start.setText("일시정지");

                bt_time_reset.setEnabled(false);
                //상태변환
                status = RUN;
                break;

            case RUN :
                //핸들러 정지
                handler.removeMessages(0);
                //MypageFragment.handler.removeMessages(0);

                //정지 시간 체크
                pauseTime = SystemClock.elapsedRealtime();

                bt_time_start.setText("다시시작");

                bt_time_reset.setEnabled(true);


                //상태변환
                status = PAUSE;
                break;

            case PAUSE :
                long reStart = SystemClock.elapsedRealtime();
                baseTime += (reStart - pauseTime);
                handler.sendEmptyMessage(0);
                //MypageFragment.handler.sendEmptyMessage(0);

                System.out.println("요기 들어왓긴햇어..pause-----------------------------------------------------------------");
                System.out.println("pauesetime : " + pauseTime);



                bt_time_reset.setEnabled(false);
                bt_time_start.setText("일시정지");

                status = RUN;


        }
    }

    static public String getTime(){
        //경과된 시간 체크

        long nowTime = SystemClock.elapsedRealtime();
        //시스템이 부팅된 이후의 시간?
        overTime = nowTime - baseTime + applyTime;


        long h = (overTime / (1000 * 60 * 60)) % 24;
        long m = (overTime / (1000 * 60)) % 60;
        long s = (overTime / 1000) % 60;


        String recTime = String.format("%02d : %02d : %02d",h,m,s);
        System.out.println("applyTime :" + applyTime);
        System.out.println("basetime : " +  baseTime);
        System.out.println("overtime : " + overTime);
        System.out.println("base_timesave : " + baseTime_save);
        System.out.println("pausetime : " + pauseTime);
        System.out.println(recTime + "실행 베이베..");

        return recTime;
    }*/ // 여기도 열어


    private void SaveRecentMusic(ArrayList<MusicDto> recent_music){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(recent_music);
        editor.putString("SaverecentMusics", json);
        editor.commit();
    }

    private ArrayList<MusicDto> ReadRecentMusic(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("SaverecentMusics", "EMPTY");
        if(json.equals("EMPTY")){
            ArrayList<MusicDto> arrayList = new ArrayList<>(5);
            return arrayList;
        }
        else {
            Type type = new TypeToken<ArrayList<MusicDto>>() {
            }.getType();
            ArrayList<MusicDto> arrayList = gson.fromJson(json, type);
            return arrayList;
        }
    }

    private void SaveRecentData(ArrayList<ArrayList<Edit_item>> devide_items){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(devide_items);
        editor.putString("recent_script", json);
        editor.commit();
    }

    private ArrayList<ArrayList<Edit_item>> ReadRecentData(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("recent_script", "EMPTY");
        if(json.equals("EMPTY")){
            ArrayList<ArrayList<Edit_item>> arrayList = new ArrayList<>();


            for(int i =0;i<5;i++)
            {
                arrayList.add(i, null);
            }

            return arrayList;
        }
        else {
            Type type = new TypeToken<ArrayList<ArrayList<Edit_item>>>() {
            }.getType();
            ArrayList<ArrayList<Edit_item>> arrayList = gson.fromJson(json, type);
            return arrayList;
        }
    }




    public void save_studytext(){
        SharedPreferences sp = getActivity().getSharedPreferences("study_text", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String text = study_text.getText().toString();
        editor.putString("name_study", text);
        // editor.putInt("setence_count", study_sentence_count);
        editor.commit();
    }


    @Override
    public void onPause() {
        super.onPause();

        System.out.println("pause 되버림..............-----------------------------------------");
    }

    @Override
    public void onStop() {
        super.onStop();

        save_studytext();
        SaveRecentData(recent_script);
        SaveRecentMusic(recent_file);
        System.out.println("onstop 되어버림..................---------------------------------------");



        /*if(status==RUN){
            status= PAUSE;
            bt_time_start.setText("다시시작");
            handler.removeMessages(0);
        }


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sFile", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("sFile2", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences3 = getActivity().getSharedPreferences("sFile3", Context.MODE_PRIVATE);
        //SharedPreferences sharedPreferences4 = getActivity().getSharedPreferences("sFile4", Context.MODE_PRIVATE);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        SharedPreferences.Editor editor3 = sharedPreferences3.edit();
        //SharedPreferences.Editor editor4 = sharedPreferences4.edit();

        String text = text_study_time.getText().toString();
        editor.putString("text", text);



        int stat = status;
        editor2.putInt("stat", stat);

        baseTime_save = overTime;
        long base = baseTime_save;
        //long pause = pauseTime;

        editor3.putLong("base", base);
        //editor4.putLong("pause", pause);



        editor.commit();
        editor2.commit();
        editor3.commit();


       // editor4.commit();
        System.out.println("----------------------------종료시--------------------------");
        System.out.println("applyTime :" + applyTime);
        System.out.println("basetime : " +  baseTime);
        System.out.println("overtime : " + overTime);
        System.out.println("base_timesave : " + baseTime_save);
        System.out.println("상태 : " + status);*/ // 여기도 열어

    }


    /*Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {


            text_study_time.setText(getTime());

            long min = Long.parseLong(getTime().substring(5, 7));
            long hour = Long.parseLong(getTime().substring(0,2));

            long real_min = min + hour*60;

            //System.out.println("--------------------------분으로는 :~~~~~~~~~~~~~~```" + real_min);

            if(real_min>0)
            {
                MypageFragment.bt_studyshare.setText(String.format("오늘 학습량\n%02d분 00문장", real_min));
                SpannableString spannableString1 = new SpannableString(MypageFragment.bt_studyshare.getText().toString());
                spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 6, 15,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                MypageFragment.bt_studyshare.setText(spannableString1);
            }
            else if(real_min>=100)
            {
                MypageFragment.bt_studyshare.setText(String.format("오늘 학습량\n%03d분 00문장", real_min));
                SpannableString spannableString1 = new SpannableString(MypageFragment.bt_studyshare.getText().toString());
                spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 6, 16,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                MypageFragment.bt_studyshare.setText(spannableString1);
            }
            else if(real_min>=1000)
            {
                MypageFragment.bt_studyshare.setText(String.format("오늘 학습량\n%02d분 00문장", real_min));
                SpannableString spannableString1 = new SpannableString(MypageFragment.bt_studyshare.getText().toString());
                spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 6, 17,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                MypageFragment.bt_studyshare.setText(spannableString1);
            }



            handler.sendEmptyMessage(0);
        }
    };*/  // 여기도 열어


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("HomeFragment", "Destroy 되었당......................................");

        Intent intent = new Intent(getActivity(), MyService.class);
        getActivity().stopService(intent);

        Log.i("HomeFragment", "서비스 종료 되었을텐데....");


    }
}