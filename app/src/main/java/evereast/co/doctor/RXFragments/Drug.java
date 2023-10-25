package evereast.co.doctor.RXFragments;

public class Drug {
    private String drugDirectoryId;
    private String brandName;
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

    public Drug() {
    }
}
