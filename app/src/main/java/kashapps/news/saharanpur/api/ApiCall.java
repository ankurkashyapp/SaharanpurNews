package kashapps.news.saharanpur.api;

import kashapps.news.saharanpur.api.responses.FeedContent;
import kashapps.news.saharanpur.api.responses.FeedHeaderContentResponse;
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

    @GET("news")
    Call<FeedContent> getSingleNewsArticle(@Query("city") String city, @Query("news_id") String newsId);

    @GET("get_latest_app")
    Call<FeedHeaderContentResponse> getFeedHeaderResponse(@Query("app_name") String appName, @Query("installed_version") String installedVersion);
}
