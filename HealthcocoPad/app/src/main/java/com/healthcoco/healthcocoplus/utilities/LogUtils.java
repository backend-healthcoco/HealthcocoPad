package com.healthcoco.healthcocoplus.utilities;

import android.util.Log;

/**
 * Created by Shreshtha on 20-01-2017.
 */

public class LogUtils {
    /**
     * Priority constant for the println method; use LogUtils.e.
     */
    public static final int ERROR = Log.ERROR;
    private static final String LOG_PREFIX = "healthcoco_";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    /**
     * Don't use this when obfuscating class names!
     */
    public static String makeLogTag(Class<?> cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static void LOGD(final String tag, String message) {
        // noinspection PointlessBooleanExpression,ConstantConditions
        if (DevConfig.LOGGING_ENABLED) {
            Log.d(tag, message);
        }
    }

    public static void LOGD(final String tag, String message, Throwable cause) {
        // noinspection PointlessBooleanExpression,ConstantConditions
//		|| Log.isLoggable(tag, Log.DEBUG)
        if (DevConfig.LOGGING_ENABLED) {
            Log.d(tag, message, cause);
        }
    }

    public static void LOGV(final String tag, String message) {
        // noinspection PointlessBooleanExpression,ConstantConditions
        if (DevConfig.LOGGING_ENABLED && Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, message);
        }
    }

    public static void LOGV(final String tag, String message, Throwable cause) {
        // noinspection PointlessBooleanExpression,ConstantConditions
        if (DevConfig.LOGGING_ENABLED && Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, message, cause);
        }
    }

    public static void LOGI(final String tag, String message) {
        Log.i(tag, message);
    }

    public static void LOGI(final String tag, String message, Throwable cause) {
        Log.i(tag, message, cause);
    }

    public static void LOGW(final String tag, String message) {
        Log.w(tag, message);
    }

    public static void LOGW(final String tag, String message, Throwable cause) {
        Log.w(tag, message, cause);
    }

    public static void LOGE(final String tag, String message) {
        Log.e(tag, message);
    }

    public static void LOGE(final String tag, String message, Throwable cause) {
        Log.e(tag, message, cause);
    }

    private LogUtils() {
    }

    /**
     * @param tag    Used to identify the source of a log message.  It usually identifies
     *               the class or activity where the log call occurs.
     * @param format the format string (see {@link java.util.Formatter#format})
     * @param args   the list of arguments passed to the formatter. If there are
     *               more arguments than required by {@code format},
     *               additional arguments are ignored.
     */
    public static int LOGD(String tag, String format, Object... args) {
        if (DevConfig.LOGGING_ENABLED && Log.isLoggable(tag, Log.VERBOSE)) {
            return Log.d(tag, String.format(format, args));
        }
        return 0;
    }

    /**
     * Send a {@link #ERROR} log message.
     *
     * @param tag    Used to identify the source of a log message.  It usually identifies
     *               the class or activity where the log call occurs.
     * @param format the format string (see {@link java.util.Formatter#format})
     * @param args   the list of arguments passed to the formatter. If there are
     *               more arguments than required by {@code format},
     *               additional arguments are ignored.
     */
    public static int LOGE(String tag, String format, Object... args) {
        if (DevConfig.LOGGING_ENABLED && Log.isLoggable(tag, Log.VERBOSE)) {
            return Log.e(tag, String.format(format, args));
        }
        return 0;
    }

    /**
     * Send a {@link #ERROR} log message.
     *
     * @param tag    Used to identify the source of a log message.  It usually identifies
     *               the class or activity where the log call occurs.
     * @param tr     An exception to log
     * @param format the format string (see {@link java.util.Formatter#format})
     * @param args   the list of arguments passed to the formatter. If there are
     *               more arguments than required by {@code format},
     *               additional arguments are ignored.
     */
    public static int LOGE(String tag, Throwable tr, String format, Object... args) {
        if (DevConfig.LOGGING_ENABLED && Log.isLoggable(tag, Log.VERBOSE)) {
            return Log.e(tag, String.format(format, args), tr);
        }
        return 0;
    }
}
