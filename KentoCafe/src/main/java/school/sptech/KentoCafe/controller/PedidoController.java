package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoRequest;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoResponse;
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
            description = "Registra um novo pedido com status 'Em preparo' automaticamente")
    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Funcionário ou produto não encontrado")
    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@RequestBody @Valid PedidoRequest request) {
        Pedido pedido = pedidoService.criar(request);
        return ResponseEntity.status(201).body(PedidoMapper.toResponse(pedido));
    }

    @Operation(summary = "Listar todos os pedidos")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        List<Pedido> pedidos = pedidoService.listarTodos();
        return pedidos.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(PedidoMapper.toResponseList(pedidos));
    }

    @Operation(summary = "Buscar pedido por ID")
    @ApiResponse(responseCode = "200", description = "Pedido encontrado")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(PedidoMapper.toResponse(pedidoService.concluir(id)));
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
}
