    package com.eduardo.eventos;

    import java.math.BigDecimal;
    import jakarta.persistence.*;

    @Entity
    public class Evento {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String nome;
        private String local;
        private int capacidade;
        private BigDecimal precoIngresso;

        public Evento() {
        }

        public Evento(String nome, String local) {
            this.nome = nome;
            this.local = local;
        }

        public Long getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        public String getLocal() {
            return local;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public void setLocal(String local) {
            this.local = local;
        }

        public int getCapacidade() {
            return capacidade;
        }

        public void setCapacidade(int capacidade) {
            this.capacidade = capacidade;
        }

        public BigDecimal getPrecoIngresso() {
            return precoIngresso;
        }

        public void setPrecoIngresso(BigDecimal precoIngresso) {
            this.precoIngresso = precoIngresso;
        }

    }