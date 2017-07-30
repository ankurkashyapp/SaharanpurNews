package kashapps.news.saharanpur.api.responses;

/**
 * Created by ankur on 30/7/17.
 */

public class AppVersionResponse {
    private String app_name;
    private String latest_version;

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getLatest_version() {
        return latest_version;
    }

    public void setLatest_version(String latest_version) {
        this.latest_version = latest_version;
    }
}
