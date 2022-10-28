package com.assignment.javaendassignment;

import javafx.collections.ObservableList;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Member implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    public Member(int id, String name, String lastName, LocalDate birthDate) {
        this.id = id;
        this.firstName = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public Member(String name, String lastName, LocalDate birthDate, ObservableList<Member> members) {
        if (members.isEmpty())
            this.id = 1;
        else
            this.id = members.get(members.size() - 1).getId() + 1;
        this.firstName = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate.format(DateTimeFormatter.ofPattern("d-MM-uuuu"));
    }

    public LocalDate getBirthDateLocalDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

}
