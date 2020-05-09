package com.patelheggere.farmconnect.activity.merchantmain.ui.liveauction;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.activity.merchantmain.ui.liveauction.model.LiveAuctionModel;
import com.patelheggere.farmconnect.base.BaseActivity;
import com.patelheggere.farmconnect.model.APIResponseModel;
import com.patelheggere.farmconnect.model.FarmerCropModel;
import com.patelheggere.farmconnect.model.PostBidModel;
import com.patelheggere.farmconnect.network.ApiInterface;
import com.patelheggere.farmconnect.network.RetrofitInstance;
import com.patelheggere.farmconnect.utils.AppUtils;
import com.patelheggere.farmconnect.utils.SharedPrefsHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.patelheggere.farmconnect.utils.AppUtils.Constants.USER_ID;

public class AuctionDetailsActivity extends BaseActivity {
    private static final String TAG = "AuctionDetailsActivity";
    private FarmerCropModel liveAuctionModel;
    private TextView textViewExpiryTime, textViewMinBid, textViewCurrentBid, textViewLocation;
    private Button mButtonBid;
    private ImageView mImageViewCrop;
    private PostBidModel mPostBidModel;
    private EditText mEditTextMerchantBidValue, mEditTextMinimumOrder;
    private LinearLayout mLinearLayoutMinimuOrder;

    @Override
    protected int getContentView() {
        return R.layout.activity_auction_details;
    }

    @Override
    protected void initView() {
        mButtonBid = findViewById(R.id.button_bid);
        textViewExpiryTime = findViewById(R.id.expirytime);
        textViewMinBid = findViewById(R.id.minbid);
        textViewCurrentBid  = findViewById(R.id.curentbid);
        textViewLocation = findViewById(R.id.location);
        mImageViewCrop = findViewById(R.id.crop_image);
        mEditTextMerchantBidValue = findViewById(R.id.merchant_bid_value);
        mLinearLayoutMinimuOrder = findViewById(R.id.linearLayoutMinimuOrder);
        mEditTextMinimumOrder = findViewById(R.id.MinimumOrder_Value);

    }

    @Override
    protected void initData() {
        liveAuctionModel = getIntent().getParcelableExtra("DATA");
        if(liveAuctionModel!=null)
        {
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(liveAuctionModel.getEndTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
            textViewExpiryTime.setText(timeAgo);
            textViewCurrentBid.setText(liveAuctionModel.getBidderPrice());
            textViewMinBid.setText(liveAuctionModel.getFarmerPrice());
            textViewLocation.setText(liveAuctionModel.getCropLocation());
            Glide.with(activity).load(liveAuctionModel.getCropImage()).into(mImageViewCrop);
            if(liveAuctionModel.getIsMinimumOrder()==1){
                mLinearLayoutMinimuOrder.setVisibility(View.VISIBLE);
            }
            else
            {
                mLinearLayoutMinimuOrder.setVisibility(View.GONE);
            }
        }

    }

    @Override
    protected void initListener() {
        mButtonBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long price = Long.parseLong(mEditTextMerchantBidValue.getText().toString());
                long currentPrice = Long.parseLong(liveAuctionModel.getBidderPrice().toString());
                long farmerPrice = Long.parseLong(liveAuctionModel.getFarmerPrice().toString());
                mPostBidModel = new PostBidModel();
                mPostBidModel.setSl_no(liveAuctionModel.getSl_no());
                if(price<currentPrice)
                {
                    AppUtils.showToast(getString(R.string.current_price));
                    return;
                }
                if(price<farmerPrice)
                {
                    AppUtils.showToast(getString(R.string.greater_than_farmer_price));
                    return;
                }
                if(liveAuctionModel.getIsMinimumOrder()==1)
                {
                    if(Long.parseLong(mEditTextMinimumOrder.getText().toString())<liveAuctionModel.getMinimumOrder())
                    {
                        AppUtils.showToast(getString(R.string.minimum_order_should_be_order));
                        return;
                    }
                }
                mPostBidModel.setBidderPrice(price+"");
                mPostBidModel.setCropImage(liveAuctionModel.getCropImage());
                mPostBidModel.setCropLocation(liveAuctionModel.getCropLocation());
                mPostBidModel.setCropName(liveAuctionModel.getCropName());
                mPostBidModel.setEndTime(liveAuctionModel.getEndTime());
                mPostBidModel.setHarvestingTime(liveAuctionModel.getHarvestingTime());
                mPostBidModel.setFarmerPrice(liveAuctionModel.getFarmerPrice());
                mPostBidModel.setIsBidSuccess(liveAuctionModel.getIsBidSuccess());
                mPostBidModel.setNoOfBids(liveAuctionModel.getNoOfBids()+1);
                mPostBidModel.setQty(liveAuctionModel.getQty());
                mPostBidModel.setStatus(liveAuctionModel.getStatus());
                mPostBidModel.setUploadedTime(liveAuctionModel.getUploadedTime());
                mPostBidModel.setUserid(liveAuctionModel.getUserid());
                mPostBidModel.setBidderID(SharedPrefsHelper.getInstance().get(USER_ID).toString());

                setUpNetwork();
                Call<APIResponseModel> postBidd = apiInterface.PostBidd(mPostBidModel);
                postBidd.enqueue(new Callback<APIResponseModel>() {
                    @Override
                    public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                        if(response.isSuccessful() && response.body().isStatus())
                        {
                            AppUtils.showToast(getString(R.string.your_bid_posted_success));
                            finish();
                        }
                        else
                        {
                            AppUtils.showToast(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponseModel> call, Throwable t) {
                        AppUtils.showToast(getString(R.string.something_wrong));
                    }
                });

            }
        });
    }

    protected ApiInterface apiInterface;

    private void setUpNetwork()
    {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }
}
