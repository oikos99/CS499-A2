package com.cs499.a2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Instructor.
 */
@Entity
@Table(name = "instructor")
public class Instructor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "instructor")
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Instructor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public Instructor age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public Instructor email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public Instructor courses(Set<Course> courses) {
        this.courses = courses;
        return this;
    }

    public Instructor addCourses(Course course) {
        courses.add(course);
        course.setInstructor(this);
        return this;
    }

    public Instructor removeCourses(Course course) {
        courses.remove(course);
        course.setInstructor(null);
        return this;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Instructor instructor = (Instructor) o;
        if (instructor.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instructor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Instructor{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", age='" + age + "'" +
            ", email='" + email + "'" +
            '}';
    }
}
