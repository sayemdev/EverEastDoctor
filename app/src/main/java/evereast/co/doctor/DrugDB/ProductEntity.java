package evereast.co.doctor.DrugDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Clap BD created by Sayem Hossen Saimon on 10/3/2021 at 7:36 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
@Entity
public class ProductEntity  implements Serializable {
    @NotNull
    @PrimaryKey()
    public String productId;
    @ColumnInfo(name = "productInfo")
    public String productInfo;

    public ProductEntity(String productId, String productInfo) {
        this.productId = productId;
        this.productInfo = productInfo;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }
}
