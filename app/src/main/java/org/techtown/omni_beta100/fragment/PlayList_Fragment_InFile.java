package org.techtown.omni_beta100.fragment;

import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.techtown.omni_beta100.R;
import org.techtown.omni_beta100.activity.Devide_RecordActivity;
import org.techtown.omni_beta100.activity.RecordActivity;
import org.techtown.omni_beta100.adapter.MyAdapter;
import org.techtown.omni_beta100.adapter.MySelectAdapter;
import org.techtown.omni_beta100.adapter.PlayListSelectAdapter;
import org.techtown.omni_beta100.util.AudioEditor;
import org.techtown.omni_beta100.util.Edit_item;
import org.techtown.omni_beta100.util.MusicDto;
import org.techtown.omni_beta100.util.ProgressDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class PlayList_Fragment_InFile extends Fragment {
    //private ListView listView;
    private ImageButton bt_upload;
    public static MySelectAdapter sel_adapter;
    private ArrayList<MusicDto> list;
    private ArrayList<MusicDto> play_list;
    private SwipeMenuListView swipeMenuListView;
    private ImageView bt_check;
    private static ProgressDialog custom_loading;
    //private Spinner spinner;
    private ArrayAdapter<String> arrayAdapter;
    private Button backButton;
    private Files_Fragment_PlayList filesFragmentPlayList;
    public int item_position;
    public static int count;
    public static int infileToplaylist_position;
   // public static PlayList_Fragment_InFile plin;
    //public static String [] spinner_file_items;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        count = 0;
        Bundle bundle = getArguments();
        System.out.println("곧");
        if(bundle != null){
            System.out.println("bundle 안에 들어옴");
            item_position = bundle.getInt("position"); //Name 받기.
            // infileToplaylist_position = item_position;
            System.out.println(item_position+"이게 포지션 값이다 임마!"); //확인

        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.files_fragment_playlist_infile, container, false);
        //View footer  = getLayoutInflater().inflate(R.layout.playlist_list_infile_footer,null,false);

        bt_upload = (ImageButton)v.findViewById(R.id.playlist_bt_upload);
        //spinner = (Spinner) v.findViewById(R.id.file_spinner);
        backButton = (Button)v.findViewById(R.id.playlist_back_button);
        String [] spinner_file_items = {"asgg","Asgsge"};

        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();



        //plin= PlayList_Fragment_InFile.this;


        //play_list = new ArrayList<>();
        //play_list = Files_Fragment_PlayList.hashMap.get(item_position);

        play_list = Files_Fragment_Item.playlist_adapter.getmItems().get(item_position).getSound_file();
        System.out.println(item_position+"afq");
        //System.out.println(Files_Fragment_PlayList.hashMap.size()+"이거 해쉬맵 사이즈 값이야");
        //System.out.println(Files_Fragment_PlayList.hashMap.get(item_position)+"이거 플레이 리스트 사이즈 값이야");
        System.out.println(play_list.size()+"이거 플레이 리스트 사이즈 값이야");

        swipeMenuListView = (SwipeMenuListView) v.findViewById(R.id.playlist_infile);

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



        //listView = (ListView)v.findViewById(R.id.playlist_infile);   // 대기
        sel_adapter = new MySelectAdapter(getActivity(),  play_list);

        // position 값이랑 ArrayList를 mapping 해야함
        // Hashmap 사용

        swipeMenuListView.setAdapter(sel_adapter);
        swipeMenuListView.setMenuCreator(creator);
        //listView.setAdapter(sel_adapter);   //대기
        //listView.addFooterView(footer);
//android.R.layout.simple_spinner_dropdown_item

        //arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.ALL, android.R.layout.simple_spinner_dropdown_item);
        //arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Files_Fragment_PlayList.spinner_file_items);

        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch(index){

                    case 0 :
                        Log.d("", "delete..............");
                        // 요기 삭제버턴 클릭시 코딩...
                        sel_adapter.removeItem(position);
                        sel_adapter.setItemList((ArrayList<MusicDto>) sel_adapter.getList());
                        Files_Fragment_Item.playlist_adapter.getmItems().get(item_position).getDevide_sound_file().remove(position);

                        sel_adapter.notifyDataSetChanged();


                        System.out.println(play_list.size() + "개 ooooo");
                        System.out.println("이거비교해야함 " +  Files_Fragment_Item.playlist_adapter.getmItems().get(item_position).getSound_file().size());
                        System.out.println("이것도???" +  Files_Fragment_Item.playlist_adapter.getmItems().get(item_position).getDevide_sound_file().size());

                        String value1 = Integer.toString(play_list.size()) + " files";
                        Files_Fragment_Item.playlist_adapter.modifyItem(item_position,value1);
                        break;

                }

                return false;
            }
        });
        //일단 요기까지...


        /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //System.out.println(Files_Fragment_PlayList.hashMap.get(position) + "종휴 다라라라 ");
                item_position = position;
                //play_list = Files_Fragment_PlayList.hashMap.get(position);
                System.out.println(play_list.size() + " play_list의 갯수");
                sel_adapter= new MySelectAdapter(getActivity(),  play_list);
                swipeMenuListView.setAdapter(sel_adapter);
                //((TextView)adapterView.getChildAt(0)).setTextColor(Color.GRAY);
                System.out.println(item_position);
                System.out.println("###############클릭되는구나#################");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {



            }
        });

        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(item_position);*/


        //TextView spinnerText = (TextView) spinner.getChildAt(0);
        //spinnerText.setTextColor(Color.GRAY);

        //getMusicList();


        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list = Files_Fragment_Item.sel_list;


                final MyAdapter adapter = new MyAdapter(getActivity(), list);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                        .setAdapter(adapter, null)
                        .setTitle("업로드할 파일을 선택하세요!")
                        .setPositiveButton("선택완료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                for (int i = 0;i<list.size();i++) {
                                    if (list.get(i).getIsSelected()==true) {
                                       // Log.d("MainActivity", music.getTitle() + " 선택");
                                        play_list.add(list.get(i));
                                        Files_Fragment_Item.playlist_adapter.getmItems().get(item_position).getDevide_sound_file().add(Files_Fragment_Item.devide_com_script.get(i));


                                        /*Uri musicURI = Uri.withAppendedPath(
                                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, ""+ music.getId());
                                        Cursor cursor = getActivity().getContentResolver().query(musicURI, null, null, null, null );
                                        cursor.moveToNext();*/

                                        // 클라우드에 들어가는 경우
                                    }
                                }

                                System.out.println(play_list.size() + "개가 선택되었습니다");
                                System.out.println("또 비교" +  Files_Fragment_Item.playlist_adapter.getmItems().get(item_position).getSound_file().size());
                                System.out.println("요곳도" +  Files_Fragment_Item.playlist_adapter.getmItems().get(item_position).getDevide_sound_file().size());
                                //이부분 참고하여 만들어보기!! sharedpreference sel_list에 담기
                               // Files_Fragment_PlayList.hashMap.put(item_position,play_list);
                               // System.out.println(Files_Fragment_PlayList.hashMap.size() + "개 (hashmap의 갯수)입니다. ");

                                String value1 = Integer.toString(play_list.size()) + " files";
                                Files_Fragment_Item.playlist_adapter.modifyItem(item_position,value1);

                                //swipeMenuListView.setAdapter(sel_adapter);
                                sel_adapter.setItemList(sel_adapter.getList());
                                sel_adapter.notifyDataSetChanged();






                                for(MusicDto sel_mu : play_list){
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
                                for(MusicDto sel_mu : play_list) {
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
                        bt_check = (ImageView) view.findViewById(R.id.check_image);

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

        swipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
               /* AlertDialog.Builder editorbuilder = new AlertDialog.Builder(getActivity())
                        .setTitle("음성파일을 편집하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                Intent intent = new Intent(getContext(), AudioEditor.class);
                                intent.putExtra("position",position);
                                intent.putExtra("playlist",play_list);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getActivity(), RecordActivity.class);
                                intent.putExtra("position",position);
                                intent.putExtra("playlist",play_list);
                                custom_loading.show();
                                startActivity(intent);
                            }
                        });

                AlertDialog alertDialog = editorbuilder.create();
                alertDialog.show();*/

                if (play_list.get(position).isDevide_complete() == false) {
                    AlertDialog.Builder editorbuilder = new AlertDialog.Builder(getActivity())
                            .setTitle("음성파일을 편집하시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(getContext(), AudioEditor.class);
                                    intent.putExtra("pl_position", position);
                                    intent.putExtra("pl_playlist", play_list);
                                    startActivityForResult(intent, 77);
                                    //startActivity(intent);
                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    int redup=0;

                                    for(int j=0;j<HomeFragment.recent_file.size();j++)
                                    {
                                        if(play_list.get(position).getUnique_key().equals(HomeFragment.recent_file.get(j).getUnique_key())){
                                            HomeFragment.recent_file.remove(j);
                                            HomeFragment.recent_script.remove(j);
                                            HomeFragment.recent_file.add(0, play_list.get(position));
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
                                            HomeFragment.recent_file.add(0, play_list.get(position));
                                            HomeFragment.recent_script.add(0, null);
                                            HomeFragment.recent_adapter.notifyDataSetChanged();
                                            HomeFragment.text_empty.setVisibility(View.GONE);
                                        } else {

                                            HomeFragment.recent_file.add(0, play_list.get(position));
                                            HomeFragment.recent_script.add(0, null);
                                            HomeFragment.recent_adapter.notifyDataSetChanged();
                                            HomeFragment.text_empty.setVisibility(View.GONE);
                                        }
                                    }



                                    Intent intent = new Intent(getActivity(), RecordActivity.class);
                                    intent.putExtra("pl_position", position);
                                    intent.putExtra("pl_playlist", play_list);
                                    //custom_loading.show();
                                    //startActivityForResult(intent, 49);
                                    startActivity(intent);


                                }
                            });

                    AlertDialog alertDialog = editorbuilder.create();
                    alertDialog.show();
                }
                else{



                    Intent intent2 = new Intent(getActivity(), Devide_RecordActivity.class);
                    intent2.putExtra("devide_com_script_pl", Files_Fragment_Item.playlist_adapter.getItem(item_position).getDevide_sound_file());
                    intent2.putExtra("position_pl", position);
                    intent2.putExtra("orin_music", Files_Fragment_Item.playlist_adapter.getItem(item_position).getSound_file().get(position));
                    intent2.putExtra("playlist_title", Files_Fragment_Item.playlist_adapter.getItem(item_position).getSound_file().get(position).getTitle());
                    startActivityForResult(intent2, 300);



                    //System.out.println(devide_com_script.get(position).size());
                    //System.out.println("이게 나와야함" + devide_com_uri.get(position).size());



                }
            }

        });

        backButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

                filesFragmentPlayList = new Files_Fragment_PlayList();
               fragmentManager.beginTransaction().replace(R.id.frame_playlist, filesFragmentPlayList).commit();

                //fragmentManager.beginTransaction().show(Files_Fragment_PlayList.pl).commit();
               // fragmentManager.beginTransaction().hide(Files_Fragment_PlayList.playListFragmentInFile).commit();
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

    /*static public ProgressDialog getCustom_loading() {
        return custom_loading;
    }*/

    // 기기 내 음악파일들을 불러와서 list로 보여주기
    /*public void getMusicList(){
        list = new ArrayList<>();

        //String str = "/music/";
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST};

        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);*/

        /*Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, android.provider.MediaStore.Audio.Media.DATA + " like ? ", new String[] {"%"+str+"%"}, null);*/

       /*while(cursor.moveToNext()){
            MusicDto musicDto = new MusicDto();
            musicDto.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            musicDto.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            musicDto.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            musicDto.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            list.add(musicDto);
        }
        cursor.close();

    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 77 ) {
            if (resultCode == RESULT_OK) {

                Log.e("playlist_infile", "일단 이거 받았네???? 여기서");
                System.out.println("77 결과..ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ2");

                Bundle bundle = data.getExtras();
                Bundle bundle2 = data.getExtras();

                Toast.makeText(getActivity(), "playlist에서 분할완료", Toast.LENGTH_SHORT).show();

                ArrayList<MusicDto> ar = (ArrayList<MusicDto>)bundle.getSerializable("devide_com_list");
                ArrayList<Edit_item> ary_devide_com_script = (ArrayList<Edit_item>)bundle2.getSerializable("devide_com_script");

                int devide_position = data.getExtras().getInt("position");
                play_list=ar;
                Files_Fragment_Item.playlist_adapter.getItem(item_position).setSound_file(ar);


                int redup=0;

                for(int j=0;j<HomeFragment.recent_file.size();j++)
                {
                    if(play_list.get(devide_position).getUnique_key().equals(HomeFragment.recent_file.get(j).getUnique_key())){
                        HomeFragment.recent_file.remove(j);
                        HomeFragment.recent_script.remove(j);
                        HomeFragment.recent_file.add(0, play_list.get(devide_position));
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
                        HomeFragment.recent_file.add(0, play_list.get(devide_position));
                        HomeFragment.recent_script.add(0, ary_devide_com_script);
                        HomeFragment.recent_adapter.notifyDataSetChanged();
                        HomeFragment.text_empty.setVisibility(View.GONE);
                    } else {

                        HomeFragment.recent_file.add(0, play_list.get(devide_position));
                        HomeFragment.recent_script.add(0, ary_devide_com_script);
                        HomeFragment.recent_adapter.notifyDataSetChanged();
                        HomeFragment.text_empty.setVisibility(View.GONE);
                    }
                }




                Log.e("File_Fragment_Item", "sel_list" + Integer.toString(play_list.size()));
                Log.e("File_Fragment_Item", "sel_list" + Boolean.toString(play_list.get(devide_position).isDevide_complete()));
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

                for(int i=0;i<Files_Fragment_Item.sel_list.size();i++)
                {
                    if(Files_Fragment_Item.sel_list.get(i).getUnique_key().equals(play_list.get(devide_position).getUnique_key())){
                        Files_Fragment_Item.sel_list.get(i).setDevide_complete(play_list.get(devide_position).isDevide_complete());
                        Files_Fragment_Item.devide_com_script.set(i, ary_devide_com_script);

                    }
                }

               for(int i=0;i<Files_Fragment_Item.playlist_adapter.getmItems().size();i++)
                {
                    for(int j=0;j<Files_Fragment_Item.playlist_adapter.getmItems().get(i).getSound_file().size();j++) {
                        if (i == item_position) {
                            continue;
                        } else {
                            if (Files_Fragment_Item.playlist_adapter.getmItems().get(i).getSound_file().get(j).getUnique_key().equals(play_list.get(devide_position).getUnique_key())) {

                                Files_Fragment_Item.playlist_adapter.getmItems().get(i).getDevide_sound_file().set(j, ary_devide_com_script);
                                Files_Fragment_Item.playlist_adapter.getmItems().get(i).getSound_file().set(j, play_list.get(devide_position));
                            }
                        }
                    }
                }

                Files_Fragment_Item.playlist_adapter.getItem(item_position).getDevide_sound_file().set(devide_position, ary_devide_com_script);




               // mItems_fav_list.clear();
                //mItems_fav_uri.clear();

                /*for(int i =0;i<devide_com_script.size();i++) {
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
                }*/


                //Files_Fragment_Fav.fav_editAdapter.setItemList(mItems_fav_list);
               // Files_Fragment_Fav.fav_editAdapter.notifyDataSetChanged();
