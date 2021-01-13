package com.antonis.bookaguide;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.antonis.bookaguide.data.Guides;
import com.antonis.bookaguide.data.Request;
import com.antonis.bookaguide.data.Routes;
import com.antonis.bookaguide.data.Transport;
import com.antonis.bookaguide.listAdapters.GuidesAdapter;
import com.antonis.bookaguide.tabView.PageAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String LOGAPP="BookAGuide";

    private static ViewPager2 viewPager;
    private Button reserve;
    private Button myTrips;
    public static String selectedDate;
    private static DatabaseReference databaseReference;
    private static DatabaseReference dbGuidesChild;
    public static final String DBTRANSPORT="Transport";
    public static final String DBROUTES="Routes";
    public static final String DBREQUESTS="Requests";
    private TextView selectDateTextView;
    private static FirebaseAuth auth;
    private static Routes route;
    private static Guides guide;
    private static Transport transport;
    private FirebaseAnalytics mFirebaseAnalytics;


    public static String getSelectedDate() {
        return selectedDate;
    }

    public static void setRoute(Routes route) {
        MainActivity.route = route;
    }

    public static void setGuide(Guides guide) {
        MainActivity.guide = guide;
    }

    public static void setTransport(Transport transport) {
        MainActivity.transport = transport;
    }

    public static Routes getRoute() {
        return route;
    }

    public static Guides getGuide() {
        return guide;
    }

    public static Transport getTransport() {
        return transport;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        dbGuidesChild=databaseReference.child(GuidesAdapter.DBGUIDES);
        auth=FirebaseAuth.getInstance();
        mFirebaseAnalytics=FirebaseAnalytics.getInstance(this);

        PageAdapter pageAdapter=new PageAdapter(this);
        viewPager=findViewById(R.id.viewpager);
        viewPager.setAdapter(pageAdapter);
        TabLayout tabs=findViewById(R.id.tabs);
        TabLayoutMediator.TabConfigurationStrategy strategy= new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText(R.string.routes_tab);
                } else if (position == 1) {
                    tab.setText(R.string.guides_tab);
                } else {
                    tab.setText(R.string.transport);
                }
            }
        };
        new TabLayoutMediator(tabs, viewPager,strategy).attach();
        selectDateTextView=findViewById(R.id.selectDateMain);
        selectDateTextView.setOnClickListener(new ClickListener());
        reserve=findViewById(R.id.reserveButton);
        reserve.setOnClickListener(new ReserveListener());
        myTrips=findViewById(R.id.myrequests);
        myTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MyRequests.class);
                finish();
                startActivity(intent);
            }
        });
