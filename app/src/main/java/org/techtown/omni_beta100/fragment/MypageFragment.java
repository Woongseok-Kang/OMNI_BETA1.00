package org.techtown.omni_beta100.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.techtown.omni_beta100.Calendar.EventDecorator;
import org.techtown.omni_beta100.Calendar.OneDayDecorator;
import org.techtown.omni_beta100.Calendar.SaturdayDecorator;
import org.techtown.omni_beta100.Calendar.SundayDecorator;
import org.techtown.omni_beta100.MainActivity;
import org.techtown.omni_beta100.R;
import org.techtown.omni_beta100.activity.ShareActivity;
import org.techtown.omni_beta100.util.ProfileDialog;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MypageFragment extends Fragment {


    MainActivity mainActivity;
    View view;
    int month_change;

    public static Button bt_studyshare;
    private ImageButton imbt_profile;
    public static TextView txt_name;
    private MaterialCalendarView calendarView;
    private  TextView txt_name2, txt_month, txt_day;

    public static ArrayList<String> studyed_list;
    private String text;
    ArrayList<CalendarDay> attend_list;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mypage, container, false);


        bt_studyshare = (Button) view.findViewById(R.id.bt_studyshare);
        imbt_profile = (ImageButton) view.findViewById(R.id.imbt_profile);
        txt_name = (TextView) view.findViewById(R.id.mypage_name);
        txt_name2 = (TextView) view.findViewById(R.id.txt_name2);
        txt_month = (TextView) view.findViewById(R.id.txt_month);
        txt_day = (TextView) view.findViewById(R.id.txt_day);


        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);

        SharedPreferences sf = getActivity().getSharedPreferences("my_page", Context.MODE_PRIVATE);
        text = sf.getString("name", "Guest님");

        txt_name.setText(text);

        studyed_list = Read_studyed_list();

        System.out.println("mypage 배열 크기 : " + studyed_list.size());



        //달력 초기 설정
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2020, 12, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        attend_list = Read_attend();
        int month = CalendarDay.today().getMonth()+1;
        String str2 = Integer.toString(month) + "월, 화이팅!";
        txt_month.setText(str2);

        month_change = Read_month_change();

        // 월이 바뀔 경우
        if(month_change!=month){
            month_change = month;
        }
        // 오늘을 출석 리스트에 추가
        if(!attend_list.contains(CalendarDay.today())) {
            attend_list.add(CalendarDay.today());
        }
        else{
        }

        int day_count = 0;

        for(int i =0;i<attend_list.size();i++)
        {
            if((attend_list.get(i).getMonth()+1)==month)
                day_count++;
        }

        txt_day.setText(Integer.toString(day_count) + "일");

        String str = txt_name.getText().toString()+ "의 ";
        txt_name2.setText(str);

        CalendarDay c = CalendarDay.today();
        // 달력 꾸미기 --> 출석된 날짜 나만의 디자인으로 표시
        calendarView.addDecorators(
                new SundayDecorator(),// 일요일 빨간색으로
                new SaturdayDecorator(), //토요일 파란색으로
                new EventDecorator(attend_list, getActivity()),//출석일 표시
                        new OneDayDecorator(c, getActivity()));//오늘 표시



        imbt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProfileDialog profileDialog = new ProfileDialog(getActivity());
                profileDialog.show();


                profileDialog.et_name.setText(txt_name.getText().toString().substring(0, txt_name.getText().toString().length() - 1));


                profileDialog.imbt_ch_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "프로필 사진 변경기능은 준비중입니다!", Toast.LENGTH_SHORT).show();
                    }
                });
                profileDialog.bt_okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s = profileDialog.et_name.getText().toString() + "님";
                        txt_name.setText(s);
                        HomeFragment.study_text.setText("Today's " + s + "의  공부시간");
                        txt_name2.setText(s + "의 ");

                        String t = txt_name2.getText().toString();
                        SpannableString spannableString = new SpannableString(t);
                        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 0, t.length()-2,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        txt_name2.setText(spannableString);


                        profileDialog.dismiss();
                    }
                });

                profileDialog.bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profileDialog.dismiss();
                    }
                });
            }
        });


        String bt_text = bt_studyshare.getText().toString();
        SpannableString spannableString1 = new SpannableString(bt_text);
        spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 7, 15,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        bt_studyshare.setText(spannableString1);


        String txt1 = txt_name2.getText().toString();
        SpannableString spannableString2 = new SpannableString(txt1);
        spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 0, txt1.length()-2,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_name2.setText(spannableString2);

        String txt2 = txt_month.getText().toString();
        SpannableString spannableString3 = new SpannableString(txt2);
        spannableString3.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 0, txt2.length()-6,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_month.setText(spannableString3);

        String txt3 = txt_day.getText().toString();
        SpannableString spannableString4 = new SpannableString(txt3);
        spannableString4.setSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")), 0, txt3.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_day.setText(spannableString4);





        bt_studyshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShareActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }


   public void save_mapage(){
       SharedPreferences sp = getActivity().getSharedPreferences("my_page", Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = sp.edit();
       String text = txt_name.getText().toString();
       editor.putString("name", text);
      // editor.putInt("setence_count", study_sentence_count);
       editor.commit();
   }

    private void Save_studyed_list(ArrayList<String> list){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("studyed_list", json);
        editor.commit();
    }

    private ArrayList<String> Read_studyed_list(){
        SharedPreferences sharedPrefs4 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs4.getString("studyed_list", "EMPTY");
        if(json.equals("EMPTY")){
            ArrayList<String> arrayList = new ArrayList<>();

            return arrayList;
        }
        else {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> arrayList = gson.fromJson(json, type);
            return arrayList;
        }
    }

    private void Save_attend(ArrayList<CalendarDay> list){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("calender_list", json);
        editor.commit();
    }

    private ArrayList<CalendarDay> Read_attend(){
        SharedPreferences sharedPrefs4 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs4.getString("calender_list", "EMPTY");
        if(json.equals("EMPTY")){
            ArrayList<CalendarDay> arrayList = new ArrayList<>();

            return arrayList;
        }
        else {
            Type type = new TypeToken<ArrayList<CalendarDay>>() {
            }.getType();
            ArrayList<CalendarDay> arrayList = gson.fromJson(json, type);
            return arrayList;
        }
    }
    private void Save_month_change(int IntMonth){
        SharedPreferences pref = getActivity().getSharedPreferences("savemonth", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putInt("month", month_change);
        ed.commit();

    }

    private int Read_month_change(){
        SharedPreferences pref = getActivity().getSharedPreferences("savemonth", Context.MODE_PRIVATE);
        return pref.getInt("month", 0);
    }


    @Override
    public void onStop() {
        super.onStop();
        Save_studyed_list(studyed_list);
        save_mapage();
        Save_month_change(month_change);
        Save_attend(attend_list);
    }
}

