package cnpm.doan.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DatetimeUtils {
    public static String YYYYMMDD = "yyyy/MM/dd";
    public static String DDMMYYYYHHmmss = "yyyy-MM-dd HH:mm:ss";

    public static Date convertStringToDateOrNull(String date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setLenient(false);
        try {
            return simpleDateFormat.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DDMMYYYYHHmmss);
        simpleDateFormat.setLenient(false);

        System.out.println(date);
        return simpleDateFormat.format(date);
    }
    public static long getDayBetweenTwoDiffDate(Date date1, Date date2) {
        long diffInMillies = Math.abs(date2.getTime() - date1.getTime());
        if (diffInMillies == 0) {
            return 0;
        }
        System.out.println("diff" + TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS));
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
