package evereast.co.doctor.Model;

/**
 * Doctor created by Sayem Hossen Saimon on 4/17/2021 at 4:28 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class TemplatesModel {
    String cc, ho, ix, dx, rx, ad, drName, drQualification, entryDate,fullJson;

    public TemplatesModel() {
    }

    public String getFullJson() {
        return fullJson;
    }

    public void setFullJson(String fullJson) {
        this.fullJson = fullJson;
    }

    public TemplatesModel(String cc, String ho, String ix, String dx, String rx, String ad, String drName, String drQualification, String entryDate) {
        this.cc = cc;
        this.ho = ho;
        this.ix = ix;
        this.dx = dx;
        this.rx = rx;
        this.ad = ad;
        this.drName = drName;
        this.drQualification = drQualification;
        this.entryDate = entryDate;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getIx() {
        return ix;
    }

    public void setIx(String ix) {
        this.ix = ix;
    }

    public String getDx() {
        return dx;
    }

    public void setDx(String dx) {
        this.dx = dx;
    }

    public String getRx() {
        return rx;
    }

    public void setRx(String rx) {
        this.rx = rx;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getDrName() {
        return drName;
    }

    public void setDrName(String drName) {
        this.drName = drName;
    }

    public String getDrQualification() {
        return drQualification;
    }

    public void setDrQualification(String drQualification) {
        this.drQualification = drQualification;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }
}
