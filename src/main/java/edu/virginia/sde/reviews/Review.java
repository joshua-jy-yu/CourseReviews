package edu.virginia.sde.reviews;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private int id;

    @Column(name = "Rating", nullable = false)
    private Integer rating;

    @Column(name = "Comment", nullable = false)
    private String comment;

    @ManyToOne
    @Column(name = "User", nullable = false)
    private User user;

    @ManyToOne
    @Column(name = "Course", nullable = false)
    private Course course;

    @Column(name = "Time", nullable = false)
    private String time;

    public Review(){

    }

    public Review(Integer rating, User user, Course course) {
        this.rating = rating;
        this.comment = "";
        this.user = user;
        this.course = course;
        this.time = setTime();
    }

    public Review(Integer rating, String comment, User user, Course course) {
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.course = course;
        this.time = setTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getTime() {
        return time;
    }

    public String setTime() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return String.valueOf(timestamp);
    }

    @Override
    public String toString(){
        return "Review: " + getRating() + " by " + getUser().getUsername() + "for " + getCourse().toString() + " at " + getTime() + " with comment: " + getComment();
    }
}
