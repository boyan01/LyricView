package com.yangbin.lyricview.utils;

import android.support.annotation.Nullable;

import com.yangbin.lyricview.bean.Lyric;
import com.yangbin.lyricview.exception.LyricParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     author : YangBin
 *     e-mail : yangbinyhbn@gmail.com
 *     time   : 2017/4/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class LyricUtils {
    private LyricUtils() {
    }

    /**
     * @param lyricFile   the lyric file
     * @param charsetName The name of a supported
     *                    {@link java.nio.charset.Charset </code>charset<code>}
     * @throws LyricParseException
     */
    public static Lyric parseLyricFromFile(File lyricFile, @Nullable String charsetName) throws LyricParseException {
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(lyricFile);
        } catch (FileNotFoundException e) {
            throw new LyricParseException("open file failed", e);
        }

        return parseLyricFromInputStream(inputStream, charsetName);
    }

    public static Lyric parseLyricFromInputStream(InputStream inputStream, @Nullable String charsetName) throws LyricParseException {
        return new LyricParser(charsetName).parse(inputStream);
    }


    private static class LyricParser {
        private static final String CHARSET_DEFAULT = "utf-8";

        LyricParser(String charsetName) {
            this.lyric = new Lyric();
            this.encoding = charsetName == null ? CHARSET_DEFAULT : charsetName;
        }


        /**
         * file text encoding
         */
        private String encoding;

        private Lyric lyric;

        Lyric parse(InputStream inputStream) throws LyricParseException {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, encoding);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String line;
                while ((line = reader.readLine()) != null) {
                    parseLine(line);
                }
            } catch (IOException e) {
                throw new LyricParseException("failed to read file", e);
            }


            return lyric;
        }

        private void parseLine(String line) {
            if (line.startsWith("[ti:")) {
                lyric.setTitle(line.substring(4, line.length() - 1));
            } else if (line.startsWith("[ar:")) {
                lyric.setArtist(line.substring(4, line.length() - 1));
            } else if (line.startsWith("[al:")) {
                lyric.setAlbum(line.substring(4, line.length() - 1));
            } else if (line.startsWith("[au:")) {
                lyric.setLyricist(line.substring(4, line.length() - 1));
            } else if (line.startsWith("[by:")) {
                lyric.setFileMaker(line.substring(4, line.length() - 1));
            } else {
                String regex = "\\[\\d\\d:\\d\\d.\\d\\d\\]";

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String timeMatcher = matcher.group();

                    //获取时间戳
                    int timeStamp = stamp2Int(timeMatcher.substring(1, timeMatcher.length() - 1));
                    //获取内容
                    String[] contents = pattern.split(line);

                    if (contents.length >= 1) {
                        lyric.putLyricLine(timeStamp, contents[contents.length - 1]);
                    }
                }
            }
        }

        private int stamp2Int(String result) {
            String[] results = result.split(":");
            int mm = Integer.parseInt(results[0]);

            String[] ssxx = results[1].split("\\.");
            int ss = Integer.parseInt(ssxx[0]);
            int xx = Integer.parseInt(ssxx[1]);

            return ((((mm * 60) + ss) * 100) + xx);
        }
    }

}
