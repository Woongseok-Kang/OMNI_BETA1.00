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
   /* MediaPlayer mp;

    View view;

    TextView tv_total;
    ImageButton bt_share;

    Button bt_sel_kakao, bt_sel_insta;

    LinearLayout cap;*/


    int month_change;

    public static Button bt_studyshare;
    //private TextView tv_studyresult;
    private ImageButton imbt_profile;
    public static TextView txt_name;
    private MaterialCalendarView calendarView;
    //public static int study_sentence_count;

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
        //tv_studyresult = (TextView) view.findViewById(R.id.text_study_result);
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

        if(month_change!=month){
            month_change = month;
            //attend_list.clear();
        }
        System.out.println("이번달은" + month_change + "월");



        if(!attend_list.contains(CalendarDay.today())) {
            attend_list.add(CalendarDay.today());
            //attend_list.add(CalendarDay.from(2021,1,5));
            System.out.println("요기");
            System.out.println(attend_list.size());
        }
        else{

            System.out.println("이미 출첵");
            System.out.println(attend_list.size());

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





        calendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new EventDecorator(attend_list, getActivity()),
                        new OneDayDecorator(c, getActivity()));






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


        /*FragmentTransaction frag_cal = getChildFragmentManager().beginTransaction();
        Fragment cal_fragment = new fragment_cal();
        if (!cal_fragment.isAdded()) {
            frag_cal.replace(R.id.cal_frame, cal_fragment);
            frag_cal.addToBackStack(null);
            frag_cal.commit();
        }*/


        // handler.sendEmptyMessage(0);






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


   /* private void buttonSetting(){
        bt_sel_kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = "image/*";
                String filename = "/capture123.png";
                String mediaPath = Environment.getExternalStorageDirectory().getPath() +filename; //"/DCIM/Camera"

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType(type);
                File media = new File(mediaPath);
                Uri uri = FileProvider.getUriForFile(getActivity().getBaseContext(), "org.techtown.omni_beta10.fileprovider",media);
                share.putExtra(Intent.EXTRA_STREAM,uri);
                share.setPackage("com.kakao.talk");

                startActivity(Intent.createChooser(share, "Share to"));

            }
        });

        bt_sel_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "image/*";
                String filename = "/capture123.png";
                String mediaPath = Environment.getExternalStorageDirectory().getPath() +filename; //"/DCIM/Camera"

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType(type);
                File media = new File(mediaPath);
                Uri uri = FileProvider.getUriForFile(getActivity().getBaseContext(), "org.techtown.omni_beta10.fileprovider",media);
                share.putExtra(Intent.EXTRA_STREAM,uri);
                share.setPackage("com.instagram.android");

                startActivity(Intent.createChooser(share, "Share to"));
            }
        });
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mp!=null)
        {
            mp.release();
            mp=null;
        }
    }
}*/
