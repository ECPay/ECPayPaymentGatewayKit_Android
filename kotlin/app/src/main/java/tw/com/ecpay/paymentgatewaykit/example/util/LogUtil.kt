package tw.com.ecpay.paymentgatewaykit.example.util

import android.util.Log

class LogUtil {

    companion object {

        private var TAG: String = "ECPayPaymentGatewayKit_Example"

        /**
         * 是否在adb顯示除錯日誌.
         */
        fun isLog(): Boolean {
            return true
        }

        fun log(msg: String) {
            log(Log.DEBUG, TAG, msg)
        }

        fun log(tag: String, msg: String) {
            log(Log.DEBUG, tag, msg)
        }

        fun log(priority: Int, msg: String) {
            log(priority, TAG, msg)
        }

        fun log(priority: Int, tag: String, msg: String) {
            if (isLog()) {
                Log.println(priority, tag, msg)
            }
        }

        fun exceptionLog(ex: Exception) {
            exceptionLog(TAG, ex)
        }

        fun exceptionLog(tag: String, ex: Exception) {
            if (isLog()) {
                log(Log.ERROR, tag, Log.getStackTraceString(ex))
            }
        }
    }
}