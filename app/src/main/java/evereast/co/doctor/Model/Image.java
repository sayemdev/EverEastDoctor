package evereast.co.doctor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("ap_img_id")
    @Expose
    private String apImgId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("appointment_id")
    @Expose
    private String appointmentId;
    @SerializedName("image_name")
    @Expose
    private String imageName;

    public String getApImgId() {
        return apImgId;
    }

    public void setApImgId(String apImgId) {
        this.apImgId = apImgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Image.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("apImgId");
        sb.append('=');
        sb.append(((this.apImgId == null) ? "<null>" : this.apImgId));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null) ? "<null>" : this.title));
        sb.append(',');
        sb.append("appointmentId");
        sb.append('=');
        sb.append(((this.appointmentId == null) ? "<null>" : this.appointmentId));
        sb.append(',');
        sb.append("imageName");
        sb.append('=');
        sb.append(((this.imageName == null) ? "<null>" : this.imageName));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
