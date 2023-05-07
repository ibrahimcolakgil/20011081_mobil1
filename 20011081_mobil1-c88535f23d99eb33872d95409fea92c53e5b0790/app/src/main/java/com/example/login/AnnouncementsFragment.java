package com.example.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnnouncementsFragment extends Fragment implements AnnouncementsDialog.OnInputSelected {

    private FirebaseUser mUser;

    private FirebaseAuth mAuth;

    private DatabaseReference mReference;

    DatabaseReference database;

    Button newButton;
    private RecyclerView mRecyclerView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView;

    MyAdapterAnnouncements myAdapter;

    ArrayList<Announcements> list;

    ArrayList<Announcements> db_list;

    private String mParam1;
    private String mParam2;

    public AnnouncementsFragment() {
        // Required empty public constructor
    }

    public static AnnouncementsFragment newInstance(String param1, String param2) {
        AnnouncementsFragment fragment = new AnnouncementsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_announcements, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        newButton = (Button) rootView.findViewById(R.id.newPost);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        myAdapter = new MyAdapterAnnouncements(getActivity(), list);
        mRecyclerView.setAdapter(myAdapter);

        // Triggers when clicked 'New' button.
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        database = FirebaseDatabase.getInstance().getReference("announcements");
        db_list = new ArrayList<>();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Announcements ann = dataSnapshot.getValue(Announcements.class);
                    list.add(ann);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void openDialog(){
        AnnouncementsDialog annDialog = new AnnouncementsDialog();
        annDialog.setTargetFragment(AnnouncementsFragment.this, 1);
        annDialog.show(getFragmentManager(), "New Announcement");
    }

    @Override
    public void sendInput(String headLine, String content) {
        Announcements ann = new Announcements();
        ann.setHeadline(headLine);
        ann.setContent(content);
        //list.add(ann);

        mUser = mAuth.getCurrentUser();
        FirebaseDatabase.getInstance().getReference("announcements/").child(Integer.toString(list.size())).setValue(new Announcements(headLine, content));

    }
}