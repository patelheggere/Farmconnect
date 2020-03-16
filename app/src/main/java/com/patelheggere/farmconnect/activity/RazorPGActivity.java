package com.patelheggere.farmconnect.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.base.BaseActivity;
import com.patelheggere.farmconnect.model.RazorPayOrderModel;
import com.patelheggere.farmconnect.model.RazorPayOrderRequestModel;
import com.patelheggere.farmconnect.network.auth.AuthAPIService;
import com.patelheggere.farmconnect.network.auth.ServiceGenerator;
import com.patelheggere.farmconnect.utils.AppUtils;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RazorPGActivity extends BaseActivity implements PaymentResultListener {
    private static final String TAG = "RazorPGActivity";

    @Override
    protected int getContentView() {
        return R.layout.activity_razor_pg;
    }

    @Override
    protected void initView() {
        //checkout.setKeyID("<YOUR_KEY_ID>");
        Checkout.preload(getApplicationContext());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        getOrderId();
    }

    public void getOrderId(){
        AuthAPIService authAPIService = ServiceGenerator.createService(AuthAPIService.class, AppUtils.Constants.API_KEY_ID, AppUtils.Constants.API_SECRET_KEY);
        RazorPayOrderRequestModel requestModel = new RazorPayOrderRequestModel();
        requestModel.setAmount(500);
        requestModel.setCurrency("INR");
        requestModel.setPayment_capture(1);
        requestModel.setReceipt("rcortp_1234");
        Call<RazorPayOrderModel> call = authAPIService.basicLogin(requestModel);
        call.enqueue(new Callback<RazorPayOrderModel>() {
            @Override
            public void onResponse(Call<RazorPayOrderModel> call, Response<RazorPayOrderModel> response) {
                if(response.body()!=null)
                if(response.body().getStatus().equalsIgnoreCase("created"))
                {
                    startPayment(response.body());
                }
            }

            @Override
            public void onFailure(Call<RazorPayOrderModel> call, Throwable t) {

            }
        });

    }

    public void startPayment(RazorPayOrderModel body) {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID(AppUtils.Constants.API_KEY_ID);
      //  checkout.setPublicKey("eIK7z1Csr3Va6OPXQizSrzhv");
        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "AgriOn");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("order_id", body.getId());
            options.put("currency", "INR");
           // options.put("signature","razorpay_signature");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", body.getAmount());

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.d(TAG, "onPaymentSuccess: "+s);
        Intent intent = new Intent();
        intent.putExtra("SUCCESS", "success");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.d(TAG, "onPaymentError: "+s);
        Log.d(TAG, "onPaymentError: "+i);
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("SUCCESS", "cancel");
        setResult(RESULT_OK, intent);
        finish();
    }
}
