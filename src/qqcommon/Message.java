package qqcommon;

import java.io.Serializable;

public class Message implements Serializable,MessageType {
    private static final long serialVersionUID = 1L;
    private String sender;//发送方
    private String receiver;//接收方
    private String content;//内容
    private String messageType;//消息的种类
    private byte[] file;//文件内容
    private int readLen;//文件大小

    public Message(String sender, String receiver, String content, String messageType) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.messageType = messageType;
    }

    public Message() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
