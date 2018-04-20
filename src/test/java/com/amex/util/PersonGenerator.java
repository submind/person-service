package com.amex.util;

import com.amex.entity.Person;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PersonGenerator {
    public static Person generatePerson(long personId, String email) throws ParseException {
        Person person = new Person();
        person.setPersonId(personId);
        person.setName("Ben");
        person.setDateOfBirth(new SimpleDateFormat("MM-DD-yyyy").parse("01-01-1992"));
        person.setAge(26);
        person.setEmail(email);
        return person;
    }
}
