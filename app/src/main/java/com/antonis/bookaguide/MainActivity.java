package com.antonis.bookaguide;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.antonis.bookaguide.listAdapters.GuidesAdapter;
import com.antonis.bookaguide.tabView.PageAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static final String LOGAPP="BookAGuide";

    private static ViewPager2 viewPager;
    private Button reserve;
    private static DatabaseReference databaseReference;
    private static DatabaseReference dbGuidesChild;
    public static final String DBTRANSPORT="Transport";
    public static final String DBROUTES="Routes";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        dbGuidesChild=databaseReference.child(GuidesAdapter.DBGUIDES);

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
//        sendRoutes();
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

//    public static void sendGuides(){
//        Guides guide1=new Guides("Antonis Papadopoulos","English,French,German,Greek");
//        guide1.addBookedDate("8-12-2020");
//        databaseReference.child(GuidesAdapter.DBGUIDES).push().setValue(guide1);
//
//        Guides guide2=new Guides("Eleni Papantoniou","English,Italian,Greek");
//        databaseReference.child(GuidesAdapter.DBGUIDES).push().setValue(guide2);
//
//        Guides guide3=new Guides("Maria Karadima","English,Greek");
//        databaseReference.child(GuidesAdapter.DBGUIDES).push().setValue(guide3);
//        Log.d(LOGAPP,"sent a couple of guides to firebase"+guide1+"\n"+guide2+"\n"+guide3);
//    }
//
//    public static void sendTransports(){
//        Transport transport1=new Transport("Opel Astra",3);
//        databaseReference.child(DBTRANSPORT).push().setValue(transport1);
//
//        Transport transport2=new Transport("Kia Sedona",6);
//        databaseReference.child(DBTRANSPORT).push().setValue(transport2);
//
//        Transport transport3=new Transport("Citroen C4",4);
//        databaseReference.child(DBTRANSPORT).push().setValue(transport3);
//
//        Transport transport4=new Transport("On foot",10);
//        databaseReference.child(DBTRANSPORT).push().setValue(transport4);
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
//        databaseReference.child(DBROUTES).push().setValue(route1);
//
//        Routes route2 = new Routes("Athens sea front", false, new LatLng(37.938635, 23.659739),new LatLng(37.812531, 23.781717),5);
//        route2.addPlace("Mikrolimano");
//        route2.addPlace("Niarxos foundation");
//        route2.addPlace("Marina Floisbou");
//        route2.addPlace("Glyfada");
//        route2.addPlace("Bouliagmeni");
//        databaseReference.child(DBROUTES).push().setValue(route2);
//
//    }


}

