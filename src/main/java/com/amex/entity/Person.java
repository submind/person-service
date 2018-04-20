package com.amex.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@ValidPerson
@Table(name = "person")
public class Person implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long personId;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "birthday")
    private Date dateOfBirth;

    @Column(name = "email")
    private String email;

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (personId != null ? !personId.equals(person.personId) : person.personId != null) return false;
        if (!name.equals(person.name)) return false;
        if (!age.equals(person.age)) return false;
        if (!dateOfBirth.equals(person.dateOfBirth)) return false;
        return email.equals(person.email);
    }

    @Override
    public int hashCode() {
        int result = personId != null ? personId.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + age.hashCode();
        result = 31 * result + dateOfBirth.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }
}
