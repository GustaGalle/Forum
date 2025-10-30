package com.ifsp.forum.controller;

import com.ifsp.forum.business.CurtidaService;
import com.ifsp.forum.infrastructure.entidys.Curtida;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/curtida")
@RequiredArgsConstructor
public class CurtidaController {

    private final CurtidaService curtidaService;

    // Salvar curtida
    @PostMapping("/salvar/{usuarioId}")
    public ResponseEntity<Void> salvarCurtida(@PathVariable Long usuarioId,
                                              @RequestParam(required = false) Long postId,
                                              @RequestParam(required = false) Long comentarioId,
                                              @RequestBody Curtida curtida) {
        curtidaService.salvarCurtida(curtida, usuarioId, postId, comentarioId);
        return ResponseEntity.ok().build();
    }

    // Listar todas as curtidas
    @GetMapping
    public ResponseEntity<List<Curtida>> listarCurtidas() {
        return ResponseEntity.ok(curtidaService.listarCurtidas());
    }

    // Buscar curtida por ID
    @GetMapping("/{id}")
    public ResponseEntity<Curtida> buscarCurtidaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(curtidaService.buscarCurtidaPorId(id));
    }

    // Deletar curtida
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarCurtida(@PathVariable Long id) {
        curtidaService.deletarCurtidaPorId(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar curtidas por post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Curtida>> buscarCurtidasPorPost(@PathVariable Long postId) {
        return ResponseEntity.ok(curtidaService.buscarCurtidasPorPost(postId));
    }

    // Buscar curtidas por comentário
    @GetMapping("/comentario/{comentarioId}")
    public ResponseEntity<List<Curtida>> buscarCurtidasPorComentario(@PathVariable Long comentarioId) {
        return ResponseEntity.ok(curtidaService.buscarCurtidasPorComentario(comentarioId));
    }

    // Buscar curtidas por usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Curtida>> buscarCurtidasPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(curtidaService.buscarCurtidasPorUsuario(usuarioId));
    }
}
