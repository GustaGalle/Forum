package com.ifsp.forum.infrastructure.entidys;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;


@Entity
@Table(name = "submissao")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Submissao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Algoritmo algoritmo;

    @Column(length = 10000)
    private String codigo;

    private Boolean aprovado; // true se passou nos testes
    private String feedback; // Mensagem de erro ou sucesso
}