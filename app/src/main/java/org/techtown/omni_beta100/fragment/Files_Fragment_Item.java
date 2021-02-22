package org.techtown.omni_beta100.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kakao.util.helper.log.Logger;


import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.techtown.omni_beta100.AsyncTask.MyAsyncTask;
import org.techtown.omni_beta100.MainActivity;
import org.techtown.omni_beta100.activity.Devide_RecordActivity;
import org.techtown.omni_beta100.adapter.EditAdapter;
import org.techtown.omni_beta100.adapter.PlayListAdapter;
import org.techtown.omni_beta100.util.AudioEditor;
import org.techtown.omni_beta100.R;
import org.techtown.omni_beta100.activity.RecordActivity;
import org.techtown.omni_beta100.adapter.MyAdapter;
import org.techtown.omni_beta100.adapter.MySelectAdapter;
import org.techtown.omni_beta100.util.Edit_item;
import org.techtown.omni_beta100.util.Edit_playlist_item;
import org.techtown.omni_beta100.util.MusicDto;
import org.techtown.omni_beta100.util.ProgressDialog;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.sql.Savepoint;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class Files_Fragment_Item extends Fragment{

    private String state;
    private TextView tv;
   // private SwipeMenuListView listView;
    public static ArrayList<MusicDto> list;
    public static ArrayList<MusicDto> sel_list;
    private ImageButton bt_upload;
    //private ImageView bt_check;
    private static ProgressDialog custom_loading;
    private CheckBox cbGoLargeChecked;

    public static SwipeMenuListView swipeMenuListView;
    private Button bt_favor, bt_del;

    public static MySelectAdapter sel_adapter;

    public MyAsyncTask myAsyncTask;
    public InputStream stream;
    GoogleCredentials credentials;
    public String projectId = "omni-1608296174411";
    public String bucketName = "ssussuk_omni_bucket";

    ArrayList<MusicDto> devide_com_list;
   // ArrayList<Edit_item> devide_com_script;
    //ArrayList<Uri> devide_com_uri;

    ArrayList<String> devide_com_uri_sub;

    private ArrayList<Edit_playlist_item> playlist_items;
    public static PlayListAdapter playlist_adapter;
    //ArrayList<ArrayList<String>> devide_com_uri; //  전달된 후 uri로 parse할것!
    public static ArrayList<ArrayList<Edit_item>> devide_com_script;

    //public static HashMap<Integer, ArrayList<MusicDto>> hash_save;



    public static ArrayList<Edit_item> mItems_fav_list;

   // public static ArrayList<Uri> mItems_fav_uri;
    //public static ArrayList<String> mItems_fav_uri_str;




    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.files_fragment_item,container,false);

        System.out.println("이걸 시작한다");

        bt_upload = (ImageButton)v.findViewById(R.id.bt_upload);









        //로딩창 객체
       // custom_loading = new ProgressDialog(getActivity());
        //로딩창 투명
        //custom_loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        stream = getResources().openRawResource(R.raw.credential);
        try {
            credentials = GoogleCredentials.fromStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

       // devide_com_script = new ArrayList<>();
       // devide_com_uri = new ArrayList<>();

       /* for(int i=0;i<200;i++)
        {
            devide_com_script.add(i, null);
            devide_com_uri.add(i, null);
        }*/



        //mItems_fav_uri = new ArrayList<>();
        //mItems_fav_list = new ArrayList<>();

        //hash_save = ReadPlaylistData();
//        System.out.println("다시 해쉬맵 크기" + hash_save.size());

        playlist_adapter = new PlayListAdapter();
        playlist_items = ReadPlaylistData();
        playlist_adapter.setItemList(playlist_items);//이고 sharedpreferfence해야함


        System.out.println("이건" + playlist_items.size());
        System.out.println("이거이거 크기???" + playlist_adapter.getmItems().size());

        mItems_fav_list = ReadFav_list_Data();
        //mItems_fav_uri_str = ReadFav_Uri_Data("fav_uri_data");

        /*for(int i =0;i<mItems_fav_uri_str.size();i++)
        {
            mItems_fav_uri.add(Uri.parse(mItems_fav_uri_str.get(i)));
        }*/

        Log.e("즐겨찾기 리스트 사이즈 :", Integer.toString(mItems_fav_list.size()));
        //Log.e("즐겨찾기 uri 사이즈 :", Integer.toString(mItems_fav_uri.size()));

        swipeMenuListView = (SwipeMenuListView)v.findViewById(R.id.musiclist);
        sel_list = ReadMusicData();
        devide_com_script = ReadDevideData();
        //devide_com_uri = ReadUriData();
        swipeMenuListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                swipeMenuListView.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {
                swipeMenuListView.smoothOpenMenu(position);
            }
        });

        //devide_com_uri_sub = ReadUriData("devide_uri");

        //devide_com_uri = new ArrayList<>();

        /*for(int i=0;i<devide_com_uri_sub.size();i++)
        {
            devide_com_uri.add(Uri.parse(devide_com_uri_sub.get(i)));
        }*/

        System.out.println("오홋 Script사이즈" + devide_com_script.size());
