package kashapps.news.saharanpur.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class is a singleton and is used to configure retorfit.
 * @author
 */

public class RestClient {

    private static ApiCall REST_CLIENT=null;
    private static String ROOT = "http://peaceful-hamlet-49025.herokuapp.com/api/v1/";
    //private static String ROOT = "http://192.168.1.10:3000/api/v1/";

    static {
        setupRestClient();
    }

    private RestClient() {}

    public static ApiCall get() {
        return REST_CLIENT;
    }

    public static void setupRestClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient
                    .Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        REST_CLIENT = retrofit.create(ApiCall.class);

    }
}