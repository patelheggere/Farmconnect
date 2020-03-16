package com.patelheggere.farmconnect.activity.farmermain.ui.notifications.adapter;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.model.FarmerBidNotificationModel;
import com.patelheggere.farmconnect.model.FarmerCropModel;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Veerendra Patel on 3/6/19.
 */

public class FarmerCropNotificationAdapter extends RecyclerView.Adapter<FarmerCropNotificationAdapter.MyViewHolder> {
    private static final String TAG = "FarmerCropAdapter";
    private Context context;
    private boolean isClicked = false;
    private long nbId=0;
    private DecimalFormat formatter;
    private List<FarmerBidNotificationModel> dataModelList;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private SelectItem mListener;
    private String type;

    public FarmerCropNotificationAdapter(Context context, List<FarmerBidNotificationModel> dataList, SelectItem selectItem) {
        this.context = context;
        this.dataModelList = dataList;
        try {
            this.mListener = selectItem;
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement AdapterCallback.");
        }
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
        private TextView time, cropName, bidsCount, qty, status, harvestingTime, bidderPrice, details;
        private ImageView imageView;
        private Button mButtonReject, mButtonAccept;
        private ImageView circleImageViewProfile;
        private LinearLayout mShareCommentLayout;
        private LinearLayout mLinearLayoutLike, mLinearLayoutComment, mLinearLayoutShare, mLinearLayoutPlace;
        private TextView mTextViewLikeCount, mTextViewShareCount, mTextViewCommentCount, textViewPlace;

        MyViewHolder(View view) {
            super(view);
            details = view.findViewById(R.id.message);
            mButtonReject = view.findViewById(R.id.buttonReject);
            mButtonAccept = view.findViewById(R.id.buttonAccept);
        }
    }
    
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.farmer_notification_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            final FarmerBidNotificationModel dataModel = dataModelList.get(position);
            if(dataModel.getUploadedTime()!=0)
            {
                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(dataModel.getUploadedTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                DateFormat simple = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");

                // Creating date from milliseconds
                // using Date() constructor
                Date date = new Date(dataModel.getUploadedTime());

                String mess = "You have posted <b>"+dataModel.getCropName()+"</b> crop on <b>"+simple.format(date)+"</b> you have got highest bid <b>"+context.getString(R.string.Rs)+""+dataModel.getBiddingPrice()+"</b> Please confirm this bid";
                holder.details.setText(Html.fromHtml(mess));

            }
            if(dataModel!=null)
            {
                holder.mButtonAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickItem(dataModel, 1);
                    }
                });

                holder.mButtonReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickItem(dataModel, 0);
                    }
                });
            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);
        //releasePlayer(player);
    }

    public void onClickItem(FarmerBidNotificationModel farmerBidNotificationModel, int isAccepted) {
        if (mListener != null) {
            farmerBidNotificationModel.setIsBidAccepted(isAccepted);
            mListener.selectedPosition(farmerBidNotificationModel);
        }
    }
    public interface SelectItem{
        void selectedPosition(FarmerBidNotificationModel farmerBidNotificationModel);
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


