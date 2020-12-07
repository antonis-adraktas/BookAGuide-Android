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
import com.antonis.bookaguide.data.Routes;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class RoutesAdapter extends BaseAdapter {
    private Activity activity;
    private DatabaseReference dbRoutes;

    private ArrayList<DataSnapshot> snapShotList;

    public RoutesAdapter(Activity activity) {
        this.activity = activity;
        this.dbRoutes = MainActivity.getDatabaseReference().child(MainActivity.DBROUTES);
        dbRoutes.addChildEventListener(listener);
        snapShotList=new ArrayList<>();
    }
    static class ViewHolder{
        TextView routesName;
        TextView onFoot;
        TextView places;
        TextView numStops;
    }

    private ChildEventListener listener=new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            snapShotList.add(snapshot);
            notifyDataSetChanged();
//            Log.d(MainActivity.LOGAPP, "notifyDataSetChanged called when onChildAdded");
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            snapShotList.remove(snapshot);
            notifyDataSetChanged();
//            Log.d(MainActivity.LOGAPP, "notifyDataSetChanged called when onChildRemoved");
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
    public Routes getItem(int position) {
        DataSnapshot snapshot=snapShotList.get(position);
        return snapshot.getValue(Routes.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            LayoutInflater inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.routes_view_row,parent,false);

            final RoutesAdapter.ViewHolder holder=new ViewHolder();
            holder.routesName=convertView.findViewById(R.id.routesName);
            holder.onFoot=convertView.findViewById(R.id.onFoot);
            holder.places=convertView.findViewById(R.id.places);
            holder.numStops=convertView.findViewById(R.id.numPlaces);
//            holder.params= (ConstraintLayout.LayoutParams) holder.guidesName.getLayoutParams();
            convertView.setTag(holder);
        }
        final Routes route= getItem(position);
        final RoutesAdapter.ViewHolder holder=(RoutesAdapter.ViewHolder) convertView.getTag();

        String name=route.getName();
        holder.routesName.setText(name);
        String onFoot;
        if (route.isOnFoot()){
            onFoot="On foot: Yes";
        }else{
            onFoot="On foot: No";
        }
        holder.onFoot.setText(onFoot);
        String places=route.getPlacesToVisit().toString();
        holder.places.setText(places);
        String numStops="Number of stops: "+String.valueOf(route.getNumPlaces());
        holder.numStops.setText(numStops);


        return convertView;
    }
    public void cleanup(){
        dbRoutes.removeEventListener(listener);
    }
}
