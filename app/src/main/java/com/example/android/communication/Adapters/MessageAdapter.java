package com.example.android.communication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.communication.Activities.ChatActivity;
import com.example.android.communication.Classes.Chat;
import com.example.android.communication.Classes.SavedArchivedDB;
import com.example.android.communication.Classes.SavedArchivedDB2;
import com.example.android.communication.Classes.SavedChat;
import com.example.android.communication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> implements View.OnClickListener {


    private Context mContext;
    private List<Chat> mMessages;
    private List<String> mSeen;
    private String activity;
    SavedArchivedDB SADB;
    SavedArchivedDB2 SADB2;
    Boolean isSaved;
    private ChildEventListener mChildEventListener;
    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mMessages, String activity, List<String> mSeen) {
        this.mMessages = mMessages;
        this.mContext = mContext;
        this.activity = activity;
        this.mSeen = mSeen;
    }

    //Inflate the views with the message_ma2_item style
    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.message_ma2_item, parent, false);
        return new MessageAdapter.ViewHolder(view);
    }

    //Information
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        //Variables
        final Chat chat = mMessages.get(position);
        SADB = new SavedArchivedDB(mContext);
        SADB2 = new SavedArchivedDB2(mContext);
        //Default isSaved
        isSaved = false;
        //Firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = firebaseUser.getUid();
        //Check saved
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
                            if (chat.getSender().equals(savedMessage.getSender()) && chat.getID1().equals(savedMessage.getSenderID())) {
                                holder.saveBtn.setImageResource(R.drawable.ic_bookmark_blue);
                                isSaved = true;
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
        //Set TextViews
        if (chat.getSender().equals("anonymous")) {
            holder.showSender.setText(R.string.anonymous);
        } else {
            holder.showSender.setText(chat.getSender());
        }

        //Set dot
        holder.dot.setVisibility(View.INVISIBLE);
        if (mSeen.contains(chat.getSender()) && mSeen.contains(chat.getID1())) {
            holder.dot.setVisibility(View.VISIBLE);
        }

        //Archive Btn
        holder.archivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.equals("archived")) {
                    //Delete the data from Firebase
                    final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(userID);
                    reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("Archived Messages")) {

                                final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Archived Messages");
                                ChildEventListener mChildEventListener2 = new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot2, @Nullable String s) {
                                        SavedChat archivedMessage = dataSnapshot2.getValue(SavedChat.class);
                                        if (archivedMessage.getSender().equals(chat.getSender()) && archivedMessage.getSenderID().equals(chat.getID1())) {
                                            String key = dataSnapshot2.getKey();
                                            FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Archived Messages").child(key).removeValue();
                                            SADB.deleteOne(chat.getSender(), chat.getID1());
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


                } else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Archived Messages");

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("sender", chat.getSender());
                    hashMap.put("senderID", chat.getID1());

                    reference.push().setValue(hashMap);
                }
            }
        });
        //Save Btn
        holder.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSaved) {
                    //Delete the data from Firebase
                    final DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Users").child(userID);
                    reference3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot3) {
                            if (dataSnapshot3.hasChild("Saved Messages")) {

                                final DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Saved Messages");
                                ChildEventListener mChildEventListener2 = new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot3, @Nullable String s) {
                                        SavedChat savedMessage = dataSnapshot3.getValue(SavedChat.class);
                                        if (savedMessage.getSender().equals(chat.getSender()) && savedMessage.getSenderID().equals(chat.getID1())) {
                                            String key = dataSnapshot3.getKey();
                                            FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Saved Messages").child(key).removeValue();
                                            SADB2.deleteOne2(chat.getSender(), chat.getID1());
                                            isSaved = false;

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
                                reference3.addChildEventListener(mChildEventListener2);
                            } else {
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                } else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Saved Messages");

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("sender", chat.getSender());
                    hashMap.put("senderID", chat.getID1());

                    reference.push().setValue(hashMap);

                    isSaved = true;
                }
            }
        });
        //Item Click Listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openChat = new Intent(mContext, ChatActivity.class);
                openChat.putExtra("id2", chat.getID1());
                if (chat.getID1().equals("school/company")) {
                    openChat.putExtra("receiver", chat.getReceiver());
                } else {
                    openChat.putExtra("receiver", chat.getSender());
                }
                if (chat.getSender().equals("anonymous") || chat.getReceiver().equals("anonymous")) {
                    openChat.putExtra("chatType", "anonymous");
                } else {
                    openChat.putExtra("chatType", "username");
                }
                mContext.startActivity(openChat);
                ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    //List size
    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    //On Click
    @Override
    public void onClick(View v) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Views
        public TextView showSender;
        public ImageView saveBtn;
        public ImageView archivedBtn;
        public ImageView dot;

        public ViewHolder(View itemView) {
            super(itemView);
            showSender = itemView.findViewById(R.id.showSender);
            saveBtn = itemView.findViewById(R.id.savingBtn);
            dot = itemView.findViewById(R.id.dot);
            archivedBtn = itemView.findViewById(R.id.archivingBtn);
            if (activity.equals("archived")) {
                archivedBtn.setImageResource(R.drawable.ic_archive_soft_blue);
            }
        }
    }

}