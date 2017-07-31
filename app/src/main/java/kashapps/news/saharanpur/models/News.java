package kashapps.news.saharanpur.models;

import android.util.Log;

import kashapps.news.saharanpur.activities.MainActivity;
import kashapps.news.saharanpur.adapters.NewsFeedAdapter;
import kashapps.news.saharanpur.api.RestClient;
import kashapps.news.saharanpur.api.responses.FeedContent;
import kashapps.news.saharanpur.api.responses.FeedHeaderContentResponse;
import kashapps.news.saharanpur.api.responses.NewsFeedResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ankur on 30/7/17.
 */

public class News {

    public static void loadNewsFeeds(String city, String page, final FeedsLoaded feedsLoaded) {
        Call<NewsFeedResponse> call = RestClient.get().getNewsFeed(city, page);
        call.enqueue(new Callback<NewsFeedResponse>() {
            int i=1;
            @Override
            public void onResponse(Call<NewsFeedResponse> call, Response<NewsFeedResponse> response) {
                feedsLoaded.onFeedsLoadSuccess(response.body());
            }

            @Override
            public void onFailure(Call<NewsFeedResponse> call, Throwable t) {
                Log.e("*********Failure", "Failure");
                if (i<=3) {
                    Log.e("***********Inside RETRY" , ""+i);
                    call.clone().enqueue(this);
                }
                if (i > 3) {
                    Log.e("***Inside OnLoadFailure" , ""+i);
                    feedsLoaded.onNewsLoadFailure();
                }
                i++;

            }
        });
    }

    public static void getSingleNews(String city, String newsId, final SingleNewsLoad singleNewsLoad) {
        Call<FeedContent> call = RestClient.get().getSingleNewsArticle(city, newsId);
        call.enqueue(new Callback<FeedContent>() {
            int i=1;
            @Override
            public void onResponse(Call<FeedContent> call, Response<FeedContent> response) {
                singleNewsLoad.onSingleNewsSuccess(response.body());
            }

            @Override
            public void onFailure(Call<FeedContent> call, Throwable t) {
                if (i<=3) {
                    Log.e("***********Inside RETRY" , ""+i);
                    call.clone().enqueue(this);
                }
                if (i > 3) {
                    Log.e("***Inside OnLoadFailure" , ""+i);
                    singleNewsLoad.onSingleNewsFailure();
                }
                i++;

            }
        });
    }

    public static void getFeedHeaderContent(String appName, String installedVersion, final FeedHeaderLoad feedHeaderLoad) {
        Call<FeedHeaderContentResponse> call = RestClient.get().getFeedHeaderResponse(appName, installedVersion);
        call.enqueue(new Callback<FeedHeaderContentResponse>() {
            int i=1;
            @Override
            public void onResponse(Call<FeedHeaderContentResponse> call, Response<FeedHeaderContentResponse> response) {
                feedHeaderLoad.onFeedHeaderContentLoaded(response.body());
            }

            @Override
            public void onFailure(Call<FeedHeaderContentResponse> call, Throwable t) {
                if (i<=3) {
                    Log.e("***********Inside RETRY" , ""+i);
                    call.clone().enqueue(this);
                }
                if (i > 3) {
                    Log.e("***Inside OnLoadFailure" , ""+i);
                    feedHeaderLoad.onFeedNotLoaded();
                }
                i++;
            }
        });
    }

    public interface FeedsLoaded {
        void onFeedsLoadSuccess(NewsFeedResponse newsFeedResponse);
        void onNewsLoadFailure();
    }

    public interface SingleNewsLoad {
        void onSingleNewsSuccess(FeedContent feedContent);
        void onSingleNewsFailure();
    }

    public interface FeedHeaderLoad {
        void onFeedHeaderContentLoaded(FeedHeaderContentResponse contentResponse);
        void onFeedNotLoaded();
    }
}
