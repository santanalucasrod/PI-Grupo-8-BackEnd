package school.sptech.KentoCafe.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DashboardResponse {
    private BigDecimal faturamentoTotal;
    private Long totalPedidos;
    private Double tempoMedioPreparoMinutos;
    private String produtoMaisVendido;
    private List<PontoSerieDiaria> serieDiaria;
    private List<CategoriaFaturamento> faturamentoPorCategoria;

    public BigDecimal getFaturamentoTotal() { return faturamentoTotal; }
    public void setFaturamentoTotal(BigDecimal faturamentoTotal) { this.faturamentoTotal = faturamentoTotal; }

    public Long getTotalPedidos() { return totalPedidos; }
    public void setTotalPedidos(Long totalPedidos) { this.totalPedidos = totalPedidos; }

    public Double getTempoMedioPreparoMinutos() { return tempoMedioPreparoMinutos; }
    public void setTempoMedioPreparoMinutos(Double tempoMedioPreparoMinutos) { this.tempoMedioPreparoMinutos = tempoMedioPreparoMinutos; }

    public String getProdutoMaisVendido() { return produtoMaisVendido; }
    public void setProdutoMaisVendido(String produtoMaisVendido) { this.produtoMaisVendido = produtoMaisVendido; }

    public List<PontoSerieDiaria> getSerieDiaria() { return serieDiaria; }
    public void setSerieDiaria(List<PontoSerieDiaria> serieDiaria) { this.serieDiaria = serieDiaria; }

    public List<CategoriaFaturamento> getFaturamentoPorCategoria() { return faturamentoPorCategoria; }
    public void setFaturamentoPorCategoria(List<CategoriaFaturamento> faturamentoPorCategoria) { this.faturamentoPorCategoria = faturamentoPorCategoria; }

    public static class PontoSerieDiaria {
        private LocalDate data;
        private BigDecimal faturamento;
        private Long totalPedidos;

        public LocalDate getData() { return data; }
        public void setData(LocalDate data) { this.data = data; }

        public BigDecimal getFaturamento() { return faturamento; }
        public void setFaturamento(BigDecimal faturamento) { this.faturamento = faturamento; }

        public Long getTotalPedidos() { return totalPedidos; }
        public void setTotalPedidos(Long totalPedidos) { this.totalPedidos = totalPedidos; }
    }

    public static class CategoriaFaturamento {
        private String categoria;
        private BigDecimal faturamento;

        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }

        public BigDecimal getFaturamento() { return faturamento; }
        public void setFaturamento(BigDecimal faturamento) { this.faturamento = faturamento; }
    }
}