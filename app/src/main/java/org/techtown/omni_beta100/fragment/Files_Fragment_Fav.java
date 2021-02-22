package org.techtown.omni_beta100.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;


import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.techtown.omni_beta100.Interface.onBackPressedListener;
import org.techtown.omni_beta100.R;
import org.techtown.omni_beta100.activity.Devide_RecordActivity;
import org.techtown.omni_beta100.adapter.EditAdapter;
import org.techtown.omni_beta100.util.Edit_item;
import org.techtown.omni_beta100.util.MusicDto;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class Files_Fragment_Fav extends Fragment {

    //public static ArrayList<MusicDto> fav_list;


    private ListView listView_fav;
    public static EditAdapter fav_editAdapter; //이거 바꾸기
    private int position;
    public static SwipeMenuListView fav_stt_list;
    public static ArrayList<Edit_item> fav_devide_items;
    //private ArrayList<Uri> fav_devide_uri_list;


    // 파일 즐겨찾기가 아니라 문장 즐겨찾기 추후 개발.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.files_fragment_fav,container,false);

        fav_stt_list = v.findViewById(R.id.fav_stt_list);
        fav_editAdapter = new EditAdapter(2);


        //fav_list = new ArrayList<MusicDto>();
        //System.out.println("생성되었습니다.");
        //System.out.println(fav_list.size());
        //Intent intent = getActivity().getIntent();

        // fav_devide_items = (ArrayList<Edit_item>) intent.getSerializableExtra("fav_list");

        /*if(Files_Fragment_Item.mItems_fav_list ==null){
            fav_devide_items = new ArrayList<>();
            fav_devide_uri_list = new ArrayList<>();
        }
        else {
            fav_devide_items = Files_Fragment_Item.mItems_fav_list;
            fav_devide_uri_list = Files_Fragment_Item.mItems_fav_uri;
            //fav_devide_items = Devide_RecordActivity.mItems_fav;
            // fav_editAdapter.setmItems(fav_devide_items);
        }*/

        //fav_devide_items = new ArrayList<>();

        fav_devide_items = Files_Fragment_Item.mItems_fav_list;
        //fav_devide_uri_list = Files_Fragment_Item.mItems_fav_uri;

        Log.e("즐겨찾기 리스트 사이즈 :", Integer.toString(fav_devide_items.size()));
       // Log.e("즐겨찾기 uri 사이즈 :", Integer.toString(fav_devide_uri_list.size()));

        //System.out.println("아이템 사이즈" + fav_devide_items.size());
        fav_editAdapter.setmItems(fav_devide_items);
        //fav_editAdapter.setmItems_uri(fav_devide_uri_list);

        fav_stt_list.setAdapter(fav_editAdapter);
        fav_stt_list.setMenuCreator(creator);


        fav_stt_list.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                fav_stt_list.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {
                fav_stt_list.smoothOpenMenu(position);
            }
        });

        fav_stt_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Devide_RecordActivity.class);
                //intent.putExtra("position",position);
                intent.putExtra("fav_script", fav_devide_items);
                //intent2.putExtra("devide_com_uri00", devide_com_uri);
                intent.putExtra("position_fav", position);

                startActivityForResult(intent, 800);



                //System.out.println(devide_com_script.get(position).size());
                //System.out.println("이게 나와야함" + devide_com_uri.get(position).size());
            }
        });


        fav_stt_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch(index){
                    case 0:
                        Log.d("", "delete..............");
                        // 요기 삭제버턴 클릭시 코딩...

                        for(int i=0;i<Files_Fragment_Item.devide_com_script.size();i++)
                        {

                            if(Files_Fragment_Item.devide_com_script.get(i)==null)
                            {
                                continue;
                            }
                            else{
                            for(int j=0;j<Files_Fragment_Item.devide_com_script.get(i).size();j++)
                            {

                                if(Files_Fragment_Item.devide_com_script.get(i).get(j).getItem_uri().equals(fav_devide_items.get(position).getItem_uri()))
                                {
                                    Files_Fragment_Item.devide_com_script.get(i).get(j).setIsCheck(false);
                                }
                            }
                        }
                        }

                        for(int i=0;i<Files_Fragment_Item.playlist_adapter.getmItems().size();i++) {
                            for (int j = 0; j < Files_Fragment_Item.playlist_adapter.getmItems().get(i).getDevide_sound_file().size(); j++) {
                                if (Files_Fragment_Item.playlist_adapter.getmItems().get(i).getDevide_sound_file().get(j) == null) {
                                    continue;
                                } else {
                                    for (int k = 0; k < Files_Fragment_Item.playlist_adapter.getmItems().get(i).getDevide_sound_file().get(j).size(); k++) {
                                        if (Files_Fragment_Item.playlist_adapter.getmItems().get(i).getDevide_sound_file().get(j).get(k).getItem_uri().equals(fav_devide_items.get(position).getItem_uri())) {
                                            Files_Fragment_Item.playlist_adapter.getmItems().get(i).getDevide_sound_file().get(j).get(k).setIsCheck(false);
                                        }
                                    }
                                }
                            }
                        }





                        fav_editAdapter.removeItem(position);
                        //Files_Fragment_Item.mItems_fav_list.remove(position);
                       // Files_Fragment_Item.mItems_fav_uri.remove(position);
                        //fav_editAdapter.setItemList(fav_editAdapter.getmItems(), fav_editAdapter.getmItems_uri());
                        fav_editAdapter.setItemList(fav_editAdapter.getmItems());
                        fav_editAdapter.notifyDataSetChanged();
                        break;

                }

                return false;
            }
        });






        return v;


    }


    public void onBackPressed() {

        List<Fragment> fragmentList = getActivity().getSupportFragmentManager().getFragments();
        for(Fragment fragment : fragmentList){
            if(fragment instanceof onBackPressedListener){
                ((onBackPressedListener)fragment).onBackPressed();

                Files_Fragment_Item.getCustom_loading().dismiss();
                getActivity().finish();
                return;
            }
        }



        /*if(PlayFragment.mediaPlayer != null){
            PlayFragment.mediaPlayer.stop();
            PlayFragment.mediaPlayer.release();
        }*/

    }
    public int getPosition(){
        return this.position;
    }

   /* public ArrayList<MusicDto> getList(){
        return this.fav_list;
    }*/


    public ArrayList<Edit_item> getDevide_items() {
        return fav_devide_items;
    }

    public void setDevide_items(ArrayList<Edit_item> devide_items) {
        this.fav_devide_items = devide_items;
    }

   /* public ArrayList<Uri> getDevide_uri_list() {
        return fav_devide_uri_list;
    }

    public void setDevide_uri_list(ArrayList<Uri> devide_uri_list) {
        this.fav_devide_uri_list = devide_uri_list;
    }*/

    public void replaceFragment(Fragment fragment) {
        FragmentManager frgm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = frgm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_devide_voice, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }


    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getActivity().getApplicationContext());
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



   /* private void SaveFavorData(ArrayList<Edit_item> shared_music){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(shared_music);
        editor.putString("SaveFavor", json);
        editor.commit();
    }

    private ArrayList<Edit_item> ReadFavorData(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("SaveFavor", "EMPTY");
        if(json.equals("EMPTY")){
            System.out.println("바보");
            ArrayList<Edit_item> arrayList = new ArrayList<>();
            return arrayList;
        }
        else {
            Type type = new TypeToken<ArrayList<Edit_item>>() {
            }.getType();
            ArrayList<Edit_item> arrayList = gson.fromJson(json, type);
            return arrayList;
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 800) {
            if (resultCode == RESULT_OK) {

                System.out.println("800결과..ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ");


                Bundle bundle2 = data.getExtras();
                //Bundle bundle3 = data.getExtras();
                Toast.makeText(getActivity(), "학습완료", Toast.LENGTH_SHORT).show();
                ArrayList<Edit_item> ary_devide_com_script = (ArrayList<Edit_item>)bundle2.getSerializable("devide_com_script");

                //Files_Fragment_Item.playlist_adapter.getmItems().get(item_position).getDevide_sound_file().set(devide_position, ary_devide_com_script);

                for(int i=0;i<ary_devide_com_script.size();i++)
                {
                    System.out.println("텍스트 : " + ary_devide_com_script.get(i).getEdit_stt_text());
                }

                Files_Fragment_Fav.fav_editAdapter.setmItems(ary_devide_com_script);

                for(int i=0;i<Files_Fragment_Item.devide_com_script.size();i++) {
                    if (Files_Fragment_Item.devide_com_script.get(i) == null) {
                        continue;
                    } else {
                        for (int j = 0; j < Files_Fragment_Item.devide_com_script.get(i).size(); j++) {
                            if (Files_Fragment_Item.devide_com_script.get(i).get(j) == null) {
                                continue;
                            } else {
                                for (int k = 0; k < ary_devide_com_script.size(); k++) {
                                    if (Files_Fragment_Item.devide_com_script.get(i).get(j).getItem_uri().equals(ary_devide_com_script.get(k).getItem_uri())) {
                                        Files_Fragment_Item.devide_com_script.get(i).get(j).setEdit_stt_text(ary_devide_com_script.get(k).getEdit_stt_text());
                                    }
                                }

                            }
                        }
                    }
                }

                for(int i=0;i<Files_Fragment_Item.playlist_adapter.getmItems().size();i++)
                {
                    for(int j=0;j<Files_Fragment_Item.playlist_adapter.getmItems().get(i).getDevide_sound_file().size();j++){
                        if(Files_Fragment_Item.playlist_adapter.getmItems().get(i).getDevide_sound_file().get(j)==null)
                        {
                            continue;
                        }
                        else{
                        for(int k=0;k<Files_Fragment_Item.playlist_adapter.getmItems().get(i).getDevide_sound_file().get(j).size();k++)
                        {
                            for(int l=0;l<ary_devide_com_script.size();l++){
                                if(Files_Fragment_Item.playlist_adapter.getmItems().get(i).getDevide_sound_file().get(j).get(k).getItem_uri().equals(ary_devide_com_script.get(l))){
                                    Files_Fragment_Item.playlist_adapter.getmItems().get(i).getDevide_sound_file().get(j).get(k).setEdit_stt_text(ary_devide_com_script.get(l).getEdit_stt_text());
                                }
                            }
                        }
                    }
                }}
;
               // Files_Fragment_Fav.fav_editAdapter.setItemList(Files_Fragment_Item.mItems_fav_list);
                Files_Fragment_Fav.fav_editAdapter.notifyDataSetChanged();
//                System.out.println("script크기" + devide_com_script.get(devide_position-1).size());
                //             System.out.println("script크기" + devide_com_script.get(devide_position+1).size());


            }


        }
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
        /*System.out.println(fav_editAdapter.getmItems().size());
        System.out.println(fav_devide_items.size());*/
       // SaveFavorData(fav_devide_items);
    }
}