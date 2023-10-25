package evereast.co.doctor.Model;

/**
 * Doctor created by Sayem Hossen Saimon on 3/13/2021 at 7:20 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class NotificationModel {
    String title, body, image, link;

    public NotificationModel() {

    }

    public NotificationModel(String title, String body, String image) {
        this.title = title;
        this.body = body;
        this.image = image;
    }

    public NotificationModel(String title, String body, String image, String link) {
        this.title = title;
        this.body = body;
        this.image = image;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
