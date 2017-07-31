package kashapps.news.saharanpur.api.responses;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ankur on 30/7/17.
 */

public class FeedHeaderContentResponse {
    private String message_type;
    private ThoughtResponse thought;
    private List<AppVersionResponse> app_version;

    public String getMessageType() {
        return message_type;
    }

    public void setMessageType(String message_type) {
        this.message_type = message_type;
    }

    public ThoughtResponse getThought() {
        return thought;
    }

    public void setThought(ThoughtResponse thought) {
        this.thought = thought;
    }

    public List<AppVersionResponse> getApp_versions() {
        return app_version;
    }

    public void setApp_versions(List<AppVersionResponse> app_version) {
        this.app_version = app_version;
    }
}
