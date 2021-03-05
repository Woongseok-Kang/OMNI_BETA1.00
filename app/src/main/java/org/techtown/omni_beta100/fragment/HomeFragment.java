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
        System.out.println("onREsume---------------------------------------------------------------------------------------------------");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


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

        return v;
    }

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

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("HomeFragment", "Destroy 되었당......................................");

        Intent intent = new Intent(getActivity(), MyService.class);
        getActivity().stopService(intent);

        Log.i("HomeFragment", "서비스 종료 되었을텐데....");


    }
}