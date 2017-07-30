package kashapps.news.saharanpur.api.responses;

import java.util.List;

/**
 * Created by ankur on 30/7/17.
 */

public class FeedHeaderContentResponse {
    private String messageType;
    private ThoughtResponse thought;
    private List<AppVersionResponse> app_version;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
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
