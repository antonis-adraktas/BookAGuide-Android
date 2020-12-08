package com.antonis.bookaguide.tabView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.antonis.bookaguide.MainActivity;
import com.antonis.bookaguide.R;
import com.antonis.bookaguide.data.Guides;
import com.antonis.bookaguide.listAdapters.GuidesAdapter;

import java.util.Calendar;

public class GuidesFragment extends Fragment {

    public static String selectedDate;
    private TextView selectDateTextView;
    private GuidesAdapter guidesAdapter;
    private ListView guidesList;
    private Guides guideSelected;


    public GuidesFragment() {
    }

//    public String getSelectedDate() {
//        return selectedDate;
//    }

    public static GuidesFragment newInstance() {
        GuidesFragment fragment = new GuidesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static String getSelectedDate() {
        return selectedDate;
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
        guidesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectedDate==null){
                    noDateSelectedDialog();
                }else{
                    guideSelected= (Guides) guidesList.getItemAtPosition(position);
                    if (guideSelected.getDatesBooked()!=null &&guideSelected.getDatesBooked().contains(selectedDate)){
                        guideBookedDialog(selectedDate);
                    }else{
                        Log.d(MainActivity.LOGAPP,guideSelected.toString());
                        Toast.makeText(GuidesFragment.this.getContext(),R.string.guideSelection,Toast.LENGTH_LONG).show();
                        MainActivity.getViewPager().setCurrentItem(0);
                    }
                }
            }
        });
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
        guidesAdapter=new GuidesAdapter(GuidesFragment.this.getActivity());
        guidesList.setAdapter(guidesAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        guidesAdapter=new GuidesAdapter(GuidesFragment.this.getActivity());
        guidesList.setAdapter(guidesAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        guidesAdapter.cleanup();
    }

    private void noDateSelectedDialog(){
        new AlertDialog.Builder(GuidesFragment.this.getContext())
                .setMessage("Please select a date for your reservation first")
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void guideBookedDialog(String date){
        new AlertDialog.Builder(GuidesFragment.this.getContext())
                .setMessage("This guide is already booked for "+date+". Please select a different guide or a different date")
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}
