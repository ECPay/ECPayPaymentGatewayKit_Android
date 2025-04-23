package tw.com.ecpay.paymentgatewaykit.example.util;

import android.util.Log;

public class LogUtil {

    private static final String TAG = "ECPayPaymentGatewayKit_Example";

    private LogUtil() {
    }

    /**
     * 是否在adb顯示除錯日誌.
     *
     * @return 回傳顯示 true 或不顯示 false
     */
    public static boolean isLog() {
        return true;
    }

    /**
     * 輸入除錯資訊.
     * <br> 預設 debug 層級
     *
     * @param msg 除錯訊息字串
     */
    public static void log(String msg) {
        log(Log.DEBUG, TAG, msg);
    }

    /**
     * 輸入除錯資訊.
     *
     * @param tag tag名稱
     * @param msg 除錯訊息字串
     */
    public static void log(String tag, String msg) {
        log(Log.DEBUG, tag, msg);
    }

    /**
     * 輸入除錯資訊.
     *
     * @param priority 顯示層級
     * @param msg      除錯訊息字串
     */
    public static void log(int priority, String msg) {
        log(priority, TAG, msg);
    }

    /**
     * 輸入除錯資訊.
     *
     * @param priority 顯示層級
     * @param tag      tag名稱
     * @param msg      除錯訊息字串
     */
    public static void log(int priority, String tag, String msg) {
        if (isLog()) {
            Log.println(priority, tag, msg);
        }
    }

    /**
     * 輸入exception的資訊.
     *
     * @param ex exception的物件
     */
    public static void exceptionLog(Exception ex) {
        exceptionLog(TAG, ex);
    }

    /**
     * 輸入exception的資訊.
     * <br> 預設 debug 層級
     *
     * @param tag tag名稱
     * @param ex  exception的物件
     */
    public static void exceptionLog(String tag, Exception ex) {
        if (isLog()) {
            log(Log.ERROR, tag, Log.getStackTraceString(ex));
        }
    }
}
