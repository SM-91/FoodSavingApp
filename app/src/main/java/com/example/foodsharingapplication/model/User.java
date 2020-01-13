package com.example.foodsharingapplication.model;

public class User {
    private String uId;
    private String userName;
    private String userPhoneNumber;
    private String userEmail;
    private String userProfilePicName;
    private String userProfilePicUrl;
    private String sex;
    private String userPassword;
    private String userDateOfBirth;
    private String userCountry;
    private String userCountryCurrency;
    private String userCountryISO;
    private String userCountryDialCode;
    private Boolean userWithFb;

    public String getUserCountryCurrency() {
        return userCountryCurrency;
    }

    public void setUserCountryCurrency(String userCountryCurrency) {
        this.userCountryCurrency = userCountryCurrency;
    }

    public String getUserCountryISO() {
        return userCountryISO;
    }

    public void setUserCountryISO(String userCountryISO) {
        this.userCountryISO = userCountryISO;
    }

    public String getUserCountryDialCode() {
        return userCountryDialCode;
    }

    public void setUserCountryDialCode(String userCountryDialCode) {
        this.userCountryDialCode = userCountryDialCode;
    }

    public Boolean getUserWithFb() {
        return userWithFb;
    }

    public void setUserWithFb(Boolean userWithFb) {
        this.userWithFb = userWithFb;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getUserDateOfBirth() {
        return userDateOfBirth;
    }

    public void setUserDateOfBirth(String userDateOfBirth) {
        this.userDateOfBirth = userDateOfBirth;
    }

    public User(){

    }
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserProfilePicName() {
        return userProfilePicName;
    }

    public void setUserProfilePicName(String userProfilePicName) {
        this.userProfilePicName = userProfilePicName;
    }

    public String getUserProfilePicUrl() {
        return userProfilePicUrl;
    }

    public void setUserProfilePicUrl(String userProfilePicUrl) {
        this.userProfilePicUrl = userProfilePicUrl;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }



    public String getUserName() {

        return userName;
    }

    public void setUserName(String username) {

        this.userName = username;
    }

    public String getUserEmail() {

        return userEmail;
    }

    public void setUserEmail(String userEmail) {

        this.userEmail = userEmail;
    }

    public String getUserPassword() {

        return userPassword;
    }

    public void setUserPassword(String userPassword) {

        this.userPassword = userPassword;
    }
}
