package com.done.recommendation.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_name")
    @Expose
    private String name;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile_number")
    @Expose
    private String mobile;
    @SerializedName("is_diabetes")
    @Expose
    private Integer isDiabetes;
    @SerializedName("is_cholestrol")
    @Expose
    private Integer isCholestrol;
    @SerializedName("is_kid")
    @Expose
    private Integer isKid;
    @SerializedName("is_senior")
    @Expose
    private Integer isSenior;
    @SerializedName("is_veg")
    @Expose
    private Integer isVeg;

    @SerializedName("is_question_done")
    @Expose
    private Integer isQuestionDone;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getIsDiabetes() {
        return isDiabetes;
    }

    public void setIsDiabetes(Integer isDiabetes) {
        this.isDiabetes = isDiabetes;
    }

    public Integer getIsCholestrol() {
        return isCholestrol;
    }

    public void setIsCholestrol(Integer isCholestrol) {
        this.isCholestrol = isCholestrol;
    }

    public Integer getIsKid() {
        return isKid;
    }

    public void setIsKid(Integer isKid) {
        this.isKid = isKid;
    }

    public Integer getIsSenior() {
        return isSenior;
    }

    public void setIsSenior(Integer isSenior) {
        this.isSenior = isSenior;
    }

    public Integer getIsVeg() {
        return isVeg;
    }

    public void setIsVeg(Integer isVeg) {
        this.isVeg = isVeg;
    }

    public Integer getIsQuestionDone() {
        return isQuestionDone;
    }

    public void setIsQuestionDone(Integer isQuestionDone) {
        this.isQuestionDone = isQuestionDone;
    }
}
