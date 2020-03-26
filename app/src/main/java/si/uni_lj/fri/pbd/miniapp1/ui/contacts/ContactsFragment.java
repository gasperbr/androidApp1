package si.uni_lj.fri.pbd.miniapp1.ui.contacts;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import si.uni_lj.fri.pbd.miniapp1.MainActivity;
import si.uni_lj.fri.pbd.miniapp1.R;

public class ContactsFragment extends Fragment {

    List<Person> contacts;
    String[] contactsArray;
    PersonWrapper[] contactsArrayChecked;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        View root = inflater.inflate(R.layout.contacts_list_view, container, false);
        contacts = new LinkedList();
        listView = root.findViewById(R.id.list);

        if (((MainActivity)getActivity()).hasPermission()) {
            getContacts();
        }

        return root;
    }

    private void getContacts() {

        // logic for getting contacts
        // store result in contacts linked list and also in contactsArray (string[])

        LongSparseArray<Person> array = new LongSparseArray();
        String[] projection = {
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Contactables.DATA,
                ContactsContract.CommonDataKinds.Contactables.TYPE
        };
        String selection = ContactsContract.Data.MIMETYPE + " in (?, ?)";
        String[] selectionArgs = {
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        };
        Uri uri = ContactsContract.Data.CONTENT_URI;

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, selection, selectionArgs, null);

        final int _id = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);
        final int _name = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        final int _mime = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
        final int _data = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DATA);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(_id);
            Person person = array.get(id);
            if (person == null) {
                person = new Person();
                person.setContactId(id);
                person.setContactName(cursor.getString(_name));

                array.put(id, person);
                contacts.add(person);
            }
            String data = cursor.getString(_data);
            String mimeType = cursor.getString(_mime);
            if (mimeType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                if (person.getContactEmailAddress() == null) {
                    person.setContactEmailAddress(data);
                }
            } else {
                if (person.getContactPhoneNumber() == null) {
                    person.setContactPhoneNumber(data);
                }
            }
        }
        cursor.close();
        // now contacts contains all retrieved contacts in a linked list, convert to array

        contactsArray = new String[contacts.size()];
        for (int i = 0; i < contacts.size(); i++) {
            contactsArray[i] = contacts.get(i).getContactName();
        }

        // show contacts in view

        // show ListView of contacts
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, contactsArray);

        listView.setAdapter(itemsAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // checkbox logic
        setClickListener(listView);
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
    }

    // event when clicking on contact
    public void setClickListener(final ListView listView) {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listView.setItemChecked(position, listView.getCheckedItemPositions().get(position));
                // remember checked people (using PersonWrapper[] stored in MainActivity)
                // don't do this in onPause because then
                // MessageFragment getsContacts before they are saved
                PersonWrapper[] pw = getContactsArrayCheckedFromView();
                ((MainActivity)getActivity()).setCheckedList(getContactsArrayCheckedFromView());
            }

        });
    }

    // used when saving checked states
    // return array of Objects, storing the contact and and it's listView index (PersonWrapper object)
    public PersonWrapper[] getContactsArrayCheckedFromView() {

        SparseBooleanArray checkedItems = listView.getCheckedItemPositions();

        int checkedCount = 0;
        for (int i = 0; i < checkedItems.size(); i++) {
            if (checkedItems.valueAt(i)) {
                checkedCount++;
            }
        }

        PersonWrapper[] array = new PersonWrapper[checkedCount];

        for (int i = 0; i < checkedItems.size(); i++) {

            if (checkedItems.valueAt(i)) { // if checked, add to our array

                int personIndex = checkedItems.keyAt(i);
                Person person = contacts.get(personIndex);

                PersonWrapper personWrapper = new PersonWrapper();
                personWrapper.setPerson(person);
                personWrapper.setPersonIndex(personIndex);

                array[--checkedCount] = personWrapper;
            }
        }
        return  array;
    }

    // remember checked people (using PersonWrapper[] stored in MainActivity)
    // check items here
    @Override
    public void onResume() {
        super.onResume();
        contactsArrayChecked = ((MainActivity)getActivity()).getCheckedList();

        if (contactsArrayChecked != null) {
            for(int i = 0; i < contactsArrayChecked.length; i++) {
                listView.setItemChecked(contactsArrayChecked[i].getPersonIndex(), true);
            }
        }
    }
}
