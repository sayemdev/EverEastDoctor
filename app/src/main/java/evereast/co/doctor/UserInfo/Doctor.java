package evereast.co.doctor.UserInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Doctor {

    @SerializedName("doctor_id")
    @Expose
    private String doctorId;
    @SerializedName("d_category")
    @Expose
    private String dCategory;
    @SerializedName("d_subcategory")
    @Expose
    private String dSubcategory;
    @SerializedName("d_image")
    @Expose
    private String dImage;
    @SerializedName("d_name")
    @Expose
    private String dName;
    @SerializedName("d_about")
    @Expose
    private String dAbout;
    @SerializedName("d_designation")
    @Expose
    private String dDesignation;
    @SerializedName("d_degree")
    @Expose
    private String dDegree;
    @SerializedName("d_higher_degree")
    @Expose
    private String dHigherDegree;
    @SerializedName("d_bmdc")
    @Expose
    private String dBmdc;
    @SerializedName("d_phone")
    @Expose
    private String dPhone;
    @SerializedName("d_email")
    @Expose
    private String dEmail;
    @SerializedName("d_fee")
    @Expose
    private String dFee;
    @SerializedName("d_starting_time")
    @Expose
    private String dStartingTime;
    @SerializedName("d_starting_time_format")
    @Expose
    private String dStartingTimeFormat;
    @SerializedName("d_ending_time")
    @Expose
    private String dEndingTime;
    @SerializedName("d_ending_time_format")
    @Expose
    private String dEndingTimeFormat;
    @SerializedName("d_password")
    @Expose
    private String dPassword;
    @SerializedName("d_active")
    @Expose
    private String dActive;
    @SerializedName("work_place")
    @Expose
    private String workPlace;
    @SerializedName("d_experience")
    @Expose
    private String dExperience;
    @SerializedName("d_status")
    @Expose
    private String dStatus;
    @SerializedName("available_on")
    @Expose
    private String availableOn;
    @SerializedName("d_android_token")
    @Expose
    private String dAndroidToken;
    @SerializedName("d_signature")
    @Expose
    private String dSignature;
    @SerializedName("total_earned")
    @Expose
    private String totalEarned;
    @SerializedName("after_charge_earned")
    @Expose
    private String afterChargeEarned;
    @SerializedName("withdraw")
    @Expose
    private String withdraw;
    @SerializedName("is_requested")
    @Expose
    private String isRequested;
    @SerializedName("approved_on")
    @Expose
    private String approvedOn;
    @SerializedName("temp_phone")
    @Expose
    private Object tempPhone;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("ratings")
    @Expose
    private List<Rating> ratings = null;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getdCategory() {
        return dCategory;
    }

    public void setdCategory(String dCategory) {
        this.dCategory = dCategory;
    }

    public String getdSubcategory() {
        return dSubcategory;
    }

    public void setdSubcategory(String dSubcategory) {
        this.dSubcategory = dSubcategory;
    }

    public String getdImage() {
        return dImage;
    }

    public void setdImage(String dImage) {
        this.dImage = dImage;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdAbout() {
        return dAbout;
    }

    public void setdAbout(String dAbout) {
        this.dAbout = dAbout;
    }

    public String getdDesignation() {
        return dDesignation;
    }

    public void setdDesignation(String dDesignation) {
        this.dDesignation = dDesignation;
    }

    public String getdDegree() {
        return dDegree;
    }

    public void setdDegree(String dDegree) {
        this.dDegree = dDegree;
    }

    public String getdHigherDegree() {
        return dHigherDegree;
    }

    public void setdHigherDegree(String dHigherDegree) {
        this.dHigherDegree = dHigherDegree;
    }

    public String getdBmdc() {
        return dBmdc;
    }

    public void setdBmdc(String dBmdc) {
        this.dBmdc = dBmdc;
    }

    public String getdPhone() {
        return dPhone;
    }

    public void setdPhone(String dPhone) {
        this.dPhone = dPhone;
    }

    public String getdEmail() {
        return dEmail;
    }

    public void setdEmail(String dEmail) {
        this.dEmail = dEmail;
    }

    public String getdFee() {
        return dFee;
    }

    public void setdFee(String dFee) {
        this.dFee = dFee;
    }

    public String getdStartingTime() {
        return dStartingTime;
    }

    public void setdStartingTime(String dStartingTime) {
        this.dStartingTime = dStartingTime;
    }

    public String getdStartingTimeFormat() {
        return dStartingTimeFormat;
    }

    public void setdStartingTimeFormat(String dStartingTimeFormat) {
        this.dStartingTimeFormat = dStartingTimeFormat;
    }

    public String getdEndingTime() {
        return dEndingTime;
    }

    public void setdEndingTime(String dEndingTime) {
        this.dEndingTime = dEndingTime;
    }

    public String getdEndingTimeFormat() {
        return dEndingTimeFormat;
    }

    public void setdEndingTimeFormat(String dEndingTimeFormat) {
        this.dEndingTimeFormat = dEndingTimeFormat;
    }

    public String getdPassword() {
        return dPassword;
    }

    public void setdPassword(String dPassword) {
        this.dPassword = dPassword;
    }

    public String getdActive() {
        return dActive;
    }

    public void setdActive(String dActive) {
        this.dActive = dActive;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getdExperience() {
        return dExperience;
    }

    public void setdExperience(String dExperience) {
        this.dExperience = dExperience;
    }

    public String getdStatus() {
        return dStatus;
    }

    public void setdStatus(String dStatus) {
        this.dStatus = dStatus;
    }

    public String getAvailableOn() {
        return availableOn;
    }

    public void setAvailableOn(String availableOn) {
        this.availableOn = availableOn;
    }

    public String getdAndroidToken() {
        return dAndroidToken;
    }

    public void setdAndroidToken(String dAndroidToken) {
        this.dAndroidToken = dAndroidToken;
    }

    public String getdSignature() {
        return dSignature;
    }

    public void setdSignature(String dSignature) {
        this.dSignature = dSignature;
    }

    public String getTotalEarned() {
        return totalEarned;
    }

    public void setTotalEarned(String totalEarned) {
        this.totalEarned = totalEarned;
    }

    public String getAfterChargeEarned() {
        return afterChargeEarned;
    }

    public void setAfterChargeEarned(String afterChargeEarned) {
        this.afterChargeEarned = afterChargeEarned;
    }

    public String getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(String withdraw) {
        this.withdraw = withdraw;
    }

    public String getIsRequested() {
        return isRequested;
    }

    public void setIsRequested(String isRequested) {
        this.isRequested = isRequested;
    }

    public String getApprovedOn() {
        return approvedOn;
    }

    public void setApprovedOn(String approvedOn) {
        this.approvedOn = approvedOn;
    }

    public Object getTempPhone() {
        return tempPhone;
    }

    public void setTempPhone(Object tempPhone) {
        this.tempPhone = tempPhone;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

}
