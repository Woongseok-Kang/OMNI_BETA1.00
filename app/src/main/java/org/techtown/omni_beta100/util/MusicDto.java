package org.techtown.omni_beta100.util;

import java.io.Serializable;

public class MusicDto implements Serializable {

    private String id;
    private String albumId;
    private String title;
    private String artist;
    private boolean isSelected;
    private String unique_key;
    private boolean devide_complete;

    public String getUnique_key() {
        return unique_key;
    }
    public void setUnique_key(String unique_key) {
        this.unique_key = unique_key;
    }

    public MusicDto() {
        this.isSelected = false;
    }
    public MusicDto(String id, String albumId, String title, String artist) {
        this.id = id;
        this.albumId = albumId;
        this.title = title;
        this.artist = artist;
        this.isSelected = false;
        this.devide_complete = false;
    }

    public MusicDto(String id, String albumId, String title, String artist, boolean isSelected) {
        this.id = id;
        this.albumId = albumId;
        this.title = title;
        this.artist = artist;
        this.isSelected = isSelected;
        this.devide_complete = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getIsSelected(){
        return isSelected;
    }
    public void setIsSelected(Boolean isSelected){ this.isSelected = isSelected; }

    public boolean isDevide_complete() {
        return devide_complete;
    }

    public void setDevide_complete(boolean devide_complete) {
        this.devide_complete = devide_complete;
    }

    @Override
    public String toString() {
        return "MusicDto{" +
                "id='" + id + '\'' +
                ", albumId='" + albumId + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }



}
