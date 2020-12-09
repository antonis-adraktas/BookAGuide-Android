package com.antonis.bookaguide.listAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.antonis.bookaguide.MainActivity;
import com.antonis.bookaguide.R;
import com.antonis.bookaguide.data.Request;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class RequestAdapter extends BaseAdapter {

    private Activity activity;
    private DatabaseReference dbRequests;

    private ArrayList<DataSnapshot> snapShotList;

    public RequestAdapter(Activity activity) {
        this.activity = activity;
        this.dbRequests = MainActivity.getDatabaseReference().child(MainActivity.DBREQUESTS).child(MainActivity.getAuth().getCurrentUser().getEmail().replace('.','_'));
        dbRequests.addChildEventListener(listener);
        snapShotList=new ArrayList<>();
    }
    static class ViewHolder{
        TextView date;
        TextView guide;
        TextView transport;
        TextView route;
    }

    private ChildEventListener listener=new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            snapShotList.add(snapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            snapShotList.remove(snapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };




    @Override
    public int getCount() {
        return snapShotList.size();
    }

    @Override
    public Request getItem(int position) {
        DataSnapshot snapshot=snapShotList.get(position);
        return snapshot.getValue(Request.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            LayoutInflater inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.request_list_per_row,parent,false);

            final ViewHolder holder=new ViewHolder();
            holder.date=convertView.findViewById(R.id.requestDate);
            holder.guide=convertView.findViewById(R.id.requestGuide);
            holder.transport=convertView.findViewById(R.id.requestTransport);
            holder.route=convertView.findViewById(R.id.requestRoute);
            convertView.setTag(holder);
        }
        final Request request= getItem(position);
        final ViewHolder holder=(ViewHolder) convertView.getTag();

        String requestDate=request.getDate();
        holder.date.setText(requestDate);

        String guide=request.getGuide().getName();
        holder.guide.setText(guide);

        String requestTransport=request.getTransport().getName();
        holder.transport.setText(requestTransport);

        String requestRoute=String.valueOf(request.getRoute().getNumPlaces());
        holder.route.setText("Number of stops: "+requestRoute);


        return convertView;
    }

    public void cleanup(){
        dbRequests.removeEventListener(listener);
    }
}
