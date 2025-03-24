package com.codeisevenlycooked.evenly.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
