package kashapps.news.saharanpur.api;

import kashapps.news.saharanpur.api.responses.NewsFeedResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ankur on 2/5/16.
 */
public interface ApiCall {

    @GET("news_feeds")
    Call<NewsFeedResponse> getNewsFeed(@Query("city") String city, @Query("page") String page);
}
