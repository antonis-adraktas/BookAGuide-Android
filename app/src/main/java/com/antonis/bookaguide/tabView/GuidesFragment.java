package com.antonis.bookaguide.tabView;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
import com.antonis.bookaguide.data.Guides;
import com.antonis.bookaguide.listAdapters.GuidesAdapter;

public class GuidesFragment extends Fragment {


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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.guides_layout,container,false);
        guidesList=view.findViewById(R.id.guidesList);
        guidesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (MainActivity.getSelectedDate()==null){
                    noDateSelectedDialog();
                }else{
                    guideSelected= (Guides) guidesList.getItemAtPosition(position);
                    if (guideSelected.getDatesBooked()!=null &&guideSelected.getDatesBooked().contains(MainActivity.getSelectedDate())){
                        guideBookedDialog(MainActivity.getSelectedDate());
                    }else{
//                        Log.d(MainActivity.LOGAPP,guideSelected.toString());
                        guideSelected.addBookedDate(MainActivity.getSelectedDate());
                        MainActivity.setGuide(guideSelected);
                        //Background color changed for selected item
                        for (int i=0;i<guidesList.getChildCount();i++){
                            if (position==i){
                                guidesList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.purple_200));
                            }else{
                                guidesList.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                            }
                        }
                        Toast.makeText(GuidesFragment.this.getContext(),R.string.guideSelection,Toast.LENGTH_LONG).show();
//                        MainActivity.getViewPager().setCurrentItem(2);
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
        guidesAdapter=new GuidesAdapter(GuidesFragment.this.getActivity());
        guidesList.setAdapter(guidesAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        guidesAdapter=new GuidesAdapter(GuidesFragment.this.getActivity());
        guidesList.setAdapter(guidesAdapter);

        new Handler(Looper.getMainLooper()).postDelayed(this::updateColorOfSelectedItem,20);  // update the color after 20 millis to give time for the guides list to be populated

    }

    private void updateColorOfSelectedItem() {
        Log.d(MainActivity.LOGAPP,"Number of items in  guides list "+guidesList.getChildCount()+" from updateColor function");
        if (MainActivity.getGuide() != null) {
            for (int i=0;i<guidesList.getChildCount();i++){
                Guides guideInList= (Guides) guidesList.getItemAtPosition(i);
                if (MainActivity.getGuide().getName().equals(guideInList.getName())){
                    guidesList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.purple_200));
                }
            }
        } else {
            for (int i = 0; i < guidesList.getChildCount(); i++) {
                guidesList.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }
        }
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
