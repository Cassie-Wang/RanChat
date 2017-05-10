package com.example.geoff.ranchat;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/*
 * This Chat class is to hold main chat screen in the Activity. This shows
 * all the conversation messages among users and also allows the user to
 * send and receive messages.
 */
public class Chat extends MainActivity {

    private ArrayList<Conversation> converList;
    private ChatAdapter adap;
    private EditText ediTxt;
    private ChatUser chatBuddies;
    private Date lastMessageDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        converList = new ArrayList<>();
        ListView list = (ListView) findViewById(R.id.list);
        adap = new ChatAdapter();
        list.setAdapter(adap);
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);

        ediTxt = (EditText) findViewById(R.id.txt);
        ediTxt.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        setTouchNClick(R.id.buttonSend);

        chatBuddies = (ChatUser) getIntent().getSerializableExtra(Constant.EXTRA_DATA);

        ActionBar actionBar = getActionBar();
        if(actionBar != null)
            actionBar.setTitle(chatBuddies.getUsername());

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadConversationList();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.buttonSend) {
            sendMessage();
        }

    }

    /*
     * this method is to send message to other chat user. It does nothing if the text
     * is empty otherwise it creates a Parse object for Chat message and send it
     * to Parse server.
     */
    private void sendMessage() {
        if (ediTxt.length() == 0)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ediTxt.getWindowToken(), 0);

        String s = ediTxt.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            final Conversation conversation = new Conversation(s,
                    Calendar.getInstance().getTime(),
                    user.getUid(),
                    chatBuddies.getId(),
                    "");
            conversation.setStatus(Conversation.STATUS_SENDING);
            converList.add(conversation);
            final String key = FirebaseDatabase.getInstance()
                    .getReference("messages")
                    .push().getKey();
            FirebaseDatabase.getInstance().getReference("messages").child(key)
                    .setValue(conversation)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        converList.get(converList.indexOf(conversation)).setStatus(Conversation.STATUS_SENT);
                        } else {
                         converList.get(converList.indexOf(conversation)).setStatus(Conversation.STATUS_FAILED);
                          }
                          FirebaseDatabase.getInstance()
                          .getReference("messages")
                          .child(key).setValue(converList.get(converList.indexOf(conversation)))
                          .addOnCompleteListener(new
                                                                                          OnCompleteListener<Void>() {
                                                                                              @Override
                                                                                              public void onComplete(@NonNull Task<Void> task) {
                          adap.notifyDataSetChanged();
                           }
                                                });
     }
                                           }
                    );
        }
        adap.notifyDataSetChanged();
        ediTxt.setText(null);
    }

    /*
     * Loading the conversation list from the server and save the date of last
     * message that will be used to load only recent new messages
     */
    private void loadConversationList() {

        FirebaseDatabase.getInstance().getReference("messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Conversation conversation = ds.getValue(Conversation.class);
                        if (conversation.getReceiver().contentEquals(user.getUid()) || conversation.getSender().contentEquals(user.getUid())) {
                            converList.add(conversation);
                            if (lastMessageDate == null
                                    || lastMessageDate.before(conversation.getDate()))
                                lastMessageDate = conversation.getDate();

                            adap.notifyDataSetChanged();

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /*
     * The Class ChatAdapter is the adapter class for Chat ListView. This
     * adapter shows the Sent or Receive Chat message.
     */
    private class ChatAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return converList.size();
        }

        @Override
        public Conversation getItem(int arg0) {
            return converList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @SuppressLint("InflateParameters")
        @Override
        public View getView(int pos, View v, ViewGroup arg2) {
            Conversation c = getItem(pos);
            if (c.isSent())
                v = getLayoutInflater().inflate(R.layout.chat_item_sent, null);
            else
                v = getLayoutInflater().inflate(R.layout.chat_item_rcv, null);

            TextView label = (TextView) v.findViewById(R.id.lbl1);
            label.setText(DateUtils.getRelativeDateTimeString(Chat.this, c
                            .getDate().getTime(), DateUtils.SECOND_IN_MILLIS,
                    DateUtils.DAY_IN_MILLIS, 0));

            label = (TextView) v.findViewById(R.id.lbl2);
            label.setText(c.getMsg());

            label = (TextView) v.findViewById(R.id.lbl3);
            if (c.isSent()) {
                if (c.getStatus() == Conversation.STATUS_SENT)
                    label.setText(R.string.delivered_text);
                else {
                    if (c.getStatus() == Conversation.STATUS_SENDING)
                        label.setText(R.string.sending_text);
                    else {
                        label.setText(R.string.failed_text);
                    }
                }
            } else
                label.setText("");

            return v;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
