package com.ifsp.forum.controller;

import com.ifsp.forum.business.ComentarioService;
import com.ifsp.forum.infrastructure.entidys.Comentario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comentario")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;

    // Salvar comentário
    @PostMapping("/salvar/{postId}/{usuarioId}")
    public ResponseEntity<Void> salvarComentario(@PathVariable Long postId,
                                                 @PathVariable Long usuarioId,
                                                 @RequestBody Comentario comentario) {
        comentarioService.salvarComentario(comentario, postId, usuarioId);
        return ResponseEntity.ok().build();
    }

    // Listar todos os comentários
    @GetMapping
    public ResponseEntity<List<Comentario>> listarComentarios() {
        return ResponseEntity.ok(comentarioService.listarComentarios());
    }

    // Buscar comentário por ID
    @GetMapping("/{id}")
    public ResponseEntity<Comentario> buscarComentarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(comentarioService.buscarComentarioPorId(id));
    }

    // Atualizar comentário
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Void> atualizarComentario(@PathVariable Long id,
                                                    @RequestBody Comentario comentario) {
        comentarioService.atualizarComentario(id, comentario);
        return ResponseEntity.ok().build();
    }

    // Deletar comentário
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarComentario(@PathVariable Long id) {
        comentarioService.deletarComentarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar comentários por post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comentario>> buscarComentariosPorPost(@PathVariable Long postId) {
        return ResponseEntity.ok(comentarioService.buscarComentariosPorPost(postId));
    }

    // Buscar comentários por usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Comentario>> buscarComentariosPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(comentarioService.buscarComentariosPorUsuario(usuarioId));
    }
}
