package com.telrreactsdk;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.gson.Gson;
import com.ulisfintech.telrpay.helper.PaymentData;
import com.ulisfintech.telrpay.helper.SyncMessage;
import com.ulisfintech.telrpay.ui.Gateway;
import com.ulisfintech.telrpay.ui.GatewaySecureCallback;

import org.json.JSONObject;


public class PaymentModule extends ReactContextBaseJavaModule implements ActivityEventListener, GatewaySecureCallback {

    public static final String MAP_KEY_ERROR_CODE = "code";
    public static final String MAP_KEY_ERROR_DESC = "description";
    ReactApplicationContext reactContext;

    public PaymentModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return "RNPaymentCheckout";
    }

    @ReactMethod
    public void open(ReadableMap options) {

        Activity currentActivity = getCurrentActivity();

        /**
         * Start payment receiver
         */
        try {

            Log.e(">>>options", new Gson().toJson(options));

            JSONObject optionsJSON = Utils.readableMapToJson(options);

            PaymentData paymentData = new Gson().fromJson(optionsJSON.toString(), PaymentData.class);

            Log.e(">>>paymentData", new Gson().toJson(paymentData));

             Gateway.startReceivingPaymentActivity(currentActivity, paymentData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        //After Result
        Gateway.handleSecureResult(requestCode, resultCode, data, this);
    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onTransactionComplete(SyncMessage syncMessage) {

        if (syncMessage.status) {

            JSONObject paymentDataJson = new JSONObject();
            try {
                paymentDataJson.put(MAP_KEY_ERROR_CODE, syncMessage.status);
                paymentDataJson.put(MAP_KEY_ERROR_DESC, syncMessage.message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            sendEvent("Telrpay::PAYMENT_SUCCESS", Utils.jsonToWritableMap(paymentDataJson));

        } else {

            WritableMap errorParams = Arguments.createMap();
            JSONObject paymentDataJson = new JSONObject();
            try {
                paymentDataJson.put(MAP_KEY_ERROR_CODE, syncMessage.status);
                paymentDataJson.put(MAP_KEY_ERROR_DESC, syncMessage.message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            sendEvent("Telrpay::PAYMENT_ERROR", Utils.jsonToWritableMap(paymentDataJson));
        }
    }

    @Override
    public void onTransactionCancel(SyncMessage syncMessage) {
        WritableMap errorParams = Arguments.createMap();
        JSONObject paymentDataJson = new JSONObject();
        try {
            paymentDataJson.put(MAP_KEY_ERROR_CODE, syncMessage.status);
            paymentDataJson.put(MAP_KEY_ERROR_DESC, syncMessage.message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendEvent("Telrpay::PAYMENT_CANCEL", Utils.jsonToWritableMap(paymentDataJson));
    }

    private void sendEvent(String eventName, WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
}