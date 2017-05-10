package com.example.geoff.ranchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This UserList class is Activity class. It shows a list of all the users of
 * this app.
 */
public class UserList extends MainActivity
{

    DatabaseReference database;
    private ArrayList<ChatUser> uList;
    public static ChatUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        database  = FirebaseDatabase.getInstance().getReference();

        getActionBar().setDisplayHomeAsUpEnabled(false);

        updateUserStatus(true);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        updateUserStatus(false);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loadUserList();

    }

    private void updateUserStatus(boolean online)
    {
        // TODO: Add user status updates
    }

    private void loadUserList()
    {
        final ProgressDialog dia = ProgressDialog.show(this, null,
                getString(R.string.alert_loading));

        database.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {dia.dismiss();
                long size  = dataSnapshot.getChildrenCount();
                if(size == 0) {
                    Toast.makeText(UserList.this,
                            R.string.msg_no_user_found,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                uList = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    ChatUser user = ds.getValue(ChatUser.class);
                    Logger.getLogger(UserList.class.getName()).log(Level.ALL,user.getUsername());
                    if(!user.getId().contentEquals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        uList.add(user);
                }
                ListView list = (ListView) findViewById(R.id.list);
                list.setAdapter(new UserAdapter());
                list.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0,
                                            View arg1, int pos, long arg3)
                    {
                        startActivity(new Intent(UserList.this,
                                Chat.class).putExtra(
                                Constant.EXTRA_DATA,  uList.get(pos)));
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private class UserAdapter extends BaseAdapter
    {


        @Override
        public int getCount()
        {
            return uList.size();
        }

        @Override
        public ChatUser getItem(int arg0)
        {
            return uList.get(arg0);
        }

        @Override
        public long getItemId(int arg0)
        {
            return arg0;
        }

        @Override
        public View getView(int pos, View v, ViewGroup arg2)
        {
            if (v == null)
                v = getLayoutInflater().inflate(R.layout.chat_item, null);

            ChatUser c = getItem(pos);
            TextView lbl = (TextView) v;
            lbl.setText(c.getUsername());
            lbl.setCompoundDrawablesWithIntrinsicBounds(
                    c.isOnline() ? R.drawable.ic_online
                            : R.drawable.ic_offline, 0, R.drawable.arrow, 0);

            return v;
        }

    }
}

