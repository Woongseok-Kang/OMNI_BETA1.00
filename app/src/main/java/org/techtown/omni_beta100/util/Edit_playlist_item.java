package org.techtown.omni_beta100.util;

import java.io.Serializable;
import java.util.ArrayList;

public class Edit_playlist_item  implements Serializable {
    private String stt_time;
    private String title;
    private String file_count;

    private ArrayList<MusicDto> sound_file;
    private ArrayList<ArrayList<Edit_item>> devide_sound_file;

    public ArrayList<MusicDto> getSound_file() {
        return sound_file;
    }

    public void setSound_file(ArrayList<MusicDto> sound_file) {
        this.sound_file = sound_file;
    }

    public ArrayList<ArrayList<Edit_item>> getDevide_sound_file() {
        return devide_sound_file;
    }

    public void setDevide_sound_file(ArrayList<ArrayList<Edit_item>> devide_sound_file) {
        this.devide_sound_file = devide_sound_file;
    }




    public String getFile_count() {
        return file_count;
    }

    public void setFile_count(String file_count) {
        this.file_count = file_count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}