//        sendGuides();
//        sendTransports();
//        sendRoutes();
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
            Calendar c = Calendar.getInstance();
            DatePickerDialog datePicker=new DatePickerDialog(MainActivity.this,onDate,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
            datePicker.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            datePicker.show();
        }
    }
    private class ReserveListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (selectedDate!=null && route!=null && transport!=null&& guide!=null){
                reservationConfirmationDialog();    //confirmation dialog and clear selected values after
            }else{
                Toast.makeText(MainActivity.this.getApplicationContext(),R.string.selectAllFields,Toast.LENGTH_SHORT).show();
                Bundle params = new Bundle();
                params.putString("user_email", auth.getCurrentUser().getEmail());
                params.putString("Status", "Data missing, reservation not send");
                mFirebaseAnalytics.logEvent("reservation_button", params);
            }
        }
    }

    public void onBackPressed() {
        if (viewPager.getCurrentItem()!=0){
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
        }else{
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            finish();
            startActivity(intent);
        }
    }

    public static ViewPager2 getViewPager() {
        return viewPager;
    }

    public static DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public  static DatabaseReference getDbGuidesChild() {
        return dbGuidesChild;
    }

    public static FirebaseAuth getAuth() {
        return auth;
    }

    public static String replaceDotsWithUnderscore(String s){
        return s.replace('.','_');
    }

    private void successfulReservationDialog(){
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(R.string.reservationCompleted)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
        clearSelectedData();
    }

    private void reservationConfirmationDialog(){
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("You have selected a tour for "+selectedDate+" on "+route.getName()+" with guide "+guide.getName()+" and transport: "+transport.getName()
                +". Do you want to complete this reservation?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendReservation();
                        successfulReservationDialog();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearSelectedData();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void clearSelectedData(){
        selectedDate=null;
        selectDateTextView.setText(R.string.selectDate);
        route=null;
        guide=null;
        transport=null;
        initializeViewpager();
    }

    private void sendReservation(){
        Request request=new Request(auth.getCurrentUser().getEmail(),selectedDate,route,guide,transport);
        Log.d(LOGAPP,request.toString());
//              add the booked date in the arraylist of datesbooked for the guide and transport selected
        Map<String, Object> updateGuide = new HashMap<String,Object>();
        updateGuide.put("datesBooked",guide.getDatesBooked());
        databaseReference.child(GuidesAdapter.DBGUIDES).child(guide.getName()).updateChildren(updateGuide);

        if (!transport.getName().equals("On foot")){                          //don't update datesbooked for onFoot since it has unlimited Capacity
            Map<String, Object> updateTransport = new HashMap<String,Object>();
            updateTransport.put("datesBooked",transport.getDatesBooked());
            databaseReference.child(DBTRANSPORT).child(transport.getName()).updateChildren(updateTransport);
        }
        //replace dots with underscore in email as firebase doesn't accept '.' in the name field
        databaseReference.child(DBREQUESTS).child(replaceDotsWithUnderscore(request.getUserEmail())).push().setValue(request);

        // Log the event in Google analytics
        Bundle params = new Bundle();
        params.putString("user_email", auth.getCurrentUser().getEmail());
        params.putString("Status", "Successful reservation");
        mFirebaseAnalytics.logEvent("reservation_button", params);
    }

    private void initializeViewpager(){
        int tabPosition=viewPager.getCurrentItem();
        PageAdapter pageAdapter=new PageAdapter(this);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(tabPosition);
    }


//    public static void sendGuides(){
//        Guides guide1=new Guides("Antonis Papadopoulos","English,French,German,Greek");
//        guide1.addBookedDate("");
//        databaseReference.child(GuidesAdapter.DBGUIDES).child(guide1.getName()).setValue(guide1);
//
//        Guides guide2=new Guides("Eleni Papantoniou","English,Italian,Greek");
//        guide2.addBookedDate("");
//        databaseReference.child(GuidesAdapter.DBGUIDES).child(guide2.getName()).setValue(guide2);
//
//        Guides guide3=new Guides("Maria Karadima","English,Greek");
//        guide3.addBookedDate("");
//        databaseReference.child(GuidesAdapter.DBGUIDES).child(guide3.getName()).setValue(guide3);
//    }
////
//    public static void sendTransports(){
//        Transport transport1=new Transport("Opel Astra",3);
//        transport1.addBookedDate("");
//        databaseReference.child(DBTRANSPORT).child(transport1.getName()).setValue(transport1);
//
//        Transport transport2=new Transport("Kia Sedona",6);
//        transport2.addBookedDate("");
//        databaseReference.child(DBTRANSPORT).child(transport2.getName()).setValue(transport2);
//
//        Transport transport3=new Transport("Citroen C4",4);
//        transport3.addBookedDate("");
//        databaseReference.child(DBTRANSPORT).child(transport3.getName()).setValue(transport3);
//
//        Transport transport4=new Transport("On foot",10);
//        databaseReference.child(DBTRANSPORT).child(transport4.getName()).setValue(transport4);
//    }
//    public static void sendRoutes() {
//        Routes route1 = new Routes("Historical center classic", true, new LatLng(37.976595, 23.725942),new LatLng(37.975377, 23.736049),6);
//        route1.addPlace("Monastiraki square");
//        route1.addPlace("Ancient Agora");
//        route1.addPlace("Thiseio");
//        route1.addPlace("Parthenon");
//        route1.addPlace("Acropolis museum");
//        route1.addPlace("Stili Dios");
//        route1.addPlace("Syntagma square");
//        ArrayList<MyMarker> list=new ArrayList<>();
//        MyMarker marker=new MyMarker(new LatLng(37.976434, 23.725882),"Monastiraki square");
//        list.add(marker);
//        marker=new MyMarker(new LatLng(37.974800, 23.721851),"Ancient Agora");
//        list.add(marker);
//        marker=new MyMarker(new LatLng(37.976483, 23.720599),"Thiseio");
//        list.add(marker);
//        marker=new MyMarker(new LatLng(37.971649, 23.726415),"Parthenon");
//        list.add(marker);
//        marker=new MyMarker(new LatLng(37.968467, 23.729021),"Acropolis museum");
//        list.add(marker);
//        marker=new MyMarker(new LatLng(37.969348, 23.732644),"Stili Dios");
//        list.add(marker);
//        marker=new MyMarker(new LatLng(37.975310, 23.736045),"Syntagma square");
//        list.add(marker);
//        route1.setPointsToVisit(list);
//        databaseReference.child(DBROUTES).push().setValue(route1);
//
//        Routes route2 = new Routes("Athens sea front", false, new LatLng(37.938635, 23.659739),new LatLng(37.812531, 23.781717),5);
//        route2.addPlace("Mikrolimano");
//        route2.addPlace("Niarxos foundation");
//        route2.addPlace("Marina Floisbou");
//        route2.addPlace("Glyfada");
//        route2.addPlace("Bouliagmeni");
//        list=new ArrayList<>();
//        marker=new MyMarker(new LatLng(37.938564, 23.659601),"Mikrolimano");
//        list.add(marker);
//        marker=new MyMarker(new LatLng(37.940460, 23.693794),"Niarxos foundation");
//        list.add(marker);
//        marker=new MyMarker(new LatLng(37.931073, 23.685534),"Marina Floisbou");
//        list.add(marker);
//        marker=new MyMarker(new LatLng(37.863097, 23.746480),"Glyfada");
//        list.add(marker);
//        marker=new MyMarker(new LatLng(37.812892, 23.782433),"Bouliagmeni");
//        list.add(marker);
//        route2.setPointsToVisit(list);
//        databaseReference.child(DBROUTES).push().setValue(route2);
//
//    }


}

