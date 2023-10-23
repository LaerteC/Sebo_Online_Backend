package com.tcc.seboonline.models;

import java.util.List;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post implements Comparable<Post>  {



	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	@Column(length = 3_000)
	private String text;
	private String imageUrl;
	private Integer voteCount = 0;
	private String nomeLivro;
	private String generoLivro;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Comment> comments;

	@ManyToOne
	private User author;

	@Override
	public int compareTo(Post o) {
		if (this.id > o.id)
			return -1;
		return 1;
	}
}
