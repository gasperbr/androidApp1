package si.uni_lj.fri.pbd.miniapp1.ui.contacts;

public class PersonWrapper {

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
}
