package com.patelheggere.farmconnect.activity.farmer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.base.BaseActivity;

public class BiddingDetailsActivity extends BaseActivity {
    private static final String TAG = "BiddingDetailsActivity";
    private TextView time, cropName, bidsCount, qty, status, harvestingTime, bidderPrice, farmerPrice;
    private ImageView imageView;
    private TextView bidSuccess;
    private ImageView circleImageViewProfile;
    private LinearLayout mShareCommentLayout;
    private LinearLayout mLinearLayoutLike, mLinearLayoutComment, mLinearLayoutShare, mLinearLayoutPlace;
    private TextView mTextViewLikeCount, mTextViewShareCount, mTextViewCommentCount, textViewPlace;

    @Override
    protected int getContentView() {
        return R.layout.activity_bidding_details;
    }

    @Override
    protected void initView() {
        circleImageViewProfile =  findViewById(R.id.profilePic);
        time = findViewById(R.id.timestamp);
        cropName = findViewById(R.id.name);
        bidsCount = findViewById(R.id.noofbids);
        qty = findViewById(R.id.qtyValue);
        imageView = findViewById(R.id.cropImage1);
        harvestingTime = findViewById(R.id.textViewHarvestingTimeValue);
        status = findViewById(R.id.txtStatus);
        bidderPrice = findViewById(R.id.bidderPrice);
        farmerPrice = findViewById(R.id.textViewYourValue);
        bidSuccess = findViewById(R.id.txtStatusSuccessbid);

        mShareCommentLayout = findViewById(R.id.linearlayoutLikesCommentShare);
        mLinearLayoutComment = findViewById(R.id.linearlayoutComment);
        mLinearLayoutLike = findViewById(R.id.linearlayoutLike);
        mLinearLayoutShare = findViewById(R.id.linearlayoutShare);
        mTextViewLikeCount = findViewById(R.id.textViewLikeCount);
        mTextViewShareCount = findViewById(R.id.textViewShareCount);
        mTextViewCommentCount = findViewById(R.id.textViewCommentCount);
        mLinearLayoutPlace = findViewById(R.id.linearLayoutPlace);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
