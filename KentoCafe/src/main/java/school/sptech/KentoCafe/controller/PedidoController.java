package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoRequest;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoResponse;
import school.sptech.KentoCafe.entity.ItemPedido;
import school.sptech.KentoCafe.entity.Pedido;
import school.sptech.KentoCafe.mapper.PedidoMapper;
import school.sptech.KentoCafe.service.PedidoService;

import java.util.List;

@Tag(name = "Pedidos", description = "Gerenciamento de pedidos da cafeteria")
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Criar pedido",
            description = "Registra um novo pedido com status 'Pendente' automaticamente")
    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Funcionário ou produto não encontrado")
    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@RequestBody @Valid PedidoRequest request) {
        Pedido pedido = pedidoService.criar(request);
        return ResponseEntity.status(201).body(PedidoMapper.toResponse(pedido));
    }

    @Operation(summary = "Listar pedidos",
            description = "Use ?ativos=true para retornar somente pedidos Pendente/Em preparo (fila do barista)")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos(
            @RequestParam(required = false, defaultValue = "false") boolean ativos) {
        List<Pedido> pedidos = ativos ? pedidoService.listarAtivos() : pedidoService.listarTodos();
        return pedidos.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(PedidoMapper.toResponseList(pedidos));
    }

    @Operation(summary = "Buscar pedido por ID")
    @ApiResponse(responseCode = "200", description = "Pedido encontrado")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(PedidoMapper.toResponse(pedidoService.buscarPorId(id)));
    }

    @Operation(summary = "Listar pedidos por status",
            description = "Filtra pedidos pelo nome do status: 'Em preparo', 'Pronto' ou 'Cancelado'")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado para esse status")
    @GetMapping("/status/{statusNome}")
    public ResponseEntity<List<Pedido>> listarPorStatus(@PathVariable String statusNome) {
        List<Pedido> pedidos = pedidoService.listarPorStatus(statusNome);
        return pedidos.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(pedidos);
    }

    @Operation(summary = "Concluir pedido",
            description = "Muda o status para 'Pronto' e registra o horário de conclusão")
    @ApiResponse(responseCode = "200", description = "Pedido concluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @ApiResponse(responseCode = "409", description = "Pedido já está pronto ou foi cancelado")
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<PedidoResponse> concluir(@PathVariable Long id) {
        return ResponseEntity.ok(PedidoMapper.toResponse(pedidoService.concluir(id)));
    }

    @Operation(summary = "Cancelar pedido",
            description = "Muda o status para 'Cancelado' — somente gerentes podem cancelar")
    @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @ApiResponse(responseCode = "409", description = "Pedido já concluído ou já cancelado")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(PedidoMapper.toResponse(pedidoService.cancelar(id)));
    }

    @Operation(summary = "Atualizar status do pedido",
            description = "Move o pedido entre PENDENTE, EM_PREPARO ou PRONTO. Usado pela tela de fila do barista.")
    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Status inválido")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @ApiResponse(responseCode = "409", description = "Pedido já está pronto ou foi cancelado")
    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(@PathVariable Long id,
                                                            @RequestBody AtualizarStatusRequest request) {
        Pedido pedido = pedidoService.atualizarStatus(id, request.getStatus());
        return ResponseEntity.ok(PedidoMapper.toResponse(pedido));
    }

    @Operation(summary = "Marcar/desmarcar item do pedido como pronto",
            description = "Usado pelo barista para riscar cada item da fila conforme prepara")
    @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Item não encontrado")
    @PatchMapping("/itens/{itemId}/pronto")
    public ResponseEntity<PedidoResponse> marcarItemPronto(@PathVariable Long itemId,
                                                             @RequestBody MarcarItemProntoRequest request) {
        ItemPedido item = pedidoService.marcarItemPronto(itemId, request.isPronto());
        return ResponseEntity.ok(PedidoMapper.toResponse(item.getPedido()));
    }

    public static class AtualizarStatusRequest {
        private String status;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class MarcarItemProntoRequest {
        private boolean pronto;

        public boolean isPronto() { return pronto; }
        public void setPronto(boolean pronto) { this.pronto = pronto; }
    }
}
