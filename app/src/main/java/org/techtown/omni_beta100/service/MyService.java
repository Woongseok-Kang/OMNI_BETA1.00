package org.techtown.omni_beta100.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import org.techtown.omni_beta100.fragment.HomeFragment;
import org.techtown.omni_beta100.fragment.MypageFragment;

import java.lang.ref.WeakReference;

public class MyService extends Service {

    public static final int INIT = 0;//처음
    public static final int RUN = 1;//실행중
    public static final int PAUSE = 2;//정지

    public static int status = INIT;
    static private long baseTime, pauseTime;
    static  private long baseTime_save, overTime, applyTime;

    private final MyHandler handler = new MyHandler(this);

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        //서비스에서 가장 먼저 호출됨(최초 한번만)
        Log.i("Timeservice","onCreate호출");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행

        baseTime = 0;
        pauseTime = 0;
        //저장된 공부시간 불러오기
        SharedPreferences sf = getApplicationContext().getSharedPreferences("sFile", Context.MODE_PRIVATE);
        String text = sf.getString("text", "00 : 00 : 00");

        //저장된 status 불러오기
        SharedPreferences sf2 = getApplicationContext().getSharedPreferences("sFile2", Context.MODE_PRIVATE);
        int stat = sf2.getInt("stat", INIT);

        SharedPreferences sf3 =getApplicationContext().getSharedPreferences("sFile3", Context.MODE_PRIVATE);
        long time_base = sf3.getLong("base", 0);

        status = stat;
        baseTime_save = time_base;
        applyTime = baseTime_save;

        long min = Long.parseLong(text.substring(5, 7));
        long hour = Long.parseLong(text.substring(0,2));
        long real_min = min + hour*60;

        HomeFragment.text_study_time.setText(text);


        //타이머의 시간을 Mypage에 넘겨주기
        if(real_min>=0)
        {
            MypageFragment.bt_studyshare.setText(String.format("오늘 학습량\n%02d분 %02d문장", real_min, MypageFragment.studyed_list.size()));
            SpannableString spannableString1 = new SpannableString(MypageFragment.bt_studyshare.getText().toString());
            spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 6, MypageFragment.bt_studyshare.getText().toString().length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            MypageFragment.bt_studyshare.setText(spannableString1);
        }
        else if(real_min>=100)
        {
            MypageFragment.bt_studyshare.setText(String.format("오늘 학습량\n%03d분 %02d문장", real_min, MypageFragment.studyed_list.size()));
            SpannableString spannableString1 = new SpannableString(MypageFragment.bt_studyshare.getText().toString());
            spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 6, MypageFragment.bt_studyshare.getText().toString().length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            MypageFragment.bt_studyshare.setText(spannableString1);
        }
        else if(real_min>=1000)
        {
            MypageFragment.bt_studyshare.setText(String.format("오늘 학습량\n%04d분 %02d문장", real_min, MypageFragment.studyed_list.size()));
            SpannableString spannableString1 = new SpannableString(MypageFragment.bt_studyshare.getText().toString());
            spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 6, MypageFragment.bt_studyshare.getText().toString().length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            MypageFragment.bt_studyshare.setText(spannableString1);
        }

