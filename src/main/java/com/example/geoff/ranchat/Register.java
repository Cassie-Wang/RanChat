package com.example.geoff.ranchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * This Register is the Activity class that displays the users' registration screen
 * that allows users to register itself on the server for this Chat app.
 */
public class Register extends MainActivity
{

    private EditText password;
    private EditText email;
    private EditText displayName;
    private ProgressDialog registerProDia;
    private LoginButton login_button;
    CallbackManager callbackManager;
private void initialControls(){
    login_button = (LoginButton)findViewById(R.id.login_button);
    callbackManager = CallbackManager.Factory.create();
}

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        setTouchNClick(R.id.btnReg);

        initialControls();
        FacebookSdk.sdkInitialize(getApplicationContext());
        loginWithFB();


        password = (EditText) findViewById(R.id.pwd);
        email = (EditText) findViewById(R.id.email);
        displayName = (EditText) findViewById(R.id.displayName);


    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);


        final String password = this.password.getText().toString();
        final String email = this.email.getText().toString();
        final String displayName = this.displayName.getText().toString();

        if ( password.length() == 0 || email.length() == 0 || displayName.length() == 0)
        {
            Utils.showDialog(this, R.string.err_fields_empty);
            return;
        }


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password) .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Logger.getLogger(Login.class.getName()).log(Level.ALL, "createUserWithEmailAndPassword:onComplete:" + task.isSuccessful());
                registerProDia.dismiss();
                if (!task.isSuccessful()) {
                    Logger.getLogger(Register.class.getName()).log(Level.ALL, "createUserWithEmailAndPassword", task.getException());
                    Utils.showDialog(
                            Register.this,
                            getString(R.string.err_singup));
                }
                else {
                    final ArrayList<String> defaultRoom = new ArrayList<String>();
                    defaultRoom.add("home");


                    final FirebaseUser user = task.getResult().getUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName)
                            .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                            .build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Logger.getLogger(Register.class.getName()).log(Level.ALL, "User profile updated.");
                                UserList.user = new ChatUser(user.getUid(),displayName, email,true,defaultRoom);
                                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).setValue(UserList.user);
                                startActivity(new Intent(Register.this, UserList.class));
                                finish();
                            }
                        }
                    });

                }

            }
        });

        registerProDia = ProgressDialog.show(this, null,
                getString(R.string.alert_wait));
    }
    private void loginWithFB(){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
