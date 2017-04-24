package com.yangbin.lyricview.bean;

import android.util.SparseArray;

/**
 * <pre>
 *     author : YangBin
 *     e-mail : yangbinyhbn@gmail.com
 *     time   : 2017/4/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class Lyric {

    private String fileName;

    /**
     * ID Tags:
     * [ti:Lyrics (song) title]
     * [ar:Lyrics artist]
     * [al:Album where the song is from]
     * [au:Creator of the Songtext]
     * [by:Creator of the LRC file]
     * [length:How long the song is] **ignore
     */
    private String title;
    private String artist;
    private String album;
    private String lyricist;
    private String fileMaker;

    private SparseArray<String> lyricArray = new SparseArray<>();

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getLyricist() {
        return lyricist;
    }

    public void setLyricist(String lyricist) {
        this.lyricist = lyricist;
    }

    public String getFileMaker() {
        return fileMaker;
    }

    public void setFileMaker(String fileMaker) {
        this.fileMaker = fileMaker;
    }

    public void putLyricLine(int time, String lyricLine) {
        lyricArray.put(time, lyricLine);
    }

    public String getLyricLine(int time) {
        return lyricArray.get(time);
    }

    public int keyAt(int index) {
        return lyricArray.keyAt(index);
    }

    public int indexOfKey(int key) {
        return lyricArray.indexOfKey(key);
    }

    @Override
    public String toString() {
        return "Lyric{" +
                "fileName='" + fileName + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", lyricist='" + lyricist + '\'' +
                ", fileMaker='" + fileMaker + '\'' +
                ", lyricArray=" + lyricArray.toString() +
                '}';
    }

    public int size() {
        return lyricArray.size();
    }
}