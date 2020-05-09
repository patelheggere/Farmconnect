package com.patelheggere.farmconnect.activity.merchantmain.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.activity.farmer.BiddingDetailsActivity;
import com.patelheggere.farmconnect.activity.merchantmain.ui.dashboard.adapter.FarmerCropAdapter;
import com.patelheggere.farmconnect.model.FarmerCropModel;
import com.patelheggere.farmconnect.network.ApiInterface;
import com.patelheggere.farmconnect.network.RetrofitInstance;
import com.patelheggere.farmconnect.utils.AppUtils;
import com.patelheggere.farmconnect.utils.RecyclerItemClickListener;
import com.patelheggere.farmconnect.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.patelheggere.farmconnect.utils.AppUtils.Constants.USER_ID;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private RecyclerView mRecyclerView;
    private DatabaseReference databaseReference;
    private ArrayList<FarmerCropModel> cropModelsList;
    private ProgressBar mProgressBar;
    private TextView no_data;
    private FarmerCropAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_merchant_dashboard, container, false);
     //   final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText("Merchant Dashboard");
            }
        });
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mProgressBar = root.findViewById(R.id.progress_bar);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        no_data = root.findViewById(R.id.no_data);
        getData();
        initListener();
        return root;
    }

    private void getData() {
        cropModelsList = new ArrayList<>();
        setUpNetwork();
        mProgressBar.setVisibility(View.VISIBLE);
        Call<List<FarmerCropModel>> cropData = apiInterface.getAllBiddedCrops(SharedPrefsHelper.getInstance().get(USER_ID).toString());
        cropData.enqueue(new Callback<List<FarmerCropModel>>() {
            @Override
            public void onResponse(Call<List<FarmerCropModel>> call, Response<List<FarmerCropModel>> response) {
                mProgressBar.setVisibility(View.GONE);
                if(response.isSuccessful() && response.body().size()>0){
                    cropModelsList = (ArrayList<FarmerCropModel>) response.body();
                    Collections.reverse(cropModelsList);
                    adapter = new FarmerCropAdapter(getContext(), cropModelsList);
                    mRecyclerView.setAdapter(adapter);
                    mProgressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    no_data.setVisibility(View.GONE);
                }
                else {
                    AppUtils.showToast(getString(R.string.no_data));
                }
            }

            @Override
            public void onFailure(Call<List<FarmerCropModel>> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                // AppUtils.showToast(getString(R.string.something_wrong));
            }
        });

    }

    private void initListener(){
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getContext(), BiddingDetailsActivity.class);
                        intent.putExtra("DATA", cropModelsList.get(position));
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    protected ApiInterface apiInterface;

    private void setUpNetwork()
    {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

}