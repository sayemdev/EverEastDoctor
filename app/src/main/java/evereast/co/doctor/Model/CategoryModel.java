package evereast.co.doctor.Model;

/**
 * Health Men created by Sayem Hossen Saimon on 12/24/2020 at 7:15 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class CategoryModel {
    String title,id;

    public CategoryModel() {
    }

    public CategoryModel(String title, String id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
