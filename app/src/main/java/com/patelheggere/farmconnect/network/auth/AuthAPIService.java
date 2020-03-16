package com.patelheggere.farmconnect.network.auth;

import com.firebase.ui.auth.data.model.User;
import com.patelheggere.farmconnect.model.RazorPayOrderModel;
import com.patelheggere.farmconnect.model.RazorPayOrderRequestModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPIService {
    @POST("v1/orders")
    Call<RazorPayOrderModel> basicLogin(@Body RazorPayOrderRequestModel data);
}
