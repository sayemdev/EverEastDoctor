package evereast.co.doctor.Model;

/**
 * Health Men created by Sayem Hossen Saimon on 12/18/2020 at 3:52 PM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class DoctorsModel {

    String worksAt,doctorId, category, subcategory, profilePath, doctorName, about, designation, degree, bmdc, doctorPhone, doctorEmail, doctorFee, startingTime, endingTime, status;

    public DoctorsModel() {
    }

    public DoctorsModel(String doctorId, String category, String subcategory, String profilePath, String doctorName, String about, String designation, String degree, String bmdc, String doctorPhone, String doctorEmail, String doctorFee, String startingTime, String endingTime, String status) {
        this.doctorId = doctorId;
        this.category = category;
        this.subcategory = subcategory;
        this.profilePath = profilePath;
        this.doctorName = doctorName;
        this.about = about;
        this.designation = designation;
        this.degree = degree;
        this.bmdc = bmdc;
        this.doctorPhone = doctorPhone;
        this.doctorEmail = doctorEmail;
        this.doctorFee = doctorFee;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.status = status;
    }

    public String getWorksAt() {
        return worksAt;
    }

    public void setWorksAt(String worksAt) {
        this.worksAt = worksAt;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getBmdc() {
        return bmdc;
    }

    public void setBmdc(String bmdc) {
        this.bmdc = bmdc;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getDoctorFee() {
        return doctorFee;
    }

    public void setDoctorFee(String doctorFee) {
        this.doctorFee = doctorFee;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public String getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(String endingTime) {
        this.endingTime = endingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
