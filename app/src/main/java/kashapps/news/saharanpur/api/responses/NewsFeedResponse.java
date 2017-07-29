package kashapps.news.saharanpur.api.responses;

import java.util.List;

/**
 * Created by ankur on 29/7/17.
 */

public class NewsFeedResponse {
    private String message;
    private List<FeedContent> content;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FeedContent> getContent() {
        return content;
    }

    public void setContent(List<FeedContent> content) {
        this.content = content;
    }
}
