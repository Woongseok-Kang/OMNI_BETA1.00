package org.techtown.omni_beta100.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.techtown.omni_beta100.R;


public class FilesFragment extends Fragment {



    public static Files_Fragment_Item files_fragment1;
    public static Files_Fragment_PlayList files_fragment2;
    public static Files_Fragment_Fav files_fragment3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Button files_btn1, files_btn2, files_btn3, files_alarm;
        View v =  inflater.inflate(R.layout.fragment_files,container,false);


        //files_alarm = (Button) v.findViewById(R.id.files_btn_alarm);
        files_btn1 = (Button) v.findViewById(R.id.files_btn_item);
        files_btn2 = (Button) v.findViewById(R.id.files_btn_playlist);
        files_btn3 = (Button) v.findViewById(R.id.files_btn_fav);

        files_fragment1 = new Files_Fragment_Item();
        files_fragment2 = new Files_Fragment_PlayList();
        files_fragment3 = new Files_Fragment_Fav();
        files_btn1.setSelected(true);

        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment1).commitAllowingStateLoss();


        fragmentManager.beginTransaction().add(R.id.frame2, files_fragment1).commit();
        fragmentManager.beginTransaction().add(R.id.frame2, files_fragment3).commit();
        fragmentManager.beginTransaction().add(R.id.frame2, files_fragment2).commit();
        if(files_fragment3!=null)
            fragmentManager.beginTransaction().hide(files_fragment3).commit();
        if(files_fragment2!=null)
            fragmentManager.beginTransaction().hide(files_fragment2).commit();


        files_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if(files_fragment1==null){
                files_fragment1 = new Files_Fragment_Item();
            fragmentManager.beginTransaction().add(R.id.frame2, files_fragment1).commit();
            }
            if(files_fragment1!=null)fragmentManager.beginTransaction().show(files_fragment1).commit();
            if(files_fragment2!=null)fragmentManager.beginTransaction().hide(files_fragment2).commit();
            if(files_fragment3!=null)fragmentManager.beginTransaction().hide(files_fragment3).commit();

                files_btn1.setSelected(true);
                files_btn2.setSelected(false);
                files_btn3.setSelected(false);
            }
        });

        files_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        if(files_fragment2==null){
        files_fragment2 = new Files_Fragment_PlayList();
        fragmentManager.beginTransaction().add(R.id.frame2, files_fragment2).commit();
        }
        if(files_fragment1!=null)fragmentManager.beginTransaction().hide(files_fragment1).commit();
        if(files_fragment2!=null)fragmentManager.beginTransaction().show(files_fragment2).commit();
        if(files_fragment3!=null)fragmentManager.beginTransaction().hide(files_fragment3).commit();

                files_btn1.setSelected(false);
                files_btn2.setSelected(true);
                files_btn3.setSelected(false);
            }
        });


        files_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        if(files_fragment3==null){
            files_fragment3 = new Files_Fragment_Fav();
        fragmentManager.beginTransaction().add(R.id.frame2, files_fragment3).commit();
        }
        //fragment3 = new MypageFragment();
        //fragmentManager.beginTransaction().add(R.id.frame, fragment3).commit();
        //fragmentManager.beginTransaction().show(fragment3).commit();
        if(files_fragment1!=null)fragmentManager.beginTransaction().hide(files_fragment1).commit();
        if(files_fragment2!=null)fragmentManager.beginTransaction().hide(files_fragment2).commit();
        if(files_fragment3!=null)fragmentManager.beginTransaction().show(files_fragment3).commit();

                files_btn1.setSelected(false);
                files_btn2.setSelected(false);
                files_btn3.setSelected(true);
            }
        });


        /*FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Files_Fragment_Item files_fragment1 = new Files_Fragment_Item();
        transaction.replace(R.id.frame2, files_fragment1);
        transaction.commit();
        files_btn1.setSelected(true);

        files_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Files_Fragment_Item files_fragment1 = new Files_Fragment_Item();
                transaction.replace(R.id.frame2, files_fragment1);
                transaction.commit();
                files_btn1.setSelected(true);
                files_btn2.setSelected(false);
                files_btn3.setSelected(false);
            }
        });

        files_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Files_Fragment_PlayList files_fragment2 = new Files_Fragment_PlayList();
                transaction.replace(R.id.frame2, files_fragment2);
                transaction.commit();
                files_btn1.setSelected(false);
                files_btn2.setSelected(true);
                files_btn3.setSelected(false);
            }
        });


        files_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Files_Fragment_Fav files_fragment4 = new Files_Fragment_Fav();
                transaction.replace(R.id.frame2, files_fragment4);
                transaction.commit();
                files_btn1.setSelected(false);
                files_btn2.setSelected(false);
                files_btn3.setSelected(true);
            }
        });*/



        return v;

        /*final Button files_btn1, files_btn2, files_btn4, files_alarm;
        View v =  inflater.inflate(R.layout.fragment_files,container,false);


        //files_alarm = (Button) v.findViewById(R.id.files_btn_alarm);
        files_btn1 = (Button) v.findViewById(R.id.files_btn_item);
        files_btn2 = (Button) v.findViewById(R.id.files_btn_playlist);
        files_btn4 = (Button) v.findViewById(R.id.files_btn_fav);


        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Files_Fragment_Item files_fragment1 = new Files_Fragment_Item();
        transaction.replace(R.id.frame2, files_fragment1);
        transaction.commit();
        files_btn1.setSelected(true);

        files_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Files_Fragment_Item files_fragment1 = new Files_Fragment_Item();
                transaction.replace(R.id.frame2, files_fragment1);
                transaction.commit();
                files_btn1.setSelected(true);
                files_btn2.setSelected(false);
                files_btn4.setSelected(false);
            }
        });

        files_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Files_Fragment_PlayList files_fragment2 = new Files_Fragment_PlayList();
                transaction.replace(R.id.frame2, files_fragment2);
                transaction.commit();
                files_btn1.setSelected(false);
                files_btn2.setSelected(true);
                files_btn4.setSelected(false);
            }
        });


        files_btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Files_Fragment_Fav files_fragment4 = new Files_Fragment_Fav();
                transaction.replace(R.id.frame2, files_fragment4);
                transaction.commit();
                files_btn1.setSelected(false);
                files_btn2.setSelected(false);
                files_btn4.setSelected(true);
            }
        });



        return v;*/
    }


}