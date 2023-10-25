package evereast.co.doctor.Model;

/**
 * Health Men created by Sayem Hossen Saimon on 12/30/2020 at 8:51 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class UploadModel {
    String imageUrl,title;

    public UploadModel() {
    }

    public UploadModel(String imageUrl, String title) {
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
