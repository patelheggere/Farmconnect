package com.patelheggere.farmconnect.activity.merchantmain.ui.notifications.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.model.FarmerCropModel;

import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by Veerendra Patel on 3/6/19.
 */

public class MerchantCropAdapter extends RecyclerView.Adapter<MerchantCropAdapter.MyViewHolder> {
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

    public MerchantCropAdapter(Context context, List<FarmerCropModel> dataList) {
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
        private TextView time, cropName, bidsCount, qty, status, harvestingTime, bidderPrice, farmerPrice;
        private ImageView imageView;
        private ImageView circleImageViewProfile;
        private LinearLayout mShareCommentLayout;
        private LinearLayout mLinearLayoutLike, mLinearLayoutComment, mLinearLayoutShare, mLinearLayoutPlace;
        private TextView mTextViewLikeCount, mTextViewShareCount, mTextViewCommentCount, textViewPlace;

        MyViewHolder(View view) {
            super(view);

            circleImageViewProfile =  view.findViewById(R.id.profilePic);
            time = view.findViewById(R.id.timestamp);
            cropName = view.findViewById(R.id.name);
            bidsCount = view.findViewById(R.id.noofbids);
            qty = view.findViewById(R.id.qtyValue);
            imageView = view.findViewById(R.id.cropImage1);
            harvestingTime = view.findViewById(R.id.textViewHarvestingTimeValue);
            status = view.findViewById(R.id.txtStatus);
            bidderPrice = view.findViewById(R.id.bidderPrice);
            farmerPrice = view.findViewById(R.id.textViewYourValue);

            mShareCommentLayout = view.findViewById(R.id.linearlayoutLikesCommentShare);
            mLinearLayoutComment = view.findViewById(R.id.linearlayoutComment);
            mLinearLayoutLike = view.findViewById(R.id.linearlayoutLike);
            mLinearLayoutShare = view.findViewById(R.id.linearlayoutShare);
            mTextViewLikeCount = view.findViewById(R.id.textViewLikeCount);
            mTextViewShareCount = view.findViewById(R.id.textViewShareCount);
            mTextViewCommentCount = view.findViewById(R.id.textViewCommentCount);
            mLinearLayoutPlace = view.findViewById(R.id.linearLayoutPlace);
        }
    }
    
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.merchant_crop_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            final FarmerCropModel dataModel = dataModelList.get(position);
            if(dataModel.getUploadedTime()!=0)
            {
                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(dataModel.getUploadedTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                holder.time.setText(timeAgo);
            }

            holder.cropName.setText(dataModel.getCropName());
            holder.bidsCount.setText(""+dataModel.getNoOfBids());
            holder.qty.setText(""+dataModel.getQty());
            if(dataModel.getCropImage()!=null)
            {
                Glide.with(context).load(dataModel.getCropImage()).into(holder.imageView);
                holder.imageView.setVisibility(View.VISIBLE);
            }
            CharSequence harvest = DateUtils.getRelativeTimeSpanString(dataModel.getHarvestingTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

            holder.harvestingTime.setText(harvest);//dataModel.getStatus());

            holder.status.setText(dataModel.getStatus());

            /*
            holder.content.setText(dataModel.getMessage());
            if(dataModel.getType()!=null && dataModel.getType().equalsIgnoreCase(AppUtils.Constants.EVENTS))
            {
                holder.time.setText(dataModel.getDate());
                if(dataModel.getPlace()!=null && !dataModel.getPlace().trim().isEmpty()) {
                    holder.mLinearLayoutPlace.setVisibility(View.VISIBLE);
                    holder.textViewPlace.setText(dataModel.getPlace());
                }
                else {
                    holder.mLinearLayoutPlace.setVisibility(View.GONE);
                }
            }
            else if(dataModel.getTimestamp()!=0)
            {
                holder.mLinearLayoutPlace.setVisibility(View.GONE);
                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(dataModel.getTimestamp(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                holder.time.setText(timeAgo);
            }

            if(dataModel.getImageurl()!=null)
            {
                Glide.with(context).load(dataModel.getImageurl()).into(holder.imageView);
                holder.imageView.setVisibility(View.VISIBLE);
            }
            if(dataModel.getUrl()!=null)
            {
                holder.url.setText(dataModel.getUrl());
                holder.url.setVisibility(View.VISIBLE);
            }
            if(dataModel.getNumComments()!=0)
                holder.mTextViewCommentCount.setText(""+dataModel.getNumComments());

            if(dataModel.getNumLikes()!=0)
                holder.mTextViewLikeCount.setText(""+dataModel.getNumLikes());

            if(dataModel.getNumShare()!=0)
                holder.mTextViewShareCount.setText(""+dataModel.getNumShare());


            holder.mLinearLayoutComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickItem(holder.getAdapterPosition());
                }
            });
            holder.mLinearLayoutLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateLikesCount(holder.getAdapterPosition(), true);

                   *//* if(!dataModel.isLiked())
                    {
                        updateLikesCount(holder.getAdapterPosition(), true);
                    }
                    else
                    {
                        updateLikesCount(holder.getAdapterPosition(), false);
                    }*//*
                }
            });

            holder.mLinearLayoutShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateSharesCount(holder.getAdapterPosition());
                }
            });*/

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


