package com.example.familysafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ContactInfo extends AppCompatActivity implements ContactDailogue.ContactDialogueListner {
//    TextView txt1;
//    TextView txt2;
    String contact_name;
    String contact_no;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter adapter;

    String s1[];
    String s2[];
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_contact_info);
        recyclerView=(RecyclerView)findViewById(R.id.contacts_recycler);
        BottomNavigationView navigationView=findViewById(R.id.bottomNavView);
        navigationView.setSelectedItemId(R.id.contacts);
        fetch();

        //firebase realtime data base
//        txt1=findViewById(R.id.username_txt);
//        txt2=findViewById(R.id.password_txt);

       // s1=getResources().getStringArray(R.array.c_name);
       // s2=getResources().getStringArray(R.array.c_no);
        //ContactsAdapter contactsAdapter=new ContactsAdapter(this,s1,s2);
       // recyclerView.setAdapter(contactsAdapter);
       // recyclerView.setLayoutManager(new LinearLayoutManager(this));




        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.contacts:
                        return true;
                    case R.id.location:
                        startActivity(new Intent(getApplicationContext(),MapDashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.help:
                        startActivity(new Intent(getApplicationContext(),NeedHelp.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance().getReference().child("posts");
        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(query, new SnapshotParser<Model>() {
                    @NonNull
                    @NotNull
                    @Override
                    public Model parseSnapshot(@NonNull @NotNull DataSnapshot snapshot) {
                        return new Model(snapshot.child("Name").getValue().toString(),
                                snapshot.child("No").getValue().toString()
                        );
                    }
                }).build();

        adapter= new FirebaseRecyclerAdapter<Model,ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull Model model) {
                holder.setName(model.contactName);
                holder.setNumber(model.contactNo);
            }

            @NonNull
            @NotNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.contacts_layout,parent,false);
                return new ViewHolder(view);
            }
        };
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
            adapter.startListening();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_contacts:
                Toast.makeText(this,"Add Contacts",Toast.LENGTH_SHORT).show();
                openDialogue();
                return true;
            case R.id.logout:
                checkUser();
                firebaseAuth.signOut();
                Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void checkUser(){
        //check if user is already loggedIn
        //if  already loged in then open maps dashboard
        //get current user

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            startActivity(new Intent(this,signIn.class));
            finish();
        }
        else{
            startActivity(new Intent(this,MapDashboard.class));
            finish();
        }
    }
    public void openDialogue(){
        ContactDailogue contactDailogue= new ContactDailogue();
        contactDailogue.show(getSupportFragmentManager(),"Add Contacts");
    }

    @Override
    public void saveContact(String name, String no) {
//        txt1.setText(name);
//        txt2.setText(no);


        databaseReference= FirebaseDatabase.getInstance().getReference().child("posts").push();
        Map<String, Object> map= new HashMap<>();
        map.put("Name",name);
        map.put("No",no);
        databaseReference.setValue(map);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setHasFixedSize(true);
        //fetch();

    }


    }
