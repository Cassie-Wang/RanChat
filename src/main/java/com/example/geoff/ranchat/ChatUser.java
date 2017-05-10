package com.example.geoff.ranchat;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * The Class ChatUser represents a single user.
 */
@IgnoreExtraProperties
public class ChatUser implements Serializable {

    public String id;
    public String username;
    public String email;
    public Boolean online;
    public ArrayList<String> chatRoom;

    public ChatUser() {
    }

    public ChatUser(String id, String username, String email, Boolean online, ArrayList<String> room) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.online = online;
        this.chatRoom = room;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Boolean isOnline() {
        return online;
    }

    public ArrayList<String> getChatRoom() {
        return chatRoom;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public void setChatRoom(ArrayList<String> chatRoom) {
        this.chatRoom = chatRoom;
    }

}
