package com.example.geoff.ranchat;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/*
 * This ChatApp class is the Main Application class of this app. The onCreate
 * method initializes the server.
 */
public class ChatApp extends Application
{

    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate()
    {
        super.onCreate();
    }


}
