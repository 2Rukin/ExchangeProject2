package com.exchangeinformant.controllers;

import com.exchangeinformant.aop.UserInfoInsertion;
import com.exchangeinformant.model.Stock;
import com.exchangeinformant.services.StockDbService;
import com.exchangeinformant.services.StockService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by e-davidenko
 * Date: 12.01.2023
 * Time: 15:10
 */

@RestController
@RefreshScope
@Tag(name = "Контроллер Quotes", description = "Позволяет получить список всех акций, а также цены на акции")
public class StockRestController {
    private final StockDbService stockDbService;
    private final StockService stockService;


    public StockRestController(StockDbService stockDbService, @Qualifier("stockServiceSearcher") StockService stockService) {
        this.stockDbService = stockDbService;
        this.stockService = stockService;
    }

    @Operation(summary = "Получение акции из БД по её SecureCode (тикеру)")
    @GetMapping("/stock")
    @UserInfoInsertion
    public Stock getStock(@RequestParam("name") @Parameter(description = "SecureCode акции (тикер)") String secureCode) {
        return stockDbService.getStock(secureCode);
    }

    @Operation(summary = "Получение акции из БД по её SecureCode (тикеру) с ценами за текущий день")
    @GetMapping("/stock/query")
    @UserInfoInsertion
    public Stock getStockWithParameters(@RequestParam(name = "dateFrom", required = false, defaultValue = "#{T(java.time.LocalDateTime).now().toLocalDate().atStartOfDay()}") LocalDateTime dateFrom,
                                        @RequestParam(name = "dateTo", required = false, defaultValue = "#{T(java.time.LocalDateTime).now()}") LocalDateTime dateTo,
                                        @RequestParam("stock") @Parameter(description = "SecureCode акции (тикер)") String secureCode) {
        return stockDbService.getStockByDate(secureCode, dateFrom, dateTo);
    }

    @Operation(summary = "Получение всех акций из БД с ценами за текущий день")
    @GetMapping("/allWithDates")
    @UserInfoInsertion
    public List<Stock> getAllStocksByDates(@RequestParam(name = "dateFrom", required = false, defaultValue = "#{T(java.time.LocalDateTime).now().toLocalDate().atStartOfDay()}") LocalDateTime dateFrom,
                                           @RequestParam(name = "dateTo", required = false, defaultValue = "#{T(java.time.LocalDateTime).now()}") LocalDateTime dateTo) {
        return stockDbService.getAllStocksByDate(dateFrom, dateTo);
    }

    @Operation(summary = "Получение списка всех акций из БД")
    @GetMapping("/all")
    @UserInfoInsertion
    public List<Stock> getAllStocks() {
        return stockDbService.getAllStocks();
    }

    @Operation(summary = "Получение списка акций из БД по их SecureCode (тикерам)")
    @PostMapping("/availableStocks")
    @UserInfoInsertion
    public List<Stock> getAllAvailableStocks(@RequestBody List<String> securityCodes) {
        return stockDbService.getAllAvailableStocksByCodes(securityCodes);
    }

    @Operation(summary = "Получение акции по её SecureCode (тикеру) напрямую с подключенного сервиса (BCS или Tinkoff)")
    @GetMapping("/directStock")
    @UserInfoInsertion
    public Stock getStockDirectlyByBcs(@RequestParam("name") @Parameter(description = "SecureCode акции (тикер)") String secureCode) {
        return stockService.getStockDirectly(secureCode);
    }

    @Operation(summary = "Получение списка акций по их SecureCode (тикерам) напрямую с подключенного сервиса (BCS или Tinkoff)")
    @PostMapping("/directStock")
    @UserInfoInsertion
    public List<Stock> getStocksDirectlyByBcs(@RequestBody List<String> secureCodes) {
        return stockService.getStocksDirectly(secureCodes);
    }
}
