package com.patelheggere.farmconnect.activity.merchantmain.ui.liveauction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LiveAuctionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LiveAuctionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}