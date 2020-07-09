package com.patelheggere.farmconnect.activity.merchantmain.ui.liveauction.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.model.FarmerCropModel;

import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by Veerendra Patel on 3/6/19.
 */

public class LiveAuctionAdapter extends RecyclerView.Adapter<LiveAuctionAdapter.MyViewHolder> {
    private static final String TAG = "FarmerCropAdapter";
    private Context context;
    private boolean isClicked = false;
    private long nbId=0;
    private DecimalFormat formatter;
    private List<FarmerCropModel> dataModelList;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private SelectItem mListener;
    private String type;
    // Animation

    public LiveAuctionAdapter(Context context, List<FarmerCropModel> dataList) {
        this.context = context;
        this.dataModelList = dataList;
    }
    

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView expiryTime, title, bidsCount,bidderPrice;
        private ImageView imageView;
        private ImageView circleImageViewProfile;
        private LinearLayout mShareCommentLayout;
        private LinearLayout mLinearLayoutLike, mLinearLayoutComment, mLinearLayoutShare, mLinearLayoutPlace;
        private TextView mTextViewLikeCount, mTextViewShareCount, mTextViewCommentCount, textViewPlace, textViewBlink;

        MyViewHolder(View view) {
            super(view);
            expiryTime = view.findViewById(R.id.bid_time_ends_in);
            title = view.findViewById(R.id.bidding_crop_title);
           // bidsCount = view.findViewById()
            imageView = view.findViewById(R.id.imageViewCrop);
            bidderPrice = view.findViewById(R.id.bid_amount);
            textViewBlink = view.findViewById(R.id.textview_blink);
            // load the animation

        }
    }
    
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_auction_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            final FarmerCropModel dataModel = dataModelList.get(position);
            if(dataModel.getUploadedTime()!=0)
            {
                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(dataModel.getUploadedTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                holder.expiryTime.setText(timeAgo);
            }

            if(dataModel.getCropName()!=null)
            {
                holder.title.setText(dataModel.getCropName());
            }

            if(dataModel.getCropImage()!=null)
            {
                Log.d(TAG, "onBindViewHolder: "+dataModel.getCropImageIcon());
                Glide.with(context).load(dataModel.getCropImage()).into(holder.imageView);
            }
            Animation animBlink;
            animBlink = AnimationUtils.loadAnimation(context, R.anim.blink);
            holder.textViewBlink.startAnimation(animBlink);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);
        //releasePlayer(player);
    }


    public void onClickItem(int position) {
        if (mListener != null) {
            mListener.selectedPosition(position);
        }
    }
    public interface SelectItem{
        void selectedPosition(int position);
    }

    public void updateLikesCount(int position, final boolean isLiked)
    {
       /* DatabaseReference databaseReference = FarmConnectApplication.getFireBaseRef().child("news");
        databaseReference = databaseReference.child(dataModelList.get(position).getKey()).child("numLikes");
        databaseReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
              *//*  if(isLiked) {*//*
                    if (currentData.getValue() == null) {
                        currentData.setValue(1);
                    } else {
                        currentData.setValue((Long) currentData.getValue() + 1);
                    }
                *//*}
                else {
                    if(currentData.getValue() != null)
                    currentData.setValue((Long) currentData.getValue() - 1);
                }*//*
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });*/


    }
    public void updateSharesCount(int position)
    {
       /* DatabaseReference databaseReference = FarmConnectApplication.getFireBaseRef().child("news");
        databaseReference = databaseReference.child(dataModelList.get(position).getKey()).child("numShare");
        databaseReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                if (currentData.getValue() == null) {
                    currentData.setValue(1);
                } else {
                    currentData.setValue((Long) currentData.getValue() + 1);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });*/
    }


}


