package evereast.co.doctor.UserInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rating {

    @SerializedName("rating_id")
    @Expose
    private String ratingId;
    @SerializedName("patient_id")
    @Expose
    private String patientId;
    @SerializedName("appointment_id")
    @Expose
    private String appointmentId;
    @SerializedName("doctor_id")
    @Expose
    private String doctorId;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("feed_back")
    @Expose
    private String feedBack;

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }

}
