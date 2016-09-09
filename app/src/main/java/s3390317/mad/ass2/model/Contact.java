package s3390317.mad.ass2.model;

/**
 * Contact class used to represents attendees to events, currently only stores
 * name and email.
 */
public class Contact
{
    private String name;
    private String email;

    public Contact(String name, String email)
    {
        this.name = name;
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
