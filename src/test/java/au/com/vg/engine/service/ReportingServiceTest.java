package au.com.vg.engine.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class ReportingServiceTest {

    @InjectMocks
    private ReportingService reportingService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(reportingService, "reportOutputDirs", "src/test/resources/reports/");
    }

    @Test
    public void testGetTradingReport() {
        ReflectionTestUtils.setField(reportingService, "eventFilesDir", "src/test/resources/events/testGetTradingReport/");
        String response = reportingService.getTradingReport();
        Assert.assertTrue(response.contains("Report file generated in path"));

        try {
            Files.delete(Paths.get(response.substring(response.indexOf(":") + 2)));
            log.info("Test File deleted Successfully");
        } catch (IOException e) {
            log.error("Exception happened while deleting the records: {}", e.getMessage());
        }
    }

    @Test
    public void testGetTradingReportNoRecords() {
        ReflectionTestUtils.setField(reportingService, "eventFilesDir", "src/test/resources/events/testGetTradingReportNoRecords/");
        String response = reportingService.getTradingReport();
        Assert.assertTrue(response.equals("No Records Found for Report"));
    }

    @Test
    public void testGetTradingReportNoNodeFound() {
        ReflectionTestUtils.setField(reportingService, "eventFilesDir", "src/test/resources/events/testGetTradingReportNoNodeFound/");
        String response = reportingService.getTradingReport();
        Assert.assertTrue(response.equals("No Records Found for Report"));
    }

    @Test
    public void testGetTradingReportNoEventFilesFound() {
        ReflectionTestUtils.setField(reportingService, "eventFilesDir", "src/test/resources/events/testGetTradingReportNoEventFilesFound/");
        String response = reportingService.getTradingReport();
        Assert.assertTrue(response.equals("No Event Files found"));
    }
}
