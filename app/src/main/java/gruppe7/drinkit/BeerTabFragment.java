package gruppe7.drinkit;

/**
 * Created by namanhnguyen on 14/06/16.
 */
import android.app.Activity;
import android.app.LauncherActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import gruppe7.drinkit.ListItemFragment;
import gruppe7.drinkit.R;

public class BeerTabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_fragment_beer, container, false);

        // Child fragment
        FragmentManager childFragMan = getChildFragmentManager();
        FragmentTransaction childFragTrans = childFragMan.beginTransaction();

        ListItemFragment listFrag = new ListItemFragment();
        childFragTrans.add(R.id.list_container, listFrag);
        childFragTrans.addToBackStack(null);

        ListItemFragment listFrag2 = new ListItemFragment();
        childFragTrans.add(R.id.list_container, listFrag2);
        childFragTrans.addToBackStack(null);

        childFragTrans.commit();

        return rootView;
    }

}