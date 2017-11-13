package com.ski.springboot.pojo;

import java.io.Serializable;

public class Student implements Serializable {
   private static final long serialVersionUID = 1L;

    /**
     * This no argument constructor is needed by the FlatFileItemReader for batch processing
     */
    public Student() {

    }

    public Student(String firstName, String lastName, String email, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
    }

    private String firstName, lastName, email;
    private int age;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {

        return new StringBuffer(this.firstName)
                .append(this.lastName)
                .append(this.email).toString();

    }
}
