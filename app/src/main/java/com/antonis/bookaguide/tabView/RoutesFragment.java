package com.antonis.bookaguide.tabView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.antonis.bookaguide.CustomRouteMap;
import com.antonis.bookaguide.MainActivity;
import com.antonis.bookaguide.R;
import com.antonis.bookaguide.data.Routes;
import com.antonis.bookaguide.listAdapters.RoutesAdapter;

public class RoutesFragment extends Fragment {


    private Button customButton;
//    private Button sendGuidesButton;
    private RoutesAdapter routeAdapter;
    private ListView routeList;
    private Routes routeSelected;

    public RoutesFragment() {
    }

    public static RoutesFragment newInstance() {
        RoutesFragment fragment = new RoutesFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.routes_layout,container,false);
        routeList=view.findViewById(R.id.routesList);
        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (MainActivity.getSelectedDate()==null){
                    noDateSelectedDialog();
                }else{
                    routeSelected= (Routes) routeList.getItemAtPosition(position);
                    Log.d(MainActivity.LOGAPP,routeSelected.toString());
                    MainActivity.setRoute(routeSelected);
                    //Background color changed for selected item
                    for (int i=0;i<routeList.getChildCount();i++){
                        if (position==i){
                            routeList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.purple_200));
                        }else{
                            routeList.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                    Toast.makeText(RoutesFragment.this.getContext(),R.string.routeSelection,Toast.LENGTH_LONG).show();
                    }
                }
        });
        customButton=view.findViewById(R.id.button);
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CustomRouteMap.class);
                startActivity(intent);
            }
        });
//        sendGuidesButton=view.findViewById(R.id.sendGuides);
//        sendGuidesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.sendTransports();
//            }
//        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
        routeAdapter=new RoutesAdapter(RoutesFragment.this.getActivity());
        routeList.setAdapter(routeAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        routeAdapter=new RoutesAdapter(RoutesFragment.this.getActivity());
        routeList.setAdapter(routeAdapter);
        new Handler(Looper.getMainLooper()).postDelayed(this::updateColorOfSelectedItem,20);  // update the color after 10 millis to give time for the routes list to be populated

    }

    @Override
    public void onStop() {
        super.onStop();
        routeAdapter.cleanup();
    }
    private void noDateSelectedDialog(){
        new AlertDialog.Builder(RoutesFragment.this.getContext())
                .setMessage("Please select a date for your reservation first")
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void updateColorOfSelectedItem() {
        Log.d(MainActivity.LOGAPP,"Number of items in  routes list "+routeList.getChildCount()+" from updateColor function");
        if (MainActivity.getRoute() != null) {
            for (int i=0;i<routeList.getChildCount();i++){
                Routes routeInList= (Routes) routeList.getItemAtPosition(i);
                if (MainActivity.getRoute().getName().equals(routeInList.getName())){
                    routeList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.purple_200));
                }
            }
        } else {
            for (int i = 0; i < routeList.getChildCount(); i++) {
                routeList.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

}
