package tw.com.ecpay.paymentgatewaykit.example.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

class AlertUtil {

    companion object {

        fun showAlertDialog(
            context: Context?,
            title: String?,
            message: String?,
            listenerRightButton: DialogInterface.OnClickListener?,
            rightButtonName: String?
        ) {
            val dialog: AlertDialog.Builder = AlertDialog.Builder(context)
            dialog.setTitle(title)
            dialog.setMessage(message)
            dialog.setNegativeButton(rightButtonName, listenerRightButton)
            dialog.show()
        }
    }
}