package org.techtown.omni_beta100.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.omni_beta100.MainActivity;
import org.techtown.omni_beta100.R;

public class Splash2Activity extends AppCompatActivity {

    Handler hand;

    ImageView image_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        image_logo = (ImageView)findViewById(R.id.imageView_logo);

        Animation anim_logo = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        image_logo.startAnimation(anim_logo);





        hand = new Handler(); //딜래이를 주기 위해 핸들러 생성
        hand.postDelayed(run, 1000); // 딜레이 ( 런어블 객체는 mrun, 시간 3초)
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(Splash2Activity.this, MainActivity.class); //인텐트 생성(현 액티비티, 새로 실행할 액티비티)
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            //overridePendingTransition 이란 함수를 이용하여 fade in,out 효과를줌. 순서가 중요

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hand.removeCallbacks(run);
    }
}
