package cn.sjjy.edu.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
public final class Utils {
    public final static BigDecimal HUNDRED = new BigDecimal(100);
    public final static BigDecimal THOUSAND = new BigDecimal(1000);
    public final static long ONE_DAY_MILLIS = 1000 * 60 * 60 * 24;

    public static List<Timestamp> getEachDays(Timestamp fromDay, Timestamp toDay) {
        if (null == fromDay || null == toDay) {
            return Collections.emptyList();
        }
        if (fromDay.getTime() > toDay.getTime()) {
            return Collections.emptyList();
        }
        List<Timestamp> result = new ArrayList<>();
        Timestamp begin = getBeginOfDay(fromDay);
        Timestamp end = getBeginOfDay(toDay);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(begin);
        Timestamp temp = new Timestamp(calendar.getTimeInMillis());
        while (temp.before(end) || temp.getTime() == end.getTime()) {
            result.add(temp);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            temp = new Timestamp(calendar.getTimeInMillis());
        }
        return result;
    }

    public static List<Timestamp> getEachHours(Timestamp fromTime, Timestamp toTime) {
        if (null == fromTime || null == toTime) {
            return Collections.emptyList();
        }
        if (fromTime.getTime() > toTime.getTime()) {
            return Collections.emptyList();
        }
        List<Timestamp> result = new ArrayList<>();
        Timestamp begin = getBeginOfHour(fromTime);
        Timestamp end = getBeginOfHour(toTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(begin);
        Timestamp temp = new Timestamp(calendar.getTimeInMillis());
        while (temp.before(end) || temp.getTime() == end.getTime()) {
            result.add(temp);
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            temp = new Timestamp(calendar.getTimeInMillis());
        }
        return result;
    }

    public static Timestamp getNow() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp getEndOfDay(Timestamp day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return new Timestamp(cal.getTime().getTime());
    }

    public static Timestamp getBeginOfDay(Timestamp day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTime().getTime());
    }

    public static Timestamp getBeginOfHour(Timestamp time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTime().getTime());
    }

    public static Timestamp getToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTime().getTime());
    }

    public static Timestamp getSomeDayAfter(Timestamp now, int i) {
        return new Timestamp(now.getTime() + i * 24 * 60 * 60 * 1000l);
    }

    public static Timestamp getSomeDayAgo(Timestamp now, int i) {
        return new Timestamp(now.getTime() - i * 24 * 60 * 60 * 1000l);
    }

    public static BigDecimal fromFenToYuan(final Long fen) {
        if (null == fen) return BigDecimal.ZERO;
        return new BigDecimal(fen).divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    public static Long fromYuanToFen(final BigDecimal yuan) {
        if (null == yuan) return 0l;
        return yuan.multiply(HUNDRED).longValue();
    }

    /**
     * 判断网址是否有效<br>
     *
     * @param
     * @return
     */
    public static boolean isReachable(String urlString) {

        boolean reachable = false;
        HttpURLConnection httpconn = null;
        HttpsURLConnection httpsconn = null;
        int code = 0;
        try {
            URL url = new URL(urlString);
            if (url.getProtocol().equals("https")) {
                httpsconn = (HttpsURLConnection) url.openConnection();
                code = httpsconn.getResponseCode();
            } else {
                httpconn = (HttpURLConnection) url.openConnection();
                code = httpconn.getResponseCode();
            }
            if (code != 404 && code < 500) {
                reachable = true;
            }
        } catch (Exception e) {
            reachable = false;
        }
        return reachable;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static boolean isStringArrayList(String stringArrayList) {
        Pattern p = Pattern.compile("\\[\\d+(,\\d+)*\\]");
        Matcher m = p.matcher(stringArrayList == null ? "" : stringArrayList);
        return m.matches();
    }
}
