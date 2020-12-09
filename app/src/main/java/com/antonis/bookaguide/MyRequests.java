package com.antonis.bookaguide;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.antonis.bookaguide.data.Request;
import com.antonis.bookaguide.listAdapters.RequestAdapter;

public class MyRequests extends AppCompatActivity {
    private RequestAdapter requestAdapter;
    private ListView requestList;
    private Request requestSelected;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_layout);
        requestList=findViewById(R.id.requestList);
    }

    @Override
    public void onStart() {
        super.onStart();
        requestAdapter=new RequestAdapter(MyRequests.this);
        requestList.setAdapter(requestAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestAdapter=new RequestAdapter(MyRequests.this);
        requestList.setAdapter(requestAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        requestAdapter.cleanup();
    }
}
