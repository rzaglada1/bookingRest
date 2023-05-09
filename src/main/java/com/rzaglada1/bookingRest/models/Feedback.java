package com.rzaglada1.bookingRest.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "feedbacks")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(columnDefinition = "text")
    private String description;

    private double rating;
    private LocalDateTime dateCreate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private House house;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;



    @PrePersist
    private void init() {
        dateCreate = LocalDateTime.now();
    }


    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", dateCreate=" + dateCreate +
                '}';
    }
}

