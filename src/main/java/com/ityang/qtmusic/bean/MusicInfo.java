package com.ityang.qtmusic.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/2/25.
 */
public class MusicInfo implements Parcelable {
    private long id;
    private long songId;
    private String artist;
    private String title;
    private String album; //专辑
    private long albumId;
    private long duration;
    private long size;
    private String url;
    private String time;

    public void setTime(String time) {
        this.time = time;
    }

    public long getSongId() {
        return songId;
    }
    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getTime() {
        return time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsMusic() {
        return isMusic;
    }

    public void setIsMusic(int isMusic) {
        this.isMusic = isMusic;
    }

    private int isMusic;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(album);
        dest.writeString(artist);
        dest.writeString(url);
        dest.writeLong(duration);
        dest.writeLong(size);
        dest.writeString(time);
    }

    public static final Parcelable.Creator<MusicInfo>
            CREATOR = new Creator<MusicInfo>() {

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }

        @Override
        public MusicInfo createFromParcel(Parcel source) {
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.setId(source.readLong());
            musicInfo.setTitle(source.readString());
            musicInfo.setAlbum(source.readString());
            musicInfo.setArtist(source.readString());
            musicInfo.setUrl(source.readString());
            musicInfo.setDuration(source.readInt());
            musicInfo.setSize(source.readLong());
            musicInfo.setTime(source.readString());
            return musicInfo;
        }
    };

    public MusicInfo(long id, String title) {
        this.id = id;
        this.title = title;
    }
    public MusicInfo() {

    }

    @Override
    public String toString() {
        return "MusicInfo{" +
                "id=" + id +
                ", artist='" + artist + '\'' +
                ", title='" + title + '\'' +
                ", album='" + album + '\'' +
                ", albumId=" + albumId +
                ", duration=" + duration +
                ", size=" + size +
                ", url='" + url + '\'' +
                ", time='" + time + '\'' +
                ", isMusic=" + isMusic +
                '}';
    }
}

