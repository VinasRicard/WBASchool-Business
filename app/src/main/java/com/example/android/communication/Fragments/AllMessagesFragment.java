package com.example.android.communication.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.communication.Adapters.MessageAdapter;
import com.example.android.communication.Classes.Chat;
import com.example.android.communication.Classes.SavedArchived;
import com.example.android.communication.Classes.SavedArchivedDB;
import com.example.android.communication.Classes.SavedChat;
import com.example.android.communication.Classes.UserProfile;
import com.example.android.communication.Notifications.Token;
import com.example.android.communication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AllMessagesFragment extends Fragment {

    private static final String TAG = "All Messages";

    private MessageAdapter messageAdapter;
    List<Chat> mMessages;
    List<String> mSeen;
    private List<Chat> chatList;
    List<String> mUsers;
    SavedArchivedDB SADB;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private ChildEventListener mChildEventListener;
    ArrayList<String> list;
    RecyclerView recyclerView;
    private String centerName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_all_messages_fragment, container, false);

        SADB = new SavedArchivedDB(getContext());


        //Recycle View
        recyclerView = view.findViewById(R.id.allMessagesRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Controlling Message's Users
        mUsers = new ArrayList<String>();

        //Firebase
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile user = dataSnapshot.getValue(UserProfile.class);
                checkArchived(firebaseUser.getUid());
                readChats(user.getUserName());
                try {
                    getView().findViewById(R.id.loadingPanel2).setVisibility(View.GONE);
                } catch (Exception E) {
                }
                try {
                    getView().findViewById(R.id.white2).setVisibility(View.GONE);
                } catch (Exception E) {
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        updateToken(FirebaseInstanceId.getInstance().getToken());


        return view;
    }

    private void checkArchived(final String userID) {

        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Archived Messages")) {

                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Archived Messages");
                    ChildEventListener mChildEventListener2 = new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot2, @Nullable String s) {
                            SavedChat archivedMessage = dataSnapshot2.getValue(SavedChat.class);
                            try {
                                list = SADB.verTodos();
                                if (!list.contains(archivedMessage.getSender()) && !list.contains(archivedMessage.getSenderID())) {
                                    SADB.insertar(archivedMessage);
                                }
                            } catch (Exception E) {
                                SADB.insertar(archivedMessage);
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
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    private void updateToken(String token) {
        Token tokenl = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token);
    }

    private void readChats(final String centerName) {
        mMessages = new ArrayList<>();
        mSeen = new ArrayList<>();
        mMessages.clear();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                if (chat.getReceiver().equals(centerName)) {
                    try {
                        list = SADB.verTodos();
                        if (!list.contains(chat.getSender()) && !list.contains(chat.getID1())) {
                            if (!chat.getSeen()) {
                                mSeen.add(chat.getSender());
                                mSeen.add(chat.getID1());
                            }
                            if (!mUsers.contains(chat.getID1() + chat.getSender())) {
                                mMessages.add(chat);
                                mUsers.add(chat.getID1() + chat.getSender());
                                Collections.reverse(mMessages);
                                MessageAdapter messageAdapter = new MessageAdapter(getActivity(), mMessages, "allmessages", mSeen);
                                recyclerView.setAdapter(messageAdapter);
                            }
                        }
                    } catch (Exception E) {
                        try {
                            if (!chat.getSeen()) {
                                mSeen.add(chat.getSender());
                                mSeen.add(chat.getID1());
                            }
                        } catch (Exception Ex) {
                            mSeen.add(chat.getSender());
                            mSeen.add(chat.getID1());
                        }
                        if (!mUsers.contains(chat.getID1() + chat.getSender())) {
                            mMessages.add(chat);
                            mUsers.add(chat.getID1() + chat.getSender());
                            MessageAdapter messageAdapter = new MessageAdapter(getActivity(), mMessages, "allmessages", mSeen);
                            recyclerView.setAdapter(messageAdapter);
                        }
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

}
