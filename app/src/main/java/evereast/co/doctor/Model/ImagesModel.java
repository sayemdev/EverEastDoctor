package evereast.co.doctor.Model;

import android.graphics.Bitmap;

/**
 * Health Men created by Sayem Hossen Saimon on 12/21/2020 at 10:21 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class ImagesModel {
    Bitmap bitmap;
    boolean isBig;
    String title, imageUrl;

    public ImagesModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImagesModel(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isBig() {
        return isBig;
    }

    public void setBig(boolean big) {
        isBig = big;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
