package com.patelheggere.farmconnect.activity.merchantmain.ui.liveauction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DatabaseReference;
import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.activity.farmeraddcrop.AddCropDataActivity;
import com.patelheggere.farmconnect.activity.farmermain.ui.home.adapter.FarmerCropAdapter;
import com.patelheggere.farmconnect.activity.merchantmain.ui.liveauction.adapter.LiveAuctionAdapter;
import com.patelheggere.farmconnect.activity.merchantmain.ui.liveauction.adapter.SpinnerCheckboxAdapter;
import com.patelheggere.farmconnect.activity.merchantmain.ui.liveauction.adapter.SpinnerCheckboxCropAdapter;
import com.patelheggere.farmconnect.activity.merchantmain.ui.liveauction.adapter.SpinnerCheckboxDistAdapter;
import com.patelheggere.farmconnect.activity.merchantmain.ui.liveauction.model.FilterCheckBoxModel;
import com.patelheggere.farmconnect.activity.merchantmain.ui.liveauction.model.LiveAuctionModel;
import com.patelheggere.farmconnect.base.BaseFragment;
import com.patelheggere.farmconnect.model.DistrictModel;
import com.patelheggere.farmconnect.model.FarmerCropModel;
import com.patelheggere.farmconnect.model.FilterModel;
import com.patelheggere.farmconnect.model.TalukModel;
import com.patelheggere.farmconnect.model.VillageModel;
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

