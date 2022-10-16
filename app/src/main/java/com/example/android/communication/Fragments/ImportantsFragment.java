package com.example.android.communication.Fragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.communication.Adapters.MessageAdapter;
import com.example.android.communication.Classes.Chat;
import com.example.android.communication.Classes.SavedArchivedDB2;
import com.example.android.communication.Classes.SavedChat;
import com.example.android.communication.Classes.UserProfile;
import com.example.android.communication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImportantsFragment extends Fragment {

    private static final String TAG = "All Messages";

    //Controlling the messages
    private MessageAdapter messageAdapter2;
    List<Chat> mMessages2;
    List<String> mSeen;
    private List<Chat> chatList2;
    List<String> mUsers2;

    SavedArchivedDB2 SADB2;


    //Set up Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private ChildEventListener mChildEventListener;
    ArrayList<String> list2;

    //View
    RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.importants_fragment, container, false);

        SADB2 = new SavedArchivedDB2(getContext());


        //Recycle View
        recyclerView = view.findViewById(R.id.importantMessagesRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Controlling Message's Users
        mUsers2 = new ArrayList<String>();


        //Firebase
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile user = dataSnapshot.getValue(UserProfile.class);
                checkSaved(firebaseUser.getUid());
                readChats(user.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void checkSaved(final String userID) {

        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Saved Messages")) {

                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Saved Messages");
                    ChildEventListener mChildEventListener2 = new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot2, @Nullable String s) {
                            SavedChat savedMessage = dataSnapshot2.getValue(SavedChat.class);
                            SADB2.insertar2(savedMessage);
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

    private void readChats(final String centerName) {
        mMessages2 = new ArrayList<>();
        mSeen = new ArrayList<>();
        mMessages2.clear();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                if (chat.getReceiver().equals(centerName)) {
                    try {
                        list2 = SADB2.verTodos2();
                        if (list2.contains(chat.getSender()) && list2.contains(chat.getID1())) {
                            if (!chat.getSeen()) {
                                mSeen.add(chat.getSender());
                                mSeen.add(chat.getID1());
                            }
                            if (!mUsers2.contains(chat.getID1() + chat.getSender())) {
                                mMessages2.add(chat);
                                mUsers2.add(chat.getID1() + chat.getSender());
                                MessageAdapter messageAdapter = new MessageAdapter(getActivity(), mMessages2, "saved", mSeen);
                                recyclerView.setAdapter(messageAdapter);
                            }
                        }
                    } catch (Exception E) {
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

