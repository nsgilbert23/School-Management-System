package sms.model;
// Abstract base class representing a person with name and email attributes
public abstract class Person {

    protected String firstName;
    protected String lastName;
    protected String email;

    public Person(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public abstract void displayInfo();
}