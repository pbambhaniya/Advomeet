package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 27-10-2017.
 */

public class ChatMessage {
    private long id;
    private boolean isMe;
    private String message;
    private String receiver_id;
    private String userId;
    private String msgType, sendMsgType;
    private String dateTime;

    public String getSendMsgType() {
        return sendMsgType;
    }

    public void setSendMsgType(String sendMsgType) {
        this.sendMsgType = sendMsgType;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getIsme() {
        return isMe;
    }

    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return dateTime;
    }

    public void setDate(String dateTime) {
        this.dateTime = dateTime;
    }
}
