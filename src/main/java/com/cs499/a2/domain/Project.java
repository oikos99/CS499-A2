package com.cs499.a2.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "namee")
    private String namee;

    @Column(name = "deadline")
    private ZonedDateTime deadline;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private Course course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamee() {
        return namee;
    }

    public Project namee(String namee) {
        this.namee = namee;
        return this;
    }

    public void setNamee(String namee) {
        this.namee = namee;
    }

    public ZonedDateTime getDeadline() {
        return deadline;
    }

    public Project deadline(ZonedDateTime deadline) {
        this.deadline = deadline;
        return this;
    }

    public void setDeadline(ZonedDateTime deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public Project description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Course getCourse() {
        return course;
    }

    public Project course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Project project = (Project) o;
        if (project.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + id +
            ", namee='" + namee + "'" +
            ", deadline='" + deadline + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
