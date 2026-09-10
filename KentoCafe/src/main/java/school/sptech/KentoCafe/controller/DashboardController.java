package school.sptech.KentoCafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.KentoCafe.dto.dashboard.DashboardResponse;
import school.sptech.KentoCafe.service.DashboardService;

import java.time.LocalDate;

@Tag(name = "Dashboard", description = "Métricas e indicadores da cafeteria")
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(summary = "Resumo do dashboard",
            description = "Retorna faturamento, total de pedidos, tempo médio de preparo, " +
                    "produto mais vendido e breakdown por categoria no xperíodo informado")
    @ApiResponse(responseCode = "200", description = "Resumo calculado com sucesso")
    @ApiResponse(responseCode = "400", description = "Período inválido")
    @GetMapping("/resumo")
    public ResponseEntity<DashboardResponse> buscarResumo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(dashboardService.buscarResumo(inicio, fim));
    }
}