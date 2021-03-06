package com.antonis.bookaguide.wear;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.antonis.bookaguide.wear.data.Request;

public class MyRequests extends WearableActivity {
    private RequestAdapter requestAdapter;
    private ListView requestList;
    private static Request requestSelected;

    public static Request getRequestSelected() {
        return requestSelected;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_layout);
        requestList=findViewById(R.id.requestList);
        requestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                requestSelected=(Request) requestList.getItemAtPosition(position);
                Intent intent=new Intent(MyRequests.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
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
