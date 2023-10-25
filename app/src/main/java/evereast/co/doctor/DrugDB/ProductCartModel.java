package evereast.co.doctor.DrugDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Entity
public class ProductCartModel implements Serializable {
    @NonNull
    @PrimaryKey()
    public String product_id;
    @ColumnInfo(name = "shop_id")
    public String shop_id;
    @ColumnInfo(name = "brand_id")
    public String brand_id;
    @ColumnInfo(name = "category_id")
    public String category_id;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "sku")
    public String sku;
    @ColumnInfo(name = "price")
    public String price;
    @ColumnInfo(name = "discount")
    public String discount;
    @ColumnInfo(name = "discountToWords")
    public String discountToWords;
    @ColumnInfo(name = "stock_count")
    public String stock_count;
    @ColumnInfo(name = "main_image")
    public String main_image;
    @ColumnInfo(name = "payment_type")
    public String payment_type;
    @ColumnInfo(name = "warranty")
    public String warranty;
    @ColumnInfo(name = "upload_date")
    public String upload_date;
    @ColumnInfo(name = "delivery_date")
    public String delivery_date;
    @ColumnInfo(name = "variation_id")
    public String variation_id;
    @ColumnInfo(name = "shop_name")
    public String shop_name;
    @ColumnInfo(name = "shop_logo")
    public String shop_logo;
    @ColumnInfo(name = "shop_cover")
    public String shop_cover;
    @ColumnInfo(name = "brand_logo")
    public String brand_logo;
    @ColumnInfo(name = "brand_cover")
    public String brand_cover;
    @ColumnInfo(name = "category_name")
    public String category_name;
    @ColumnInfo(name = "category_logo")
    public String category_logo;
    @ColumnInfo(name = "main_color")
    public String main_color;
    @ColumnInfo(name = "variationsArray")
    public String variationsArray;
    @ColumnInfo(name = "variationDataArray")
    public String variationDataArray;
    @ColumnInfo(name = "specificationsArray")
    public String specificationsArray;
    @ColumnInfo(name = "specificationDataArray")
    public String specificationDataArray;
    @ColumnInfo(name = "hasInWishList")
    public boolean hasInWishList;

    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "product_name")
    private String product_name;
    @ColumnInfo(name = "product_price")
    private String product_price;
    @ColumnInfo(name = "piece")
    private String piece;
    @ColumnInfo(name = "brand_name")
    private String brand_name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "image")
    private String image;
    @ColumnInfo(name = "itemCount")
    private String itemCount;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "isChecked")
    private boolean isChecked;

    public ProductCartModel() {
    }

    @Ignore
    public ProductCartModel(@NotNull String product_id, String shop_id, String brand_id, String category_id, String title, String sku, String price, String discount, String discountToWords, String stock_count, String main_image, String payment_type, String warranty, String upload_date, String delivery_date, String variation_id, String shop_name, String shop_logo, String shop_cover, String brand_logo, String brand_cover, String category_name, String category_logo, String main_color, String variationsArray, String variationDataArray, String specificationsArray, String specificationDataArray, boolean hasInWishList, String type, String product_name, String product_price, String piece, String brand_name, String description, String image, String itemCount) {
        this.product_id = product_id;
        this.shop_id = shop_id;
        this.brand_id = brand_id;
        this.category_id = category_id;
        this.title = title;
        this.sku = sku;
        this.price = price;
        this.discount = discount;
        this.discountToWords = discountToWords;
        this.stock_count = stock_count;
        this.main_image = main_image;
        this.payment_type = payment_type;
        this.warranty = warranty;
        this.upload_date = upload_date;
        this.delivery_date = delivery_date;
        this.variation_id = variation_id;
        this.shop_name = shop_name;
        this.shop_logo = shop_logo;
        this.shop_cover = shop_cover;
        this.brand_logo = brand_logo;
        this.brand_cover = brand_cover;
        this.category_name = category_name;
        this.category_logo = category_logo;
        this.main_color = main_color;
        this.variationsArray = variationsArray;
        this.variationDataArray = variationDataArray;
        this.specificationsArray = specificationsArray;
        this.specificationDataArray = specificationDataArray;
        this.hasInWishList = hasInWishList;
        this.type = type;
        this.product_name = product_name;
        this.product_price = product_price;
        this.piece = piece;
        this.brand_name = brand_name;
        this.description = description;
        this.image = image;
        this.itemCount = itemCount;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    @NotNull
    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(@NotNull String product_id) {
        this.product_id = product_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountToWords() {
        return discountToWords;
    }

    public void setDiscountToWords(String discountToWords) {
        this.discountToWords = discountToWords;
    }

    public String getStock_count() {
        return stock_count;
    }

    public void setStock_count(String stock_count) {
        this.stock_count = stock_count;
    }

    public String getMain_image() {
        return main_image;
    }

    public void setMain_image(String main_image) {
        this.main_image = main_image;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getVariation_id() {
        return variation_id;
    }

    public void setVariation_id(String variation_id) {
        this.variation_id = variation_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_logo() {
        return shop_logo;
    }

    public void setShop_logo(String shop_logo) {
        this.shop_logo = shop_logo;
    }

    public String getShop_cover() {
        return shop_cover;
    }

    public void setShop_cover(String shop_cover) {
        this.shop_cover = shop_cover;
    }

    public String getBrand_logo() {
        return brand_logo;
    }

    public void setBrand_logo(String brand_logo) {
        this.brand_logo = brand_logo;
    }

    public String getBrand_cover() {
        return brand_cover;
    }

    public void setBrand_cover(String brand_cover) {
        this.brand_cover = brand_cover;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_logo() {
        return category_logo;
    }

    public void setCategory_logo(String category_logo) {
        this.category_logo = category_logo;
    }

    public String getMain_color() {
        return main_color;
    }

    public void setMain_color(String main_color) {
        this.main_color = main_color;
    }

    public String getVariationsArray() {
        return variationsArray;
    }

    public void setVariationsArray(String variationsArray) {
        this.variationsArray = variationsArray;
    }

    public String getVariationDataArray() {
        return variationDataArray;
    }

    public void setVariationDataArray(String variationDataArray) {
        this.variationDataArray = variationDataArray;
    }

    public String getSpecificationsArray() {
        return specificationsArray;
    }

    public void setSpecificationsArray(String specificationsArray) {
        this.specificationsArray = specificationsArray;
    }

    public String getSpecificationDataArray() {
        return specificationDataArray;
    }

    public void setSpecificationDataArray(String specificationDataArray) {
        this.specificationDataArray = specificationDataArray;
    }

    public boolean isHasInWishList() {
        return hasInWishList;
    }

    public void setHasInWishList(boolean hasInWishList) {
        this.hasInWishList = hasInWishList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
