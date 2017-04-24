# LyricView
a view used to display lyric for android

Renderings show:
[image](/image/Animation.gif)


##how to use it
xml
```
    <com.yangbin.lyricview.widget.LyricView
        android:id="@+id/view_lyric"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:lyricNormalTextColor="@color/colorPrimary"
        app:lyricPlayingTextColor="@color/colorAccent"
        app:lyricTextSize="18sp" />
```
and by java code
```
lyricView.setLyric(lyricFilePath);
```
##Copyright
MIT License Copyright (c) 2017 YangBin.
See [LICENSE](LICENSE.txt) for details
