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

    @Column(name = "UserId", nullable = false)
    private String userid;

    @Column(name = "Time", nullable = false)
    private String time;

    public Review(){

    }

    public Review(Integer rating, String userid) {
        this.rating = rating;
        this.comment = "";
        this.userid = userid;
        this.time = setTime();
    }

    public Review(Integer rating, String comment, String userid) {
        this.rating = rating;
        this.comment = comment;
        this.userid = userid;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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
        return "Review: " + getRating() + " by " + getUserid() + " at " + getTime() + " with comment: " + getComment();
    }
}
