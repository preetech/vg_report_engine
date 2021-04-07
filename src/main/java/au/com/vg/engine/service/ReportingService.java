package au.com.vg.engine.service;

import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class ReportingService {

    private static final String BUYER_PARTY_XPATH = "//buyerPartyReference/@href";
    private static final String SELLER_PARTY_XPATH = "//sellerPartyReference/@href";
    private static final String PREMIUM_AMOUNT_XPATH = "//paymentAmount/amount";
    private static final String PREMIUM_CURRENCY_XPATH = "//paymentAmount/currency";

    @Value("${report.event.file.path}")
    private String eventFilesDir;

    @Value("${report.event.report.path}")
    private String reportOutputDirs;

    public String getTradingReport() {
        Path reportPath = null;
        try (Stream<Path> fileStream = Files.list(Paths.get(eventFilesDir))) {

            List<File> files = fileStream
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> file.getFileName().toString().endsWith(".xml"))
                    .map(Path::toFile)
                    .collect(Collectors.toList());

            if (files.isEmpty()) {
                log.info("No Event Files found");
                return "No Event Files found";
            }

            List<String[]> csvRecords = new ArrayList<>();
            String[] csvHeader = {"BuyerParty", "SellerParty", "Amount", "Currency"};
            csvRecords.add(csvHeader);

            for (File file : files) {
                try {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document xmlDocument = dBuilder.parse(file);

                    XPath xPath = XPathFactory.newInstance().newXPath();

                    String buyerParty = getValueByXpath(BUYER_PARTY_XPATH, xmlDocument, xPath);
                    String sellerParty = getValueByXpath(SELLER_PARTY_XPATH, xmlDocument, xPath);
                    String premiumAmount = getValueByXpath(PREMIUM_AMOUNT_XPATH, xmlDocument, xPath);
                    String premiumCurrency = getValueByXpath(PREMIUM_CURRENCY_XPATH, xmlDocument, xPath);

                    if (((sellerParty.equalsIgnoreCase("EMU_BANK") && premiumCurrency.equalsIgnoreCase("AUD")) ||
                            (sellerParty.equalsIgnoreCase("BISON_BANK") && premiumCurrency.equalsIgnoreCase("USD"))) &&
                            !isAnagramSort(sellerParty, buyerParty)) {
                        String[] record = {buyerParty, sellerParty, premiumAmount, premiumCurrency};
                        csvRecords.add(record);
                    }
                } catch (Exception e) {
                    log.error("Exception happened while adding report for file: {}", file.getName(), e);
                }
            }

            if (csvRecords.size() == 1) {
                log.info("No records found");
                return "No Records Found for Report";
            }

            reportPath = File.createTempFile("Report", ".csv", new File(reportOutputDirs)).toPath();
            csvWriterAll(csvRecords, reportPath);
            log.info("Report File generated in path : {}", reportPath);

        } catch (Exception e) {
            log.error("Exception happened while generating report with message : {}", e.getMessage(), e);
        }
        return "Report file generated in path: " + reportPath.toString();
    }

    private void csvWriterAll(List<String[]> stringArray, Path path) throws Exception {
        CSVWriter writer = new CSVWriter(new FileWriter(path.toString()),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.RFC4180_LINE_END);

        writer.writeAll(stringArray);
        writer.close();
    }

    private String getValueByXpath(String buyerPartyXpath, Document xmlDocument, XPath xPath) throws Exception {
        NodeList nodeList = (NodeList) xPath.compile(buyerPartyXpath).evaluate(xmlDocument, XPathConstants.NODESET);
        if (nodeList.getLength() == 0) {
            throw new Exception("XML Node not found");
        }
        return nodeList.item(0).getTextContent();
    }

    private boolean isAnagramSort(String string1, String string2) {
        if (string1.length() != string2.length()) {
            return false;
        }
        char[] a1 = string1.toCharArray();
        char[] a2 = string2.toCharArray();
        Arrays.sort(a1);
        Arrays.sort(a2);
        return Arrays.equals(a1, a2);
    }

}
