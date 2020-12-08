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
import androidx.core.content.ContextCompat;

import com.antonis.bookaguide.MainActivity;
import com.antonis.bookaguide.R;
import com.antonis.bookaguide.data.Transport;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class TransportAdapter extends BaseAdapter {
    private Activity activity;
    private DatabaseReference dbTransport;

    private ArrayList<DataSnapshot> snapShotList;

    public TransportAdapter(Activity activity) {
        this.activity = activity;
        this.dbTransport = MainActivity.getDatabaseReference().child(MainActivity.DBTRANSPORT);
        dbTransport.addChildEventListener(listener);
        snapShotList=new ArrayList<>();
    }

    static class ViewHolder{
        TextView transportName;
        TextView capacity;
//        ConstraintLayout.LayoutParams params;
    }
    private ChildEventListener listener=new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            snapShotList.add(snapshot);
            notifyDataSetChanged();
//            Log.d(MainActivity.LOGAPP, "notifyDataSetChanged called when onChildAdded from transport adapter");
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            snapShotList.remove(snapshot);
            notifyDataSetChanged();
//            Log.d(MainActivity.LOGAPP, "notifyDataSetChanged called when onChildRemoved from transport adapter");
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
    public Transport getItem(int position) {
        DataSnapshot snapshot=snapShotList.get(position);
        return snapshot.getValue(Transport.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            LayoutInflater inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.transport_list_view_row,parent,false);

            final TransportAdapter.ViewHolder holder=new ViewHolder();
            holder.transportName=convertView.findViewById(R.id.transportName);
            holder.capacity=convertView.findViewById(R.id.capacity);
//            holder.params= (ConstraintLayout.LayoutParams) holder.guidesName.getLayoutParams();
            convertView.setTag(holder);
        }

        final Transport transport= getItem(position);
        final TransportAdapter.ViewHolder holder=(TransportAdapter.ViewHolder) convertView.getTag();
        boolean isBooked;
        if (transport.getDatesBooked()==null){
            isBooked=false;
        }else{
            isBooked=transport.getDatesBooked().contains(MainActivity.getSelectedDate());
        }

        setGuideRowAppearance(isBooked,holder);

        String name=transport.getName();
        holder.transportName.setText(name);

        String capacity= "Capacity of guests: "+String.valueOf(transport.getCapacity());
        holder.capacity.setText(capacity);


        return convertView;
    }

    private void setGuideRowAppearance(boolean isItBooked, TransportAdapter.ViewHolder holder){
        if (isItBooked){
            holder.transportName.setBackgroundColor(ContextCompat.getColor(this.activity.getApplicationContext(),R.color.red));
        }else{
            holder.transportName.setBackgroundColor(ContextCompat.getColor(this.activity.getApplicationContext(),R.color.green));
        }
    }

    public void cleanup(){
        dbTransport.removeEventListener(listener);
    }
}
