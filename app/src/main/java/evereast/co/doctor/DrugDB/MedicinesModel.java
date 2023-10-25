package evereast.co.doctor.DrugDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class MedicinesModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid_id")
    private int uid;

    @SerializedName("drug_directory_id")
    @ColumnInfo(name = "drug_directory_id")
    @Expose
    private String drugDirectoryId;
    @SerializedName("brand_name")
    @ColumnInfo(name = "brand_name")
    @Expose
    private String brandName;
    @SerializedName("dosage_form")
    @ColumnInfo(name = "dosage_form")
    @Expose
    private String dosageForm;
    @SerializedName("strength")
    @ColumnInfo(name = "strength")
    @Expose
    private String strength;
    @SerializedName("company")
    @ColumnInfo(name = "company")
    @Expose
    private String company;
    @SerializedName("price")
    @ColumnInfo(name = "price")
    @Expose
    private String price;
    @SerializedName("generic_name")
    @ColumnInfo(name = "generic_name")
    @Expose
    private String genericName;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

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

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
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
        sb.append("dosageForm");
        sb.append('=');
        sb.append(((this.dosageForm == null) ? "<null>" : this.dosageForm));
        sb.append(',');
        sb.append("strength");
        sb.append('=');
        sb.append(((this.strength == null) ? "<null>" : this.strength));
        sb.append(',');
        sb.append("company");
        sb.append('=');
        sb.append(((this.company == null) ? "<null>" : this.company));
        sb.append(',');
        sb.append("price");
        sb.append('=');
        sb.append(((this.price == null) ? "<null>" : this.price));
        sb.append(',');
        sb.append("genericName");
        sb.append('=');
        sb.append(((this.genericName == null) ? "<null>" : this.genericName));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}