package com.healthcoco.healthcocoplus.utilities;

import android.text.format.DateFormat;
import android.text.format.DateUtils;

import com.healthcoco.healthcocoplus.bean.server.CalendarEvents;
import com.healthcoco.healthcocoplus.bean.server.DOB;
import com.healthcoco.healthcocoplus.dialogFragment.AddEditClinicHoursDialogFragment;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Shreshtha on 24-01-2017.
 */
public class DateTimeUtil {
    protected static final String TAG = CalendarEvents.class.getSimpleName();
    private static final String DATE_FORMAT_DAY_MONTH_YEAR_SLASH = "dd/MM/yyyy";
    public static final String YEAR_FORMAT = "yyyy";

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

    public static long getMaxDate(long time) {
        Calendar maxDate = Calendar.getInstance();
        maxDate.setTimeInMillis(time);
        maxDate.set(Calendar.HOUR_OF_DAY, 23);
        maxDate.set(Calendar.MINUTE, 59);
        maxDate.set(Calendar.SECOND, 59);
        return maxDate.getTimeInMillis();
    }
}
