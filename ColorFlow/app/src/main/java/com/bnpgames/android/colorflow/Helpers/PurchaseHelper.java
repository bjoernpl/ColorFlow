package com.bnpgames.android.colorflow.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.bnpgames.android.colorflow.R;

import java.io.IOException;
import java.security.Signature;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class PurchaseHelper implements PurchasesUpdatedListener, BillingClientStateListener {

    private static final String PREMIUM_VERSION = "buy_premium_version";
    private static final String HAS_PREMIUM = "has_premium";
    private static final String ORDER_ID = "order_id";
    private static final String ORDER_TOKEN = "order_token";
    private static final String ORDER_TIME = "order_time";

    private BillingClient client;
    private PurchaseInteractionListener mListener;
    private Activity activity;
    private HasPremiumListener premiumListener;

    public static boolean hasPremium(Context context){
        return context.getSharedPreferences("premium",Context.MODE_PRIVATE).getBoolean(HAS_PREMIUM,false);
    }

    public PurchaseHelper(Activity activity, PurchaseInteractionListener listener) {
        client = BillingClient.newBuilder(activity).setListener(this).build();
        client.startConnection(this);
        mListener = listener;
        this.activity = activity;
    }

    public PurchaseHelper(Activity activity, HasPremiumListener listener) {
        client = BillingClient.newBuilder(activity).setListener(this).build();
        client.startConnection(this);
        premiumListener = listener;
        this.activity = activity;
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            userCancelled();
        } else {
            // Handle any other error codes.
            errorOcurred();
        }
    }

    private void errorOcurred() {
        Log.i("purchase","error occured");
        mListener.onPurchaseFailed();
    }

    private void userCancelled() {
        mListener.onPurchaseCanceled();
        Log.i("purchase","cancelled");
    }

    private void handlePurchase(Purchase purchase) {
        try {
            if(Security.verifyPurchase(activity.getString(R.string.pkey),purchase.getOriginalJson(),purchase.getSignature())){
                savePurchase(purchase);
                mListener.onPurchaseSuccess();
            }else{
                Log.i("purchase","verification failed");
                mListener.onPurchaseFailed();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePurchase(Purchase purchase){
        String orderID = purchase.getOrderId();
        String token = purchase.getPurchaseToken();
        long purchaseTime = purchase.getPurchaseTime();
        SharedPreferences.Editor editor = activity.getSharedPreferences("premium",Context.MODE_PRIVATE).edit();
        editor.putBoolean(HAS_PREMIUM,true);
        editor.putString(ORDER_ID,orderID);
        editor.putString(ORDER_TOKEN,token);
        editor.putLong(ORDER_TIME,purchaseTime);
        editor.apply();
    }

    @Override
    public void onBillingSetupFinished(int responseCode) {
        if(responseCode == BillingClient.BillingResponse.OK){
            queryDetails();
        }else{
            Log.i("purchase",responseCode+"");
        }
    }

    private void queryDetails() {
        if(mListener!=null) {
            List skuList = new ArrayList<>();
            skuList.add(PREMIUM_VERSION);
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
            client.querySkuDetailsAsync(params.build(),
                    (responseCode, skuDetailsList) -> {
                        if (responseCode == BillingClient.BillingResponse.OK && skuDetailsList != null) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                String sku = skuDetails.getSku();
                                String price = skuDetails.getPrice();
                                if (PREMIUM_VERSION.equals(sku)) {
                                    mListener.onPriceReceived(price);
                                    Log.i("purchase", "price received: " + price);
                                }
                            }

                        } else {
                            Log.i("purchase", responseCode + "");
                        }
                    });
        }else if(premiumListener!=null) {
            Purchase.PurchasesResult result = client.queryPurchases(BillingClient.SkuType.INAPP);
            for (Purchase purchase : result.getPurchasesList()) {
                if (purchase.getSku().equals(PREMIUM_VERSION)) {
                    premiumListener.onPremiumReceived(true);
                    savePremium(true,purchase);
                    return;
                }
            }
            premiumListener.onPremiumReceived(false);
            savePremium(false,null);
        }
    }


    private void savePremium(boolean hasPremium, @Nullable Purchase purchase){
        if(hasPremium&&purchase!=null)savePurchase(purchase);
        else activity.getSharedPreferences("premium",Context.MODE_PRIVATE).edit().putBoolean(HAS_PREMIUM,hasPremium).apply();
    }

    @Override
    public void onBillingServiceDisconnected() {
        client.startConnection(this);
    }

    public interface PurchaseInteractionListener{
        void onPriceReceived(String price);
        void onPurchaseSuccess();
        void onPurchaseFailed();
        void onPurchaseCanceled();
    }

    public interface HasPremiumListener{
        void onPremiumReceived(boolean hasPremium);
    }

    public void startBillingFlow(){
        /*<String> skuList = new ArrayList<String>();
        skuList.add(PREMIUM_VERSION);
        SkuDetailsParams details = SkuDetailsParams.newBuilder().setSkusList(skuList).setType(BillingClient.SkuType.INAPP).build();*/
        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSku(PREMIUM_VERSION)
                .setType(BillingClient.SkuType.INAPP) // SkuType.SUB for subscription
                .build();
        int responseCode = client.launchBillingFlow(activity,flowParams);
    }
}
