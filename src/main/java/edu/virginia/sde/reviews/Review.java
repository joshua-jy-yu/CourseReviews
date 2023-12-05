package edu.virginia.sde.reviews;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Review_ID")
    private int reviewid;

    @Column(name = "Rating", nullable = false)
    private Integer rating;

    @Column(name = "Comment", nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "User_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "Course_ID")
    private Course course;

    @Column(name = "Time", nullable = false)
    private Timestamp time;

    public Review(){

    }

    public Review(Integer rating, User user, Course course) {
        this.rating = rating;
        this.comment = "";
        this.user = user;
        this.course = course;
        this.setTime();
    }

    public Review(Integer rating, String comment, User user, Course course) {
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.course = course;
        this.setTime();
    }

    public int getId() {
        return reviewid;
    }

    public void setId(int id) {
        this.reviewid = id;
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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) { this.time = time; }

    public void setTime() { time = new Timestamp(System.currentTimeMillis());}



    @Override
    public String toString(){
        return "Review: " + getRating() + " by " + getUser().getUsername() + "for " + getCourse().toString() + " at " + getTime() + " with comment: " + getComment();
    }
}
