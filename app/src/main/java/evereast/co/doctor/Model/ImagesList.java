package evereast.co.doctor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImagesList {

    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("success")
    @Expose
    private String success;

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ImagesList.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("images");
        sb.append('=');
        sb.append(((this.images == null) ? "<null>" : this.images));
        sb.append(',');
        sb.append("success");
        sb.append('=');
        sb.append(((this.success == null) ? "<null>" : this.success));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