//        System.out.println("오홋" + devide_com_uri_sub.size());
        //System.out.println("uri사이즈" + devide_com_uri.size());



//        sel_list = ReadMusicData();

        /*ArrayList<MusicDto> asdf = ReadMusicData();
        System.out.println(asdf.size() + "이정도 들어있어..ㅜ");
        for(MusicDto music : asdf){
            System.out.println(music.getArtist() + "오켓");
        }*/



        sel_adapter= new MySelectAdapter(getActivity(), sel_list);
        swipeMenuListView.setAdapter(sel_adapter);
        swipeMenuListView.setMenuCreator(creator);

        getMusicList();

        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch(index){
                    case 0:
                        Log.d("", "delete..............");
                        // 요기 삭제버턴 클릭시 코딩...


                        for(int i=0;i<HomeFragment.recent_file.size();i++)
                        {
                            if(HomeFragment.recent_file.get(i).getUnique_key().equals(sel_list.get(position).getUnique_key())){
                                HomeFragment.recent_file.remove(i);
                            }
                        }

                        HomeFragment.recent_adapter.setItemList(HomeFragment.recent_adapter.getList());
                        HomeFragment.recent_adapter.notifyDataSetChanged();

                        for(int i=0;i<playlist_adapter.getmItems().size();i++)
                        {
                            for(int j=0;j<playlist_adapter.getmItems().get(i).getSound_file().size();j++)
                            {
                                if(playlist_adapter.getmItems().get(i).getSound_file().get(j).getUnique_key().equals(sel_list.get(position).getUnique_key())){
                                    playlist_adapter.getmItems().get(i).getSound_file().remove(j);
                                    playlist_adapter.getmItems().get(i).getDevide_sound_file().remove(j);
                                }
                            }
                        }

                        for(int i=0;i<mItems_fav_list.size();i++)
                        {
                            if(devide_com_script.get(position)!=null){
                            for(int j=0;j<devide_com_script.get(position).size();j++){
                                if(mItems_fav_list.get(i).getItem_uri().equals(devide_com_script.get(position).get(j).getItem_uri()))
                                {
                                    mItems_fav_list.remove(i);
                                }
                            }
                            }

                        }



                        if(PlayList_Fragment_InFile.sel_adapter!=null) {
                            PlayList_Fragment_InFile.sel_adapter.setItemList(PlayList_Fragment_InFile.sel_adapter.getList());
                            PlayList_Fragment_InFile.sel_adapter.notifyDataSetChanged();
                        }
                            for (int i = 0; i < playlist_adapter.getmItems().size(); i++) {
                                String value1 = Integer.toString(playlist_adapter.getmItems().get(i).getSound_file().size()) + " files";
                                playlist_adapter.modifyItem(i, value1);
                            }
                            playlist_adapter.setItemList(playlist_adapter.getmItems());
                            playlist_adapter.notifyDataSetChanged();


                        Files_Fragment_Fav.fav_editAdapter.setItemList(Files_Fragment_Fav.fav_editAdapter.getmItems());
                        Files_Fragment_Fav.fav_editAdapter.notifyDataSetChanged();



                        sel_list.remove(position);
                        devide_com_script.remove(position);

                        sel_adapter.setItemList(sel_adapter.getList());
                        sel_adapter.notifyDataSetChanged();
                        //listView.setAdapter(sel_adapter);
                        System.out.println(sel_list.size());

                }

                return false;
            }
        });




        swipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    if (sel_list.get(position).isDevide_complete() == false) {
                        AlertDialog.Builder editorbuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                                .setTitle("음성파일을 편집하시겠습니까?")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                        Intent intent = new Intent(getContext(), AudioEditor.class);
                                        intent.putExtra("position", position);
                                        intent.putExtra("playlist", sel_list);
                                        startActivityForResult(intent, 130);



                                        //startActivity(intent);
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                        int redup=0;

                                        for(int j=0;j<HomeFragment.recent_file.size();j++)
                                        {
                                            if(sel_list.get(position).getUnique_key().equals(HomeFragment.recent_file.get(j).getUnique_key())){
                                                HomeFragment.recent_file.remove(j);
                                                HomeFragment.recent_script.remove(j);
                                                HomeFragment.recent_file.add(0, sel_list.get(position));
                                                HomeFragment.recent_script.add(0, null);
                                                HomeFragment.recent_adapter.notifyDataSetChanged();
                                                HomeFragment.text_empty.setVisibility(View.GONE);
                                                redup=1;
                                            }
                                        }

                                        if(redup==0) {
                                            if (HomeFragment.recent_file.size() == 5) {
                                                HomeFragment.recent_file.remove(4);
                                                HomeFragment.recent_script.remove(4);
                                                HomeFragment.recent_file.add(0, sel_list.get(position));
                                                HomeFragment.recent_script.add(0, null);
                                                HomeFragment.recent_adapter.notifyDataSetChanged();
                                                HomeFragment.text_empty.setVisibility(View.GONE);
                                            } else {

                                                HomeFragment.recent_file.add(0, sel_list.get(position));
                                                HomeFragment.recent_script.add(0, null);
                                                HomeFragment.recent_adapter.notifyDataSetChanged();
                                                HomeFragment.text_empty.setVisibility(View.GONE);
                                            }
                                        }



                                        Intent intent = new Intent(getActivity(), RecordActivity.class);
                                        intent.putExtra("position", position);
                                        intent.putExtra("playlist", sel_list);
                                        //custom_loading.show();
                                        startActivityForResult(intent, 0);



                                        //startActivity(intent);
                                    }
                                });

                        AlertDialog alertDialog = editorbuilder.create();
                        alertDialog.show();
                    }
                    else{


                        Intent intent2 = new Intent(getActivity(), Devide_RecordActivity.class);
                        //intent.putExtra("position",position);
                        intent2.putExtra("devide_com_script00", devide_com_script);
                        //intent2.putExtra("devide_com_uri00", devide_com_uri);
                        intent2.putExtra("position00", position);
                        intent2.putExtra("files_title", sel_list.get(position).getTitle());
                        intent2.putExtra("orin_music", sel_list.get(position));

                        startActivityForResult(intent2, 400);



                        System.out.println(devide_com_script.get(position).size());
                        //System.out.println("이게 나와야함" + devide_com_uri.get(position).size());



                }


            }

        });





     bt_upload.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             //Toast.makeText(getActivity(),"음성파일의 위치를 핸드폰내의 Download 폴더로 옮긴 후 업로드를 실행하세요!", Toast.LENGTH_LONG).show();



             final MyAdapter adapter = new MyAdapter(getActivity(), list);
             AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                     .setAdapter(adapter, null)
                     .setTitle("업로드할 파일을 선택하세요!")
                     .setPositiveButton("선택완료", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {


                             for (MusicDto music : list) {
                                 if (music.getIsSelected()==true) {
                                     Log.d("MainActivity", music.getTitle() + " 선택");
                                     music.setUnique_key(getUniqueId());
                                     sel_list.add(music);


                                     /*Uri musicURI = Uri.withAppendedPath(
                                             MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, ""+ music.getId());
                                     Cursor cursor = getActivity().getContentResolver().query(musicURI, null, null, null, null );
                                     cursor.moveToNext();
                                     try {
                                         myAsyncTask = new MyAsyncTask(projectId,bucketName,music,cursor,credentials);
                                     } catch (IOException e) {
                                         e.printStackTrace();
                                     }
                                     myAsyncTask.execute();*/
                                 }
                             }

                             System.out.println(sel_list.size() + "개가 선택되었습니다");
                             for(int i =0;i<sel_list.size();i++){
                                 System.out.println("고유 아이디들?? : " + sel_list.get(i).getUnique_key());
                             }
                             //이부분 참고하여 만들어보기!! sharedpreference sel_list에 담기

                             sel_adapter.setItemList(sel_adapter.getList());
                             sel_adapter.notifyDataSetChanged();
                            // listView.setAdapter(sel_adapter);
                             for(MusicDto sel_mu : sel_list){
                                 sel_mu.setIsSelected(false);
                             }
                             for(int i=0;i<list.size();i++)
                             {
                                 list.get(i).setIsSelected(false);
                             }
                             adapter.setItemList(adapter.getList());
                             adapter.notifyDataSetChanged();

                         }
                     })
                     .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             Log.d("getActivity()", "취소 터치");
                             for(MusicDto sel_mu : sel_list) {
                                 sel_mu.setIsSelected(false);
                             }
                                 for(int i=0;i<list.size();i++)
                                 {
                                     list.get(i).setIsSelected(false);
                                 }
                                 adapter.setItemList(adapter.getList());
                                 adapter.notifyDataSetChanged();

                         }
                     });

             AlertDialog alertDialog = builder.create();
             alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
             final ListView alertlist = alertDialog.getListView(); // 팝업의 listview
             alertlist.setAdapter(adapter);
             alertlist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
             alertlist.setDivider(new ColorDrawable(Color.LTGRAY));
             alertlist.setDividerHeight(1);
             alertlist.setFocusable(false);
             /*alertlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 }
             });*/
             alertlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                     //cbGoLargeChecked = (CheckBox) view.findViewById(R.id.productChecked);
                     ImageView bt_check = (ImageView) view.findViewById(R.id.check_image);

                     final MusicDto selectMusic = list.get(position);
                     if(selectMusic.getIsSelected()==false){
                         selectMusic.setIsSelected(true);
                         bt_check.setColorFilter(Color.parseColor("#8795ff"));


                         //cbGoLargeChecked.setChecked(selectMusic.getIsSelected());
                     }
                     else {
                         selectMusic.setIsSelected(false);
                         bt_check.setColorFilter(null);

                         //cbGoLargeChecked.setChecked(selectMusic.getIsSelected());

                     }

                     /*final MusicDto selectMusic = list.get(position);
                     if(selectMusic.getIsSelected()==false){
                         selectMusic.setIsSelected(true);
                         adapter.setItemList(list);
                         adapter.notifyDataSetChanged();
                         //bt_check.setColorFilter(Color.parseColor("#8795ff"));


                         //cbGoLargeChecked.setChecked(selectMusic.getIsSelected());
                     }
                     else {
                         selectMusic.setIsSelected(false);
                         //bt_check.setColorFilter(null);
                         adapter.setItemList(list);
                         adapter.notifyDataSetChanged();

                             //cbGoLargeChecked.setChecked(selectMusic.getIsSelected());

                     }*/


                 }
             });




             alertDialog.show();

             Display display = getActivity().getWindowManager().getDefaultDisplay();
             Point size = new Point();
             display.getSize(size);

             Window window = alertDialog.getWindow();

             int x = (int)(size.x * 1.00f);
             int y = (int)(size.y * 1.00f);

             window.setLayout(x, y);

             //WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
             //params.width = 1000;
             //params.height = 1700;
             //alertDialog.getWindow().setAttributes(params);
         }
         });

        return v;
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

    static public ProgressDialog getCustom_loading() {
        return custom_loading;
    }

    // 기기 내 음악파일들을 불러와서 list로 보여주기
    public void getMusicList() {
        try {
            list = new ArrayList<>();

            String str = "/Download/";
            String[] projection = {MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST};

            Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection, null, null, null);

        /*Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, android.provider.MediaStore.Audio.Media.DATA + " like ? ", new String[] {"%"+str+"%"}, null);*/

            while (cursor.moveToNext()) {

                MusicDto musicDto = new MusicDto();
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                System.out.println(name);


                if(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)).equals("AUDIO_TEMP")){

            }
                else{
                    musicDto.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                    musicDto.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                    musicDto.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                    musicDto.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                    //musicDto.setUnique_key(getUniqueId());
                    list.add(musicDto);
                }
            }
            cursor.close();

        }catch(Exception e){
            Log.e("exceiption", e.getMessage());
            Toast.makeText(getActivity(), "접근 권한을 '허용'으로 설정하고 다시 실행해 주세요", Toast.LENGTH_LONG).show();
        }
    }

    public static String getUniqueId(){
        String uniqueId = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Calendar dateTime = Calendar.getInstance();
        uniqueId = sdf.format(dateTime.getTime());

        uniqueId = uniqueId + "_" + RandomStringUtils.randomAlphanumeric(8);

        return uniqueId;
    }


    /*{
        list = new ArrayList<>();

        //String str = "/Download/";
        String[] projection = {MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST};

        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, null,null, null);

        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, android.provider.MediaStore.Audio.Media.DATA + " like ? ", new String[] {"%"+str+"%"}, null);

        while(cursor.moveToNext()){
            MusicDto musicDto = new MusicDto();
            musicDto.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            musicDto.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            musicDto.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            musicDto.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            list.add(musicDto);
        }
        cursor.close();

        }*/

       private void SaveMusicData(ArrayList<MusicDto> shared_music){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
           SharedPreferences.Editor editor = preferences.edit();
           Gson gson = new Gson();
           String json = gson.toJson(shared_music);
           editor.putString("SaveMusics", json);
           editor.commit();
       }

       private ArrayList<MusicDto> ReadMusicData(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("SaveMusics", "EMPTY");
        if(json.equals("EMPTY")){
            System.out.println("바보");
            ArrayList<MusicDto> arrayList = new ArrayList<>();
            return arrayList;
        }
           else {
            Type type = new TypeToken<ArrayList<MusicDto>>() {
            }.getType();
            ArrayList<MusicDto> arrayList = gson.fromJson(json, type);
            return arrayList;
        }
       }

    /*private void SaveUriData(String key, ArrayList<String> devide_items){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor2 = preferences.edit();
        JSONArray jsonArray = new JSONArray();
        for(int i =0;i<devide_items.size();i++){
            jsonArray.put(devide_items.get(i));
        }
        if(!devide_items.isEmpty()) {
            editor2.putString(key, jsonArray.toString());
        }
        else{
            editor2.putString(key, null);
        }
        editor2.apply();
    }

    private ArrayList<String> ReadUriData(String key){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String json = sharedPrefs.getString(key, null);
        ArrayList<String> arrayList = new ArrayList<>();
        if(json != null){
            try{
                JSONArray jArray = new JSONArray(json);
                for(int i =0;i<jArray.length();i++){
                    String data = jArray.optString(i);
                    arrayList.add(data);
                }
            }catch(JSONException e){
                Logger.e("Error", e.getMessage());
            }
        }

            return arrayList;
        }*/


    private void SaveDevideData(ArrayList<ArrayList<Edit_item>> devide_items){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(devide_items);
        editor.putString("Devide_items_script", json);
        editor.commit();
    }

    private ArrayList<ArrayList<Edit_item>> ReadDevideData(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("Devide_items_script", "EMPTY");
        if(json.equals("EMPTY")){
            System.out.println("바보");
            ArrayList<ArrayList<Edit_item>> arrayList = new ArrayList<>();


            for(int i =0;i<200;i++)
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

   /* private void SaveUriData(ArrayList<ArrayList<String>> devide_items){
        SharedPreferences preferences2 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor2 = preferences2.edit();
        Gson gson = new Gson();
        String json = gson.toJson(devide_items);
        editor2.putString("Devide_items_uri", json);
        editor2.commit();
    }

    private ArrayList<ArrayList<String>> ReadUriData(){
        SharedPreferences sharedPrefs2 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs2.getString("Devide_items_uri", "EMPTY");
        if(json.equals("EMPTY")){
            System.out.println("바보");
            ArrayList<ArrayList<String>> arrayList = new ArrayList<>();

            for(int i =0;i<200;i++)
            {
                arrayList.add(i, null);
            }

            return arrayList;
        }
        else {
            Type type = new TypeToken<ArrayList<ArrayList<String>>>() {
            }.getType();
            ArrayList<ArrayList<String>> arrayList = gson.fromJson(json, type);
            return arrayList;
        }
    }*/

    private void SaveFav_list_Data(ArrayList<Edit_item> fav_list){
        SharedPreferences preferences3 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor3 = preferences3.edit();
        Gson gson = new Gson();
        String json = gson.toJson(fav_list);
        editor3.putString("fav_list", json);
        editor3.commit();
    }

    private ArrayList<Edit_item> ReadFav_list_Data(){
        SharedPreferences sharedPrefs3 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs3.getString("fav_list", "EMPTY");
        if(json.equals("EMPTY")){
            System.out.println("fav 바보");
            ArrayList<Edit_item> arrayList = new ArrayList<>();


            return arrayList;
        }
        else {
            Type type = new TypeToken<ArrayList<Edit_item>>() {
            }.getType();
            ArrayList<Edit_item> arrayList = gson.fromJson(json, type);
            return arrayList;
        }
    }

    private void SavePlaylistData(ArrayList<Edit_playlist_item> play_list){


        SharedPreferences preferences4 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor4 = preferences4.edit();
        Gson gson = new Gson();
        String json = gson.toJson(play_list);
        editor4.putString("play_list", json);
        editor4.commit();
    }

    private ArrayList<Edit_playlist_item> ReadPlaylistData() {

        SharedPreferences sharedPrefs4 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs4.getString("play_list", "EMPTY");
        if(json.equals("EMPTY")){
            System.out.println("fav 바보");
            ArrayList<Edit_playlist_item> arrayList = new ArrayList<>();


            return arrayList;
        }
        else {
            Type type = new TypeToken<ArrayList<Edit_playlist_item>>() {
            }.getType();
            ArrayList<Edit_playlist_item> arrayList = gson.fromJson(json, type);
            return arrayList;
        }
    }

    /*private void SaveFav_Uri_Data(ArrayList<String> fav_uri){
        SharedPreferences preferences4 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor4 = preferences4.edit();
        Gson gson = new Gson();
        String json = gson.toJson(fav_uri);
        editor4.putString("fav_uri", json);
        editor4.commit();
    }

    private ArrayList<String> ReadFav_Uri_Data(){
        SharedPreferences sharedPrefs4 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs4.getString("fav_uri", "EMPTY");
        if(json.equals("EMPTY")){
            System.out.println("바보");
            ArrayList<String> arrayList = new ArrayList<>();

            return arrayList;
        }
        else {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> arrayList = gson.fromJson(json, type);
            return arrayList;
        }
    }*/

   /* private void SaveFav_Uri_Data(String key, ArrayList<String> fav_uri){
        SharedPreferences preferences4 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor4 = preferences4.edit();
        JSONArray a = new JSONArray();
        for(int i=0;i<fav_uri.size();i++){
            a.put(fav_uri.get(i));
        }
        if(!fav_uri.isEmpty()) {
            editor4.putString(key, a.toString());
        }else{
            editor4.putString(key, null);
        }
        editor4.apply();
    }*/

   /* private ArrayList<String> ReadFav_Uri_Data(String key){
        SharedPreferences sharedPrefs4 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String json = sharedPrefs4.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if(json != null){
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            }
                catch(JSONException e){
                    e.printStackTrace();
                }
            }
        return urls;
        }*/





    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


       /* int request = requestCode & 0xffff;

        Fragment fragment = getSupportFragmentManager().findFragmentByTag("item");
        fragment.onActivityResult(request, resultCode, data);*/

        //System.out.println("여기 결과..ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ");
        if (requestCode == 130 ) {
            if (resultCode == RESULT_OK) {
                System.out.println("130 결과..ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ2");

                Bundle bundle = data.getExtras();
                Bundle bundle2 = data.getExtras();
                //Bundle bundle3 = data.getExtras();
                Toast.makeText(getActivity(), "분할완료", Toast.LENGTH_SHORT).show();

                devide_com_list = (ArrayList<MusicDto>)bundle.getSerializable("devide_com_list");
                ArrayList<Edit_item> ary_devide_com_script = (ArrayList<Edit_item>)bundle2.getSerializable("devide_com_script");
                //ArrayList<Uri> ary_devide_com_uri = (ArrayList<Uri>)bundle3.getSerializable("devide_com_uri");
                int devide_position = data.getExtras().getInt("position");
                /*sel_list.get(sel_list.size()-1).setDevide_complete(false);
                listView.setAdapter(sel_adapter);
                System.out.println(sel_list.get(sel_list.size()-1).isDevide_complete());
                System.out.println(sel_list.get(sel_list.size()-2).isDevide_complete());*/

                sel_list = devide_com_list;

                for(int i=0;i<ary_devide_com_script.size();i++){
                    System.out.println("file_fragment에서의 script" + ary_devide_com_script.get(i));
                }

                Log.e("File_Fragment_Item", "sel_list" + Integer.toString(sel_list.size()));
                Log.e("File_Fragment_Item", "sel_list" + Boolean.toString(sel_list.get(devide_position).isDevide_complete()));
                //Log.e("File_Fragment_Item", "sel_list" + Boolean.toString(sel_list.get(devide_position-1).isDevide_complete()));

                Log.e("File_Fragment_Item", "script_list" + Integer.toString(ary_devide_com_script.size()));
                //Log.e("File_Fragment_Item", "uri_list" + Integer.toString(ary_devide_com_uri.size()));

                Log.e("File_Fragment_Item", "position" + Integer.toString(devide_position));

                Log.e("File_Fragment_Item", "진짜전달완료!!!!");
                //listView.setAdapter(sel_adapter);

                /*ArrayList<String> sub = new ArrayList<>();

                for(int i=0;i<ary_devide_com_uri.size();i++){
                    sub.add(ary_devide_com_uri.get(i).toString());
                }*/

                devide_com_script.set(devide_position, ary_devide_com_script);

                int redup=0;

                for(int j=0;j<HomeFragment.recent_file.size();j++)
                {
                    if(sel_list.get(devide_position).getUnique_key().equals(HomeFragment.recent_file.get(j).getUnique_key())){
                        HomeFragment.recent_file.remove(j);
                        HomeFragment.recent_script.remove(j);
                        HomeFragment.recent_file.add(0, sel_list.get(devide_position));
                        HomeFragment.recent_script.add(0, ary_devide_com_script);
                        HomeFragment.recent_adapter.notifyDataSetChanged();
                        HomeFragment.text_empty.setVisibility(View.GONE);
                        redup=1;
                    }
                }

                if(redup==0) {
                    if (HomeFragment.recent_file.size() == 5) {
                        HomeFragment.recent_file.remove(4);
                        HomeFragment.recent_script.remove(4);
                        HomeFragment.recent_file.add(0, sel_list.get(devide_position));
                        HomeFragment.recent_script.add(0, ary_devide_com_script);
                        HomeFragment.recent_adapter.notifyDataSetChanged();
                        HomeFragment.text_empty.setVisibility(View.GONE);
                    } else {

                        HomeFragment.recent_file.add(0, sel_list.get(devide_position));
                        HomeFragment.recent_script.add(0, ary_devide_com_script);
                        HomeFragment.recent_adapter.notifyDataSetChanged();
                        HomeFragment.text_empty.setVisibility(View.GONE);
                    }
                }



                for(int i=0;i<playlist_items.size();i++){
                    for(int j=0;j<playlist_items.get(i).getSound_file().size();j++)
                    {
                        if(playlist_items.get(i).getSound_file().get(j).getUnique_key().equals(sel_list.get(devide_position).getUnique_key())){
                            playlist_items.get(i).getSound_file().set(j, sel_list.get(devide_position));
                            playlist_items.get(i).getDevide_sound_file().set(j, ary_devide_com_script);
                        }
                    }
                }

                playlist_adapter.setItemList(playlist_adapter.getmItems());
                playlist_adapter.notifyDataSetChanged();

                if( PlayList_Fragment_InFile.sel_adapter!=null) {
                    PlayList_Fragment_InFile.sel_adapter.setItemList(PlayList_Fragment_InFile.sel_adapter.getList());
                    PlayList_Fragment_InFile.sel_adapter.notifyDataSetChanged();

                }
                mItems_fav_list.clear();
                //mItems_fav_uri.clear();

                for(int i =0;i<devide_com_script.size();i++) {
                    if (devide_com_script.get(i) == null) {
                        continue;
                    } else {
                        for (int j = 0; j < devide_com_script.get(i).size(); j++) {

                            if(devide_com_script.get(i).get(j).getIsCheck()==true){
                                mItems_fav_list.add(devide_com_script.get(i).get(j));
                                //mItems_fav_uri.add(Uri.parse(devide_com_uri.get(i).get(j)));
                            }else
                                continue;

                        }
                    }
                }




                //Files_Fragment_Fav.fav_editAdapter.setItemList(mItems_fav_list, mItems_fav_uri);
                Files_Fragment_Fav.fav_editAdapter.setItemList(mItems_fav_list);
                Files_Fragment_Fav.fav_editAdapter.notifyDataSetChanged();
//                System.out.println("script크기" + devide_com_script.get(devide_position-1).size());
   //             System.out.println("script크기" + devide_com_script.get(devide_position+1).size());



            } else {   // RESULT_CANCEL
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
            //listView.setAdapter(sel_adapter);

            sel_adapter= new MySelectAdapter(getActivity(), sel_list);
            swipeMenuListView.setAdapter(sel_adapter);







            //sel_adapter.setItemList(MySelectAdapter.list);
            //sel_adapter.notifyDataSetChanged();
//
        }else if (requestCode == 400 ) {
            if (resultCode == RESULT_OK) {
                System.out.println("400 결과..ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ");


                Bundle bundle2 = data.getExtras();
                //Bundle bundle3 = data.getExtras();
                Toast.makeText(getActivity(), "학습완료", Toast.LENGTH_SHORT).show();

                ArrayList<Edit_item> ary_devide_com_script = (ArrayList<Edit_item>)bundle2.getSerializable("devide_com_script");
                //ArrayList<Uri> ary_devide_com_uri = (ArrayList<Uri>)bundle3.getSerializable("devide_com_uri");
                int devide_position = data.getExtras().getInt("position_re");
                /*sel_list.get(sel_list.size()-1).setDevide_complete(false);
                listView.setAdapter(sel_adapter);
                System.out.println(sel_list.get(sel_list.size()-1).isDevide_complete());
                System.out.println(sel_list.get(sel_list.size()-2).isDevide_complete());*/

                Log.e("File_Fragment_Item", "script_list" + Integer.toString(ary_devide_com_script.size()));
                //Log.e("File_Fragment_Item", "uri_list" + Integer.toString(ary_devide_com_uri.size()));

                Log.e("File_Fragment_Item", "position" + Integer.toString(devide_position));

                Log.e("File_Fragment_Item", "진짜학습완료!!!!");
                //listView.setAdapter(sel_adapter);

                /*ArrayList<String> sub = new ArrayList<>();

                for(int i=0;i<ary_devide_com_uri.size();i++){
                    sub.add(ary_devide_com_uri.get(i).toString());
                }*/

                devide_com_script.set(devide_position, ary_devide_com_script);

                int redup=0;

                for(int j=0;j<HomeFragment.recent_file.size();j++)
                {
                    if(sel_list.get(devide_position).getUnique_key().equals(HomeFragment.recent_file.get(j).getUnique_key())){
                        HomeFragment.recent_file.remove(j);
                        HomeFragment.recent_script.remove(j);
                        HomeFragment.recent_file.add(0, sel_list.get(devide_position));
                        HomeFragment.recent_script.add(0, devide_com_script.get(devide_position));
                        HomeFragment.recent_adapter.notifyDataSetChanged();
                        HomeFragment.text_empty.setVisibility(View.GONE);
                        redup=1;
                    }
                }

                if(redup==0) {
                    if (HomeFragment.recent_file.size() == 5) {
                        HomeFragment.recent_file.remove(4);
                        HomeFragment.recent_script.remove(4);
                        HomeFragment.recent_file.add(0, sel_list.get(devide_position));
                        HomeFragment.recent_script.add(0, devide_com_script.get(devide_position));
                        HomeFragment.recent_adapter.notifyDataSetChanged();
                        HomeFragment.text_empty.setVisibility(View.GONE);
                    } else {

                        HomeFragment.recent_file.add(0, sel_list.get(devide_position));
                        HomeFragment.recent_script.add(0, devide_com_script.get(devide_position));
                        HomeFragment.recent_adapter.notifyDataSetChanged();
                        HomeFragment.text_empty.setVisibility(View.GONE);
                    }
                }
               // devide_com_uri.set(devide_position, sub);

                //devide_com_uri. = sub;


                //playlist_adapter;
                //playlist_items;


                for(int i=0;i<playlist_items.size();i++){
                    for(int j=0;j<playlist_items.get(i).getSound_file().size();j++)
                    {
                            if(sel_list.get(devide_position).getUnique_key().equals(playlist_items.get(i).getSound_file().get(j).getUnique_key())){
                                playlist_items.get(i).getDevide_sound_file().set(j, ary_devide_com_script);
                            }


                    }
                }

                /*
                for(int i=0;i<playlist_items.size();i++)
                {
                    for(int j=0;j<playlist_items.get(i).getDevide_sound_file().size();j++){
                        for(int k =0;k<playlist_items.get(i).getDevide_sound_file().get(j).size();k++)
                        {
                            for(int l=0;l<ary_devide_com_script.size();l++)
                            {
                                if(playlist_items.get(i).getDevide_sound_file().get(j).get(k).getItem_uri().equals(ary_devide_com_script.get(l).getItem_uri())){
                                    playlist_items.get(i).getDevide_sound_file().get(j).set(k, ary_devide_com_script.get(l));                                }
                            }
                        }


                    }
                }*/





                mItems_fav_list.clear();
               // mItems_fav_uri.clear();

                for(int i =0;i<devide_com_script.size();i++) {
                    if (devide_com_script.get(i) == null) {
                        continue;
                    } else {
                        for (int j = 0; j < devide_com_script.get(i).size(); j++) {

                            if(devide_com_script.get(i).get(j).getIsCheck()==true){
                                mItems_fav_list.add(devide_com_script.get(i).get(j));
                                //mItems_fav_uri.add(Uri.parse(devide_com_uri.get(i).get(j)));
                            }else
                                continue;

                        }
                    }
                }

                //System.out.println("subsubsub크기" + sub.size());

                System.out.println("favor크기 : " + mItems_fav_list.size());

                //Files_Fragment_Fav.fav_editAdapter.setItemList(mItems_fav_list, mItems_fav_uri);
                Files_Fragment_Fav.fav_editAdapter.setItemList(mItems_fav_list);
                Files_Fragment_Fav.fav_editAdapter.notifyDataSetChanged();
//                System.out.println("script크기" + devide_com_script.get(devide_position-1).size());
                //             System.out.println("script크기" + devide_com_script.get(devide_position+1).size());



            } else {   // RESULT_CANCEL
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
            //listView.setAdapter(sel_adapter);

            //sel_adapter= new MySelectAdapter(getActivity(), sel_list);
            //listView.setAdapter(sel_adapter);







            //sel_adapter.setItemList(MySelectAdapter.list);
            //sel_adapter.notifyDataSetChanged();
//
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("파일 onstop!!-----------------------------");


        //uri 는 바로 sharedpreferecne가 안되기 때문에 String으로 변환
        /*mItems_fav_uri_str.clear();
        for(int i=0;i<mItems_fav_uri.size();i++){
            mItems_fav_uri_str.add(mItems_fav_uri.get(i).toString());
        }*/
        //SharedPreference호출

        //System.out.println("저장 hash 크기" + Files_Fragment_PlayList.hashMap.size());

        System.out.println("종료되기전 : " + playlist_adapter.getmItems().size());
        SavePlaylistData(playlist_adapter.getmItems());
        SaveMusicData(sel_list);
        SaveDevideData(devide_com_script);
        //SaveUriData(devide_com_uri);
        SaveFav_list_Data(mItems_fav_list);
        //SaveFav_Uri_Data("fav_uri_data", mItems_fav_uri_str);


    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("파일 onpause!!-----------------------------");
    }
}
