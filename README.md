# 站內付2.0_Android版


## About

此套件提供手機端的全功能付款功能。

- 整合商家信用卡金流服務
- 消費者直接輸入信用卡號並且付款
- 支援信用卡端 3D驗證流程
- 支援非信用卡付款方式

##  Installation

ECPay Payment SDK公開於[JCenter](https://bintray.com/bintray/jcenter)來源庫<br/>
使用SDK需在````app/build.gradle````檔案新增dependency
````gradle
dependencies {
    // ECPay Payment SDK
    implementation 'tw.com.ecpay.paymentkit:ECPayPaymentGatewayKit:1.0.0'
}
````
請於````project/build.gradle````檔案加入[JCenter](https://bintray.com/bintray/jcenter)

````gradle
buildscript {
    repositories {
        jcenter()
    }
}
````
## Usage

### Import
````java
import tw.com.ecpay.paymentkit.manager.*
````

* ### Initialize

初始化SDK, 帶入Activity與環境參數

````
PaymentkitManager.initialize(mActivity, serverType);
````
#### 環境參數
````
SDK 涵蓋以下環境
* Stage：提供廠商串接測試
* Prod：正式環境
````


* ### CreditPayment
產生信用卡刷卡功能
````
PaymentkitManager.createPayment(mActivity, mFragment,
        mModel.token.get(), languageCode, useResultPage,
        mExampleData.getAppStoreName(), PaymentkitManager.RequestCode_CreatePayment);
````
非同步 onActivityResult
````java
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    Utility.log("onActivityResult(), requestCode:" + requestCode + ", resultCode:" + resultCode);
    if(requestCode == PaymentkitManager.RequestCode_CreatePayment) {
        PaymentkitManager.createPaymentResult(mActivity, resultCode, data, new CallbackFunction<CreatePaymentCallbackData>() {
            @Override
            public void callback(CreatePaymentCallbackData callbackData) {
                switch (callbackData.getCallbackStatus()) {
                    case Success:
                        if(callbackData.getRtnCode() == 1) {
                            StringBuffer sb = new StringBuffer();
                            sb.append("PaymentType:");
                            sb.append("\r\n");
                            sb.append(getPaymentTypeName(callbackData.getPaymentType()));
                            sb.append("\r\n");
                            sb.append("\r\n");
                            sb.append("OrderInfo.MerchantTradeNo");
                            sb.append("\r\n");
                            sb.append(callbackData.getOrderInfo().getMerchantTradeNo());
                            sb.append("\r\n");
                            sb.append("OrderInfo.TradeDate");
                            sb.append("\r\n");
                            sb.append(callbackData.getOrderInfo().getTradeDate());
                            sb.append("\r\n");
                            sb.append("OrderInfo.TradeNo");
                            sb.append("\r\n");
                            sb.append(callbackData.getOrderInfo().getTradeNo());

                            if(callbackData.getPaymentType() == PaymentType.CreditCard ||
                                    callbackData.getPaymentType() == PaymentType.CreditInstallment ||
                                    callbackData.getPaymentType() == PaymentType.PeriodicFixedAmount ||
                                    callbackData.getPaymentType() == PaymentType.NationalTravelCard) {
                                sb.append("\r\n");
                                sb.append("\r\n");
                                sb.append("CardInfo.AuthCode");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getAuthCode());
                                sb.append("\r\n");
                                sb.append("CardInfo.Gwsr");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getGwsr());
                                sb.append("\r\n");
                                sb.append("CardInfo.ProcessDate");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getProcessDate());
                                sb.append("\r\n");
                                sb.append("CardInfo.Amount");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getAmount());
                                sb.append("\r\n");
                                sb.append("CardInfo.Eci");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getEci());
                                sb.append("\r\n");
                                sb.append("CardInfo.Card6No");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getCard6No());
                                sb.append("\r\n");
                                sb.append("CardInfo.Card4No");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getCard4No());
                            }
                            if(callbackData.getPaymentType() == PaymentType.CreditCard) {
                                sb.append("\r\n");
                                sb.append("CardInfo.RedDan");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getRedDan());
                                sb.append("\r\n");
                                sb.append("CardInfo.RedDeAmt");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getRedDeAmt());
                                sb.append("\r\n");
                                sb.append("CardInfo.RedOkAmt");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getRedOkAmt());
                                sb.append("\r\n");
                                sb.append("CardInfo.RedYet");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getRedYet());
                            }
                            if(callbackData.getPaymentType() == PaymentType.CreditInstallment) {
                                sb.append("\r\n");
                                sb.append("CardInfo.Stage");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getStage());
                                sb.append("\r\n");
                                sb.append("CardInfo.Stast");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getStast());
                                sb.append("\r\n");
                                sb.append("CardInfo.Staed");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getStaed());
                            }

                            if(callbackData.getPaymentType() == PaymentType.ATM) {
                                sb.append("\r\n");
                                sb.append("\r\n");
                                sb.append("ATMInfo.BankCode");
                                sb.append("\r\n");
                                sb.append(callbackData.getAtmInfo().getBankCode());
                                sb.append("\r\n");
                                sb.append("ATMInfo.vAccount");
                                sb.append("\r\n");
                                sb.append(callbackData.getAtmInfo().getvAccount());
                                sb.append("\r\n");
                                sb.append("ATMInfo.ExpireDate");
                                sb.append("\r\n");
                                sb.append(callbackData.getAtmInfo().getExpireDate());
                            }
                            if(callbackData.getPaymentType() == PaymentType.CVS) {
                                sb.append("\r\n");
                                sb.append("\r\n");
                                sb.append("CVSInfo.PaymentNo");
                                sb.append("\r\n");
                                sb.append(callbackData.getCvsInfo().getPaymentNo());
                                sb.append("\r\n");
                                sb.append("CVSInfo.ExpireDate");
                                sb.append("\r\n");
                                sb.append(callbackData.getCvsInfo().getExpireDate());
                                sb.append("\r\n");
                                sb.append("CVSInfo.PaymentURL");
                                sb.append("\r\n");
                                sb.append(callbackData.getCvsInfo().getPaymentURL());
                            }
                            if(callbackData.getPaymentType() == PaymentType.Barcode) {
                                sb.append("\r\n");
                                sb.append("\r\n");
                                sb.append("BarcodeInfo.ExpireDate");
                                sb.append("\r\n");
                                sb.append(callbackData.getBarcodeInfo().getExpireDate());
                                sb.append("\r\n");
                                sb.append("BarcodeInfo.Barcode1");
                                sb.append("\r\n");
                                sb.append(callbackData.getBarcodeInfo().getBarcode1());
                                sb.append("\r\n");
                                sb.append("BarcodeInfo.Barcode2");
                                sb.append("\r\n");
                                sb.append(callbackData.getBarcodeInfo().getBarcode2());
                                sb.append("\r\n");
                                sb.append("BarcodeInfo.Barcode3");
                                sb.append("\r\n");
                                sb.append(callbackData.getBarcodeInfo().getBarcode3());
                            }

                            UIUtil.showAlertDialog(mActivity, "提醒您", sb.toString(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }, "確定");
                        } else {
                            StringBuffer sb = new StringBuffer();
                            sb.append(callbackData.getRtnCode());
                            sb.append("\r\n");
                            sb.append(callbackData.getRtnMsg());

                            UIUtil.showAlertDialog(mActivity, "提醒您", sb.toString(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }, "確定");
                        }
                        break;
                    case Fail:
                        UIUtil.showAlertDialog(mActivity, "提醒您", "Fail Code=" + callbackData.getRtnCode() + ", Msg=" + callbackData.getRtnMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }, "確定");
                        break;
                    case Cancel:
                        UIUtil.showAlertDialog(mActivity, "提醒您", "交易取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }, "確定");
                        break;
                }
            }
        });
    } else if(requestCode == PaymentkitManager.RequestCode_GooglePay) {
        PaymentkitManager.googlePayResult(mActivity, resultCode, data);
    }
}
````
#### callback 狀態
````
callback 包含以下狀態
* Fail：執行失敗
* Success：執行成功
* Cancel：取消
* Unknown：未知
````



## Contact

綠界科技 技術客服信箱：techsupport@ecpay.com.tw

## License

Copyright © 1996-2021 Green World FinTech Service Co., Ltd. All rights reserved. 
