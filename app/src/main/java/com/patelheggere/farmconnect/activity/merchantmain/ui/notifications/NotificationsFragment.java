package com.patelheggere.farmconnect.activity.merchantmain.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.activity.farmermain.ui.home.adapter.FarmerCropAdapter;
import com.patelheggere.farmconnect.model.FarmerCropModel;

import java.util.ArrayList;


public class NotificationsFragment extends Fragment {

    private NotificationsViewModel homeViewModel;
    private RecyclerView mRecyclerView;
    private FarmerCropAdapter adapter;
    private DatabaseReference databaseReference;
    private ArrayList<FarmerCropModel> cropModelsList;
    private ProgressBar mProgressBar;
    private TextView no_data;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mProgressBar = root.findViewById(R.id.progress_bar);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        no_data = root.findViewById(R.id.no_data);
        getData();
        return root;
    }

    private void getData() {

    }
}