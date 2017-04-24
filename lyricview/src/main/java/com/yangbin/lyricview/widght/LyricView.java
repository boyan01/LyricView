package com.yangbin.lyricview.widght;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yangbin.lyricview.R;
import com.yangbin.lyricview.bean.Lyric;
import com.yangbin.lyricview.exception.LyricParseException;
import com.yangbin.lyricview.utils.DensityUtil;
import com.yangbin.lyricview.utils.LyricUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * <pre>
 *     author : YangBin
 *     e-mail : yangbinyhbn@gmail.com
 *     time   : 2017/4/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class LyricView extends FrameLayout {

    private static final int DEFAULT_LYRIC_ADJUST = 0;

    private static final String DEFAULT_CHARSET = "utf-8";

    /**
     * 歌词时间偏移 例:<hr/>-500 :提前500毫秒 <br>  500 :推后500毫秒
     */
    private int offsetLyric = DEFAULT_LYRIC_ADJUST;

    /**
     * 歌词字体大小
     */
    private float lyricTextSize;

    /**
     * 默认状态下的歌词颜色
     */
    private int lyricNormalTextColor;

    /**
     * 处于播放状态下的歌词的颜色
     */

    private int lyricPlayingTextColor;

    /**
     * 行间距
     */
    private float lyricLineSpacing;

    /**
     * 歌词
     */
    private Lyric lyric;

    /**
     * 当前处于播放状态的歌词的位置
     */
    private int currentLyricPosition = 0;

    private static final String TAG = "LyricView";

    private ListView listLyric;

    private LyricsListAdapter lyricsListAdapter;

    public LyricView(@NonNull Context context) {
        this(context, null);
    }

    public LyricView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LyricView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LyricView);
        if (typedArray != null) {
            lyricTextSize = DensityUtil.px2dip(context, typedArray.getDimension(R.styleable.LyricView_lyricTextSize, DensityUtil.dip2px(context, 16)));
            lyricNormalTextColor = typedArray.getColor(R.styleable.LyricView_lyricNormalTextColor, Color.WHITE);
            lyricPlayingTextColor = typedArray.getColor(R.styleable.LyricView_lyricPlayingTextColor, Color.GRAY);
            lyricLineSpacing = typedArray.getDimension(R.styleable.LyricView_lyricLineSpacing, 0);
            typedArray.recycle();
        }
        LayoutInflater.from(context).inflate(R.layout.view_lyric, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        listLyric = (ListView) findViewById(R.id.list_lyric);
        lyricsListAdapter = new LyricsListAdapter();
        listLyric.setAdapter(lyricsListAdapter);
    }


    /**
     * 指定歌词进行显示
     *
     * @param filePath 歌词文件路径
     */
    public void setLyric(String filePath) {
        setLyric(filePath, DEFAULT_CHARSET);
    }


    /**
     * 指定歌词进行显示
     *
     * @param path        歌词文件路径
     * @param charsetName 歌词文件编码
     */
    public void setLyric(String path, String charsetName) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setLyric(inputStream, charsetName);
    }

    public void setLyric(InputStream inputStream, String charsetName) {
        if (inputStream == null) {
            setLoadLyricError(new LyricParseException("无效的文件输入!"));
        }
        try {
            lyric = LyricUtils.parseLyricFromInputStream(inputStream, charsetName);
            lyricsListAdapter.notifyDataSetChanged();
        } catch (LyricParseException e) {
            e.printStackTrace();
            setLoadLyricError(e);
        }
    }

    private void setLoadLyricError(LyricParseException e) {
        listLyric.setEmptyView(findViewById(R.id.empty_layout));
    }

    /**
     * @param millionSecond 歌词所对应的毫秒数
     */
    public void scrollLyricTo(int millionSecond) {
        millionSecond += offsetLyric;
        if (lyricsListAdapter != null) {
            final int currentLyricPosition = lyricsListAdapter.getCurrentLyricPosition();
            final int position = lyricsListAdapter.indexAt(
                    currentLyricPosition, millionSecond / 10);
            Log.i(TAG, "scrollLyricTo: " + position);
            if (position >= 0) {
                //刷新选中歌词显示状态
                lyricsListAdapter.notifyDataSetChanged();
//                        lyricsListAdapter.notifyItemChanged(currentLyricPosition);
//                        lyricsListAdapter.notifyItemChanged(position);

                int offset = (int) ((listLyric.getHeight()) / (2 * 1.4));
                listLyric.smoothScrollToPositionFromTop(position, offset);
            }
        }
    }

    /**
     * 歌词显示时间偏移
     *
     * @param offsetLyric 偏移量 毫秒
     */
    public void setOffsetLyric(int offsetLyric) {
        this.offsetLyric = offsetLyric;
    }

    /**
     * Set the default text size to the given value, interpreted as "scaled
     * pixel" units.  This size is adjusted based on the current density and
     * user font size preference.
     *
     * @param lyricTextSize The scaled pixel size.
     */
    public void setLyricTextSize(float lyricTextSize) {
        this.lyricTextSize = lyricTextSize;
        lyricsListAdapter.notifyDataSetChanged();
    }

    public void setLyricNormalTextColor(int lyricNormalTextColor) {
        this.lyricNormalTextColor = lyricNormalTextColor;
        lyricsListAdapter.notifyDataSetChanged();
    }

    public void setLyricPlayingTextColor(int lyricPlayingTextColor) {
        this.lyricPlayingTextColor = lyricPlayingTextColor;
        lyricsListAdapter.notifyDataSetChanged();
    }

    public void setLyricLineSpacing(float lyricLineSpacing) {
        this.lyricLineSpacing = lyricLineSpacing;
        lyricsListAdapter.notifyDataSetChanged();
    }


    private class LyricsListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return lyric == null ? 0 : lyric.size();
        }

        @Override
        public Object getItem(int position) {
            return lyric.getLyricLine(lyric.keyAt(position));
        }

        @Override
        public long getItemId(int position) {
            return lyric.keyAt(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lyric_line, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tvLyricLine = (TextView) convertView.findViewById(R.id.tv_lyric);
                viewHolder.flContainer = (FrameLayout) convertView.findViewById(R.id.fl_container);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //设置歌词颜色和大小
            if (currentLyricPosition == position) {
                viewHolder.tvLyricLine.setTextColor(lyricPlayingTextColor);
            } else {
                viewHolder.tvLyricLine.setTextColor(lyricNormalTextColor);
            }
            viewHolder.tvLyricLine.setTextSize(lyricTextSize);
            viewHolder.tvLyricLine.setText((String) getItem(position));
            return convertView;
        }

        /**
         * @param previousPosition 前一句歌词的索引
         * @param timeStamp        歌词的时间戳(百分之一秒)
         * @return 该时间戳所对应的歌词位置. -1表示未找到
         */
        int indexAt(int previousPosition, int timeStamp) {

            if (lyric == null) {
                return -1;
            }

            int position = previousPosition;

            if (position < 0 || position > lyric.size() - 1) {
                return 0;
            }

            if (lyric.keyAt(position) > timeStamp) {//需要向之前滚动
                while (lyric.keyAt(position) > timeStamp) {
                    position--;
                    if (position <= 0) {
                        position = 0;
                        break;
                    }
                }
            } else {//向后滚动
                while (lyric.keyAt(position) < timeStamp) {
                    position++;
                    if (position >= lyric.size() - 1) {
                        position = lyric.size() - 1;
                        break;
                    }
                }
                position = position > 0 ? position - 1 : 0;//back status
            }
            if (position == currentLyricPosition) {//do not need to notify to refresh  ui
                return -1;
            }
            currentLyricPosition = position;
            return position;
        }

        int getCurrentLyricPosition() {
            return currentLyricPosition;
        }

        private class ViewHolder {
            TextView tvLyricLine;
            FrameLayout flContainer;
        }
    }
}
