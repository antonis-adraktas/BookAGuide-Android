package com.antonis.bookaguide.listAdapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
import com.antonis.bookaguide.data.Guides;
import com.antonis.bookaguide.tabView.GuidesFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class GuidesAdapter extends BaseAdapter {
    public static final String DBGUIDES="Guides_new";
    private Activity activity;
    private DatabaseReference dbGuidesChild;

    private ArrayList<DataSnapshot> snapShotList;

    public GuidesAdapter(Activity activity) {
        this.activity = activity;
        this.dbGuidesChild = MainActivity.getDbGuidesChild();
        dbGuidesChild.addChildEventListener(listener);
        snapShotList=new ArrayList<>();
    }

    static class ViewHolder{
        TextView guidesName;
        TextView languages;
//        ConstraintLayout.LayoutParams params;
    }
    private ChildEventListener listener=new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            snapShotList.add(snapshot);
            notifyDataSetChanged();
            Log.d(MainActivity.LOGAPP, "notifyDataSetChanged called when onChildAdded");
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            snapShotList.remove(snapshot);
            notifyDataSetChanged();
            Log.d(MainActivity.LOGAPP, "notifyDataSetChanged called when onChildRemoved");
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
    public Guides getItem(int position) {
        DataSnapshot snapshot=snapShotList.get(position);
        return snapshot.getValue(Guides.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            LayoutInflater inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.guide_list_view_row,parent,false);

            final ViewHolder holder=new ViewHolder();
            holder.guidesName=convertView.findViewById(R.id.guideName);
            holder.languages=convertView.findViewById(R.id.languages);
//            holder.params= (ConstraintLayout.LayoutParams) holder.guidesName.getLayoutParams();
            convertView.setTag(holder);
        }

        final Guides guide= getItem(position);
        final ViewHolder holder=(ViewHolder) convertView.getTag();
        boolean isBooked;
        if (guide.getDatesBooked()==null){
            isBooked=false;
        }else{
            isBooked=guide.getDatesBooked().contains(GuidesFragment.selectedDate);
        }

        setGuideRowAppearance(isBooked,holder);

        String name=guide.getName();
        holder.guidesName.setText(name);

        String languages=guide.getSpokenLanguages();
        holder.languages.setText(languages);


        return convertView;
    }

    private void setGuideRowAppearance(boolean isItBooked,ViewHolder holder){
        if (isItBooked){
            holder.guidesName.setBackgroundColor(ContextCompat.getColor(this.activity.getApplicationContext(),R.color.red));
        }else{
            holder.guidesName.setBackgroundColor(ContextCompat.getColor(this.activity.getApplicationContext(),R.color.green));
        }
    }

    public void cleanup(){
        dbGuidesChild.removeEventListener(listener);
    }

}
