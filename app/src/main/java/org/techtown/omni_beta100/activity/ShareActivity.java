package org.techtown.omni_beta100.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;


import org.techtown.omni_beta100.R;
import org.techtown.omni_beta100.fragment.MypageFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShareActivity extends AppCompatActivity {


    private ImageButton bt_kakao;
    private ImageButton bt_insta;
    private ImageButton bt_back, bt_capture;
    private ImageButton share_normal;
    private LinearLayout cap;
    private TextView share_text;
    private TextView text_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);


        bt_kakao = (ImageButton)findViewById(R.id.kakao_button);
        bt_insta = (ImageButton)findViewById(R.id.insta_button);
        share_text = (TextView)findViewById(R.id.text_share_social);
        share_normal = (ImageButton)findViewById(R.id.share_normal);
        text_name = (TextView)findViewById(R.id.textView_name);


        if(MypageFragment.bt_studyshare.getText().toString().length()==15){
            share_text.setText(MypageFragment.bt_studyshare.getText().toString().substring(7, 15));
        }
        else if(MypageFragment.bt_studyshare.getText().toString().length()==16)
        {
            share_text.setText(MypageFragment.bt_studyshare.getText().toString().substring(7, 16));
        }
        else if(MypageFragment.bt_studyshare.getText().toString().length()==17)
        {
            share_text.setText(MypageFragment.bt_studyshare.getText().toString().substring(7, 17));
        }

        text_name.setText(MypageFragment.txt_name.getText().toString() + "의 총 학습량");


        cap = (LinearLayout) findViewById(R.id.layout_share);


        bt_back = (ImageButton)findViewById(R.id.back_button);
        bt_capture = (ImageButton)findViewById(R.id.bt_capture);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(ShareActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);


                cap.setBackgroundResource(R.drawable.share_background);
                cap.buildDrawingCache();   // 캡처할 뷰를 지정하여 buildDrawingCache() 한다
                Bitmap captureView = cap.getDrawingCache();   // 캡쳐할 뷰를 지정하여 getDrawingCache() 한다



                FileOutputStream fos;   // FileOutputStream 이용 파일 쓰기 한다
               // String strFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath();

                File strFolderPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + "omni_capture");
                File folder = new File(String.valueOf(strFolderPath));
                if(!folder.exists()) {  // 해당 폴더 없으면 만들어라
                    folder.mkdirs();
                }

                long time = System.currentTimeMillis();  //시간 받기
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
                //포멧 변환  형식 만들기
                Date dd = new Date(time);  //받은 시간을 Date 형식으로 바꾸기
                String strTime = sdf.format(dd); //Data 정보를 포멧 변환하기


               // String strFilePath = strFolderPath + "/" + "studytime_" + strTime + ".png";
                String strFilePath = strFolderPath + "/" + "studytime.png";
                File fileCacheItem = new File(strFilePath);



                try {
                    fos = new FileOutputStream(fileCacheItem);
                    captureView.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    Toast.makeText(ShareActivity.this, "캡쳐 되었습니다", Toast.LENGTH_SHORT).show();
                    cap.setBackgroundColor(00000000);
                }
            }
        });



        share_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_normal();
            }
        });

        bt_kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_kakao();

            }
        });

        bt_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                share_insta();
            }
        });


    }
    private void share_normal() {
        String type = "image/*";
        String filename = "/studytime.png";
        String mediaPath = String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + "omni_capture")) + filename; //"/DCIM/Camera"

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType(type);
        File media = new File(mediaPath);

        if (!media.exists()) {
            Toast.makeText(ShareActivity.this, "먼저 화면 우측 상단의 캡쳐버튼을 눌러 캡쳐하세요!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ShareActivity.this, "최신 학습량을 공유하려면 캡쳐버튼을 다시 누른뒤 공유하면 됩니다^^", Toast.LENGTH_SHORT).show();
            Uri uri = FileProvider.getUriForFile(ShareActivity.this.getBaseContext(), "org.techtown.omni_beta10.fileprovider", media);
            share.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(share, "Share to"));
        }
    }

    private void share_kakao() {
        String type = "image/*";
        String filename = "/studytime.png";
        String mediaPath = String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + "omni_capture")) + filename; //"/DCIM/Camera"

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType(type);
        File media = new File(mediaPath);

        if (!media.exists()) {
            Toast.makeText(ShareActivity.this, "먼저 화면 우측 상단의 캡쳐버튼을 눌러 캡쳐하세요!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ShareActivity.this, "최신 학습량을 공유하려면 캡쳐버튼을 다시 누른뒤 공유하면 됩니다^^", Toast.LENGTH_SHORT).show();
            Uri uri = FileProvider.getUriForFile(ShareActivity.this.getBaseContext(), "org.techtown.omni_beta10.fileprovider", media);
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.setPackage("com.kakao.talk");

            startActivity(Intent.createChooser(share, "Share to"));
        }
    }

    private void share_insta() {
        String type = "image/*";
        String filename = "/studytime.png";
        String mediaPath = String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + "omni_capture")) + filename; //"/DCIM/Camera"

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType(type);
        File media = new File(mediaPath);

        if (!media.exists()) {
            Toast.makeText(ShareActivity.this, "먼저 화면 우측 상단의 캡쳐버튼을 눌러 캡쳐하세요!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ShareActivity.this, "최신 학습량을 공유하려면 캡쳐버튼을 다시 누른뒤 공유하면 됩니다^^", Toast.LENGTH_SHORT).show();
            Uri uri = FileProvider.getUriForFile(ShareActivity.this.getBaseContext(), "org.techtown.omni_beta10.fileprovider", media);
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.setPackage("com.instagram.android");

            startActivity(Intent.createChooser(share, "Share to"));
        }

    }

}
