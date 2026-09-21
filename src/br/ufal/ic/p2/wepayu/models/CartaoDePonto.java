package br.ufal.ic.p2.wepayu.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CartaoDePonto {
        private LocalDate data;
        private BigDecimal horas;

        public CartaoDePonto(LocalDate data, BigDecimal horas){
            this.data = data;
            this.horas = horas;
        }

        public LocalDate getData() {return data;}
        public BigDecimal getHoras() {return horas;}
}

