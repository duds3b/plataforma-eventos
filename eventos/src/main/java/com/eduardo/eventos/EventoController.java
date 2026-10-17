package com.eduardo.eventos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class EventoController {

    private final EventoRepository eventoRepository;

    public EventoController(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @GetMapping("/eventos")
    public ArrayList<Evento> listarEventos() {
        return new ArrayList<>(eventoRepository.findAll());
    }

    @PostMapping("/eventos")
    public ResponseEntity<String> cadastrarEvento(@RequestBody Evento evento) {

        if (evento.getNome() == null || evento.getNome().isBlank()
                || evento.getLocal() == null || evento.getLocal().isBlank()) {

            return ResponseEntity.badRequest().body("Nome e local são obrigatórios!");
        }

        eventoRepository.save(evento);

        return ResponseEntity.ok("Evento cadastrado!");
    }

    @PutMapping("/eventos/{id}")
    public String atualizarEvento(@PathVariable Long id, @RequestBody Evento dados) {

        Evento evento = eventoRepository.findById(id).orElse(null);

        if (evento == null) {
            return "Evento não encontrado!";
        }

        evento.setNome(dados.getNome());
        evento.setLocal(dados.getLocal());

        eventoRepository.save(evento);

        return "Evento atualizado!";
    }

    @DeleteMapping("/eventos/{id}")
    public ResponseEntity<String> excluirEvento(@PathVariable Long id) {

        if (!eventoRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Evento não encontrado!");
        }

        eventoRepository.deleteById(id);

        return ResponseEntity.ok("Evento excluído com sucesso!");
    }

}