        HomeFragment.bt_time_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("service", "start");
                start_time();
            }
        });
        Log.i("Myservice", "onStartCommand 호출");

        return super.onStartCommand(intent, flags, startId);

    }


    // 서비스가 종료될 때 실행
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Timeservice", "서비스 onDestroy 호출-----------------------------------------------------");

        System.out.println("onstop 되어버림..................---------------------------------------");

        if(status==RUN){
            status= PAUSE;
            handler.removeMessages(0);
        }

        SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences("sFile2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();

        int stat = status;
        editor2.putInt("stat", stat);
        editor2.commit();


    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i("TimeService", "진짜 요기를 들어와버리네?-------------------");
        stopSelf();
    }

    public static long getBaseTime() {
        return baseTime;
    }

    public static void setBaseTime(long baseTime) {
        MyService.baseTime = baseTime;
    }

    public static long getPauseTime() {
        return pauseTime;
    }

    public static void setPauseTime(long pauseTime) {
        MyService.pauseTime = pauseTime;
    }

    public static long getBaseTime_save() {
        return baseTime_save;
    }

    public static void setBaseTime_save(long baseTime_save) {
        MyService.baseTime_save = baseTime_save;
    }

    public static long getOverTime() {
        return overTime;
    }

    public static void setOverTime(long overTime) {
        MyService.overTime = overTime;
    }

    public static long getApplyTime() {
        return applyTime;
    }

    public static void setApplyTime(long applyTime) {
        MyService.applyTime = applyTime;
    }

    public static int getStatus() {
        return status;
    }

    public static void setStatus(int status) {
        MyService.status = status;
    }


    private void start_time(){
        switch(status){
            case INIT:
                //어플리케이션이 실행되고 나서 실제로 경과된 시간
                baseTime = SystemClock.elapsedRealtime();
                //핸들러 실행
                handler.sendEmptyMessage(0);
                HomeFragment.bt_time_start.setText("일시정지");
                HomeFragment.bt_time_reset.setEnabled(false);
                //상태변환
                status = RUN;
                break;

            case RUN :
                //핸들러 정지
                handler.removeMessages(0);
                //정지 시간 체크
                pauseTime = SystemClock.elapsedRealtime();
                HomeFragment.bt_time_start.setText("다시시작");
                HomeFragment.bt_time_reset.setEnabled(true);
                //상태변환
                status = PAUSE;
                break;
            case PAUSE :
                long reStart = SystemClock.elapsedRealtime();
                baseTime += (reStart - pauseTime);
                handler.sendEmptyMessage(0);
                HomeFragment.bt_time_reset.setEnabled(false);
                HomeFragment.bt_time_start.setText("일시정지");
                status = RUN;
        }
    }

    static public String getTime(){
        //경과된 시간 체크

        long nowTime = SystemClock.elapsedRealtime();
        //시스템이 부팅된 이후의 시간
        overTime = nowTime - baseTime + applyTime;

        long h = (overTime / (1000 * 60 * 60)) % 24;
        long m = (overTime / (1000 * 60)) % 60;
        long s = (overTime / 1000) % 60;

        String recTime = String.format("%02d : %02d : %02d",h,m,s);
        return recTime;
    }

    private class MyHandler extends Handler {
        private final WeakReference<MyService> weakReference;
        public MyHandler(MyService service){ this.weakReference = new WeakReference<>(service); }

        @Override
        public void handleMessage(@NonNull Message msg) {

            HomeFragment.text_study_time.setText(getTime());

            long min = Long.parseLong(getTime().substring(5, 7));
            long hour = Long.parseLong(getTime().substring(0,2));
            long real_min = min + hour*60;

            //Handler 부분에서 데이터를 저장 시켜버림 --> 강제종료시 저장 가능
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sFile", Context.MODE_PRIVATE);
            SharedPreferences sharedPreferences3 = getApplicationContext().getSharedPreferences("sFile3", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            SharedPreferences.Editor editor3 = sharedPreferences3.edit();

            String text = HomeFragment.text_study_time.getText().toString();
            editor.putString("text", text);

            baseTime_save = overTime;
            long base = baseTime_save;
            long pause = pauseTime;
            editor3.putLong("base", base);

             editor.commit();
             editor3.commit();

            if(real_min>0)
            {
                MypageFragment.bt_studyshare.setText(String.format("오늘 학습량\n%02d분 %02d문장", real_min, MypageFragment.studyed_list.size()));
                SpannableString spannableString1 = new SpannableString(MypageFragment.bt_studyshare.getText().toString());
                spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 6, 15,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                MypageFragment.bt_studyshare.setText(spannableString1);
            }
            else if(real_min>=100)
            {
                MypageFragment.bt_studyshare.setText(String.format("오늘 학습량\n%02d분 %02d문장", real_min, MypageFragment.studyed_list.size()));
                SpannableString spannableString1 = new SpannableString(MypageFragment.bt_studyshare.getText().toString());
                spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 6, 16,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                MypageFragment.bt_studyshare.setText(spannableString1);
            }
            else if(real_min>=1000)
            {
                MypageFragment.bt_studyshare.setText(String.format("오늘 학습량\n%02d분 %02d문장", real_min, MypageFragment.studyed_list.size()));
                SpannableString spannableString1 = new SpannableString(MypageFragment.bt_studyshare.getText().toString());
                spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 6, 17,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                MypageFragment.bt_studyshare.setText(spannableString1);
            }

            handler.sendEmptyMessage(0);
        }
    };
}
