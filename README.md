## 若您有興趣了解站內付2.0的服務，請前往以下網址並填寫資料，我們將派專人與您聯繫
## https://member.ecpay.com.tw/MemberReg/MerchantRegister

# 站內付2.0_Android版
[![Maven Central](https://img.shields.io/maven-central/v/tw.com.ecpay/ECPayPaymentGatewayKit.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22tw.com.ecpay%22%20AND%20a:%22ECPayPaymentGatewayKit%22)


## About

此套件提供手機端的全功能付款功能。

- 整合商家信用卡金流服務
- 消費者直接輸入信用卡號並且付款
- 支援信用卡端 3D驗證流程
- 支援非信用卡付款方式

## Requirements

- Android Gradle Plugin `3.6.3`
- minSdkVersion `21` (Android 5.0)
- targetSdkVersion `30` (Android 11.0)

##  Installation

ECPay Payment SDK公開於[Maven Central](https://search.maven.org/)來源庫<br/>

請於````app/build.gradle````檔案加上dataBinding設定

````gradle
android {
    dataBinding {
        enabled = true
    }
}
````
````app/build.gradle````檔案新增dependencies

````gradle
dependencies {
    // ECPay Payment SDK
    implementation 'tw.com.ecpay:ECPayPaymentGatewayKit:1.2.0'
}
````
````project/build.gradle````檔案加入[Maven Central](https://search.maven.org/)

````gradle
buildscript {
    repositories {
        mavenCentral()
    }
}
````
## Usage

### Import
````java
import tw.com.ecpay.paymentgatewaykit.manager.*
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


* ### CreatePayment
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
                            sb.append("PlatformID:");
                            sb.append("\r\n");
                            sb.append(callbackData.getPlatformID());
                            sb.append("\r\n");
                            sb.append("MerchantID:");
                            sb.append("\r\n");
                            sb.append(callbackData.getMerchantID());
                            sb.append("\r\n");
                            sb.append("CustomField:");
                            sb.append("\r\n");
                            sb.append(callbackData.getCustomField());
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
                            sb.append("\r\n");
                            sb.append("OrderInfo.TradeAmt");
                            sb.append("\r\n");
                            sb.append(callbackData.getOrderInfo().getTradeAmt());
                            sb.append("\r\n");
                            sb.append("OrderInfo.PaymentType");
                            sb.append("\r\n");
                            sb.append(callbackData.getOrderInfo().getPaymentType());
                            sb.append("\r\n");
                            sb.append("OrderInfo.ChargeFee");
                            sb.append("\r\n");
                            sb.append(callbackData.getOrderInfo().getChargeFee());
                            sb.append("\r\n");
                            sb.append("OrderInfo.TradeStatus");
                            sb.append("\r\n");
                            sb.append(callbackData.getOrderInfo().getTradeStatus());

                            if(callbackData.getPaymentType() == PaymentType.CreditCard ||
                                    callbackData.getPaymentType() == PaymentType.CreditInstallment ||
                                    callbackData.getPaymentType() == PaymentType.PeriodicFixedAmount ||
                                    callbackData.getPaymentType() == PaymentType.NationalTravelCard ||
                                    callbackData.getPaymentType() == PaymentType.UnionPay ||
                                    callbackData.getPaymentType() == PaymentType.FlexibleInstallment) {
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
                            if(callbackData.getPaymentType() == PaymentType.CreditInstallment ||
                                    callbackData.getPaymentType() == PaymentType.FlexibleInstallment) {
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
                            if(callbackData.getPaymentType() == PaymentType.PeriodicFixedAmount) {
                                sb.append("\r\n");
                                sb.append("CardInfo.PeriodType");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getPeriodType());
                                sb.append("\r\n");
                                sb.append("CardInfo.Frequency");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getFrequency());
                                sb.append("\r\n");
                                sb.append("CardInfo.ExecTimes");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getExecTimes());
                                sb.append("\r\n");
                                sb.append("CardInfo.PeriodAmount");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getPeriodAmount());
                                sb.append("\r\n");
                                sb.append("CardInfo.TotalSuccessTimes");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getTotalSuccessTimes());
                                sb.append("\r\n");
                                sb.append("CardInfo.TotalSuccessAmount");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getTotalSuccessAmount());
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
                    case Exit:
                        UIUtil.showAlertDialog(mActivity, "提醒您", "離開", new DialogInterface.OnClickListener() {
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
* Exit：離開
* Unknown：未知
````



## Contact

綠界科技 技術客服信箱：techsupport@ecpay.com.tw

## License

Copyright © 1996-2021 Green World FinTech Service Co., Ltd. All rights reserved. 
