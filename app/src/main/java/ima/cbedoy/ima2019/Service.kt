package ima.cbedoy.ima2019

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Service{
    @GET("search")
    fun getChannel(@Query("key") key: String,
                   @Query("channelId") channelId: String,
                   @Query("part") part: String,
                   @Query("order") order: String,
                   @Query("maxResults") maxResults: Int): Call<Map<String, Any>>

    @GET("videos")
    fun getStatistics(@Query("part") part: String,
                 @Query("id") id: String,
                 @Query("key") key: String): Call<Map<String, Any>>
}