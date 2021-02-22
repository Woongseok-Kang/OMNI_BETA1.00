package org.techtown.omni_beta100.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.techtown.omni_beta100.R;
import org.techtown.omni_beta100.adapter.PlayListAdapter;
import org.techtown.omni_beta100.util.Edit_item;
import org.techtown.omni_beta100.util.Edit_playlist_item;
import org.techtown.omni_beta100.util.MusicDto;

import java.util.ArrayList;
import java.util.HashMap;

public class Files_Fragment_PlayList extends Fragment{

    public static ListView listView;
    public static SwipeMenuListView swipeMenuPlayList;
    public static String title_txt;
    private PlayListAdapter playListAdapter;
    ArrayList<Edit_playlist_item> fav_devide_items;
    private TextView addplaylist;
    public static PlayList_Fragment_InFile playListFragmentInFile;
    //public static HashMap<Integer, ArrayList<MusicDto>> hashMap;
    private int file_count = 0;
    //public static ArrayList<String >spinner_file_items;
   // public static ArrayList<String> spinner_file_items =  new ArrayList<>();
    //public static Files_Fragment_PlayList pl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.files_fragment_playlist,container,false);

        View footer  = getLayoutInflater().inflate(R.layout.playlist_list_footer,null,false);

        //listView = (ListView)v.findViewById(R.id.playlist);  //대기

        //hashMap = new HashMap<>();
        swipeMenuPlayList = (SwipeMenuListView) v.findViewById(R.id.playlist);

        swipeMenuPlayList.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                swipeMenuPlayList.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {
                swipeMenuPlayList.smoothOpenMenu(position);
            }
        });

        //hashMap = Files_Fragment_Item.hash_save;


        playListFragmentInFile = new PlayList_Fragment_InFile();
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


        //pl = Files_Fragment_PlayList.this;

        final EditText et = new EditText(getContext());


        playListAdapter = Files_Fragment_Item.playlist_adapter;

        //playListAdapter.setmItems(fav_devide_items);
       // playListAdapter = new PlayListAdapter();

        swipeMenuPlayList.setAdapter(playListAdapter);
        swipeMenuPlayList.addFooterView(footer);
        //listView.setStackFromBottom(true);
        swipeMenuPlayList.setMenuCreator(creator);

        // 김문곤 //
        //addplaylist = (TextView)v.findViewById(R.id.addplaylist);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et.getParent() != null)
                    ((ViewGroup) et.getParent()).removeView(et);

                AlertDialog.Builder editorbuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                        .setTitle("새 플레이리스트를 추가합니다")
                        .setView(et)
                        .setPositiveButton("완료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                ArrayList<MusicDto> arrayList = new ArrayList<>();
                                ArrayList<ArrayList<Edit_item>> de_arrayList = new ArrayList<>();
                                String value = et.getText().toString();
                                String value1 = Integer.toString(file_count) + " files";
                                title_txt = value;
                                playListAdapter.addItem(title_txt,value1, arrayList, de_arrayList);
                                //spinner_file_items.add(title_txt);//spinner 보관위해
                                //hashMap.put(listView.getCheckedItemPosition(),arrayList);.notifyDataSetChanged();
                                //swipeMenuPlayList.setAdapter(playListAdapter);
                                playListAdapter.notifyDataSetChanged();
                                et.setText("");

                                System.out.println("요건 플레이리스트꺼 : " + playListAdapter.getmItems().size());
                                System.out.println("요건 초기화면꺼 : " + Files_Fragment_Item.playlist_adapter.getmItems().size());
                                // int position = swipeMenuPlayList.getCount()-2;//footer는 항상 마지막
                                //System.out.println(position+" ~~~ 요기는 listview position 값!");

                                // hashMap.put(position,arrayList);
                                // System.out.println(hashMap.get(position).size() +" aaa");
                            }
                        });

                AlertDialog alertDialog = editorbuilder.create();
                alertDialog.show();


            }
        });



        swipeMenuPlayList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch(index){
                    case 0:
                        Log.d("", "delete..............");
                        // 요기 삭제버턴 클릭시 코딩...
                        playListAdapter.removeItem(position);
                        playListAdapter.setItemList(playListAdapter.getmItems());
                        playListAdapter.notifyDataSetChanged();
                        System.out.println(position +" 포지션 몇이고?");
                       // System.out.println(hashMap.size() +" 해쉬 사이즈 몇이고?");
                        //spinner_file_items.remove(position);
                        //hashMap.remove(position);
                       //System.out.println(hashMap.size() +" 해쉬 사이즈 몇이고?");
                        /*if(position == hashMap.size())
                        {
                            System.out.println("#######");
                        }
                        else{
                            for(int i = position;i<hashMap.size();i++)
                            {
                                System.out.println(hashMap.get(i+1)+"임마!");
                                hashMap.put(i,hashMap.get(i+1));
                                System.out.println(hashMap.get(i+1)+"임마 이거 뭐야!");
                            }
                            hashMap.remove(hashMap.size());
                        }
                        System.out.println(hashMap.get(position)+"뭐야");
                        break;*/

                }

                return false;
            }
        });

        swipeMenuPlayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {

                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                System.out.println(position+"이제 position값 날리나간다!");
                playListFragmentInFile.setArguments(bundle);



                fragmentManager.beginTransaction().replace(R.id.frame_playlist, playListFragmentInFile).commit();
               // fragmentManager.beginTransaction().show(playListFragmentInFile).commit();
               // fragmentManager.beginTransaction().hide(pl).commit();


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
}