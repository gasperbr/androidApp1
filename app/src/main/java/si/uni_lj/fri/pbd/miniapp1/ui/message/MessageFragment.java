package si.uni_lj.fri.pbd.miniapp1.ui.message;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import si.uni_lj.fri.pbd.miniapp1.MainActivity;
import si.uni_lj.fri.pbd.miniapp1.R;
import si.uni_lj.fri.pbd.miniapp1.ui.contacts.Person;
import si.uni_lj.fri.pbd.miniapp1.ui.contacts.PersonWrapper;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MessageFragment extends Fragment {

    PersonWrapper[] personWrapper;
    String[] numbersArray;
    String[] emailsArray;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_message, container, false);

        root.findViewById(R.id.buttonEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { sendEmail(); }
        });

        root.findViewById(R.id.buttonMMS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { sendMMS(); }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getChosenContacts();
        ((TextView)getView().findViewById(R.id.textViewEmailWarning)).setText(getText());
    }

    private String getText() {
        // assume each contact has a phone number
        if (emailsArray.length == 0 && numbersArray.length == 0) {
            return "No contacts chosen.";
        } else if (emailsArray.length == 0 && numbersArray.length != 0) {
            return "All of the " + numbersArray.length + " chosen contacts have no associated email.";
        } else if (emailsArray.length != 0 && numbersArray.length != emailsArray.length) {
            return numbersArray.length - emailsArray.length + " of the " + numbersArray.length + " chosen contacts have no associated email.";
        } else if (emailsArray.length == 1) {
            return "One contact chosen.";
        } else {
            return emailsArray.length + " contacts chosen.";
        }
    }

    private void getChosenContacts() {

        personWrapper = ((MainActivity)getActivity()).getCheckedList();

        numbersArray = new String[0];
        emailsArray = new String[0];
        if (personWrapper != null) {
            setEmailAndNumbersArray();
        }
    }

    private void setEmailAndNumbersArray() {

        int emailCount = 0;
        int numberCount = 0;
        // count emails & numbers to set arrays with proper length

        for (int i = 0; i < personWrapper.length; i++) {
            Person person = personWrapper[i].getPerson();
            if (person.getContactEmailAddress() != null) {
                emailCount++;
            }
            if (person.getContactPhoneNumber() != null) {
                numberCount++;
            }
        }

        emailsArray = new String[emailCount--];
        numbersArray = new String[numberCount--];

        for (int i = 0; i < personWrapper.length; i++) {

            Person person = personWrapper[i].getPerson();
            String email = person.getContactEmailAddress();
            String number = person.getContactPhoneNumber();

            if (email != null) {
                emailsArray[emailCount--] = email;
            }
            if (number != null) {
                numbersArray[numberCount--] = number;
            }
            // will fill emailsArray and numbers array with emails and numbers
        }

    }

    public void sendEmail() {
        if (emailsArray.length > 0) {
            _sendEmail(emailsArray);
        }
    }
    public void sendMMS() {
        if (numbersArray.length > 0) {
            _sendMMS(numbersArray);
        }
    }

    private void _sendEmail(String[] emails) {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_EMAIL, emails);
        intent.putExtra(Intent.EXTRA_SUBJECT, "PBD2020 Group Email");
        intent.putExtra(Intent.EXTRA_TEXT, "Sent from my Android mini app 1");
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void _sendMMS(String[] phoneNumbers) {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mmsto:" + String.join(",", phoneNumbers)));  // This ensures only SMS apps respond
        intent.putExtra("sms_body", "Sent from my Android mini app 1");

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
