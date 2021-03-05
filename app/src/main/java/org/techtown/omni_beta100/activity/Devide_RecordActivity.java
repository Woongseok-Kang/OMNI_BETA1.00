package org.techtown.omni_beta100.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.techtown.omni_beta100.Interface.onBackPressedListener;
import org.techtown.omni_beta100.R;
import org.techtown.omni_beta100.adapter.EditAdapter;
import org.techtown.omni_beta100.fragment.Devide_PlayFragment;
import org.techtown.omni_beta100.fragment.Files_Fragment_Fav;
import org.techtown.omni_beta100.fragment.Files_Fragment_Item;
import org.techtown.omni_beta100.fragment.HomeFragment;
import org.techtown.omni_beta100.util.AudioEditor;
import org.techtown.omni_beta100.util.Edit_item;
import org.techtown.omni_beta100.util.MusicDto;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class Devide_RecordActivity extends AppCompatActivity {


    private int position;
    private ArrayList<MusicDto> list;
    public int sttMode;
    public static EditAdapter devide_editAdapter;

    public static SwipeMenuListView devide_stt_list;
    ArrayList<Edit_item> devide_items;

    Intent intent;

    int devide_position=-1;

    public static int fav_positon;

    private TextView txt_title;
    private ImageButton bt_re_devide;
    private String music_title;
    private MusicDto orin_music;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devide__record);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_devide_voice, Devide_PlayFragment.newInstance()).commit();

        devide_stt_list = findViewById(R.id.devide_stt_list);

        txt_title = (TextView)findViewById(R.id.devide_file_title);
        bt_re_devide = (ImageButton)findViewById(R.id.re_devide_button);

        intent = getIntent();

        sttMode = 0;

        if(intent.getSerializableExtra("devide_list")!=null){
            devide_items = (ArrayList<Edit_item>) intent.getSerializableExtra("devide_list");
            music_title = intent.getStringExtra("audio_title");
            orin_music = (MusicDto)intent.getSerializableExtra("origin_music");
            devide_position = -1;

            Log.e("Devide_RecordActivity", "AudioEditor에서 들어옴");


            devide_editAdapter = new EditAdapter(1);

        }else if(intent.getSerializableExtra("devide_com_script00")!=null)
        {

            Log.e("Devide_RecordActivity", "File_fragment에서 들어옴");

            ArrayList<ArrayList<Edit_item>> ary_script = (ArrayList<ArrayList<Edit_item>>) intent.getSerializableExtra("devide_com_script00");
            devide_position = intent.getIntExtra("position00", -1);
            music_title = intent.getStringExtra("files_title");

            orin_music = (MusicDto)intent.getSerializableExtra("orin_music");


            devide_editAdapter = new EditAdapter(1);


            devide_items = ary_script.get(devide_position);

        }

        else if(intent.getSerializableExtra("fav_script")!=null){
            Log.e("Devide_RecordActivity", "File_Fav에서 들어옴");

            ArrayList<Edit_item> ary_script = (ArrayList<Edit_item>) intent.getSerializableExtra("fav_script");
            fav_positon = intent.getIntExtra("position_fav", -1);

            music_title = "즐겨찾기";


            devide_editAdapter = new EditAdapter(2);

            bt_re_devide.setVisibility(View.GONE);

            devide_items = ary_script;
            devide_stt_list.setSelection(fav_positon);
            //devide_uri_list = ary_uri;
        }

        else{
            Log.e("Devide_RecordActivity", "File_playlist에서 바로 들어옴");

            ArrayList<ArrayList<Edit_item>> ary_script = (ArrayList<ArrayList<Edit_item>>) intent.getSerializableExtra("devide_com_script_pl");
            devide_position = intent.getIntExtra("position_pl", -1);
            music_title = intent.getStringExtra("playlist_title");
            orin_music = (MusicDto)intent.getSerializableExtra("orin_music");

            System.out.println(ary_script.size());
            System.out.println(ary_script.get(devide_position).size());

            devide_editAdapter = new EditAdapter(1);

            devide_items = ary_script.get(devide_position);

        }




        //Script 정렬
        Collections.sort(devide_items, new Edit_item.Sort1());
        Collections.sort(devide_items, new Edit_item.Sort2());

        System.out.println("아이템 사이즈" + devide_items.size());
        devide_editAdapter.setmItems(devide_items);
        System.out.println("아이템 사이즈" + devide_editAdapter.getmItems().size());


        txt_title.setText(music_title);



        bt_re_devide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertbuilder = new AlertDialog.Builder(Devide_RecordActivity.this, R.style.MyDialogTheme)
                        .setTitle("음성 파일을 다시 편집하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                for(int j=0;j<devide_items.size();j++)
                                {
                                    devide_items.get(j).setStatus_studying(false);
                                }
                                devide_editAdapter.setItemList(devide_items);
                                devide_editAdapter.notifyDataSetChanged();


                                Intent intent = new Intent(Devide_RecordActivity.this, AudioEditor.class);
                                intent.putExtra("orin_music", orin_music);
                                intent.putExtra("devide_list0", devide_items);
                                startActivityForResult(intent, 258);



                                //startActivity(intent);
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




        new Handler().post(new Runnable() {
            @Override
            public void run() {

                if (devide_editAdapter.getmItems() != null) {
                    if (devide_editAdapter.getmItems().size() != 0) {
                        devide_stt_list.performItemClick(
                                devide_stt_list.getChildAt(0),
                                0,
                                devide_stt_list.getAdapter().getItemId(0));

                        devide_editAdapter.notifyDataSetChanged();
                    }
                }
            }
        });







        devide_stt_list.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                devide_stt_list.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {
                devide_stt_list.smoothOpenMenu(position);
            }
        });



        devide_stt_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch(index){
                    case 0 :
                        Log.d("", "스크립트 편집");


                        devide_editAdapter.getItem(position).setMode_edit(1);
                        devide_editAdapter.notifyDataSetChanged();

                        break;
                    case 1:
                        Log.d("", "delete..............");
                        // 요기 삭제버턴 클릭시 코딩...

                        if(intent.getSerializableExtra("fav_script")!=null) {


                            for (int i = 0; i < Files_Fragment_Item.devide_com_script.size(); i++) {

                                if (Files_Fragment_Item.devide_com_script.get(i) == null) {
                                    continue;
                                } else {
                                    for (int j = 0; j < Files_Fragment_Item.devide_com_script.get(i).size(); j++) {

                                        if (Files_Fragment_Item.devide_com_script.get(i).get(j).getItem_uri().equals(Files_Fragment_Fav.fav_devide_items.get(position).getItem_uri())) {
                                            Files_Fragment_Item.devide_com_script.get(i).get(j).setIsCheck(false);
                                        }
                                    }
                                }
                            }

                            for (int i = 0; i < Files_Fragment_Item.playlist_adapter.getmItems().size(); i++) {
                                for (int j = 0; j < Files_Fragment_Item.playlist_adapter.getmItems().get(i).getDevide_sound_file().size(); j++) {
                                    if (Files_Fragment_Item.playlist_adapter.getmItems().get(i).getDevide_sound_file().get(j) == null) {
                                        continue;
                                    } else {
                                        for (int k = 0; k < Files_Fragment_Item.playlist_adapter.getmItems().get(i).getDevide_sound_file().get(j).size(); k++) {
                                            if (Files_Fragment_Item.playlist_adapter.getmItems().get(i).getDevide_sound_file().get(j).get(k).getItem_uri().equals(Files_Fragment_Fav.fav_devide_items.get(position).getItem_uri())) {
                                                Files_Fragment_Item.playlist_adapter.getmItems().get(i).getDevide_sound_file().get(j).get(k).setIsCheck(false);
                                            }
                                        }
                                    }
                                }
                            }

                        }else{


                            fileDelete(devide_editAdapter.getmItems().get(position).getOutpath());
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {

                                    if (devide_editAdapter.getmItems() != null) {

                                        if(devide_editAdapter.getmItems().size()!=0){


                                            devide_stt_list.performItemClick(
                                                    devide_stt_list.getChildAt(0),
                                                    0,
                                                    devide_stt_list.getAdapter().getItemId(0));

                                            devide_editAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            });


                            if(Devide_PlayFragment.devide_item!=null) {
                                if(Devide_PlayFragment.devide_item.size()!=0)
                                    Devide_PlayFragment.now_uri = Uri.parse( Devide_PlayFragment.devide_item.get(0).getItem_uri());


                            }
                        }

                        devide_editAdapter.removeItem(position);
                        devide_editAdapter.setItemList(devide_editAdapter.getmItems());
                        devide_editAdapter.notifyDataSetChanged();

                        break;
                }

                return false;
            }
        });

        devide_stt_list.setAdapter(devide_editAdapter);
        devide_stt_list.setMenuCreator(creator);

        System.out.println("포지션 = " + position);

    }

    @Override
    public void onBackPressed() {

        Devide_PlayFragment.stopPlay();

        Devide_PlayFragment.bt_speed.setText("x1.0");
        Devide_PlayFragment.val_speed =0;

        Devide_PlayFragment.bt_term.setText("0.5");
        Devide_PlayFragment.val_term =0;

        Devide_PlayFragment.bt_count.setText("1회");
        Devide_PlayFragment.val_counter =0;



        if ((intent.getSerializableExtra("devide_list") != null) || (intent.getSerializableExtra("devide_com_script00") != null) || (intent.getSerializableExtra("devide_com_script_pl")!=null)) {
            for (int i = 0; i < devide_items.size(); i++) {
                System.out.println("Devide_Record-> AudioEditor" + devide_items.get(i));
            }

            for (int i = 0; i < devide_items.size(); i++) {
                devide_items.get(i).setMode_edit(0);
            }

            for(int i=0;i<devide_items.size();i++){
                devide_items.get(i).setStatus_studying(false);
            }


            Intent intent_re = new Intent();
            Bundle bundle = new Bundle();
            //Bundle bundle2 = new Bundle();
            bundle.putSerializable("devide_com_script", devide_items);
            //bundle2.putSerializable("devide_com_uri", devide_uri_list);
            intent_re.putExtras(bundle);
            //intent_re.putExtras(bundle2);

            intent_re.putExtra("position_re", devide_position);


            setResult(RESULT_OK, intent_re);


            finish();
        }
        else{ // FAV에서 호출했을때


            Intent intent_re = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("devide_com_script", devide_items);

            intent_re.putExtras(bundle);
            setResult(RESULT_OK, intent_re);
            finish();
        }
    }


    public int getPosition(){
        return this.position;
    }

    public ArrayList<MusicDto> getList(){
        return this.list;
    }


    public ArrayList<Edit_item> getDevide_items() {
        return devide_items;
    }

    public void setDevide_items(ArrayList<Edit_item> devide_items) {
        this.devide_items = devide_items;
    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager frgm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = frgm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_devide_voice, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "Close" item
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                    0xCE)));
            // set item width
            openItem.setWidth(200);
            // set item title
            openItem.setTitle("스크립트 편집");
            // set item title fontsize
            openItem.setTitleSize(18);
            // set item title font color
            openItem.setTitleColor(Color.WHITE);
            // add to menu
            menu.addMenuItem(openItem);

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(200);
            // set a icon
            deleteItem.setTitle("삭제");
            deleteItem.setTitleSize(18);
            deleteItem.setTitleColor(Color.WHITE);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 258 ) {
            if (resultCode == RESULT_OK) {

                Bundle bundle = data.getExtras();

                final ArrayList<Edit_item> devide_com_script = (ArrayList<Edit_item>)bundle.getSerializable("new_script");

                devide_items = devide_com_script;
               // devide_editAdapter.notifyDataSetChanged();

                Collections.sort(devide_items, new Edit_item.Sort1());
                Collections.sort(devide_items, new Edit_item.Sort2());


                devide_editAdapter.setItemList(devide_items);
                Devide_PlayFragment.devide_item = devide_items;
                devide_stt_list.setAdapter(devide_editAdapter);

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {

                        if (devide_editAdapter.getmItems() != null) {

                            if(devide_editAdapter.getmItems().size()!=0){


                            devide_stt_list.performItemClick(
                                    devide_stt_list.getChildAt(0),
                                    0,
                                    devide_stt_list.getAdapter().getItemId(0));

                            devide_editAdapter.notifyDataSetChanged();
                        }
                        }
                    }
                });

                if(Devide_PlayFragment.devide_item!=null) {
                    if(Devide_PlayFragment.devide_item.size()!=0)
                   Devide_PlayFragment.now_uri = Uri.parse( Devide_PlayFragment.devide_item.get(0).getItem_uri());

                }else {

                }


                System.out.println(devide_editAdapter.getmItems().size());
                System.out.println(Devide_PlayFragment.devide_item.size());

                if(Devide_PlayFragment.mediaPlayer!=null)
                {
                    Devide_PlayFragment.stopPlay();
                }

                Log.e("AudioEditor", "Script" + Integer.toString(devide_com_script.size()));


            } else {   // RESULT_CANCEL
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }

//
        }
    }


    public boolean fileDelete(String filePath){
        try{
            File del_file = new File(filePath);

            if(del_file.exists()){
                del_file.delete();
                System.out.println("성공");

                return true;
            }
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("실패");
        }

        return false;

    }
    @Override
    public void onStop() {
        super.onStop();
        System.out.println("favor onstop!!-----------------------------");

    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("favor onpause!!-----------------------------");
    }




}










