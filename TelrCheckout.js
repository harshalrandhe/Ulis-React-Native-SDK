'use strict';

import { NativeModules, NativeEventEmitter } from 'react-native';

const telrpayEvents = new NativeEventEmitter(NativeModules.TelrpayEventEmitter);

const removeSubscriptions = () => {
    telrpayEvents.removeAllListeners('Telrpay::PAYMENT_SUCCESS');
    telrpayEvents.removeAllListeners('Telrpay::PAYMENT_ERROR');
    telrpayEvents.removeAllListeners('Telrpay::EXTERNAL_WALLET_SELECTED');
};

class TelrPayCheckout {
    static open(options, successCallback, errorCallback) {
        return new Promise(function (resolve, reject) {
            telrpayEvents.addListener('Telrpay::PAYMENT_SUCCESS', (data) => {
                let resolveFn = successCallback || resolve;
                resolveFn(data);
                removeSubscriptions();
            });
            telrpayEvents.addListener('Telrpay::PAYMENT_ERROR', (data) => {
                let rejectFn = errorCallback || reject;
                rejectFn(data);
                removeSubscriptions();
            });
            telrpayEvents.addListener('Telrpay::PAYMENT_CANCEL', (data) => {
                let rejectFn = errorCallback || reject;
                rejectFn(data);
                removeSubscriptions();
            });
            NativeModules.RNTelrPayCheckout.open(options);
        });
    }
}

export default TelrPayCheckout;