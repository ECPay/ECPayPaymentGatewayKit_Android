package tw.com.ecpay.paymentgatewaykit.example.util;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class AlertUtil {

    private AlertUtil() {
    }

    public static void showAlertDialog(Context context,
                                       String title,
                                       String message,
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

    public static void showAlertDialog(Context context,
                                       String title,
                                       String message,
                                       DialogInterface.OnClickListener listenerRightButton,
                                       String rightButtonName) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setNegativeButton(rightButtonName, listenerRightButton);
        dialog.show();
    }
}
