package org.techtown.omni_beta100.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.techtown.omni_beta100.R;
import org.techtown.omni_beta100.util.Edit_item;
import org.techtown.omni_beta100.util.Edit_playlist_item;
import org.techtown.omni_beta100.util.MusicDto;

import java.util.ArrayList;

public class PlayListAdapter extends BaseAdapter {


    /* 아이템을 세트로 담기 위한 어레이 */
    private ArrayList<Edit_playlist_item> mItems = new ArrayList<>();
    // public static ArrayList<Edit_item> mItems_fav = new ArrayList<>();
    public int mode;
    boolean isCheck;


    /*public PlayListAdapter(ArrayList<Edit_playlist_item>mItems)
    {
        this.mItems = mItems;
    }*/


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Edit_playlist_item getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public ArrayList<Edit_playlist_item> getmItems() {
        return mItems;
    }

    public static void setmItems(ArrayList<Edit_playlist_item> mItems) {
        // EditAdapter.mItems = mItems;
    }

    private TextView text_devide_stt;


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.playlist_item, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */

        TextView playlist_title = (TextView) convertView.findViewById(R.id.playlist_title) ;
        TextView playlist_item_count = (TextView) convertView.findViewById(R.id.playlist_item_count) ;

        // final ImageButton imbt_favor = (ImageButton) convertView.findViewById(R.id.imbt_favor) ;

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        final Edit_playlist_item myItem = getItem(position);


        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        playlist_title.setText(myItem.getTitle());
        playlist_item_count.setText(myItem.getFile_count());
        //  playlist_title.setText(mItems.get(position).getTitle());
        //  playlist_item_count.setText(mItems.get(position).getTime());

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */

        return convertView;
    }

    public void addItem(String title, String count, ArrayList<MusicDto> music ,ArrayList<ArrayList<Edit_item>> de_music) {

        Edit_playlist_item mItem = new Edit_playlist_item();

        /* MyItem에 아이템을 setting한다. */
        mItem.setTitle(title);
        mItem.setFile_count(count);
        mItem.setSound_file(music);
        mItem.setDevide_sound_file(de_music);

        /* mItems에 MyItem을 추가한다. */
        mItems.add(mItem);
    }


    //리스트 수정
    public void modifyItem(int position, String s){
        mItems.get(position).setFile_count(s);
    }

    //리스트 갱신
    public void setItemList(ArrayList<Edit_playlist_item> list){
        this.mItems = list;
    }

    //리스트 리셋
    public void resetItem(){
        mItems.clear();
    }
    //리스트 목록 삭제
    public void removeItem(int position){
        mItems.remove(position);
    }

}
