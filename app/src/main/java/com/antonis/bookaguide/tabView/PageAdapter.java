package com.antonis.bookaguide.tabView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PageAdapter extends FragmentStateAdapter {


    public PageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

//    public PageAdapter(@NonNull Fragment fragment) {
//        super(fragment);
//    }

    @Override
    public int getItemCount() {
        return(3);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: //Page number 1
                return RoutesFragment.newInstance();
            case 1: //Page number 2
                return GuidesFragment.newInstance();
            case 2: //Page number 3
                return CarsFragment.newInstance();
            default:
                return null;
        }
    }



//    @Override
//    public CharSequence getPageTitle(int position) {
//        switch (position){
//            case 0: //Page number 1
//                return R.string.routes_tab;
//            case 1: //Page number 2
//                return R.string.guides_tab;
//            case 2: //Page number 3
//                return R.string.drivers_tab;
//            default:
//                return null;
//        }
//    }
}
