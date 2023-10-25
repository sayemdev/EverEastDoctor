package evereast.co.doctor.Drug;

/**
 * Hckvbj created by Sayem Hossen Saimon on 2/18/2021 at 9:18 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class Medicines {
    String brandName,dosageForm,strength,company,price1,price2,genericName;

    public Medicines() {
    }

    public Medicines(String brandName, String dosageForm, String strength, String company, String price1, String price2, String genericName) {
        this.brandName = brandName;
        this.dosageForm = dosageForm;
        this.strength = strength;
        this.company = company;
        this.price1 = price1;
        this.price2 = price2;
        this.genericName = genericName;
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

    public String getPrice1() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }
}
