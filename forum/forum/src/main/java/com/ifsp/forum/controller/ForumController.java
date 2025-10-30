package com.ifsp.forum.controller;

import com.ifsp.forum.business.ForumService;
import com.ifsp.forum.infrastructure.entidys.Forum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;

    @PostMapping
    public ResponseEntity<Void> salvarForum(@RequestBody Forum forum) {
        forumService.salvarForum(forum);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Forum>> listarForuns() {
        return ResponseEntity.ok(forumService.listarForuns());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Forum> buscarForumPorId(@PathVariable Long id) {
        return ResponseEntity.ok(forumService.buscarForumPorId(id));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Void> atualizarForum(@PathVariable Long id, @RequestBody Forum forum) {
        forumService.atualizarForum(id, forum);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarForum(@PathVariable Long id) {
        forumService.deletarForum(id);
        return ResponseEntity.noContent().build();
    }
}
