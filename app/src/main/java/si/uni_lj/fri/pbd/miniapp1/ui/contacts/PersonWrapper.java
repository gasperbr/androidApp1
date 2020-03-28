package si.uni_lj.fri.pbd.miniapp1.ui.contacts;

import android.os.Parcel;
import android.os.Parcelable;

public class PersonWrapper implements Parcelable {

    // Object for storing a CONTACT and its INDEX in the list view
    // use for storing checked state in MainActivity

    private Person person;
    private int personIndex;

    public PersonWrapper() {
        this.person = null;
        this.personIndex = 0;
    }

    public void setPerson(Person person) { this.person = person; }
    public Person getPerson() { return person; }

    public void setPersonIndex(int personIndex) { this.personIndex = personIndex; }
    public int getPersonIndex() { return personIndex; }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(personIndex);
        if (person != null) {
            builder.append(" " + person.toString());
        }
        return builder.toString();
    }

    // methods for implementing Parcelable
    protected PersonWrapper(Parcel in) {
        person = (Person) in.readValue(Person.class.getClassLoader());
        personIndex = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(person);
        dest.writeInt(personIndex);
    }

    public static final Parcelable.Creator<PersonWrapper> CREATOR = new Parcelable.Creator<PersonWrapper>() {
        @Override
        public PersonWrapper createFromParcel(Parcel in) {
            return new PersonWrapper(in);
        }

        @Override
        public PersonWrapper[] newArray(int size) {
            return new PersonWrapper[size];
        }
    };
}
