package evereast.co.doctor.HttpRetrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedicinesModel {

    @SerializedName("drug_directory_id")
    @Expose
    private String drugDirectoryId;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("price")
    @Expose
    private String price;

    public String getDrugDirectoryId() {
        return drugDirectoryId;
    }

    public void setDrugDirectoryId(String drugDirectoryId) {
        this.drugDirectoryId = drugDirectoryId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MedicinesModel.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("drugDirectoryId");
        sb.append('=');
        sb.append(((this.drugDirectoryId == null) ? "<null>" : this.drugDirectoryId));
        sb.append(',');
        sb.append("brandName");
        sb.append('=');
        sb.append(((this.brandName == null) ? "<null>" : this.brandName));
        sb.append(',');
        sb.append("price");
        sb.append('=');
        sb.append(((this.price == null) ? "<null>" : this.price));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}