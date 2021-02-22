package org.techtown.omni_beta100;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.techtown.omni_beta100.fragment.FilesFragment;
import org.techtown.omni_beta100.fragment.HomeFragment;
import org.techtown.omni_beta100.fragment.MypageFragment;
import org.techtown.omni_beta100.service.MyService;
import org.techtown.omni_beta100.service.TimeService;
import org.techtown.omni_beta100.util.PermissionSupport;


public class MainActivity extends AppCompatActivity {


    private PermissionSupport permission;

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    private HomeFragment fragment1;
    private FilesFragment fragment2;
    private MypageFragment fragment3;

    private long time = 0;

    @Override
    public void onBackPressed() {

        if(System.currentTimeMillis() - time >=2000) {
            time = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
        else if(System.currentTimeMillis() - time < 2000){
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionCheck();


        //startService(new Intent(getBaseContext(), TimeService.class));

        //startService(new Intent(MainActivity.this, TimeService.class));



        System.out.println("서비스 시작");



        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        fragment1 = new HomeFragment();
        fragment3 = new MypageFragment();
        //getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment1).commitAllowingStateLoss();

        fragmentManager.beginTransaction().add(R.id.frame, fragment3).commit();
        fragmentManager.beginTransaction().add(R.id.frame, fragment1).commit();
        if(fragment3!=null)
            fragmentManager.beginTransaction().hide(fragment3).commit();
        //fragmentManager.beginTransaction().replace(R.id.frame, fragment1).commit();

        startService(new Intent(MainActivity.this, MyService.class));


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch(item.getItemId()){
                    case R.id.navigation_home: {
                        if(fragment1==null){
                            fragment1 = new HomeFragment();
                            fragmentManager.beginTransaction().add(R.id.frame, fragment1).commit();
                        }
                        if(fragment1!=null)fragmentManager.beginTransaction().show(fragment1).commit();
                        if(fragment2!=null)fragmentManager.beginTransaction().hide(fragment2).commit();
                        if(fragment3!=null)fragmentManager.beginTransaction().hide(fragment3).commit();
                        //getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment1).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_file: {
                        if(fragment2==null){
                            fragment2 = new FilesFragment();
                            fragmentManager.beginTransaction().add(R.id.frame, fragment2, "item").commit();
                        }
                        if(fragment1!=null)fragmentManager.beginTransaction().hide(fragment1).commit();
                        if(fragment2!=null)fragmentManager.beginTransaction().show(fragment2).commit();
                        if(fragment3!=null)fragmentManager.beginTransaction().hide(fragment3).commit();
                        break;
                    }
                    case R.id.navigation_report: {
                        if(fragment3==null){
                            fragment3 = new MypageFragment();
                            fragmentManager.beginTransaction().add(R.id.frame, fragment3).commit();
                        }
                        //fragment3 = new MypageFragment();
                        //fragmentManager.beginTransaction().add(R.id.frame, fragment3).commit();
                        //fragmentManager.beginTransaction().show(fragment3).commit();
                        if(fragment1!=null)fragmentManager.beginTransaction().hide(fragment1).commit();
                        if(fragment2!=null)fragmentManager.beginTransaction().hide(fragment2).commit();
                        if(fragment3!=null)fragmentManager.beginTransaction().show(fragment3).commit();

                        break;
                    }
                }
                return true;
            }
        });
    }

    private void permissionCheck(){
        //SDK23 버전 이하 버전에서는 Permission불필요
        if(Build.VERSION.SDK_INT >=23){
            permission = new PermissionSupport(this, this);


            //권한 체크후 리턴이 false로 들어온다면
            if(!permission.checkPermission()){
                permission.requestPermission();
                //System.out.println("작동이 잘 안될수도 있습니다.");
                System.out.println("1차 리턴");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 리턴이 false로 들어온다면!!!

        if(!permission.permissionResult(requestCode, permissions, grantResults)){
            //permission.requestPermission(); //  요기 alert만들기!!!
            System.out.println("2차 리턴");
            System.out.println("작동이 잘 안될수도 있습니다.");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity" , "onStart...........");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity" , "onStop............");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity" , "onPause............");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.e("MainActivity", "??????????????????????????");
        /*System.out.println("여기 결과가 들어오ㅘ야해!!!!!!!!!!!!!!!!!!!!!!!!!1");
        if (requestCode == 0 ) {
            if (resultCode == RESULT_OK) {
                System.out.println("결과ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ");
                Toast.makeText(this, "Result: " + data.getStringExtra("devide_com"), Toast.LENGTH_SHORT).show();
            } else {   // RESULT_CANCEL
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
//
        }*/

        /*if (requestCode == 49) {
            if (resultCode == RESULT_OK) {
                Log.e("MainAcitivy", "일단 이거 받았네????");
                Toast.makeText(this, "Result: " + data.getStringExtra("position_re"), Toast.LENGTH_SHORT).show();
            } else {   // RESULT_CANCEL
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }

        } */

            int request = requestCode & 0xffff;

            Fragment fragment = getSupportFragmentManager().findFragmentByTag("item");
            fragment.onActivityResult(request, resultCode, data);

    }
}

