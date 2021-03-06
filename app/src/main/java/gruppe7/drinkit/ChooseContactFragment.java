package gruppe7.drinkit;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ChooseContactFragment extends Fragment{

    ArrayList<ContactItemFragment> contactFrags = new ArrayList<>();

    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> numbers = new ArrayList<>();

    private static final Uri PHONE_CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private static final String DISPLAY_NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
    private static final String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.contact_fragment, container, false);

        FragmentManager childFragmentManager = getChildFragmentManager();
        FragmentTransaction childFragTransaction = childFragmentManager.beginTransaction();

        //Put the cursor on the right place in the phones data
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(PHONE_CONTENT_URI, null,null,null, null);

        //Go through the contacts and save name and number for all
        while (cursor.moveToNext())
        {
            String name=cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
            names.add(name);
            String phoneNumber = cursor.getString(cursor.getColumnIndex(NUMBER));
            numbers.add(phoneNumber);
        }
        cursor.close();

        for (int i = 0; i < names.size(); i++) {
            ContactItemFragment contactItemFrag = new ContactItemFragment();
            contactItemFrag.contact = names.get(i);
            contactItemFrag.setContactNumber(numbers.get(i));
            contactFrags.add(contactItemFrag);
            childFragTransaction.add(R.id.contact_container, contactItemFrag);
            childFragTransaction.addToBackStack(null);
        }

        childFragTransaction.commit();

        return rootView;
    }

    public  ArrayList<ContactItemFragment> getContactItemFragments() {
        return contactFrags;
    }
}
