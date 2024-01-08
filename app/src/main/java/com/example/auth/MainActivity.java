package com.example.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    SearchView search;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        search = findViewById(R.id.search);
        search.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();

        adapter = new MyAdapter(MainActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for(DataSnapshot itemsSnapshot: snapshot.getChildren()){
                    DataClass dataClass = itemsSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemsSnapshot.getKey());
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(i);
            }
        });

    }

    public void searchList(String text){
        ArrayList<DataClass> searchList = new ArrayList<>();
        for (DataClass dataClass: dataList){
            if (dataClass.getDataTitle().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }
}








//    FirebaseAuth auth;
//    Button btn;
//    TextView textView;
//    FirebaseUser user;


// auth = FirebaseAuth.getInstance();
//         btn= findViewById(R.id.logout);
//         textView = findViewById(R.id.user_details);
//         user = auth.getCurrentUser();
//
//         if(user == null) {
//         Intent i = new Intent(getApplicationContext(), Login.class);
//        startActivity(i);
//        finish();
//        }else{
//        textView.setText(user.getEmail());
//        }
//
//        btn.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        FirebaseAuth.getInstance().signOut();
//        Intent i = new Intent(getApplicationContext(), Login.class);
//        startActivity(i);
//        finish();
//        }
//        });