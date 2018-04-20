package com.amex.repository;

import com.amex.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long>{
    List<Person> findByEmail(String email);
}
