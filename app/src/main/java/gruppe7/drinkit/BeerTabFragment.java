package gruppe7.drinkit;

/**
 * Created by namanhnguyen on 14/06/16.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class BeerTabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // ListView barList =

        return inflater.inflate(R.layout.tab_fragment_beer, container, false);
    }

}