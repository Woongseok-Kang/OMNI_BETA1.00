package org.techtown.omni_beta100.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.techtown.omni_beta100.R;
import org.techtown.omni_beta100.activity.Devide_RecordActivity;
import org.techtown.omni_beta100.fragment.Files_Fragment_Item;
import org.techtown.omni_beta100.util.Edit_item;

import java.io.File;
import java.util.ArrayList;

public class EditAdapter extends BaseAdapter {



    /* 아이템을 세트로 담기 위한 어레이 */
    private ArrayList<Edit_item> mItems = new ArrayList<>();



   // private ArrayList<Uri> mItems_uri = new ArrayList<>();


    public int mode;
    boolean isCheck;


   /* public ArrayList<Uri> getmItems_uri() {
        return mItems_uri;
    }

    public void setmItems_uri(ArrayList<Uri> mItems_uri) {
        this.mItems_uri = mItems_uri;
    }*/


    public ArrayList<Edit_item> getmItems() {
        return mItems;
    }

    public void setmItems(ArrayList<Edit_item> mItems) {
        this.mItems = mItems;
    }


    public EditAdapter(int mode)
    {
        this.mode = mode;
    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Edit_item getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*public Uri getUri(int position) {
        return mItems_uri.get(position);
    }*/


    /*ArrayList<Edit_item> getmItems() {
        return mItems;
    }

    void setmItems(ArrayList<Edit_item> mItems) {
        EditAdapter.mItems = mItems;
    }*/

    //private TextView text_devide_stt;


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_edit_stt, parent, false);
        }

        //isCheck = false;

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        final LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.study_layout);
        final ImageButton imbt_play = (ImageButton) convertView.findViewById(R.id.imbt_play);
        TextView edit_stt_time = (TextView) convertView.findViewById(R.id.edit_stt_time);

        final TextView text_devide_stt = (TextView) convertView.findViewById(R.id.text_devide_stt);
        final EditText edit_devide_stt = (EditText) convertView.findViewById(R.id.edit_devide_stt);


        //ImageButton imbt_setting = (ImageButton) convertView.findViewById(R.id.imbt_setting);
        final ImageButton imbt_favor = (ImageButton) convertView.findViewById(R.id.imbt_favor);
        final ImageButton imbt_edit_done = (ImageButton)convertView.findViewById(R.id.imbt_done);


        Log.v("김문곤", "바보");
        imbt_favor.setVisibility(View.GONE);
        imbt_play.setVisibility(View.GONE);
        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */


        final Edit_item myItem = getItem(position);
        //final Uri myUri = getUri(position);
        //myItem.setIsCheck(false);

        if(mode == 0)
        {
            imbt_play.setVisibility(View.GONE);
            imbt_favor.setVisibility(View.GONE);
        }
        else if(mode ==1)    // 학습모드
        {
            imbt_play.setVisibility(View.VISIBLE);
            imbt_favor.setVisibility(View.VISIBLE);
        }
        else if(mode == 2)  // 즐겨찾기 모드
        {
            imbt_play.setVisibility(View.VISIBLE);
            imbt_favor.setVisibility(View.GONE);
        }



        if(myItem.getStatus_studying()==false)
        {
            imbt_play.setImageResource(R.drawable.icon_edit_play);
            convertView.setBackgroundResource(R.drawable.stt_round_list);
        }
        else{
            imbt_play.setImageResource(R.drawable.icon_edit_studying);
            convertView.setBackgroundResource(R.drawable.stt_round_list2);
        }

        if(myItem.getIsCheck() == true){
            imbt_favor.setColorFilter(Color.parseColor("#8795ff"));

        }
        else
        {
            imbt_favor.setColorFilter(Color.parseColor("#ffffff"));
        }



        //Edit_text 키보드 보이게


        /* 각 위젯에 세팅된 아이템을 뿌려준다 */

        edit_stt_time.setText(myItem.getStt_time());
        text_devide_stt.setText(myItem.getEdit_stt_text());
        edit_devide_stt.setText(myItem.getEdit_stt_text());

        if(myItem.getMode_edit() ==0){

        }
        else if(myItem.getMode_edit() ==1) {


            layout.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
            text_devide_stt.setVisibility(View.GONE);
            edit_devide_stt.setVisibility(View.VISIBLE);
            imbt_favor.setVisibility(View.GONE);
            imbt_edit_done.setVisibility(View.VISIBLE);
            //edit_devide_stt.requestFocus();
            // edit_devide_stt.setImeOptions(EditorInfo.IME_ACTION_DONE);

           /* InputMethodManager imm = (InputMethodManager)convertView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(convertView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            imm.showSoftInput(edit_devide_stt, InputMethodManager.SHOW_FORCED);*/
        }
        else {
            if (mode == 2) {
                layout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                text_devide_stt.setVisibility(View.VISIBLE);
                edit_devide_stt.setVisibility(View.GONE);
                imbt_favor.setVisibility(View.GONE);
                imbt_edit_done.setVisibility(View.GONE);
            } else {
                layout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                text_devide_stt.setVisibility(View.VISIBLE);
                edit_devide_stt.setVisibility(View.GONE);
                imbt_favor.setVisibility(View.VISIBLE);
                imbt_edit_done.setVisibility(View.GONE);
            }
        }

        imbt_edit_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mode==2){
                    text_devide_stt.setText(edit_devide_stt.getText());
                    edit_devide_stt.setVisibility(View.GONE);
                    text_devide_stt.setVisibility(View.VISIBLE);
                    imbt_edit_done.setVisibility(View.GONE);
                    imbt_favor.setVisibility(View.GONE);

                    String s = edit_devide_stt.getText().toString();
                    myItem.setEdit_stt_text(s);
                    myItem.setMode_edit(2);

                    layout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    //Devide_RecordActivity.f.setVisibility(View.VISIBLE);
                }else{
                    text_devide_stt.setText(edit_devide_stt.getText());
                    edit_devide_stt.setVisibility(View.GONE);
                    text_devide_stt.setVisibility(View.VISIBLE);
                    imbt_edit_done.setVisibility(View.GONE);
                    imbt_favor.setVisibility(View.VISIBLE);

                    String s = edit_devide_stt.getText().toString();
                    myItem.setEdit_stt_text(s);
                    myItem.setMode_edit(2);

                    layout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    //Devide_RecordActivity.f.setVisibility(View.VISIBLE);
                }



            }
        });


        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */

        imbt_favor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(myItem.getIsCheck() == false)
                {
                    //addItem(imbt_favor_color);
                    Log.v("클릭1", "보라");
                    //imbt_favor.setSelected(true);
                    //imbt_favor.imageTintList = ColorStateList.valueOf(Color.parseColor("#55ff0000"));
                    imbt_favor.setColorFilter(Color.parseColor("#8795ff"));
                    //imbt_favor.setImageResource(R.drawable.bell);
                    Log.v("isSelected", "z" + imbt_favor.isSelected());
                    System.out.println(position + "번눌렀어");
                    //isCheck = true;
                    // 좋아요 데이터 저장 //
                    /*Files_Fragment_Item.mItems_fav_list.add(myItem);
                    Files_Fragment_Item.mItems_fav_uri.add(myUri);

                    System.out.println("아이템은????" + myItem);
                    System.out.println("fav 아이템은???" + Files_Fragment_Item.mItems_fav_list.get(Files_Fragment_Item.mItems_fav_list.size()-1));

                    System.out.println("현재 mItem_fav_list사이즈 : " + Files_Fragment_Item.mItems_fav_list.size());
                    System.out.println("현재 mItem_fav_uri사이즈 : " + Files_Fragment_Item.mItems_fav_uri.size());*/
                    myItem.setIsCheck(true);
                    // Intent intent = new Intent(context, Files_Fragment_Fav.class);  //context final 로 만듬
                    //intent.putExtra("position",position);
                    // intent.putExtra("devide_list", mItems_fav);
                    //intent.putExtra("uri_list", devide_mp3_list);
                    // startActivity(intent);
                }
                else
                {
                    //addItem(imbt_favor1);
                    Log.v("클릭2", "흰");
                    //imbt_favor.setSelected(false);
                    //imbt_favor.setImageResource(R.drawable.icon_favor);
                    imbt_favor.setColorFilter(Color.parseColor("#ffffff"));
                    Log.v("f", "보라s");
                    Log.v("isSelected", "z" + imbt_favor.isSelected());
                    System.out.println(position + "번눌렀어");
                    myItem.setIsCheck(false);
                    //isCheck = false;
                    /*Files_Fragment_Item.mItems_fav_list.remove(myItem);
                    Files_Fragment_Item.mItems_fav_uri.remove(myUri);

                    System.out.println("현재 mItem_fav_list사이즈 : " + Files_Fragment_Item.mItems_fav_list.size());
                    System.out.println("현재 mItem_fav_uri사이즈 : " + Files_Fragment_Item.mItems_fav_uri.size());*/
                    //Files_Fragment_Item.mItems_fav_uri.remove(position);

                }

            }




        });

        /*imbt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isCheck == false)
                {
                    //addItem(imbt_favor_color);
                    Log.v("클릭1", "보라");
                    //imbt_favor.setSelected(true);
                    //imbt_favor.imageTintList = ColorStateList.valueOf(Color.parseColor("#55ff0000"));
                    imbt_play.setColorFilter(Color.parseColor("#8795ff"));
                    //imbt_favor.setImageResource(R.drawable.bell);
                    Log.v("isSelected", "z" + imbt_favor.isSelected());
                    System.out.println(position + "번눌렀어");
                    isCheck = true;

                }
                else
                {
                    //addItem(imbt_favor1);
                    Log.v("클릭2", "흰");
                    //imbt_favor.setSelected(false);
                    //imbt_favor.setImageResource(R.drawable.icon_favor);
                    imbt_play.setColorFilter(Color.parseColor("#ffffff"));
                    Log.v("f", "보라s");
                    Log.v("isSelected", "z" + imbt_favor.isSelected());
                    System.out.println(position + "번눌렀어");
                    isCheck = false;
                }

            }
        });*/



        return convertView;
    }

    public void addItem(String time, String edit_stt, String uri, String Path) {

        Edit_item mItem = new Edit_item();

        /* MyItem에 아이템을 setting한다. */

        mItem.setStt_time(time);
        mItem.setEdit_stt_text(edit_stt);
        mItem.setItem_uri(uri);
        mItem.setOutpath(Path);

        /* mItems에 MyItem을 추가한다. */
        mItems.add(0, mItem);


    }
    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(String time, String edit_stt, Drawable imageButton ) {

        Edit_item mItem = new Edit_item();

        /* MyItem에 아이템을 setting한다. */

        mItem.setStt_time(time);
        mItem.setEdit_stt_text(edit_stt);

        mItem.setImbt_favor(imageButton);

        /* mItems에 MyItem을 추가한다. */
        mItems.add(mItem);

    }

    //리스트 수정
    public void modifyItem(String s){
        mItems.get(0).setEdit_stt_text(s);
    }


    public void modifyTime(String s){
        mItems.get(0).setStt_time(s);
       // if(s.length()=13, 16, 19)


        int h_start, m_start, sec_start;
        int h_end, m_end, sec_end;

        if(s.length()==13){
            h_start=0;
            m_start = Integer.parseInt(s.substring(0,2));
            sec_start = Integer.parseInt(s.substring(3,5));

            sec_start = (h_start*3600) + (m_start*60) + sec_start;

            h_end = 0;
            m_end = Integer.parseInt(s.substring(8,10));
            sec_end = Integer.parseInt(s.substring(11, 13));

            sec_end = (h_end*3600) + (m_end*60) + sec_end;
        }
        else if(s.length()==16){
            h_start=0;
            m_start = Integer.parseInt(s.substring(0,2));
            sec_start = Integer.parseInt(s.substring(3,5));

            sec_start = (h_start*3600) + (m_start*60) + sec_start;

            h_end = Integer.parseInt(s.substring(8,10));
            m_end = Integer.parseInt(s.substring(11,13));
            sec_end = Integer.parseInt(s.substring(14, 16));

            sec_end = (h_end*3600) + (m_end*60) + sec_end;
        }
        else{
            h_start= Integer.parseInt(s.substring(0,2));
            m_start = Integer.parseInt(s.substring(3,5));
            sec_start = Integer.parseInt(s.substring(6,8));

            sec_start = (h_start*3600) + (m_start*60) + sec_start;

            h_end = Integer.parseInt(s.substring(11,13));
            m_end = Integer.parseInt(s.substring(14,16));
            sec_end = Integer.parseInt(s.substring(17, 19));

            sec_end = (h_end*3600) + (m_end*60) + sec_end;
        }

        mItems.get(0).setStt_time_start(sec_start);
        mItems.get(0).setStt_time_end(sec_end);

        Log.e("분할 시간의 시작시간 : ", sec_start + "초이다");
        Log.e("분할 시간의 끝시간 : ", sec_end + "초이다");


    }

    public void modifyItem(String s, int position){
        mItems.get(position).setEdit_stt_text(s);
    }


    //uri 수정
    public void modifyUri(String uri){

        mItems.get(0).setItem_uri(uri);
    }

    public void modifyFilePath(String path){
        mItems.get(0).setOutpath(path);
    }

    //리스트 갱신
    public void setItemList(ArrayList<Edit_item> list){
        this.mItems = list;
        //this.mItems_uri = uri_list;
    }



    //리스트 리셋
    public void resetItem(){
        mItems.clear();
    }
    //리스트 목록 삭제
    public void removeItem(int position){
        mItems.remove(position);
        //mItems_uri.remove(position);
        Log.e("EditAdapter", "목록 지워짐");
    }

/*    public void removeUri(int position){
        mItems_uri.remove(position);
    }*/

}