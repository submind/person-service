package com.amex.controller;

import com.amex.entity.Person;
import com.amex.service.PersonService;
import com.amex.util.PersonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    public static final String BASE_URI = "/people";
    public static final long PERSON_ID = 1L;
    public static final String EMAIL = "ben@gmail.com";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Autowired
    ObjectMapper objectMapper;

    private Person generatePerson() throws ParseException {
        return PersonGenerator.generatePerson(PERSON_ID, EMAIL);
    }

    @Test
    public void getPersonById() throws Exception {
        Person person = generatePerson();

        given(personService.getPersonById(1L)).willReturn(person);

        mockMvc.perform(get("/people/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(person)))
        ;

    }

    @Test
    public void getAllPerson() throws Exception {
        Person person1 = generatePerson();
        Person person2 = generatePerson();
        person2.setEmail("ben2@gmail.com");
        person2.setPersonId(2L);
        List<Person> personList = Arrays.asList(person1, person2);
        given(personService.getAllPerson()).willReturn(personList);
        mockMvc.perform(get(BASE_URI))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(personList)))
        ;
    }

    @Test
    public void addPerson() throws Exception {
        Person person = generatePerson();
        given(personService.addPerson(any())).willReturn(true);
        mockMvc.perform(post(BASE_URI).content(objectMapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                ;
    }

    @Test
    public void updatePerson() throws Exception {
        Person person = generatePerson();
        given(personService.updatePerson(any())).willReturn(true);
        mockMvc.perform(put(BASE_URI).content(objectMapper.writeValueAsString(person)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void deletePerson() throws Exception {
        mockMvc.perform(delete(BASE_URI + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
        ;
    }

}