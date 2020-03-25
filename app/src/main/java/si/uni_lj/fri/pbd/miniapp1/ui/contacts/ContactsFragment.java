package si.uni_lj.fri.pbd.miniapp1.ui.contacts;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

import si.uni_lj.fri.pbd.miniapp1.MainActivity;
import si.uni_lj.fri.pbd.miniapp1.R;

public class ContactsFragment extends Fragment {

    List<Person> contacts;
    String[] contactsArray;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        View root = inflater.inflate(R.layout.contacts_list_view, container, false);

        contacts = new LinkedList();

        listView = root.findViewById(R.id.list);

        getContacts();

        return root;
    }

    private void getContacts() {
        LongSparseArray<Person> array = new LongSparseArray();

        String[] projection = {
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Contactables.DATA,
                ContactsContract.CommonDataKinds.Contactables.TYPE,
        };
        String selection = ContactsContract.Data.MIMETYPE + " in (?, ?)";
        String[] selectionArgs = {
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
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

        // contactsArray = contacts.toArray(new Person[contacts.size()]);

        contactsArray = new String[contacts.size()];
        for (int i = 0; i < contacts.size(); i++) {
            contactsArray[i] = contacts.get(i).getContactName();
        }

        for (String p : contactsArray) {
            Log.d("ARRAY", p);
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity(), R.layout.contacts_list_item, contactsArray);
        listView.setAdapter(itemsAdapter);

    }
}

/*
public class ContactsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener {


    public ContactsFragment() {}

    @SuppressLint("InlinedApi")
    private final static String[] FROM_COLUMNS = {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
            ContactsContract.Contacts.DISPLAY_NAME
    };

    private final static int[] TO_IDS = {android.R.id.text1};
    // Define global mutable variables
    // Define a ListView object
    ListView contactsList;
    // Define variables for the contact the user selects
    // The contact's _ID value
    long contactId;
    // The contact's LOOKUP_KEY
    String contactKey;
    // A content URI for the selected contact
    Uri contactUri;
    // An adapter that binds the result Cursor to the ListView
    private SimpleCursorAdapter cursorAdapter;

    @SuppressLint("InlinedApi")
    private static final String[] PROJECTION = {
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.LOOKUP_KEY,
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
            ContactsContract.Contacts.DISPLAY_NAME
    };

    // The column index for the _ID column
    private static final int CONTACT_ID_INDEX = 0;
    // The column index for the CONTACT_KEY column
    private static final int CONTACT_KEY_INDEX = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // Always call the super method first
        super.onCreate(savedInstanceState);
        // Initializes the loader
        getLoaderManager().initLoader(0, null, this);
    }

    // A UI Fragment must inflate its View
    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        View root =  inflater.inflate(R.layout.contacts_list_view, container, false);
        contactsList = root.findViewById(R.id.list);
        return root;
    }


    @SuppressLint("ResourceType")
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contactsCall();
    }

    private void contactsCall() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            initContactList();
        } else {
            // show button to set permission
        }
    }

    @SuppressLint("ResourceType")
    public void initContactList() {
        // Gets the ListView from the View list of the parent activity
        // contactsList = getView().findViewById(R.layout.contacts_list_view);
        if (contactsList != null) {

            Log.d("contacts list -----------------------------", contactsList.toString());
            // Gets a CursorAdapter
            cursorAdapter = new SimpleCursorAdapter(
                    getActivity(),
                    R.layout.contacts_list_item,
                    null,
                    FROM_COLUMNS, TO_IDS,
                    0);
            // Sets the adapter for the ListView
            contactsList.setAdapter(cursorAdapter);
            // Set the item click listener to be the current fragment.
            contactsList.setOnItemClickListener(this);

        } else {
            Log.d("its null", "contact list");
        }
    }

    @Override
    public void onItemClick(
            AdapterView<?> parent, View item, int position, long rowID) {
        // Get the Cursor
        Cursor cursor = ((CursorAdapter)parent.getAdapter()).getCursor();

        Log.d("cusor success --------------------", cursor.toString());

        // Move to the selected contact
        cursor.moveToPosition(position);
        // Get the _ID value
        contactId = cursor.getLong(CONTACT_ID_INDEX);
        // Get the selected LOOKUP KEY
        contactKey = cursor.getString(CONTACT_KEY_INDEX);
        // Create the contact's content Uri
        contactUri = ContactsContract.Contacts.getLookupUri(contactId, contactKey);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        Uri contentUri = Uri.withAppendedPath(
                ContactsContract.Contacts.CONTENT_FILTER_URI,
                Uri.encode(null)); // searchString
        // Starts the query
        return new CursorLoader(
                getActivity(),
                contentUri,
                PROJECTION,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        // Put the result Cursor in the adapter for the ListView
        if (contactUri != null) {
            cursorAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // Delete the reference to the existing Cursor
        if (cursorAdapter != null) {
            cursorAdapter.swapCursor(null);
        }
    }

}
*/