//



            } else {   // RESULT_CANCEL
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }

            sel_adapter= new MySelectAdapter(getActivity(), play_list);
            swipeMenuListView.setAdapter(sel_adapter);

            Files_Fragment_Item.sel_adapter.setItemList(Files_Fragment_Item.sel_adapter.getList());
            Files_Fragment_Item.sel_adapter.notifyDataSetChanged();


//
        }else if(requestCode == 300){
            if (resultCode == RESULT_OK) {
                System.out.println("300 결과..ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ");


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

                int redup=0;

                for(int j=0;j<HomeFragment.recent_file.size();j++)
                {
                    if(play_list.get(devide_position).getUnique_key().equals(HomeFragment.recent_file.get(j).getUnique_key())){
                        HomeFragment.recent_file.remove(j);
                        HomeFragment.recent_script.remove(j);
                        HomeFragment.recent_file.add(0, play_list.get(devide_position));
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
                        HomeFragment.recent_file.add(0, play_list.get(devide_position));
                        HomeFragment.recent_script.add(0, ary_devide_com_script);
                        HomeFragment.recent_adapter.notifyDataSetChanged();
                        HomeFragment.text_empty.setVisibility(View.GONE);
                    } else {

                        HomeFragment.recent_file.add(0, play_list.get(devide_position));
                        HomeFragment.recent_script.add(0, ary_devide_com_script);
                        HomeFragment.recent_adapter.notifyDataSetChanged();
                        HomeFragment.text_empty.setVisibility(View.GONE);
                    }
                }





                Files_Fragment_Item.playlist_adapter.getmItems().get(item_position).getDevide_sound_file().set(devide_position, ary_devide_com_script);

                for(int i=0;i<Files_Fragment_Item.sel_list.size();i++)
                {
                    System.out.println(Files_Fragment_Item.sel_list.size());
                    if(Files_Fragment_Item.sel_list.get(i).getUnique_key().equals(play_list.get(devide_position).getUnique_key())){
                        Files_Fragment_Item.devide_com_script.set(i, ary_devide_com_script);
                        System.out.println("요기 들어온건가???");
                    }
                    else{
                        System.out.println("이겨면 실패인데???");
                    }
                }

                for(int i=0;i<Files_Fragment_Item.playlist_adapter.getmItems().size();i++)
                {
                    for(int j=0;j<Files_Fragment_Item.playlist_adapter.getmItems().get(i).getSound_file().size();j++) {
                        if (i == item_position) {
                            continue;
                        } else {
                            if (Files_Fragment_Item.playlist_adapter.getmItems().get(i).getSound_file().get(j).getUnique_key().equals(play_list.get(devide_position).getUnique_key())) {

                                Files_Fragment_Item.playlist_adapter.getmItems().get(i).getDevide_sound_file().set(j, ary_devide_com_script);
                                Files_Fragment_Item.playlist_adapter.getmItems().get(i).getSound_file().set(j, play_list.get(devide_position));
                            }
                        }
                    }
                }



                Files_Fragment_Item.mItems_fav_list.clear();
                // mItems_fav_uri.clear();

                for(int i =0;i<Files_Fragment_Item.devide_com_script.size();i++) {
                    if (Files_Fragment_Item.devide_com_script.get(i) == null) {
                        continue;
                    } else {
                        for (int j = 0; j < Files_Fragment_Item.devide_com_script.get(i).size(); j++) {

                            if(Files_Fragment_Item.devide_com_script.get(i).get(j).getIsCheck()==true){
                                Files_Fragment_Item.mItems_fav_list.add(Files_Fragment_Item.devide_com_script.get(i).get(j));
                                //mItems_fav_uri.add(Uri.parse(devide_com_uri.get(i).get(j)));
                            }else
                                continue;

                        }
                    }
                }


                //Files_Fragment_Fav.fav_editAdapter.setItemList(mItems_fav_list, mItems_fav_uri);
                Files_Fragment_Fav.fav_editAdapter.setItemList(Files_Fragment_Item.mItems_fav_list);
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

    /*private void SaveMusicData(ArrayList<MusicDto> shared_music){
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
    }*/
}