package org.techtown.omni_beta100.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.omni_beta100.MainActivity;
import org.techtown.omni_beta100.R;


public class SplashActivity extends AppCompatActivity {


    Handler h;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

       /* ImageView o = findViewById(R.id.o);
        ImageView m = findViewById(R.id.m);
        ImageView n = findViewById(R.id.n);
        ImageView i = findViewById(R.id.i);

        Animation anim_o = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_splash_imageview);
        o.startAnimation(anim_o);
        Animation anim_m = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_splash_iamgeview2);
        m.startAnimation(anim_m);
        Animation anim_n = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_splash_imageview3);
        n.startAnimation(anim_n);
        Animation anim_i = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_splash_imageview4);
        i.startAnimation(anim_i);
*/

       ImageView new_logo = findViewById(R.id.new_logo);

       Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
       new_logo.startAnimation(animation);





        h= new Handler(); //딜래이를 주기 위해 핸들러 생성
        h.postDelayed(mrun, 2000); // 딜레이 ( 런어블 객체는 mrun, 시간 3초)
    }

    Runnable mrun = new Runnable() {
        @Override
        public void run(){
            Intent intent = new Intent(SplashActivity.this, MainActivity.class); //인텐트 생성(현 액티비티, 새로 실행할 액티비티)
            startActivity(intent);
            finish();
            overridePendingTransition(0, R.anim.fade_out);
            //overridePendingTransition 이란 함수를 이용하여 fade in,out 효과를줌. 순서가 중요

        }
    };

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        h.removeCallbacks(mrun);
    }

}


