package com.healthcoco.healthcocopad.utilities;

import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.widget.TextView;

import com.healthcoco.healthcocopad.bean.DOB;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.dialogFragment.AddEditClinicHoursDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.BookAppointmentDialogFragment;
import com.healthcoco.healthcocopad.fragments.CalendarFragment;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Shreshtha on 24-01-2017.
 */
public class DateTimeUtil {
    public static final String YEAR_FORMAT = "yyyy";
    public static final String DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH = "EEE, MMM dd,yyyy";
    public static final String DATE_TIME_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH = "EEE, MMM dd,yyyy hh:mm aaa";
    public static final String TIME_FORMAT_24_HOUR = "H:mm";
    public static final String DATE_FORMAT_DAY_MONTH_YEAR_SLASH = "dd/MM/yyyy";
    protected static final String TAG = CalendarEvents.class.getSimpleName();

    /**
     * in YY format for year
     *
     * @param date
     * @return
     */
    public static String getFormatedDateAndTime(Long date) {
        if (date != null)
            // give date time with 24hr format(use "dd/MM/yyyy HH:mm a" for 12 hr
            // format )
            return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(date));
        return null;
    }

    public static String getFormatedDateAndTime(String format, Long date) {
        if (date != null)
            // give date time with 24hr format(use "dd/MM/yyyy HH:mm a" for 12 hr
            // format )
            return new SimpleDateFormat(format, Locale.ENGLISH).format(new Date(date));
        return null;
    }

    public static String getFormattedDateTime(String format, long date) {
        return new SimpleDateFormat(format).format(new Date(date));
    }

    /**
     * @param timeInDayMonthYearFormat : String date in format "dd/MM/yyyy"
     * @return
     */
    public static DOB getDob(String timeInDayMonthYearFormat) {
        if (!Util.isNullOrBlank(timeInDayMonthYearFormat)) {
            long timeInMillis = getLongFromFormattedDayMonthYearFormatString(DATE_FORMAT_DAY_MONTH_YEAR_SLASH, timeInDayMonthYearFormat);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(timeInMillis));

            DOB dob = new DOB();
            dob.setDays(cal.get(Calendar.DAY_OF_MONTH));
            dob.setMonths(cal.get(Calendar.MONTH) + 1);
            dob.setYears(cal.get(Calendar.YEAR));
            return dob;
        }
        return null;
    }

    public static String getCurrentFormattedDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());
    }

    public static String getCurrentFormattedDate(String format) {
        return new SimpleDateFormat(format).format(new java.util.Date());
    }

    public static int getHoursFromMinutes(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, 0, minutes, 00);
        LogUtils.LOGD(TAG, "Hour of day " + calendar.get(Calendar.HOUR_OF_DAY));
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinutes(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, 0, minutes, 00);
        LogUtils.LOGD(TAG, "Minutes of day " + calendar.get(Calendar.MINUTE));
        return calendar.get(Calendar.MINUTE);
    }

    public static String getDateDifference(Calendar calendar) {
        String difference = getDateDiffString(calendar.getTimeInMillis(), new java.util.Date().getTime());
        LogUtils.LOGD(TAG, "Date difference " + difference);
        return difference;
    }

    public static String getFormattedTime(String format, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, hour, minute, 00);
        return (String) DateFormat.format(format, calendar.getTime());
    }

    public static String getFormattedTime(int hour, int minute) {
        String delegate = "hh:mm aaa";
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, hour, minute, 00);
        return (String) DateFormat.format(delegate, calendar.getTime());
    }

    /**
     * Returns a string that describes the number of days
     * between timeOne and timeTwo.
     */
    private static String getDateDiffString(long timeOne, long timeTwo) {
        long oneDay = 1000 * 60 * 60 * 24;
        int delta = (int) ((timeTwo - timeOne) / oneDay);
        String formattedAge = " ";
        if (delta >= 0) {
            int year = delta / 365;
            if (year > 0) {
                formattedAge = formattedAge + year + " Year";
                if (year > 14)
                    return formattedAge + "s";
//                if (year > 1)
//                    formattedAge = formattedAge + "s";

            }

            int rest = delta % 365;
            int month = rest / 30;
            if (month > 0) {
                if (!Util.isNullOrBlank(formattedAge))
                    formattedAge = formattedAge + " ";
                formattedAge = formattedAge + month + " Month";
                if (month > 1)
                    formattedAge = formattedAge + "s";
            }

            //return formatted age with only years ad months if age is beetween 1 year na 14 years
            if (year >= 1 && year < 14)
                return formattedAge;
            rest = rest % 30;
//            int weeks = rest / 7;
//            int days = rest % 7;

            //show days also if age is less than one year
            if (rest > 0) {
                if (!Util.isNullOrBlank(formattedAge))
                    formattedAge = formattedAge + " ";
                formattedAge = formattedAge + rest + " Day";
                if (rest > 1)
                    formattedAge = formattedAge + "s";
            } else {
                rest = 1;
                formattedAge = formattedAge + rest + " Day";
            }
            return formattedAge;
        } else {
            delta *= -1;
            return "";
        }
    }

    public static Calendar setCalendarDefaultvalue(String format, Calendar calendar, TextView textView) {
        Calendar cal = Calendar.getInstance();
        String displayedDate = Util.getValidatedValueOrNull(textView);
        //will set the time if displayedDate is no null
        //or elsewill return the current date in Calendar object
        if (!Util.isNullOrBlank(displayedDate)) {
            long timeInMillis = 0;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                timeInMillis = sdf.parse(displayedDate).getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
            cal.setTime(new Date(timeInMillis));
        }
        return cal;
    }

    public static Calendar setCalendarDefaultvalue(Calendar calendar, String timeInDayMonthYearFormat) {
        if (!Util.isNullOrBlank(timeInDayMonthYearFormat)) {
            long timeInMillis = getLongFromFormattedDayMonthYearFormatString(DATE_FORMAT_DAY_MONTH_YEAR_SLASH, timeInDayMonthYearFormat);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(timeInMillis));
            return cal;
        }
        return calendar;
    }

    public static long getLongFromFormattedDayMonthYearFormatString(String format, String displayedDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(displayedDate).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    public static String getBirthDateFormat(int dayOfMonth, int monthOfYear, int year) {
        return dayOfMonth + "/" + monthOfYear + "/" + year;
    }

    public static boolean isDifferenceGreaterThan15Minutes(String toTime, String fromTime) {
        if (!Util.isNullOrBlank(fromTime)) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("hh:mm aaa");
            try {
                java.util.Date toDate = originalFormat.parse(toTime);
                java.util.Date fromDate = originalFormat.parse(fromTime);
                long toDateTimeinMillis = toDate.getTime();
                long fromDateTimeInMills = fromDate.getTime();
                long difference = toDateTimeinMillis - fromDateTimeInMills;
                long differenceInMinutes = 0;
                if (difference > 0) {
                    differenceInMinutes = difference / DateUtils.MINUTE_IN_MILLIS;
                }
                if (differenceInMinutes < 15)
                    return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;

    }

    public static boolean selectedTimeIsGreaterThanTime(String toTime, String fromTime) {
        if (!Util.isNullOrBlank(fromTime)) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("hh:mm aaa");
            try {
                java.util.Date toDate = originalFormat.parse(toTime);
                java.util.Date fromDate = originalFormat.parse(fromTime);
                int comparision = toDate.compareTo(fromDate);
                if (comparision < 0 || comparision == 0)
                    return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;

    }

    public static boolean selectedTimeIsGreaterThanTime(long toTime, long fromTime) {
        if (fromTime > 0) {
            try {
                java.util.Date toDate = new Date(toTime);
                java.util.Date fromDate = new Date(fromTime);
                int comparision = toDate.compareTo(fromDate);
                if (comparision < 0)
                    return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * if fomrattedFrom time is null or blank wil return current calnedar time
     * else
     * will return calendar with default time as minute value  greater than fomattedTime by DEFAULT_TIME_INTERVAL value
     *
     * @param formattedFromTime
     * @return
     */
    public static Calendar getCalendarInstanceFromFormattedTime(String formattedFromTime, boolean isTextShown, int timeDifference) {
        Calendar calendar = Calendar.getInstance();
        if (!Util.isNullOrBlank(formattedFromTime)) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("hh:mm aaa");
            try {
                java.util.Date date = originalFormat.parse(formattedFromTime);
                System.out.println("Old Format :   " + originalFormat.format(date));
                calendar.setTime(date);

                int selectedHourFrom = calendar.get(Calendar.HOUR_OF_DAY);
                int selectedMinuteFrom = calendar.get(Calendar.MINUTE);
                if (!isTextShown) {
                    selectedMinuteFrom = selectedMinuteFrom + AddEditClinicHoursDialogFragment.DEFAULT_TIME_INTERVAL;
                    if (selectedMinuteFrom > 60) {
                        selectedHourFrom++;
                        selectedMinuteFrom = selectedMinuteFrom - 60;
                    }
                }
                calendar.set(Calendar.HOUR_OF_DAY, selectedHourFrom);
                calendar.set(Calendar.MINUTE, selectedMinuteFrom);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return calendar;
    }


    public static Calendar getCalendarInstanceFromFormattedTime(String format, String formattedFromTime, boolean isTextShown, int timeDifference) {
        Calendar calendar = getCalendarInstance();
        if (!Util.isNullOrBlank(formattedFromTime)) {
            SimpleDateFormat originalFormat = new SimpleDateFormat(format, Locale.ENGLISH);
            try {
                java.util.Date date = new java.util.Date(getLongFromFormattedDateTime(format, formattedFromTime));
                System.out.println("Old Format :   " + originalFormat.format(date));
                calendar.setTime(date);

                int selectedHourFrom = calendar.get(Calendar.HOUR_OF_DAY);
                int selectedMinuteFrom = calendar.get(Calendar.MINUTE);
                if (!isTextShown) {
                    selectedMinuteFrom = selectedMinuteFrom + BookAppointmentDialogFragment.DEFAULT_TIME_INTERVAL;
                    if (selectedMinuteFrom > 60) {
                        selectedHourFrom++;
                        selectedMinuteFrom = selectedMinuteFrom - 60;
                    }
                }
                calendar.set(Calendar.HOUR_OF_DAY, selectedHourFrom);
                calendar.set(Calendar.MINUTE, selectedMinuteFrom);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return calendar;
    }

    public static Float getMinutesFromFormattedTime(String formattedTime) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("hh:mm aaa");
            try {
                float totalMinutes = 0;
                java.util.Date date = originalFormat.parse(formattedTime);
                System.out.println("Old Format :   " + originalFormat.format(date));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                int seconds = calendar.get(Calendar.SECOND);
                totalMinutes = (hours * 60) + minutes + (seconds / 60);
                return totalMinutes;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Float getMinutesFromFormattedTime(long time) {
        float totalMinutes = 0;
        Calendar calendar = getCalendarInstance();
        calendar.setTimeInMillis(time);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        totalMinutes = (hours * 60) + minutes + (seconds / 60);
        return totalMinutes;
    }

    public static long getMaxDate(long time) {
        Calendar maxDate = Calendar.getInstance();
        maxDate.setTimeInMillis(time);
        maxDate.set(Calendar.HOUR_OF_DAY, 23);
        maxDate.set(Calendar.MINUTE, 59);
        maxDate.set(Calendar.SECOND, 59);
        return maxDate.getTimeInMillis();
    }

    public static long getDifferenceInSecondsFromCurrent(long receivedTime) {
        long currentTime = new java.util.Date().getTime();
        long diffInMs = currentTime - receivedTime;

        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
        return diffInSec;
    }

    public static String getFormatedDate(Long date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(date));
    }

    public static Long getCurrentDateLong() {
        long currentDateTime = new java.util.Date().getTime();
        return currentDateTime;
    }

    public static Long getCurrentDateLong(String format) {
        long currentDateTime = new java.util.Date().getTime();
        String formatteddate = new SimpleDateFormat(format, Locale.ENGLISH).format(new Date(currentDateTime));
        return getLongFromFormattedDateTime(format, formatteddate);
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateTime = sdf.format(c.getTime());
        return currentDateTime;
    }

    public static Calendar getCalendarInstance() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        return calendar;
    }


    public static String getFormattedDate(String format, int year, int monthOfYear, int dayOfMonth, int hour, int minute, int seconds) {
        Calendar calendar = getCalendarInstance();
        if (year == 0)
            year = calendar.get(Calendar.YEAR);
        if (monthOfYear == 0)
            monthOfYear = calendar.get(Calendar.MONTH);
        if (dayOfMonth == 0)
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        if (hour == 0)
            hour = calendar.get(Calendar.HOUR);
        if (minute == 0)
            minute = calendar.get(Calendar.MINUTE);
        if (seconds == 0)
            seconds = calendar.get(Calendar.SECOND);
        calendar.set(year, monthOfYear, dayOfMonth, hour, minute, seconds);
        return (String) DateFormat.format(format, calendar.getTime());
    }

    public static List<Long> getFormattedDatesBetween(Long fromMilli, Long toMilli) {
        List<Long> dates = new ArrayList<>();
        Date fromDate = new Date(fromMilli);
        Date toDate = new Date(toMilli);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        LogUtils.LOGD(TAG, "getConvertedLong foratted dates cal.getTime().getTime() " + getFormattedDateTime(DATE_TIME_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, cal.getTime().getTime()));
        LogUtils.LOGD(TAG, "getConvertedLong foratted dates toMilli " + getFormattedDateTime(DATE_TIME_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, toMilli));
        LogUtils.LOGD(TAG, "getConvertedLong foratted dates fromMilli " + getFormattedDateTime(DATE_TIME_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, fromMilli));

        long diff = Math.abs(toMilli - fromMilli);
        int totalNumberOfDaysBetween = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        LogUtils.LOGD(TAG, "getConvertedLong totalNumberOfDaysBetween " + totalNumberOfDaysBetween);
        LogUtils.LOGD(TAG, "getConvertedLong Days: " + totalNumberOfDaysBetween);
        while ((cal.getTime().before(toDate) || cal.getTime().equals(toDate)) && totalNumberOfDaysBetween > 0 && totalNumberOfDaysBetween < CalendarFragment.MAX_NUMBER_OF_EVENTS) {
            LogUtils.LOGD(TAG, "getConvertedLong Date between " + cal.getTime().getTime() + " Formatted value " + getFormattedDateTime(DATE_TIME_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, cal.getTime().getTime()));
            dates.add(cal.getTime().getTime());
            cal.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static Float getMinutesFromFormattedDateime(String format, String formattedTime) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat(format);
            try {
                float totalMinutes = 0;
                java.util.Date date = originalFormat.parse(formattedTime);
                System.out.println("Old Format :   " + originalFormat.format(date));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                int seconds = calendar.get(Calendar.SECOND);
                totalMinutes = (hours * 60) + minutes + (seconds / 60);
                return totalMinutes;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFormattedTime(String format, int year, int monthOfYear, int dayOfMonth, int hour, int minute, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth, hour, minute, seconds);
        return (String) DateFormat.format(format, calendar.getTime());
    }

    public static String convertFormattedDate(String currentFormat, String newFormat, String displayedDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(currentFormat);
            long timeInMilliseconds = sdf.parse(displayedDate).getTime();
            return new SimpleDateFormat(newFormat).format(new Date(timeInMilliseconds));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return displayedDate;
    }

    public static long getNextDate(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTimeInMillis();
    }

    public static long getDateAfterFiveDays(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.add(Calendar.DATE, 5);
        return calendar.getTimeInMillis();
    }

    public static String getFormttedTime(long date) {
        String delegate = "hh:mm aaa";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return (String) DateFormat.format(delegate, calendar.getTime());

    }

    public static String getFormttedTimeInMin(long date) {
        String delegate = "hh:mm:ss";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return (String) DateFormat.format(delegate, calendar.getTime());

    }

    public static long getPreviousDate(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTimeInMillis();
    }

    public static boolean isCurrentDateSelected(String format, String selectedTime) {
        if (getFormattedDateTime(format, getCurrentDateLong()).equalsIgnoreCase(selectedTime))
            return true;
        return false;
    }

    public static long getLongFromFormattedDateTime(String format, String formattedDate) {
        try {
            if (!Util.isNullOrBlank(formattedDate)) {
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
                sdf.setTimeZone(TimeZone.getDefault());
                long timeInMillis = sdf.parse(formattedDate).getTime();
                Calendar calendar = getCalendarInstance();
                calendar.setTimeInMillis(timeInMillis);
                return calendar.getTimeInMillis();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (formattedDate.contains("PM") || formattedDate.contains("pm") || formattedDate.contains("AM") || formattedDate.contains("am")) {
                formattedDate = formattedDate.replace("PM", "p.m.");
                formattedDate = formattedDate.replace("pm", "p.m.");
                formattedDate = formattedDate.replace("AM", "a.m.");
                formattedDate = formattedDate.replace("am", "a.m.");
                getLongFromFormattedDateTime(format, formattedDate);
            }
        }
        return 0;
    }

    public static Long getFirstDayOfMonthMilli(long milliseconds) {
        Date today = new Date(milliseconds);
        Calendar calendar = getCalendarInstance();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        LogUtils.LOGD(TAG, "Milliseconds FirstDay of Month  " + calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
        LogUtils.LOGD(TAG, "Milliseconds FirstDay " + calendar.getTimeInMillis());
        return calendar.getTimeInMillis();
    }

    public static Long getLastDayOfMonthMilli(long milliseconds) {
        Date today = new Date(milliseconds);
        Calendar calendar = getCalendarInstance();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        LogUtils.LOGD(TAG, "Milliseconds LastDay of Month  " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        LogUtils.LOGD(TAG, "Milliseconds Last Day " + calendar.getTimeInMillis());
        return calendar.getTimeInMillis();
    }

    public static Long getStartTimeOfDayMilli(long milliseconds) {
        Date today = new Date(milliseconds);
        Calendar cal = getCalendarInstance();
        cal.setTime(today);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        LogUtils.LOGD(TAG, "Milliseconds Start Time Of Day " + cal.getTimeInMillis());
        return cal.getTimeInMillis();
    }

    public static Long getEndTimeOfDayMilli(long milliseconds) {
        Date today = new Date(milliseconds);
        Calendar cal = getCalendarInstance();
        cal.setTime(today);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        LogUtils.LOGD(TAG, "Milliseconds Last Day " + cal.getTimeInMillis());
        return cal.getTimeInMillis();
    }

    public static boolean isCurrentMonthSelected(long curentMonthDayYearInMillis, long selectedMonthDayYearInMillis) {
        Calendar cal = getCalendarInstance();
        cal.setTimeInMillis(curentMonthDayYearInMillis);
        int currentMonth = cal.get(Calendar.MONTH);
        cal.setTimeInMillis(selectedMonthDayYearInMillis);
        int selectedMonth = cal.get(Calendar.MONTH);

        if (currentMonth == selectedMonth)
            return true;
        else
            return false;
    }

    public static long getSelectedDate(int year, int monthOfYear, int dayOfMonth, int hour, int minute, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth, hour, minute, seconds);
        return calendar.getTimeInMillis();
    }


    /**
     * Checks if two times are on the same day.
     *
     * @param dayOne The first day.
     * @param dayTwo The second day.
     * @return Whether the times are on the same day.
     */
    public static boolean isSameDay(Calendar dayOne, Calendar dayTwo) {
        return dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR) && dayOne.get(Calendar.DAY_OF_YEAR) == dayTwo.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Returns a calendar instance at the start of this day
     *
     * @return the calendar instance
     */
    public static Calendar today() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today;
    }

}
