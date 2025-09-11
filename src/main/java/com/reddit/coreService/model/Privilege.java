package com.reddit.coreService.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "privileges")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private PrivilegeName name;

    private String description;

    private LocalDateTime createdAt= LocalDateTime.now();
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private boolean deleted = false ;

}
