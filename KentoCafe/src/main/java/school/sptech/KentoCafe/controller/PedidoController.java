package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoRequest;
import school.sptech.KentoCafe.dto.pedido.pedido.PedidoResponse;
import school.sptech.KentoCafe.service.PedidoService;
@Tag(name = "Pedido", description = "Orquestrador de requisições envolvendo pedido")
@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Adiciona novo pedido", description = "utilizado quando um novo pedido é criado")
    @PostMapping
    public ResponseEntity<PedidoResponse> postPedido(@RequestBody @Valid PedidoRequest request) {
        PedidoResponse pedido = pedidoService.finalizarPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }
}
