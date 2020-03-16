package com.patelheggere.farmconnect.network;



import com.patelheggere.farmconnect.activity.merchantmain.ui.liveauction.model.LiveAuctionModel;
import com.patelheggere.farmconnect.model.APIResponseModel;
import com.patelheggere.farmconnect.model.AssemblyModel;
import com.patelheggere.farmconnect.model.DistrictModel;
import com.patelheggere.farmconnect.model.FarmerAndCropDetails;
import com.patelheggere.farmconnect.model.FarmerBidNotificationModel;
import com.patelheggere.farmconnect.model.FarmerCropModel;
import com.patelheggere.farmconnect.model.FilterModel;
import com.patelheggere.farmconnect.model.PostBidModel;
import com.patelheggere.farmconnect.model.TalukModel;
import com.patelheggere.farmconnect.model.UserDetails;
import com.patelheggere.farmconnect.model.VillageModel;
import com.patelheggere.farmconnect.model.notify.PushNotificationModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     */

   /* //mine AIzaSyD_Zbbwx7aYQaAWnl5O2Dv4-6r2G3dhEUI
    //ind AIzaSyDexSpfSK4WI1XnxQCuusnateV57knMJww
    @GET("api/place/nearbysearch/json?sensor=true&rankby=distance&key=AIzaSyDexSpfSK4WI1XnxQCuusnateV57knMJww")
    Call<Place> getNearbyPlaces(@Query("types") String type, @Query("location") String location);
    //Call<Place> getNearbyPlaces(@Query("location") String location);

    @GET("api/place/nearbysearch/json?sensor=true&rankby=distance&key=AIzaSyDexSpfSK4WI1XnxQCuusnateV57knMJww")
    Call<Place> getNearbyPlacesWithToken(@Query("location") String location, @Query("pagetoken") String token);*/

    // with type
    //Call<Place> getNearbyPlaces(@Query("types") String type, @Query("location") String location);

   // Call<Place> getNearbyPlaces(@Query("types") String type, @Query("location") String location, @Query("radius") int radius);

   // @GET("beneficiary/getByMobile.php")
  //  Call<BeneficiaryModel> getByMobile(@Query("mobile") String mobile);

/*    @GET("getTaskByExeId.php")
    Call<List<AssignedTasksModel>> getTaskAssignedToExe(@Query("id") String id);

    @GET("VerifyUser.php")
    Call<ExecVerifyModel> verifyUser(@Query("uname") String uname, @Query("pwd") String pwd);

    @GET("getAllProducts.php")
    Call<List<ProductsOnlyModel>> getAllProducts(@Query("name") String name);

    @GET("getProductDetails.php")
    Call<List<ProductDetails>> getProductDetails(@Query("pid") String name);




   /* @GET("http://stage-central.oustme.com/rest/services/course/getAdaptiveCourseData/3007")
    Call<Object> getData(@Header("org-id") String orgId);
*/
   // @GET("beneficiary/getByEPIC.php")
   // Call<BeneficiaryModel> getByEPIC(@Query("epic") String epic);

    @POST("InsertUsers.php")
    Call<APIResponseModel> updateUserTable(@Body UserDetails csrModel);

    @GET("VerifyUser.php")
    Call<APIResponseModel> verifyUser(@Query("phone") String phone, @Query("pwd") String pwd, @Query("type") String type);


    @GET("FCMUpdate.php")
    Call<APIResponseModel> updateFcm(@Query("phone") String phone, @Query("fcm_token") String fcm);

    @GET("MerchantFCMUpdate.php")
    Call<APIResponseModel> updateMerchantFcm(@Query("phone") String phone, @Query("fcm_token") String fcm);

    @GET("ProfileUrlUpdate.php")
    Call<APIResponseModel> updateAvatar(@Query("phone") String phone, @Query("url") String url);

    @GET("getAllDistricts.php")
    Call<List<DistrictModel>> getAllDistricts();

    @GET("getAllTaluks.php")
    Call<List<TalukModel>> getTaluks(@Query("dist_id") String dist_id);

    @GET("getAllAssembly.php")
    Call<List<AssemblyModel>> getAssembly(@Query("dist_id") String dist_id);


    @GET("getAllTandasByTLK.php")
    Call<List<VillageModel>> getTandasByTaluk(@Query("tlk_id") String tlk_id);

    @GET("getAllCrops.php")
    Call<List<FarmerCropModel>> getAllCrops(@Query("userid") String userid);

    @GET("getAllBiddedCrops.php")
    Call<List<FarmerCropModel>> getAllBiddedCrops(@Query("userid") String userid);


    @GET("getAllLiveCrops.php")
    Call<List<FarmerCropModel>> getAllLive(@Query("time") long time);

    @GET("getAllLiveCropsUserID.php")
    Call<List<FarmerCropModel>> getAllLiveUserId(@Query("time") long time, @Query("userid") String userid);

    /*@POST("InsertEnrollTbl.php")
    Call<APIResponseModel> insertEnrolData(@Body EnrollmentModel enrollmentModel);*/

    @POST("SendFCM.php")
    Call<PushNotificationModel> sendPushMessage(@Body PushNotificationModel pushNotificationModel);

    @POST("AddCropDetails.php")
    Call<APIResponseModel> addCropDetails(@Body FarmerCropModel farmerCropModel);

    @POST("getAllLiveCropsFilter.php")
    Call<List<FarmerCropModel>> getFilteredCrop(@Body FilterModel filterModel);

    @POST("PostBidding.php")
    Call<APIResponseModel> PostBidd(@Body PostBidModel postBidModel);


    @POST("NotifyMerchantFarmerAccept.php")
    Call<APIResponseModel> NotifyMerchant(@Body FarmerBidNotificationModel farmerBidNotificationModel);

    @GET("GetAllNotifications.php")
    Call<List<FarmerBidNotificationModel>> getAllFarmerNotifications(@Query("userid") String userid);

    @GET("GetAllMerchantNotifications.php")
    Call<List<FarmerBidNotificationModel>> getAllMerchantNotifications(@Query("userid") String userid);

    @GET("GetFarmerAndCropDetails.php")
    Call<FarmerAndCropDetails> GetFarmerAndCropDetails(@Query("userid") String userid, @Query("cropId") int cropId);


    @POST("updateMerchantPayment.php")
    Call<APIResponseModel> updateMerchantPayment(@Body FarmerBidNotificationModel farmerBidNotificationModel);
}
