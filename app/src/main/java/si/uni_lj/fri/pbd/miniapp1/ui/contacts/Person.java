package si.uni_lj.fri.pbd.miniapp1.ui.contacts;

public class Person {

    // Object for storing Contacts

    private long personId;
    private String personName;
    private String personEmailAddress;
    private String personPhoneNumber;

    public Person() {
        this.personName = null;
        this.personEmailAddress = null;
        this.personPhoneNumber = null;
    }

    public void setContactId(long personId) { this.personId = personId; }
    public long getContactId() { return personId; }

    public void setContactName(String personName) { this.personName = personName; }
    public String getContactName() { return personName; }

    public void setContactEmailAddress(String personEmailAddress) { this.personEmailAddress = personEmailAddress; }
    public String getContactEmailAddress() { return personEmailAddress; }

    public void setContactPhoneNumber(String personPhoneNumber) { this.personPhoneNumber = personPhoneNumber; }
    public String getContactPhoneNumber() { return personPhoneNumber; }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(personName);
        if (personEmailAddress != null) {
            builder.append(" " + personEmailAddress);
        }
        if (personPhoneNumber != null) {
            builder.append(" " + personPhoneNumber);
        }
        return builder.toString();
    }
}
