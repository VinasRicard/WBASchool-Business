package com.example.android.communication.Activities;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.communication.Adapters.ChatAdapter;
import com.example.android.communication.Classes.APIService;
import com.example.android.communication.Classes.Chat;
import com.example.android.communication.Classes.UserProfile;
import com.example.android.communication.Notifications.Data;
import com.example.android.communication.Notifications.MyResponse;
import com.example.android.communication.Notifications.Sender;
import com.example.android.communication.Notifications.Token;
import com.example.android.communication.Notifications.Client;
import com.example.android.communication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivity extends AppCompatActivity {


    private String chatType;
    private String userType;
    private TextView destinataryTv, userTv;
    private ImageView backBtn;
    private ImageView send;
    private EditText messageEt;
    private ImageView folder;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private String sender, receiver, receiverID, userID;
    ChatAdapter chatAdapter;
    List<Chat> mchat;
    List<Chat> mchatA;
    RecyclerView recyclerView;

    APIService apiService;
    boolean notify = false;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        //Find views
        messageEt = findViewById(R.id.message_et);
        recyclerView = findViewById(R.id.recyclerView);
        destinataryTv = findViewById(R.id.destinatary_Tv);
        userTv = findViewById(R.id.user_Tv);
        folder = findViewById(R.id.folder);
        backBtn = findViewById(R.id.backBtn_ChA);
        ImageView attatchFile = findViewById(R.id.attach_Btn);
        send = findViewById(R.id.send_Btn);

        //Manage the Recycler Viewer
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Default user type
        userType = "student";

        //Get the chat type
        final Intent intent = getIntent();
        chatType = intent.getStringExtra("chatType");

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        userID = firebaseUser.getUid();
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile user = dataSnapshot.getValue(UserProfile.class);
                //Get the user type
                assert user != null;
                userType = user.getAccountType();
                //Student or worker
                if (userType.equals("student") | userType.equals("worker")) {
                    //Get receiver and sender and set the TextViews
                    receiver = user.getUserCenter();
                    destinataryTv.setText(getResources().getString(R.string.toDestinatary) + receiver);
                    sender = "anonymous";
                    userTv.setText(getResources().getString(R.string.from) + getResources().getString(R.string.anonymous));
                    receiverID = "school/worker";
                    if (chatType.equals("username")) {
                        sender = user.getUserName();
                        userTv.setText(getResources().getString(R.string.from) + sender);
                    }
                    //Set the folder icon invisible
                    folder.setVisibility(View.INVISIBLE);
                    //School or company
                } else if (userType.equals("company") | userType.equals("school")) {
                    //Get receiver and sender and set the TextViews
                    receiverID = intent.getStringExtra("id2");
                    receiver = intent.getStringExtra("receiver");
                    sender = user.getUserName();
                    destinataryTv.setText(getResources().getString(R.string.toDestinatary) + receiver);
                    if (receiver.equals("anonymous")) {
                        destinataryTv.setText(getResources().getString(R.string.toDestinatary) + getResources().getString(R.string.anonymous));
                    }
                    userTv.setText(getResources().getString(R.string.from) + sender);
                }
                readMessages(userID, receiver, sender, chatType, receiverID, userType);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        //Folder Btn
        folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open the folderAddTo pop up window and send the receiver and receiverID to it
                Intent addToFolder = new Intent(ChatActivity.this, FolderAddToActivity.class);
                addToFolder.putExtra("receiverID2", receiverID);
                addToFolder.putExtra("receiver2", receiver);
                startActivity(addToFolder);
            }
        });


        //Change TextViews if chatType is anonymous
        if (chatType.equals("anonymous")) {
            userTv.setText(getResources().getString(R.string.from) + "Anonymous");
            messageEt.setHint(getResources().getString(R.string.writeAnonymous));
        }

        //Back Btn
        if (userType.equals("student") | userType.equals("worker")) {
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Clean the chatList and return to MainActivity
                    mchat.clear();
                    mchatA.clear();
                    Intent goBackToMain = new Intent(ChatActivity.this, MainActivity.class);
                    startActivity(goBackToMain);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
        } else {
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Clean the chatList and return to MainActivity2
                    mchat.clear();
                    Intent goBackToMain = new Intent(ChatActivity.this, MainActivity2.class);
                    startActivity(goBackToMain);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
        }


        //Attatch File Btn
        attatchFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this, getString(R.string.attatchFile), Toast.LENGTH_SHORT).show();
            }
        });

        //Send Btn
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String message = messageEt.getText().toString();
                if (!message.equals("")) {
                    sendMessage(sender, receiver, message, userID, receiverID);
                    messageEt.setText("");
                }
            }
        });


    }

    //The method called by the sendBtn
    //Add the chat object to the FirebaseDatabase
    private void sendMessage(String sender, String receiver, String message, String ID1, String ID2) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("ID1", ID1);
        hashMap.put("ID2", ID2);
        hashMap.put("seen", false);

        reference.push().setValue(hashMap);

        if (notify) {
            sendNotification(receiver);
        }
        notify = false;
    }

    //Send notification
    private void sendNotification(final String receiver) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    String title = "New message";
                    String body = "You have a new message";
                    Data data = new Data(firebaseUser.getUid().toString(), R.drawable.icon, title, body, receiver);
                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {
                                if (response.body().success == 1) {
                                    Toast.makeText(ChatActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Read the database to find the chats
    private void readMessages(final String ID1, final String receiver, final String sender, final String chatType, final String ID2, final String userType) {
        //Create the chatList and the chatListA
        mchat = new ArrayList<>();
        mchatA = new ArrayList<>();
        //Go to firebase database Chats folder
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        //For students and workers
        //For companies and schools
        ChildEventListener mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                String key = dataSnapshot.getKey();
                //For students and workers
                if (userType.equals("student") || userType.equals("worker")) {
                    if (chatType.equals("anonymous")) {
                        assert chat != null;
                        if (chat.getReceiver().equals(receiver) && chat.getID1().equals(ID1) && chat.getSender().equals("anonymous") || chat.getReceiver().equals(sender) && chat.getSender().equals(receiver) && chat.getID2().equals(ID1)) {
                            mchatA.add(chat);
                            chatAdapter = new ChatAdapter(ChatActivity.this, mchatA);
                            recyclerView.setAdapter(chatAdapter);
                        }
                    } else if (chatType.equals("username")) {
                        assert chat != null;
                        if (chat.getReceiver().equals(receiver) && chat.getID1().equals(ID1) && chat.getSender().equals(sender) || chat.getID2().equals(ID1) && chat.getSender().equals(receiver) && chat.getReceiver().equals(sender)) {
                            mchat.add(chat);
                            if (chat.getSender().equals(sender)) {
                            } else {
                            }
                            chatAdapter = new ChatAdapter(ChatActivity.this, mchat);
                            recyclerView.setAdapter(chatAdapter);
                        }
                    }
                }
                //For companies and schools
                if (userType.equals("school") || userType.equals("company")) {
                    if (chat.getReceiver().equals(receiver) && chat.getSender().equals(sender) && chat.getID2().equals(ID2) || chat.getReceiver().equals(sender) && chat.getSender().equals(receiver) && chat.getID1().equals(ID2)) {
                        if (chat.getReceiver().equals(sender)) {
                            chat.setSeen(true);
                            FirebaseDatabase.getInstance().getReference("Chats").child(key).setValue(chat);
                        }
                        mchat.add(chat);
                        if (chat.getSender().equals(sender)) {
                        } else {
                        }
                        chatAdapter = new ChatAdapter(ChatActivity.this, mchat);
                        recyclerView.setAdapter(chatAdapter);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        reference.addChildEventListener(mChildEventListener);

    }


    //Long click menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = ((int) ChatAdapter.getPosition());
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case 121:
                if (chatType.equals("username")) {
                    try {
                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Chats");
                        ChildEventListener mChildEventListener2 = new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot2, @Nullable String s) {
                                Chat chat = dataSnapshot2.getValue(Chat.class);
                                if (chat.getReceiver().equals(mchat.get(ChatAdapter.getPosition()).getReceiver()) && chat.getSender().equals(mchat.get(ChatAdapter.getPosition()).getSender()) && chat.getID2().equals(mchat.get(ChatAdapter.getPosition()).getID2()) && chat.getID1().equals(mchat.get(ChatAdapter.getPosition()).getID1()) && chat.getMessage().equals(mchat.get(ChatAdapter.getPosition()).getMessage())) {
                                    String key = dataSnapshot2.getKey();
                                    FirebaseDatabase.getInstance().getReference("Chats").child(key).removeValue();
                                    Toast.makeText(ChatActivity.this, getString(R.string.delete), Toast.LENGTH_SHORT).show();
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        };
                        reference2.addChildEventListener(mChildEventListener2);

                    } catch (Exception E) {
                        Toast.makeText(this, getString(R.string.cantDelete2), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.cantDelete), Toast.LENGTH_SHORT).show();
                }
                break;
            case 122:
                Toast.makeText(this, getString(R.string.copy), Toast.LENGTH_SHORT).show();
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(mchat.get(ChatAdapter.getPosition()).getMessage());
                break;
        }
        return super.onContextItemSelected(item);
    }

    //Back Btn
    @Override
    public void onBackPressed() {
        if (userType.equals("student") | userType.equals("worker")) {
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mchat.clear();
                    mchatA.clear();
                    Intent goBackToMain = new Intent(ChatActivity.this, MainActivity.class);
                    startActivity(goBackToMain);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
        } else {
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mchat.clear();
                    Intent goBackToMain = new Intent(ChatActivity.this, MainActivity2.class);
                    startActivity(goBackToMain);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
        }

    }

}
