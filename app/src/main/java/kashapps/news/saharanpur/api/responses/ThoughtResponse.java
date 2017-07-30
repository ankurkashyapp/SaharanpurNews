package kashapps.news.saharanpur.api.responses;

/**
 * Created by ankur on 30/7/17.
 */

public class ThoughtResponse {
    private String thought_of_day;
    private String author;

    public String getThought_of_day() {
        return thought_of_day;
    }

    public void setThought_of_day(String thought_of_day) {
        this.thought_of_day = thought_of_day;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
