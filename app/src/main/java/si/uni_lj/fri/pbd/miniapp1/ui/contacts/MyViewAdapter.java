package si.uni_lj.fri.pbd.miniapp1.ui.contacts;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import si.uni_lj.fri.pbd.miniapp1.R;

public class MyViewAdapter extends ArrayAdapter<Person> {

    // Basic ArrayAdapter implementation
    // nothing of substance here

    private Activity activity;
    private ArrayList<Person> lPerson;
    private static LayoutInflater inflater = null;

    public MyViewAdapter(Activity activity, int textViewResourceId, ArrayList<Person> _lPerson) {
        super(activity, textViewResourceId, _lPerson);
        try {
            this.activity = activity;
            this.lPerson = _lPerson;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return lPerson.size();
    }

    public Person getItem(Person person) {
        return person;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView display_name;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(android.R.layout.simple_list_item_checked, null);
                holder = new ViewHolder();

                holder.display_name = vi.findViewById(R.id.text);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }



        } catch (Exception e) {


        }
        return vi;
    }
}
