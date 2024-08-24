package com.cjg.traveling.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Comment {

    @Id
    @Column(name="COMMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commnetId;

    @ManyToOne
    @JoinColumn(name="BOARD_ID")
    private Board board;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    private String comment;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime regDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime modDate;
}