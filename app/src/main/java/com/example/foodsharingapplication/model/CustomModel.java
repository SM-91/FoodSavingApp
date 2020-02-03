package com.example.foodsharingapplication.model;

public class CustomModel {

    private ChatModel chatModel;
    private UserUploadFoodModel userUploadFoodModel;
    private User user;
    private BidModel bidModel;

    public ChatModel getChatModel() {
        return chatModel;
    }

    public void setChatModel(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public UserUploadFoodModel getUserUploadFoodModel() {
        return userUploadFoodModel;
    }

    public void setUserUploadFoodModel(UserUploadFoodModel userUploadFoodModel) {
        this.userUploadFoodModel = userUploadFoodModel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BidModel getBidModel() {
        return bidModel;
    }

    public void setBidModel(BidModel bidModel) {
        this.bidModel = bidModel;
    }
}
