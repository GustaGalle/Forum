package com.ifsp.forum.controller;

import com.ifsp.forum.business.AlgoritmoService;
import com.ifsp.forum.infrastructure.dto.AlgoritmoRequestDTO;
import com.ifsp.forum.infrastructure.dto.AlgoritmoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/algoritmo")
@RequiredArgsConstructor
public class AlgoritmoController {

    private final AlgoritmoService algoritmoService;

    @PostMapping("/criar")
    public ResponseEntity<AlgoritmoResponseDTO> criar(@RequestBody AlgoritmoRequestDTO dto) {
        AlgoritmoResponseDTO response = algoritmoService.criarAlgoritmo(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<AlgoritmoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AlgoritmoRequestDTO dto
    ) {
        AlgoritmoResponseDTO response = algoritmoService.atualizarAlgoritmo(id, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlgoritmoResponseDTO> buscarPorId(@PathVariable Long id) {
        AlgoritmoResponseDTO response = algoritmoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<AlgoritmoResponseDTO>> listarTodos() {
        List<AlgoritmoResponseDTO> lista = algoritmoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        algoritmoService.deletarAlgoritmo(id);
        return ResponseEntity.noContent().build();
    }
}
