package tw.com.ecpay.paymentgatewaykit.example.util

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface

class UIUtil {

    companion object {

        fun createProgressDialog(
            context: Context,
            message: String
        ): ProgressDialog? {
            val progressDialog = ProgressDialog(context)
            progressDialog.setMessage(message)
            progressDialog.setCancelable(false)
            return progressDialog
        }

        fun createProgressDialog(
            context: Context
        ): ProgressDialog? {
            return createProgressDialog(context, "請稍待.")
        }

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