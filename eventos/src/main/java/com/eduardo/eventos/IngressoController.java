package com.eduardo.eventos;

import java.util.ArrayList;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class IngressoController {

    private final IngressoRepository ingressoRepository;
    private final EventoRepository eventoRepository;

    public IngressoController(IngressoRepository ingressoRepository,
                              EventoRepository eventoRepository) {
        this.ingressoRepository = ingressoRepository;
        this.eventoRepository = eventoRepository;
    }

    @PostMapping("/eventos/{id}/ingressos")
    public ResponseEntity<String> cadastrarIngresso(
            @PathVariable Long id,
            @RequestBody Ingresso ingresso) {

        Evento evento = eventoRepository.findById(id).orElse(null);

        if (evento == null) {
            return ResponseEntity.status(404).body("Evento não encontrado!");
        }

        if (ingresso.getNomeComprador() == null ||
                ingresso.getNomeComprador().isBlank()) {

            return ResponseEntity.badRequest()
                    .body("Nome do comprador é obrigatório!");
        }

        long ingressosVendidos = ingressoRepository.countByEvento_Id(id);

        if (ingressosVendidos >= evento.getCapacidade()) {
            return ResponseEntity.status(409).body("Ingressos esgotados!");
        }

        ingresso.setEvento(evento);
        ingressoRepository.save(ingresso);

        return ResponseEntity.ok("Ingresso cadastrado!");
    }

    @GetMapping("/eventos/{id}/ingressos")
    public ArrayList<Ingresso> listarIngressos(@PathVariable Long id) {

        return new ArrayList<>(ingressoRepository.findByEvento_Id(id));
    }

    @DeleteMapping("/ingressos/{id}")
    public ResponseEntity<String> cancelarIngresso(@PathVariable Long id) {

        if (!ingressoRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Ingresso não encontrado!");
        }

        ingressoRepository.deleteById(id);

        return ResponseEntity.ok("Ingresso cancelado!");
    }

    @GetMapping("/eventos/{id}/vagas")
    public ResponseEntity<String> consultarVagas(@PathVariable Long id) {

        Evento evento = eventoRepository.findById(id).orElse(null);

        if (evento == null) {
            return ResponseEntity.status(404).body("Evento não encontrado!");
        }

        long vendidos = ingressoRepository.countByEvento_Id(id);

        long vagas = evento.getCapacidade() - vendidos;

        return ResponseEntity.ok("Vagas disponíveis: " + vagas);
    }

    @GetMapping("/eventos/{id}/faturamento")
    public ResponseEntity<String> calcularFaturamento(@PathVariable Long id) {

        Evento evento = eventoRepository.findById(id).orElse(null);

        if (evento == null) {
            return ResponseEntity.status(404).body("Evento não encontrado!");
        }

        if (evento.getPrecoIngresso() == null) {
            return ResponseEntity.status(409).body("Evento sem preço cadastrado!");
        }

        long vendidos = ingressoRepository.countByEvento_Id(id);

        BigDecimal faturamento = evento.getPrecoIngresso()
                .multiply(BigDecimal.valueOf(vendidos));

        return ResponseEntity.ok("Faturamento: R$ " + faturamento);
    }

}