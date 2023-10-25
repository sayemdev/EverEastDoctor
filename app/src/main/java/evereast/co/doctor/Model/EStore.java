package evereast.co.doctor.Model;

/**
 * Doctor created by Sayem Hossen Saimon on 3/13/2021 at 7:23 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class EStore {
    String price,name,link,image;

    public EStore() {
    }

    public EStore(String price, String name, String link, String image) {
        this.price = price;
        this.name = name;
        this.link = link;
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
