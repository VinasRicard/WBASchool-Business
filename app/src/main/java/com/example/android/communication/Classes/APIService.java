package com.example.android.communication.Classes;

import com.example.android.communication.Notifications.MyResponse;
import com.example.android.communication.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:spplication/json",
                    "Authorization:key=AAAAxPx1yCM:APA91bG8rNHLAZIaQvjNinLGNW68Y29l_2sW8ZxOfn7Ltl_osNyKf7O7LuExEcsi2m49vDS2sr460ixma12wCWkPfdJq4YgQ2rpTgcfoNkQNi7CqnSkt02f6ItJl3MuFtyqXjxygqyoO"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
