package gruppe7.drinkit;

/**
 * Created by namanhnguyen on 14/06/16.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/*
public class CoffeeFragment extends Fragment {
    ArrayList<ListItemFragment> listFrags = new ArrayList<ListItemFragment>();
    ArrayList<String> barNames = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_fragment_coffee, container, false);

        FragmentManager childFragMan = getChildFragmentManager();
        FragmentTransaction childFragTrans = childFragMan.beginTransaction();

        // Kan Bundle savedInstanceState benyttes til at gemme navnene?
        // Så de kan indsættes i nedenstående, hvor listItemFragments tilføjes til listen

        // TODO: Undersøg om OnSaveInstanceState kan bruges
        // måske sammen med ArrayListen barNames


        ListItemFragment listItemFrag1 = new ListItemFragment();
        //listFrags.add(listItemFrag);
        childFragTrans.add(R.id.list_container_coffee, listItemFrag1);
        childFragTrans.addToBackStack(null);

        ListItemFragment listItemFrag2 = new ListItemFragment();
        //listFrags.add(listItemFrag);
        childFragTrans.add(R.id.list_container_coffee, listItemFrag2);
        childFragTrans.addToBackStack(null);



        childFragTrans.commit();


        return rootView;
    }
}
*/