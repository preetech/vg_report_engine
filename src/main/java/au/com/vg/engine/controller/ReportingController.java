package au.com.vg.engine.controller;

import au.com.vg.engine.service.ReportingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/public")
public class ReportingController {

    @Autowired
    ReportingService reportingService;

    @GetMapping("generateReport")
    public String getTradingReport() {
        log.info("Request recieved for calculating the trading report.");
        return reportingService.getTradingReport();
    }
}
