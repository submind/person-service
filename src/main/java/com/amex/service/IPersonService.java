package com.amex.service;

import com.amex.entity.Person;

import java.util.List;

public interface IPersonService {
    List<Person> getAllPerson();
    Person getPersonById(long personId);
    boolean addPerson(Person person);
    boolean updatePerson(Person person);
    void deletePerson(long personId);
}
