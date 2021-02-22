package org.techtown.omni_beta100.util;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.Serializable;
import java.util.Comparator;

public class Edit_item implements Serializable {

    private String stt_time;
    private int stt_time_start;
    private int stt_time_end;
    private String edit_stt_text;
    private Drawable imbt_favor;
    private Boolean isCheck = false;
    private int mode_edit=0;
    private String item_uri;

    private String outpath;




    public String getOutpath() {
        return outpath;
    }

    public void setOutpath(String outpath) {
        this.outpath = outpath;
    }


    private Boolean status_studying = false;


    public Boolean getStatus_studying() {
        return status_studying;
    }

    public void setStatus_studying(Boolean status_studying) {
        this.status_studying = status_studying;
    }


    public String getItem_uri() {
        return item_uri;
    }

    public void setItem_uri(String item_uri) {
        this.item_uri = item_uri;
    }



    public int getStt_time_start() {
        return stt_time_start;
    }

    public void setStt_time_start(int stt_time_start) {
        this.stt_time_start = stt_time_start;
    }

    public int getStt_time_end() {
        return stt_time_end;
    }

    public void setStt_time_end(int stt_time_end) {
        this.stt_time_end = stt_time_end;
    }

    public int getMode_edit() {
        return mode_edit;
    }

    public void setMode_edit(int mode_edit) {
        this.mode_edit = mode_edit;
    }

    public void setIsCheck(Boolean isCheck)
    {
        this.isCheck = isCheck;

    }
    public Boolean getIsCheck()
    {
        return this.isCheck;
    }


    public String getStt_time() {
        return stt_time;
    }

    public void setStt_time(String stt_time) {
        this.stt_time = stt_time;
    }

    public String getEdit_stt_text() {
        return edit_stt_text;
    }

    public void setEdit_stt_text(String edit_stt_text) {
        this.edit_stt_text = edit_stt_text;
    }

    public Drawable getImbt_favor() {
        return this.imbt_favor;
    }

    public void setImbt_favor(Drawable imbt_favor) {
        this.imbt_favor = imbt_favor;

    }

    //time_start기준으로 정렬
    public static class Sort1 implements Comparator<Edit_item> {

        @Override
        public int compare(Edit_item o1, Edit_item o2) {
            if(o1.stt_time_start==o2.stt_time_start){
                return 0;
            }
            return o1.stt_time_start>o2.stt_time_start?1:-1;
        }
    }
    //time_start같을 경우 time_end로 두번째 정렬
    public static class Sort2 implements Comparator<Edit_item>{

        @Override
        public int compare(Edit_item o1, Edit_item o2) {
            if(o1.stt_time_end == o2.stt_time_end) {
                return 0;
            }
            return o1.stt_time_end > o2.stt_time_end ? 1 : -1;
        }
    }
}
