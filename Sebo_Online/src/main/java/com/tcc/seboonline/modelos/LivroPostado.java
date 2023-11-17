package com.tcc.seboonline.modelos;

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
@Table(name = "livropostado")
public class LivroPostado implements Comparable<LivroPostado>  {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	@Column(length = 360)
	private String text;
	private String imageUrl;
	private Integer voteCount = 0;
	private String nomeLivro;
	private String generoLivro;

	private boolean ocultar;


	@OneToMany(cascade = CascadeType.ALL)
	private List<ComentariosLivros> comentario;

	@ManyToOne
	private Usuario author;

	@Override
	public int compareTo(LivroPostado o) {
		if (this.id > o.id)
			return -1;
		return 1;
	}
}
