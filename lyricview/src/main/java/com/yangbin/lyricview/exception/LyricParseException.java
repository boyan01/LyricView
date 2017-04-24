package com.yangbin.lyricview.exception;

/**
 * <pre>
 *     author : YangBin
 *     e-mail : yangbinyhbn@gmail.com
 *     time   : 2017/4/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class LyricParseException extends Exception {
    public LyricParseException() {
    }

    public LyricParseException(String message) {
        super(message);
    }

    public LyricParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
