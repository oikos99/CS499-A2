package com.cs499.a2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "namee")
    private String namee;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(unique = true)
    private OfficeHour officehour;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private Set<Project> projects = new HashSet<>();

    @ManyToOne
    private Instructor instructor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamee() {
        return namee;
    }

    public Course namee(String namee) {
        this.namee = namee;
        return this;
    }

    public void setNamee(String namee) {
        this.namee = namee;
    }

    public String getDescription() {
        return description;
    }

    public Course description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OfficeHour getOfficehour() {
        return officehour;
    }

    public Course officehour(OfficeHour officeHour) {
        this.officehour = officeHour;
        return this;
    }

    public void setOfficehour(OfficeHour officeHour) {
        this.officehour = officeHour;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Course students(Set<Student> students) {
        this.students = students;
        return this;
    }

    public Course addStudents(Student student) {
        students.add(student);
        student.setCourse(this);
        return this;
    }

    public Course removeStudents(Student student) {
        students.remove(student);
        student.setCourse(null);
        return this;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public Course projects(Set<Project> projects) {
        this.projects = projects;
        return this;
    }

    public Course addProjects(Project project) {
        projects.add(project);
        project.setCourse(this);
        return this;
    }

    public Course removeProjects(Project project) {
        projects.remove(project);
        project.setCourse(null);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public Course instructor(Instructor instructor) {
        this.instructor = instructor;
        return this;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Course course = (Course) o;
        if (course.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Course{" +
            "id=" + id +
            ", namee='" + namee + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
