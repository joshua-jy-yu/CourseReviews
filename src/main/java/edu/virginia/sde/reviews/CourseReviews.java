package edu.virginia.sde.reviews;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseReviews {
    private HashMap<Integer, Review> reviews = new HashMap<>();
    private Course course = new Course();

    public void addReview(Review review){
        reviews.put(review.getId(), review);
    }

    public void setCourse(Course course){
        this.course = course;
    }

    public void removeReview(Review review){
        if(!reviews.containsKey(review.getId())){
            throw new IllegalArgumentException("Cannot remove non-existent review");
        }
        reviews.remove(review.getId());
    }

    public void setReview(int reviewid, Review newReview){
        Review review = reviews.get(reviewid);
        review.setCourse(newReview.getCourse());
        review.setComment(newReview.getComment());
        review.setRating(newReview.getRating());
        review.setUser(newReview.getUser());
        review.setTime(newReview.getTime());
        reviews.put(reviewid, newReview);
    }

    public Review getReview(int reviewid){
        return reviews.get(reviewid);
    }
}
