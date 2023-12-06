package edu.virginia.sde.reviews;

import jakarta.persistence.*;

import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name = "Reviews")
public class Review {
    @Id
    @ManyToOne
    @JoinColumn(name = "User", referencedColumnName = "Id")
    private User user;

    @Column(name = "Rating", nullable = false)
    private int rating;

    @Column(name = "Comment", nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "Course", referencedColumnName = "Id")
    private Course course;

    @Column(name = "Time", nullable = false)
    private Timestamp time;

    public Review(){

    }

    public Review(int rating, String comment, User user, Course course, Timestamp time) {
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.course = course;
        this.time = time;
    }


    @Override
    public String toString(){
        return "Review: " + getRating() + " by " + getUser().getUsername() + "for " + getCourse().toString() + " at " + getTime() + " with comment: " + getComment();
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = Timestamp.valueOf(time);
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
