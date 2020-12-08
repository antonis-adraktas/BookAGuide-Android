package com.antonis.bookaguide.tabView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.antonis.bookaguide.MainActivity;
import com.antonis.bookaguide.R;
import com.antonis.bookaguide.data.Transport;
import com.antonis.bookaguide.listAdapters.TransportAdapter;

public class CarsFragment extends Fragment {

    private TransportAdapter transportAdapter;
    private ListView transportList;
    private Transport transport;

    public CarsFragment() {
    }

    public static CarsFragment newInstance() {
        CarsFragment fragment = new CarsFragment();
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
        View view=inflater.inflate(R.layout.drivers_layout,container,false);
        transportList=view.findViewById(R.id.transportList);
        transportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (MainActivity.getSelectedDate()==null){
                    noDateSelectedDialog();
                }else{
                    transport= (Transport) transportList.getItemAtPosition(position);
                    if (transport.getDatesBooked()!=null &&transport.getDatesBooked().contains(MainActivity.getSelectedDate())){
                        transportBookedDialog(MainActivity.getSelectedDate());
                    }else{
//                        Log.d(MainActivity.LOGAPP,transport.toString());
                        Toast.makeText(CarsFragment.this.getContext(),R.string.transportSelection,Toast.LENGTH_LONG).show();
                        MainActivity.getViewPager().setCurrentItem(0);
                    }
                }
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        transportAdapter=new TransportAdapter(CarsFragment.this.getActivity());
        transportList.setAdapter(transportAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        transportAdapter=new TransportAdapter(CarsFragment.this.getActivity());
        transportList.setAdapter(transportAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        transportAdapter.cleanup();
    }

    private void noDateSelectedDialog(){
        new AlertDialog.Builder(CarsFragment.this.getContext())
                .setMessage("Please select a date for your reservation first")
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void transportBookedDialog(String date){
        new AlertDialog.Builder(CarsFragment.this.getContext())
                .setMessage("This transport is already booked for "+date+". Please select a different transport or a different date")
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}
