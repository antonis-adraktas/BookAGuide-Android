package com.antonis.bookaguide.tabView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.antonis.bookaguide.CustomRouteMap;
import com.antonis.bookaguide.R;
import com.antonis.bookaguide.listAdapters.RoutesAdapter;

public class RoutesFragment extends Fragment {


    private Button customButton;
//    private Button sendGuidesButton;
    private RoutesAdapter routeAdapter;
    private ListView routeList;

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
    }

    @Override
    public void onStop() {
        super.onStop();
        routeAdapter.cleanup();
    }
}
