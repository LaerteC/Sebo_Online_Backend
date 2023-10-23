package com.tcc.seboonline.models;

import java.io.Serializable;
import java.util.List;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	private String text;
	private String imageUrl;

    @OneToMany(cascade = CascadeType.ALL)
	private List<Comment> comments;

    @ManyToOne
	private User author;
}
