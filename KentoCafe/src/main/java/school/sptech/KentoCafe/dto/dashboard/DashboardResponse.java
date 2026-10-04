package school.sptech.KentoCafe.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DashboardResponse {
    private LocalDate inicio;
    private LocalDate fim;
    private LocalDate inicioAnterior;
    private LocalDate fimAnterior;
    private Integer metaMinutos;
    private Indicadores atual;
    private Indicadores anterior;
    // menor volume de pedidos numa hora a partir do qual muitos pedidos passam da meta de tempo (null = não atingido)
    private Integer limitePedidosPorHora;
    // dia da semana + hora em que alguma hora do período chegou ao limite, dos mais frequentes aos menos
    private List<HorarioCritico> horariosCriticos;
    private List<PontoSerieDiaria> serieDiaria;
    private List<MovimentoHora> movimentoPorHora;
    private List<EsperaPorVolume> esperaPorVolume;
    private List<ProdutoVendido> topProdutos;
    private List<CategoriaFaturamento> faturamentoPorCategoria;
    private List<PersonalizacaoPedida> personalizacoes;

    public LocalDate getInicio() { return inicio; }
    public void setInicio(LocalDate inicio) { this.inicio = inicio; }

    public LocalDate getFim() { return fim; }
    public void setFim(LocalDate fim) { this.fim = fim; }

    public LocalDate getInicioAnterior() { return inicioAnterior; }
    public void setInicioAnterior(LocalDate inicioAnterior) { this.inicioAnterior = inicioAnterior; }

    public LocalDate getFimAnterior() { return fimAnterior; }
    public void setFimAnterior(LocalDate fimAnterior) { this.fimAnterior = fimAnterior; }

    public Integer getMetaMinutos() { return metaMinutos; }
    public void setMetaMinutos(Integer metaMinutos) { this.metaMinutos = metaMinutos; }

    public Indicadores getAtual() { return atual; }
    public void setAtual(Indicadores atual) { this.atual = atual; }

    public Indicadores getAnterior() { return anterior; }
    public void setAnterior(Indicadores anterior) { this.anterior = anterior; }

    public Integer getLimitePedidosPorHora() { return limitePedidosPorHora; }
    public void setLimitePedidosPorHora(Integer limitePedidosPorHora) { this.limitePedidosPorHora = limitePedidosPorHora; }

    public List<HorarioCritico> getHorariosCriticos() { return horariosCriticos; }
    public void setHorariosCriticos(List<HorarioCritico> horariosCriticos) { this.horariosCriticos = horariosCriticos; }

    public List<PontoSerieDiaria> getSerieDiaria() { return serieDiaria; }
    public void setSerieDiaria(List<PontoSerieDiaria> serieDiaria) { this.serieDiaria = serieDiaria; }

    public List<MovimentoHora> getMovimentoPorHora() { return movimentoPorHora; }
    public void setMovimentoPorHora(List<MovimentoHora> movimentoPorHora) { this.movimentoPorHora = movimentoPorHora; }

    public List<EsperaPorVolume> getEsperaPorVolume() { return esperaPorVolume; }
    public void setEsperaPorVolume(List<EsperaPorVolume> esperaPorVolume) { this.esperaPorVolume = esperaPorVolume; }

    public List<ProdutoVendido> getTopProdutos() { return topProdutos; }
    public void setTopProdutos(List<ProdutoVendido> topProdutos) { this.topProdutos = topProdutos; }

    public List<CategoriaFaturamento> getFaturamentoPorCategoria() { return faturamentoPorCategoria; }
    public void setFaturamentoPorCategoria(List<CategoriaFaturamento> faturamentoPorCategoria) { this.faturamentoPorCategoria = faturamentoPorCategoria; }

    public List<PersonalizacaoPedida> getPersonalizacoes() { return personalizacoes; }
    public void setPersonalizacoes(List<PersonalizacaoPedida> personalizacoes) { this.personalizacoes = personalizacoes; }

    // Números de um período; o mesmo formato é usado para o período atual e o anterior.
    public static class Indicadores {
        private BigDecimal faturamento;
        private Long pedidos;
        private Long cancelados;
        private BigDecimal ticketMedio;
        private Double itensPorPedido;
        private Double tempoMedianoMinutos;   // null quando não há pedido pronto no período
        private Double percentualNoPrazo;     // % dos pedidos prontos em até metaMinutos

        public BigDecimal getFaturamento() { return faturamento; }
        public void setFaturamento(BigDecimal faturamento) { this.faturamento = faturamento; }

        public Long getPedidos() { return pedidos; }
        public void setPedidos(Long pedidos) { this.pedidos = pedidos; }

        public Long getCancelados() { return cancelados; }
        public void setCancelados(Long cancelados) { this.cancelados = cancelados; }

        public BigDecimal getTicketMedio() { return ticketMedio; }
        public void setTicketMedio(BigDecimal ticketMedio) { this.ticketMedio = ticketMedio; }

        public Double getItensPorPedido() { return itensPorPedido; }
        public void setItensPorPedido(Double itensPorPedido) { this.itensPorPedido = itensPorPedido; }

        public Double getTempoMedianoMinutos() { return tempoMedianoMinutos; }
        public void setTempoMedianoMinutos(Double tempoMedianoMinutos) { this.tempoMedianoMinutos = tempoMedianoMinutos; }

        public Double getPercentualNoPrazo() { return percentualNoPrazo; }
        public void setPercentualNoPrazo(Double percentualNoPrazo) { this.percentualNoPrazo = percentualNoPrazo; }
    }

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

    // Média de pedidos naquela hora do dia, separando dias úteis de fins de semana.
    public static class MovimentoHora {
        private Integer hora;
        private Double mediaDiasUteis;
        private Double mediaFimDeSemana;

        public Integer getHora() { return hora; }
        public void setHora(Integer hora) { this.hora = hora; }

        public Double getMediaDiasUteis() { return mediaDiasUteis; }
        public void setMediaDiasUteis(Double mediaDiasUteis) { this.mediaDiasUteis = mediaDiasUteis; }

        public Double getMediaFimDeSemana() { return mediaFimDeSemana; }
        public void setMediaFimDeSemana(Double mediaFimDeSemana) { this.mediaFimDeSemana = mediaFimDeSemana; }
    }

    // Espera dos pedidos feitos em horas que tiveram exatamente `pedidosNaHora` pedidos.
    public static class EsperaPorVolume {
        private Integer pedidosNaHora;
        private Double esperaMedianaMinutos;
        private Double percentualAcimaDaMeta;
        private Integer quantidadeHoras;

        public Integer getPedidosNaHora() { return pedidosNaHora; }
        public void setPedidosNaHora(Integer pedidosNaHora) { this.pedidosNaHora = pedidosNaHora; }

        public Double getEsperaMedianaMinutos() { return esperaMedianaMinutos; }
        public void setEsperaMedianaMinutos(Double esperaMedianaMinutos) { this.esperaMedianaMinutos = esperaMedianaMinutos; }

        public Double getPercentualAcimaDaMeta() { return percentualAcimaDaMeta; }
        public void setPercentualAcimaDaMeta(Double percentualAcimaDaMeta) { this.percentualAcimaDaMeta = percentualAcimaDaMeta; }

        public Integer getQuantidadeHoras() { return quantidadeHoras; }
        public void setQuantidadeHoras(Integer quantidadeHoras) { this.quantidadeHoras = quantidadeHoras; }
    }

    public static class HorarioCritico {
        private Integer diaSemana;   // 1 = segunda ... 7 = domingo
        private Integer hora;
        private Integer ocorrencias;

        public Integer getDiaSemana() { return diaSemana; }
        public void setDiaSemana(Integer diaSemana) { this.diaSemana = diaSemana; }

        public Integer getHora() { return hora; }
        public void setHora(Integer hora) { this.hora = hora; }

        public Integer getOcorrencias() { return ocorrencias; }
        public void setOcorrencias(Integer ocorrencias) { this.ocorrencias = ocorrencias; }
    }

    public static class ProdutoVendido {
        private String produto;
        private Long quantidade;
        private BigDecimal faturamento;
        private Double percentualFaturamento;

        public String getProduto() { return produto; }
        public void setProduto(String produto) { this.produto = produto; }

        public Long getQuantidade() { return quantidade; }
        public void setQuantidade(Long quantidade) { this.quantidade = quantidade; }

        public BigDecimal getFaturamento() { return faturamento; }
        public void setFaturamento(BigDecimal faturamento) { this.faturamento = faturamento; }

        public Double getPercentualFaturamento() { return percentualFaturamento; }
        public void setPercentualFaturamento(Double percentualFaturamento) { this.percentualFaturamento = percentualFaturamento; }
    }

    public static class CategoriaFaturamento {
        private String categoria;
        private BigDecimal faturamento;
        private Double percentual;

        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }

        public BigDecimal getFaturamento() { return faturamento; }
        public void setFaturamento(BigDecimal faturamento) { this.faturamento = faturamento; }

        public Double getPercentual() { return percentual; }
        public void setPercentual(Double percentual) { this.percentual = percentual; }
    }

    public static class PersonalizacaoPedida {
        private String nome;
        private Long quantidade;

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public Long getQuantidade() { return quantidade; }
        public void setQuantidade(Long quantidade) { this.quantidade = quantidade; }
    }
}
