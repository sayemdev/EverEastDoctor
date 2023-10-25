package evereast.co.doctor.Model;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Doctor created by Sayem Hossen Saimon on 7/26/2021 at 11:54 AM.
 * Email: saimonchowdhuryi96@gmail.com.
 * Phone: +8801882046404.
 **/
public class TemplateModel {
    private final Map<String, Object> additionalProperties = new HashMap<>();
    private String prescId;
    private String doctorName;
    private String doctorInfo;
    private String issueDate;
    private String cc;
    private String ho;
    private String ix;
    private String dx;
    private String rx;
    private String ad;
    private String prescriptionType;
    private String doctorId;
    private String patientIdForP;
    private String title;
    private String description;
    private String fullJson;

    public String getFullJson() {
        return fullJson;
    }

    public void setFullJson(String fullJson) {
        this.fullJson = fullJson;
    }

    public String getPrescId() {
        return prescId;
    }

    public void setPrescId(String prescId) {
        this.prescId = prescId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorInfo() {
        return doctorInfo;
    }

    public void setDoctorInfo(String doctorInfo) {
        this.doctorInfo = doctorInfo;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
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

    public String getPrescriptionType() {
        return prescriptionType;
    }

    public void setPrescriptionType(String prescriptionType) {
        this.prescriptionType = prescriptionType;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientIdForP() {
        return patientIdForP;
    }

    public void setPatientIdForP(String patientIdForP) {
        this.patientIdForP = patientIdForP;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @NotNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TemplateModel.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("prescId");
        sb.append('=');
        sb.append(((this.prescId == null) ? "<null>" : this.prescId));
        sb.append(',');
        sb.append("doctorName");
        sb.append('=');
        sb.append(((this.doctorName == null) ? "<null>" : this.doctorName));
        sb.append(',');
        sb.append("doctorInfo");
        sb.append('=');
        sb.append(((this.doctorInfo == null) ? "<null>" : this.doctorInfo));
        sb.append(',');
        sb.append("issueDate");
        sb.append('=');
        sb.append(((this.issueDate == null) ? "<null>" : this.issueDate));
        sb.append(',');
        sb.append("cc");
        sb.append('=');
        sb.append(((this.cc == null) ? "<null>" : this.cc));
        sb.append(',');
        sb.append("ho");
        sb.append('=');
        sb.append(((this.ho == null) ? "<null>" : this.ho));
        sb.append(',');
        sb.append("ix");
        sb.append('=');
        sb.append(((this.ix == null) ? "<null>" : this.ix));
        sb.append(',');
        sb.append("dx");
        sb.append('=');
        sb.append(((this.dx == null) ? "<null>" : this.dx));
        sb.append(',');
        sb.append("rx");
        sb.append('=');
        sb.append(((this.rx == null) ? "<null>" : this.rx));
        sb.append(',');
        sb.append("ad");
        sb.append('=');
        sb.append(((this.ad == null) ? "<null>" : this.ad));
        sb.append(',');
        sb.append("prescriptionType");
        sb.append('=');
        sb.append(((this.prescriptionType == null) ? "<null>" : this.prescriptionType));
        sb.append(',');
        sb.append("patientId");
        sb.append('=');
        sb.append(((this.doctorId == null) ? "<null>" : this.doctorId));
        sb.append(',');
        sb.append("patientIdForP");
        sb.append('=');
        sb.append(((this.patientIdForP == null) ? "<null>" : this.patientIdForP));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null) ? "<null>" : this.title));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null) ? "<null>" : this.description));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null) ? "<null>" : this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }
}
