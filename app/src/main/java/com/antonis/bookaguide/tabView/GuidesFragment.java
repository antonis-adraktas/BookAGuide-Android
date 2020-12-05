package com.antonis.bookaguide.tabView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.antonis.bookaguide.MainActivity;
import com.antonis.bookaguide.R;
import com.antonis.bookaguide.listAdapters.GuidesAdapter;

import java.util.Calendar;

public class GuidesFragment extends Fragment {
//    private static final String ARG_COUNT = "param1";
//    private Integer counter;
    private String selectedDate;
    private TextView selectDateTextView;
    private GuidesAdapter guidesAdapter;
    private ListView guidesList;


    public GuidesFragment() {
    }

    public static GuidesFragment newInstance() {
        GuidesFragment fragment = new GuidesFragment();
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
        View view= inflater.inflate(R.layout.guides_layout,container,false);
        selectDateTextView=view.findViewById(R.id.selectDateGuides);
        selectDateTextView.setOnClickListener(new ClickListener());
        guidesList=view.findViewById(R.id.guidesList);
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

    @Override
    public void onStart() {
        super.onStart();
        guidesAdapter=new GuidesAdapter(GuidesFragment.this.getActivity(), MainActivity.getDatabaseReference());
        guidesList.setAdapter(guidesAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        guidesAdapter.cleanup();
    }
}
