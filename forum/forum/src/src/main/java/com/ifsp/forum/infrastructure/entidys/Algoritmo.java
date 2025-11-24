package com.ifsp.forum.infrastructure.entidys;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.Id;

@Entity
@Table(name = "algoritmo")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Algoritmo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo; // título do desafio
    private String descricao; // descrição do problema

    private String linguagemPermitida; // ex: "java", "python"

    private String gabarito;
    private Integer judge0LanguageId;
    private String casosDeTeste;
}
