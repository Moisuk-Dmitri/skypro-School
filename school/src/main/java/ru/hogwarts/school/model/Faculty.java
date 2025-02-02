package ru.hogwarts.school.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.*;

@Entity
public class Faculty {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "name must not be blank")
    private String name;
    @NotBlank(message = "color must not be blank")
    private String color;

    @JsonIgnore
    @OneToMany(mappedBy = "faculty",fetch = FetchType.EAGER)
    private Set<Student> students;

    public Faculty() {
    }

    public Faculty(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Collection<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        for (Student student : students) {
            student.setFaculty(this);
        }

        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return Objects.equals(id, faculty.id) && Objects.equals(name, faculty.name) && Objects.equals(color, faculty.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
