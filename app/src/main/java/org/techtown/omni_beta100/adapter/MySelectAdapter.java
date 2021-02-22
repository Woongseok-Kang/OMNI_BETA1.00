package org.techtown.omni_beta100.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.techtown.omni_beta100.R;
import org.techtown.omni_beta100.util.MusicDto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static android.view.View.VISIBLE;

public class MySelectAdapter extends BaseAdapter {



    List<MusicDto> list;
    LayoutInflater inflater;
    Activity activity;

    public MySelectAdapter(){

    }

    public List<MusicDto> getList() {
        return list;
    }

    public void setList(List<MusicDto> list) {
        this.list = list;
    }

    public MySelectAdapter(Activity activity, List<MusicDto> list) {
        this.list = list;
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.sel_listview_item, parent, false);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            convertView.setLayoutParams(layoutParams);
        }

        final MusicDto data = list.get(position);


        ImageView imageView = (ImageView) convertView.findViewById(R.id.album);
        if(getAlbumImage(activity, Integer.parseInt((list.get(position)).getAlbumId()), 170)==null) {

            AssetManager am = convertView.getResources().getAssets() ;
            InputStream is = null ;

            try {
                // 애셋 폴더에 저장된 field.png 열기.
                is = am.open("omni_image_logo.png") ;

                // 입력스트림 is를 통해 field.png 을 Bitmap 객체로 변환.
                Bitmap bm = BitmapFactory.decodeStream(is) ;

                if (bm != null) {
                    // finally rescale to exactly the size we need
                    if (options.outWidth != 170 || options.outHeight != 170) {
                        Bitmap tmp = Bitmap.createScaledBitmap(bm, 170, 170, true);
                        bm.recycle();
                        bm = tmp;
                    }
                }

                // 만들어진 Bitmap 객체를 이미지뷰에 표시.
                imageView.setImageBitmap(bm) ;

                is.close() ;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (is != null) {
                try {
                    is.close() ;
                } catch (Exception e) {
                    e.printStackTrace() ;
                }
            }
        }
        else{
            Bitmap albumImage = getAlbumImage(activity, Integer.parseInt((list.get(position)).getAlbumId()), 170);
            imageView.setImageBitmap(albumImage);
        }



        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(list.get(position).getTitle());

        TextView artist = (TextView) convertView.findViewById(R.id.artist);
        artist.setText(list.get(position).getArtist());


        ImageView imageView_devide = (ImageView)convertView.findViewById(R.id.imageView_devide);

        if(data.isDevide_complete()==true){

            imageView_devide.setVisibility(VISIBLE);
            imageView_devide.setColorFilter(Color.parseColor("#8795ff"));
        }else{
            imageView_devide.setVisibility(View.GONE);
            //imageView_devide.setColorFilter(Color.parseColor("#8795ff"));
        }



        return convertView;
    }

    private static final BitmapFactory.Options options = new BitmapFactory.Options();

    private static Bitmap getAlbumImage(Context context, int album_id, int MAX_IMAGE_SIZE) {
        // NOTE: There is in fact a 1 pixel frame in the ImageView used to
        // display this drawable. Take it into account now, so we don't have to
        // scale later.
        ContentResolver res = context.getContentResolver();
        Uri uri = Uri.parse("content://media/external/audio/albumart/" + album_id);
        if (uri != null) {
            ParcelFileDescriptor fd = null;
            try {
                fd = res.openFileDescriptor(uri, "r");


                // Compute the closest power-of-two scale factor
                // and pass that to sBitmapOptionsCache.inSampleSize, which will
                // result in faster decoding and better quality

                //크기를 얻어오기 위한옵션 ,
                //inJustDecodeBounds값이 true로 설정되면 decoder가 bitmap object에 대해 메모리를 할당하지 않고, 따라서 bitmap을 반환하지도 않는다.
                // 다만 options fields는 값이 채워지기 때문에 Load 하려는 이미지의 크기를 포함한 정보들을 얻어올 수 있다.
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(
                        fd.getFileDescriptor(), null, options);
                int scale = 0;
                if (options.outHeight > MAX_IMAGE_SIZE || options.outWidth > MAX_IMAGE_SIZE) {
                    scale = (int) Math.pow(2, (int) Math.round(Math.log(MAX_IMAGE_SIZE / (double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
                }
                options.inJustDecodeBounds = false;
                options.inSampleSize = scale;



                Bitmap b = BitmapFactory.decodeFileDescriptor(
                        fd.getFileDescriptor(), null, options);

                if (b != null) {
                    // finally rescale to exactly the size we need
                    if (options.outWidth != MAX_IMAGE_SIZE || options.outHeight != MAX_IMAGE_SIZE) {
                        Bitmap tmp = Bitmap.createScaledBitmap(b, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE, true);
                        b.recycle();
                        b = tmp;
                    }
                }

                return b;
            } catch (FileNotFoundException e) {
            } finally {
                try {
                    if (fd != null)
                        fd.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }


    //리스트 수정
    /*public void modifyItem(String s){
        list.get(0).setEdit_stt_text(s);
    }*/

    //리스트 갱신
    public void setItemList(List<MusicDto> list){
        this.list = list;
    }

    //리스트 리셋
    public void resetItem(){
        list.clear();
    }
    //리스트 목록 삭제
    public void removeItem(int position){
        list.remove(position);
    }
}

