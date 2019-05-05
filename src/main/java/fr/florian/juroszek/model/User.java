package fr.florian.juroszek.model;

import fr.florian.juroszek.exceptions.IllegalUserAgeException;
import fr.florian.juroszek.exceptions.IllegalUserCountryException;
import fr.florian.juroszek.utils.Country;
import org.springframework.data.annotation.Id;

import java.util.Objects;

/**
 * Describe the User model
 */
public class User {
    @Id
    private String email;
    private String lastName;
    private String middleName;
    private String firstName;
    private int age;
    private Country country;

    public User() {
    }

    /**
     * User constructor which manages checks of user's age and country
     *
     * @param email the user's email
     * @param firstName the user's first name
     * @param middleName the user's middle name
     * @param lastName the user's last name
     * @param age the user's age
     * @param country the user's country where he lives
     * @throws IllegalUserAgeException only adults can create an account
     * @throws IllegalUserCountryException only peoples who lives in France can create an account
     */
    public User(final String email, final String lastName, final String middleName, final String firstName,
                final int age, final Country country) throws IllegalUserAgeException, IllegalUserCountryException {
        if (age <= 18) throw new IllegalUserAgeException();
        if (!country.equals(Country.FRANCE)) throw new IllegalUserCountryException();
        this.email = email;
        this.lastName = lastName;
        this.middleName = middleName;
        this.firstName = firstName;
        this.age = age;
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getAge() {
        return age;
    }

    public Country getCountry() {
        return country;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", age=" + age +
                ", country=" + country +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age &&
                email.equals(user.email) &&
                lastName.equals(user.lastName) &&
                Objects.equals(middleName, user.middleName) &&
                firstName.equals(user.firstName) &&
                country == user.country;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, lastName, middleName, firstName, age, country);
    }
}
