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
            description = "Retorna, para o período informado e para o período anterior de mesmo tamanho, " +
                    "faturamento, pedidos, ticket médio e atendimento dentro da meta de tempo; além do " +
                    "movimento médio por hora, da espera por volume de pedidos na hora, dos produtos mais " +
                    "vendidos, do faturamento por categoria e das personalizações mais pedidas")
    @ApiResponse(responseCode = "200", description = "Resumo calculado com sucesso")
    @ApiResponse(responseCode = "400", description = "Período inválido ou maior que 366 dias")
    @GetMapping("/resumo")
    public ResponseEntity<DashboardResponse> buscarResumo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(dashboardService.buscarResumo(inicio, fim));
    }
}