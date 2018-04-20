package com.amex.service;

import com.amex.entity.Person;
import com.amex.repository.PersonRepository;
import com.amex.util.PersonGenerator;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PersonServiceTest {

    public static final String EMAIL = "ben@gmail.com";
    public static final long PERSON_ID = 1L;
    private PersonRepository personRepository;

    private IPersonService personService;

    @Before
    public void setUp() throws Exception {
        personRepository = mock(PersonRepository.class);
        personService = new PersonService(personRepository);
    }

    private Person generatePerson() throws ParseException {
        return PersonGenerator.generatePerson(PERSON_ID, EMAIL);
    }

    @Test
    public void getAllPerson() throws Exception {
        List<Person> personList = new ArrayList<>();
        Person person = generatePerson();
        personList.add(person);
        when(personRepository.findAll()).thenReturn(personList);
        assertEquals(personService.getAllPerson(), personList);
    }

    @Test
    public void getPersonByIdNotFound() throws Exception {
        long id = 1;
        when(personRepository.findById(id)).thenThrow(new NoSuchElementException());
        assertNull(personService.getPersonById(id));
    }

    @Test
    public void getPersonByIdFound() throws Exception {
        long id = 1;
        Person person = generatePerson();
        when(personRepository.findById(id)).thenReturn(Optional.of(person));
        assertEquals(person, personService.getPersonById(id));
    }

    @Test
    public void addPersonWithDuplicateEmail() throws Exception {
        Person person1 = generatePerson();
        Person person2 = generatePerson();
        person2.setPersonId(2L);
        person2.setDateOfBirth(new SimpleDateFormat("MM-DD-yyyy").parse("01-01-1993"));
        person2.setAge(24);
        person2.setName("Benjamin");
        List<Person> personList = new ArrayList<>();
        personList.add(person1);
        when(personRepository.findByEmail(person2.getEmail())).thenReturn(personList);
        assertFalse(personService.addPerson(person2));
    }

    @Test
    public void addPersonSuccess() throws Exception {
        Person person = generatePerson();
        List<Person> personList = new ArrayList<>();
        when(personRepository.findByEmail(person.getEmail().toLowerCase())).thenReturn(personList);
        assertTrue(personService.addPerson(person));
    }

    @Test
    public void updatePersonWithNoRecordInDB() throws Exception {
        Person person = generatePerson();
        List<Person> personList = new ArrayList<>();
        when(personRepository.findByEmail(person.getEmail().toLowerCase())).thenReturn(personList);
        assertTrue(personService.updatePerson(person));
    }

    @Test
    public void updatePersonWithOneRecordInDBAndHasDifferentId() throws Exception {
        Person person = generatePerson();
        List<Person> personList = new ArrayList<>();
        Person existingPerson = generatePerson();
        existingPerson.setPersonId(2L);
        personList.add(existingPerson);
        when(personRepository.findByEmail(person.getEmail().toLowerCase())).thenReturn(personList);
        assertFalse(personService.updatePerson(person));
    }

    @Test
    public void updatePersonSuccess() throws Exception {
        Person person = generatePerson();
        List<Person> personList = new ArrayList<>();
        Person existingPerson = generatePerson();
        personList.add(existingPerson);
        when(personRepository.findByEmail(person.getEmail().toLowerCase())).thenReturn(personList);
        assertTrue(personService.updatePerson(person));
    }

    @Test
    public void deletePerson() throws Exception {
        Person person = generatePerson();
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        personService.deletePerson(1L);
        verify(personRepository).delete(person);
    }

}