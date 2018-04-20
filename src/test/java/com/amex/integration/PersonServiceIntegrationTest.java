package com.amex.integration;

import com.amex.entity.Person;
import com.amex.util.PersonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PersonServiceIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addAndRetrieveOnePerson() throws ParseException, JsonProcessingException {
        Person person1 = PersonGenerator.generatePerson(1L, "ben@gmail.com");
        ResponseEntity<Person> responseEntity = testRestTemplate.postForEntity("/people",
                person1, Person.class);
        ResponseEntity<Person> getResponse = testRestTemplate.getForEntity("/people/1",
                Person.class);
        Person person2 = getResponse.getBody();
        assertEquals(person1, person2);
    }


    @Test
    public void addTwoPersonAndRetrieveAll() throws ParseException {
        Person person1 = PersonGenerator.generatePerson(1L, "ben1@gmail.com");
        Person person2 = PersonGenerator.generatePerson(2L, "ben2@gmail.com");
        List<Person> requestList = Arrays.asList(person1, person2);
        testRestTemplate.postForEntity("/people", person1, Person.class);
        testRestTemplate.postForEntity("/people", person2, Person.class);
        ResponseEntity<List<Person>> getResponse = testRestTemplate.exchange("/people",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Person>>() {});
        List<Person> responseList = getResponse.getBody();
        assertTrue(requestList.equals(responseList));
    }

    @Test
    public void addAndUpdateOnePerson() throws ParseException {
        Person person = PersonGenerator.generatePerson(1L, "ben1@gmail.com");
        testRestTemplate.postForEntity("/people", person, Person.class);
        final String newEmail = "ben2@gmail.com";
        person.setEmail(newEmail);
        testRestTemplate.put("/people", person, Person.class);
        ResponseEntity<Person> getResponse = testRestTemplate.getForEntity("/people/1",
                Person.class);
        person = getResponse.getBody();
        assertEquals(newEmail, person.getEmail());
    }

    @Test
    public void addTwoPersonAndDeleteOne() throws ParseException {
        Person person1 = PersonGenerator.generatePerson(1L, "ben1@gmail.com");
        Person person2 = PersonGenerator.generatePerson(2L, "ben2@gmail.com");
        testRestTemplate.postForEntity("/people", person1, Person.class);
        testRestTemplate.postForEntity("/people", person2, Person.class);
        testRestTemplate.delete("/people/1");
        ResponseEntity<List<Person>> getResponse = testRestTemplate.exchange("/people",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Person>>() {});
        List<Person> responseList = getResponse.getBody();
        assertEquals(1, responseList.size());
        assertEquals(person2, responseList.get(0));
    }

    @Test
    public void addTwoPersonButSecondConflictWithFirstOne() throws ParseException {
        Person person1 = PersonGenerator.generatePerson(1L, "ben1@gmail.com");
        Person person2 = PersonGenerator.generatePerson(2L, "ben1@gmail.com");
        testRestTemplate.postForEntity("/people", person1, Person.class);
        ResponseEntity<Person> postResponse = testRestTemplate.postForEntity("/people", person2, Person.class);
        assertEquals(HttpStatus.CONFLICT, postResponse.getStatusCode());
    }

    @Test
    public void addInvalidPersonWithEmptyEmail() throws ParseException {
        Person person = PersonGenerator.generatePerson(1L, "");
        ResponseEntity<Person> postResponse = testRestTemplate.postForEntity("/people", person, Person.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, postResponse.getStatusCode());
    }

    @Test
    public void addInvalidPersonWithEmptyAge() throws ParseException {
        Person person = PersonGenerator.generatePerson(1L, "ben@gmail.com");
        person.setAge(null);
        ResponseEntity<Person> postResponse = testRestTemplate.postForEntity("/people", person, Person.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, postResponse.getStatusCode());
    }

    @Test
    public void addInvalidPersonWithEmptyDoB() throws ParseException {
        Person person = PersonGenerator.generatePerson(1L, "ben@gmail.com");
        person.setDateOfBirth(null);
        ResponseEntity<Person> postResponse = testRestTemplate.postForEntity("/people", person, Person.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, postResponse.getStatusCode());
    }

    @Test
    public void addInvalidPersonWithConflictDobAndAge() throws ParseException {
        Person person = PersonGenerator.generatePerson(1L, "ben@gmail.com");
        person.setDateOfBirth(new SimpleDateFormat("MM-DD-yyyy").parse("01-01-1993"));
        ResponseEntity<Person> postResponse = testRestTemplate.postForEntity("/people", person, Person.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, postResponse.getStatusCode());
    }

    @Test
    public void addPersonInTheFormOfXML() throws ParseException {
        Person person = PersonGenerator.generatePerson(1L, "ben@gmail.com");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> request = new HttpEntity<>(person.toString(), httpHeaders);
        ResponseEntity<Person> postResponse = testRestTemplate.postForEntity("/people", request, Person.class);
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, postResponse.getStatusCode());
    }

}
