package edu.virginia.sde.reviews;

import jakarta.persistence.*;
@Entity
@Table(name = "Courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private int id;

    @Column(name = "Subject", nullable = false)
    private String subject;

    @Column(name = "Number", nullable = false)
    private Integer number;

    @Column(name = "Title", nullable = false)
    private String title;

    public Course(){

    }
    public Course(String subject, Integer number, String title){
        this.subject = subject;
        this.number = number;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString(){
        return getSubject() + " " + getNumber() + " - " + getTitle();
    }
}
