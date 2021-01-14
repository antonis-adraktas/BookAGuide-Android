package com.antonis.bookaguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.antonis.bookaguide.data.Request;
import com.antonis.bookaguide.listAdapters.RequestAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MyRequests extends AppCompatActivity {
    private RequestAdapter requestAdapter;
    private ListView requestList;
    private static Request requestSelected;
    private FirebaseAnalytics mFirebaseAnalytics;

    public static Request getRequestSelected() {
        return requestSelected;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_layout);
        requestList=findViewById(R.id.requestList);
        mFirebaseAnalytics=FirebaseAnalytics.getInstance(this);
        requestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                requestSelected=(Request) requestList.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "My_trip_access");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                Intent intent=new Intent(MyRequests.this,RequestMap.class);
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
