package com.amex.entity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class PersonValidator implements ConstraintValidator<ValidPerson, Person>{

    @Override
    public void initialize(ValidPerson constraintAnnotation) {

    }

    @Override
    public boolean isValid(Person person, ConstraintValidatorContext constraintValidatorContext) {
        return person.getName() != null && !person.getName().isEmpty() && person.getEmail() != null && !person.getEmail().isEmpty() && person.getAge() != null && person.getDateOfBirth() != null && isAgeMatchDob(person);
    }

    private boolean isAgeMatchDob(Person person) {
        LocalDate dateOfBirth = person.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return person.getAge() == (int) ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
    }
}
