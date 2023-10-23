package com.tcc.seboonline.models;

import jakarta.persistence.*;
import lombok.*;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Vote implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer voteId;
    private VoteType voteType;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

}
