package gruppe7.drinkit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class BeerFragment extends Fragment {

    ArrayList<Bar> bars = new ArrayList<>();

    public boolean settingsOptionsOpen;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_fragment_beer, container, false);

        FragmentManager childFragMan = getChildFragmentManager();
        FragmentTransaction childFragTrans = childFragMan.beginTransaction();

        for (int i = 0; i < bars.size(); i++) {
            ListItemFragment listItemFrag = new ListItemFragment();
            listItemFrag.bar = bars.get(i);
            if (!settingsOptionsOpen){
                childFragTrans.add(R.id.list_container_beer, listItemFrag);
                childFragTrans.addToBackStack(null);
        } else if (listItemFrag.bar.isOpen())
                 childFragTrans.add(R.id.list_container_beer, listItemFrag);
                childFragTrans.addToBackStack(null);
        }

        childFragTrans.commit();

        return rootView;
    }

}