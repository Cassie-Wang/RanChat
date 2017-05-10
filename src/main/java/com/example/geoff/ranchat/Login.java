package com.example.geoff.ranchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * The Login class is an Activity that shows the login screen to the chat users.
 * The current implementation is simply including the options for Login and button
 * for Registration. On login button click, it sends the Login details to the
 * server to verify user.
 */
public class Login extends MainActivity
{

    private EditText user;
    private EditText password;
    private ProgressDialog loginProDia;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        setTouchNClick(R.id.btnLogin);
        setTouchNClick(R.id.btnReg);

        user = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.pwd);

    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v.getId() == R.id.btnReg)
        {
            startActivityForResult(new Intent(this, Register.class), 10);
        }
        else
        {
            String user = this.user.getText().toString();
            String password = this.password.getText().toString();
            if (user.length() == 0 || password.length() == 0)
            {
                Utils.showDialog(this, R.string.err_fields_empty);
                return;
            }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(user, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Logger.getLogger(Login.class.getName()).log(Level.ALL, "signInWithEmail:onComplete:" + task.isSuccessful());
                            loginProDia.dismiss();
                            if (!task.isSuccessful()) {

                                Logger.getLogger(Login.class.getName()).log(Level.ALL, "signInWithEmail", task.getException());
                                Toast.makeText(Login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else {
                                ArrayList<String> defaultRoom = new ArrayList<String>();
                                defaultRoom.add("home");
                                UserList.user = new ChatUser(task.getResult().getUser().getUid(),task.getResult().getUser().getDisplayName(), task.getResult().getUser().getEmail(),true,defaultRoom);
                                startActivity(new Intent(Login.this, UserList.class));
                                finish();
                            }

                        }
                    });

            loginProDia = ProgressDialog.show(this, null,
                    getString(R.string.alert_wait));

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK)
            finish();

    }
}
