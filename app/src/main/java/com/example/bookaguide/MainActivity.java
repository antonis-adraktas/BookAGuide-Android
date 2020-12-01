package com.example.bookaguide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PageAdapter pageAdapter=new PageAdapter(this);
        ViewPager2 viewPager=findViewById(R.id.viewpager);
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
                    tab.setText(R.string.drivers_tab);
                }
            }
        };
        new TabLayoutMediator(tabs, viewPager,strategy).attach();

    }

//    public void onBackPressed() {
//        if (viewPager.getCurrentItem()!=0){
//            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
//        }else{
//            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
//            finish();
//            startActivity(intent);
//        }
//    }
}