package com.ifsp.forum.controller;

import com.ifsp.forum.business.TopicoService;
import com.ifsp.forum.infrastructure.entidys.Topico;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topico")
@RequiredArgsConstructor
public class TopicoController {

    private final TopicoService topicoService;

    // Salvar tópico
    @PostMapping("/salvar")
    public ResponseEntity<Void> salvarTopico(@RequestBody Topico topico) {
        topicoService.salvarTopico(topico);
        return ResponseEntity.ok().build();
    }

    // Listar todos os tópicos
    @GetMapping
    public ResponseEntity<List<Topico>> listarTopicos() {
        List<Topico> topicos = topicoService.listarTopicos();
        return ResponseEntity.ok(topicos);
    }

    // Buscar tópico por ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Topico> buscarTopicoPorId(@PathVariable Long id) {
        Topico topico = topicoService.buscarTopicoPorId(id);
        return ResponseEntity.ok(topico);
    }

    // Atualizar tópico
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Void> atualizarTopico(@PathVariable Long id, @RequestBody Topico topico) {
        topicoService.atualizarTopico(id, topico);
        return ResponseEntity.ok().build();
    }

    // Deletar tópico
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarTopico(@PathVariable Long id) {
        topicoService.deletarTopicoPorId(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar tópicos por fórum
    @GetMapping("/forum/{forumId}")
    public ResponseEntity<List<Topico>> buscarTopicosPorForum(@PathVariable Long forumId) {
        List<Topico> topicos = topicoService.buscarTopicosPorForum(forumId);
        return ResponseEntity.ok(topicos);
    }
}
