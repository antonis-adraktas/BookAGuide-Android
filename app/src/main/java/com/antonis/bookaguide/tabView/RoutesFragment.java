package com.antonis.bookaguide.tabView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.antonis.bookaguide.CustomRouteMap;
import com.antonis.bookaguide.R;

import java.util.Calendar;

public class RoutesFragment extends Fragment {

    private String selectedDate;
    private TextView selectDateTextView;
    private Button customButton;
    private Button sendGuidesButton;

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
        selectDateTextView=view.findViewById(R.id.selectDateRoutes);
        customButton=view.findViewById(R.id.button);
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CustomRouteMap.class);
                startActivity(intent);
            }
        });
        selectDateTextView.setOnClickListener(new ClickListener());
        sendGuidesButton=view.findViewById(R.id.sendGuides);
//        sendGuidesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.sendTransports();
//            }
//        });
        return view;
    }
    private DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(final DatePicker view, final int year, final int month, final int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            selectedDate=dayOfMonth+"-"+(month+1)+"-"+year;
            selectDateTextView.setText(selectedDate);
        }
    };
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            DatePickerFragment dpf = new DatePickerFragment().newInstance();
            dpf.setCallBack(onDate);
            dpf.show(getFragmentManager().beginTransaction(),"DatePickerFragment");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