public class LiveAuctionFragment extends BaseFragment implements
        SpinnerCheckboxAdapter.CheckedValue,
        SpinnerCheckboxCropAdapter.CheckedValueCrop,
        SpinnerCheckboxDistAdapter.CheckedValue {
    private static final String TAG = "LiveAuctionFragment";

    private LiveAuctionViewModel notificationsViewModel;
    private RecyclerView mRecyclerView;
    private LiveAuctionAdapter adapter;
    private DatabaseReference databaseReference;
    private ArrayList<FarmerCropModel> liveAuctionModels;
    private ProgressBar mProgressBar;
    private TextView no_data;
    private View mRootView;
    private Spinner mStateSpinner,mCropSpinner, mDistSpinner;
    private List<DistrictModel> districtModelList;
    private List<TalukModel> talukModelList;
    private List<VillageModel> villageModelList;

    private List<String> mDistrictNamesList;
    private List<String> mAssemblyNamesList;
    private List<String> mTalukNames;
    private List<String> mTandaNames;
    private List<String> listState, listDistrict, listTaluks, listHobli, listVillage, listGP;
    private List<String> cropNamesList;
    ArrayList<FilterCheckBoxModel> stateFilterList = new ArrayList<>();
    ArrayList<FilterCheckBoxModel> cropFilterList = new ArrayList<>();
    ArrayList<FilterCheckBoxModel> distFilterList = new ArrayList<>();
    private Button mButtonSearch;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = ViewModelProviders.of(this).get(LiveAuctionViewModel.class);
         mRootView = inflater.inflate(R.layout.fragment_liveauction_merchant, container, false);
         setUpNetwork();
         initViews();
         initListeners();
         //getData(filterModel);
        return mRootView;
    }

    private void initViews(){
        mRecyclerView = mRootView.findViewById(R.id.recyclerViewLiveAuction);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mProgressBar = mRootView.findViewById(R.id.progress_bar);
        mStateSpinner = mRootView.findViewById(R.id.spinnerState);
        mCropSpinner = mRootView.findViewById(R.id.spinnerCrop);
        mDistSpinner = mRootView.findViewById(R.id.spinnerDist);
        mButtonSearch = mRootView.findViewById(R.id.search);
        no_data = mRootView.findViewById(R.id.no_data);
        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(filterModel);
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getData(filterModel);
            }
        });
    }

    String[] stateList = {
            "Select State", "Karnataka", "Kerala", "Maharashtra", "TamilNadu",
            "Goa", "AndhraPradesh"};
    private void initListeners() {

        stateFilterList = new ArrayList<>();
        for (int i = 0; i < stateList.length; i++) {
            FilterCheckBoxModel stateVO = new FilterCheckBoxModel();
            stateVO.setTitle(stateList[i]);
            stateVO.setSelected(false);
            stateFilterList.add(stateVO);
        }
        SpinnerCheckboxAdapter myAdapter = new SpinnerCheckboxAdapter(mActivity, 0, stateFilterList, this);
        mStateSpinner.setAdapter(myAdapter);


        if(cropNamesList==null) {
            cropNamesList = new ArrayList<>();
            cropNamesList.add("Select Crop");
            cropNamesList.add("Onion");
            cropNamesList.add("Groundnut");
            cropNamesList.add("Tomato");
            cropNamesList.add("Brinjal");
            cropNamesList.add("Bitter Guard");

            for (int i = 0; i < cropNamesList.size(); i++) {
                FilterCheckBoxModel stateVO = new FilterCheckBoxModel();
                stateVO.setTitle(cropNamesList.get(i));
                stateVO.setSelected(false);
                cropFilterList.add(stateVO);
            }
        }


        SpinnerCheckboxCropAdapter cropFilterAdapter = new SpinnerCheckboxCropAdapter(mActivity, 0, cropFilterList, this);
        mCropSpinner.setAdapter(cropFilterAdapter);

        getDistricts();


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getContext(), AuctionDetailsActivity.class);
                        intent.putExtra("DATA", liveAuctionModels.get(position));
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(filterModel);
            }
        });
    }

    private void getDistricts() {
        Call<List<DistrictModel>> listCall = apiInterface.getAllDistricts();
        listCall.enqueue(new Callback<List<DistrictModel>>() {
            @Override
            public void onResponse(Call<List<DistrictModel>> call, Response<List<DistrictModel>> response) {
                if(response.isSuccessful()){
                    districtModelList = response.body();
                    mDistrictNamesList = new ArrayList<>();
                    distFilterList = new ArrayList<>();
                    mDistrictNamesList.add(getString(R.string.Select_District));
                    for (int i = 0; i<districtModelList.size(); i++){
                        mDistrictNamesList.add(districtModelList.get(i).getDist_name());
                        FilterCheckBoxModel stateVO = new FilterCheckBoxModel();
                        stateVO.setTitle(mDistrictNamesList.get(i));
                        stateVO.setSelected(false);
                        distFilterList.add(stateVO);
                    }
                    SpinnerCheckboxDistAdapter distAdapter = new SpinnerCheckboxDistAdapter(mActivity, 0, distFilterList, LiveAuctionFragment.this);
                    mDistSpinner.setAdapter(distAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<DistrictModel>> call, Throwable t) {
                Toast.makeText(mActivity, R.string.something_wrong, Toast.LENGTH_LONG).show();
            }
        });
    }
    private FilterModel filterModel = new FilterModel();
    private void getData(FilterModel filterModel) {
        liveAuctionModels = new ArrayList<>();
       // mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        Call<List<FarmerCropModel>> cropData;
        if(filterModel.getState()!=null || filterModel.getDistrict()!=null || filterModel.getCropName()!=null) {
            cropData = apiInterface.getFilteredCrop(filterModel);
        }
        else {
            cropData = apiInterface.getAllLive(System.currentTimeMillis());
        }
        cropData.enqueue(new Callback<List<FarmerCropModel>>() {
            @Override
            public void onResponse(Call<List<FarmerCropModel>> call, Response<List<FarmerCropModel>> response) {
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                if(response.isSuccessful() && response.body().size()>0){
                   /*// cropModelsList = (ArrayList<FarmerCropModel>) response.body();
                   // Collections.reverse(cropModelsList);
                   // adapter = new FarmerCropAdapter(getContext(), cropModelsList);
                    mRecyclerView.setAdapter(adapter);
                    mProgressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    no_data.setVisibility(View.GONE);*/
                   liveAuctionModels = (ArrayList<FarmerCropModel>) response.body();
                    Collections.reverse(liveAuctionModels);
                    adapter = new LiveAuctionAdapter(getContext(), liveAuctionModels);
                    mRecyclerView.setAdapter(adapter);
                    //   mProgressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    no_data.setVisibility(View.GONE);
                }
                else {
                    no_data.setVisibility(View.VISIBLE);
                    AppUtils.showToast(getString(R.string.no_data));
                }
            }

            @Override
            public void onFailure(Call<List<FarmerCropModel>> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                // AppUtils.showToast(getString(R.string.something_wrong));
            }
        });

      //  no_data.setVisibility(View.GONE);

    }
    protected ApiInterface apiInterface;

    private void setUpNetwork()
    {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

    @Override
    public void getPosition(List<FilterCheckBoxModel> liststate, int pos) {
        Log.d(TAG, "getPosition: "+pos);
        if(stateFilterList.get(pos).isSelected())
        {
            filterModel.setState(stateList[pos]);
        }
        else {
            filterModel.setState(null);
        }
    }
    @Override
    public void getCropPosition(List<FilterCheckBoxModel> liststate, int pos) {
        if(liststate.get(pos).isSelected())
        {
            filterModel.setCropName(cropNamesList.get(pos));
        }
        else {
            filterModel.setCropName(null);
        }
    }

    @Override
    public void getDistPosition(List<FilterCheckBoxModel> liststate, int pos) {
        if(liststate.get(pos).isSelected())
        {
            filterModel.setDistrict(mDistrictNamesList.get(pos));
        }
        else {
            filterModel.setCropName(null);
        }
    }
}