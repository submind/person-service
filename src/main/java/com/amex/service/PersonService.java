package com.amex.service;

import com.amex.entity.Person;
import com.amex.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService implements IPersonService {

    private PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<Person> getAllPerson() {
        List<Person> personList = new ArrayList<>();
        personRepository.findAll().forEach(person -> {
            updateBirthdayAndEmail(person);
            personList.add(person);
        });
        return personList;
    }

    private void updateBirthdayAndEmail(Person person) {
        if (person.getDateOfBirth() != null) {
            LocalDate dateOfBirth = person.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            person.setAge((int) ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now()));
        }
        if (person.getEmail() != null) {
            person.setEmail(person.getEmail().toLowerCase());
        }
    }

    @Override
    public Person getPersonById(long personId) {
        try {
            Person person = personRepository.findById(personId).get();
            updateBirthdayAndEmail(person);
            return person;
        } catch (java.util.NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public boolean addPerson(Person person) {
        List<Person> personList = personRepository.findByEmail(person.getEmail().toLowerCase());
        if (personList.size() > 0) return false;
        personRepository.save(person);
        return true;
    }

    @Override
    public boolean updatePerson(Person person) {
        List<Person> personList = personRepository.findByEmail(person.getEmail().toLowerCase());
        if (personList.size() == 0 || personList.size() == 1 && personList.get(0).getPersonId() == person.getPersonId()) {
            personRepository.save(person);
            return true;
        }
        return false;
    }

    @Override
    public void deletePerson(long personId) {
        Person person = getPersonById(personId);
        if (person != null)
            personRepository.delete(person);
    }
}
