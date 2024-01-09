package tw.com.ecpay.paymentgatewaykit.example.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class UIUtil {

    private UIUtil() {
    }

    /**
     * 建立 loading 提示 View.
     *
     * @param context context
     * @param message 提示文字
     * @return ProgressDialog物件
     */
    private static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    /**
     * 建立 loading 提示 View.
     *
     * @param context context
     * @return ProgressDialog物件
     */
    public static ProgressDialog createProgressDialog(Context context) {
        return createProgressDialog(context, "請稍待.");
    }

    public static void showAlertDialog(Context context, String title, String message,
                                       DialogInterface.OnClickListener listenerLeftButton,
                                       String leftButtonName,
                                       DialogInterface.OnClickListener listenerRightButton,
                                       String rightButtonName) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setNeutralButton(leftButtonName, listenerLeftButton);
        dialog.setNegativeButton(rightButtonName, listenerRightButton);
        dialog.show();
    }

    public static void showAlertDialog(Context context, String title, String message,
                                       DialogInterface.OnClickListener listenerRightButton,
                                       String rightButtonName) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setNegativeButton(rightButtonName, listenerRightButton);
        dialog.show();
    }
}
