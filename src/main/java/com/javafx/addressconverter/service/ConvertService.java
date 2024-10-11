package com.javafx.addressconverter.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javafx.addressconverter.config.DotEnvConfig;
import com.javafx.addressconverter.util.Util;
import javafx.scene.control.Label;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class ConvertService {

    private static final String API = DotEnvConfig.getApi();
    private static final String KEY = DotEnvConfig.getApiConfirmKey();
    private static final String TYPE = DotEnvConfig.getApiResultType();
    private static File fileData = null;

    private static final Logger log = Logger.getLogger(ConvertService.class.getName());

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Util util = new Util();

    public void convertAddress(File file, Label label) {
        fileData = file;
        validateFileSelected();
        convertFile(fileData);
        util.resetFileSelection(fileData, label);
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void validateFileSelected() {
        if (fileData == null) {
            throw new IllegalArgumentException("No file selected for converting");
        }
    }

    private void convertFile(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            sheet.forEach(row -> {
                Cell cellToRead = row.getCell(0);
                if (cellToRead == null) {
                    throw new IllegalArgumentException("cellToRead is null");
                }
                String response = fetchResponseFromApi(cellToRead);
                String jibunAddr = parseJibunAddrFromResponse(response);
                setNewCellValue(row, jibunAddr);
            });
            saveExcelFile(file, workbook);
        } catch (IOException e) {
            log.warning("Conversion failed : " + e.getMessage());
        }
    }

    private static void setNewCellValue(Row row, String jibunAddr) {
        Cell cellToWrite = row.createCell(1);
        cellToWrite.setCellValue(jibunAddr);
    }

    private String parseJibunAddrFromResponse(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode jusoArray = rootNode.path("results").path("juso");
            if (jusoArray.isEmpty() || !jusoArray.isArray()) {
                throw new IllegalArgumentException("No address found");
            }
            log.info("jibunAddr = " + jusoArray.get(0).get("jibunAddr").asText());
            return jusoArray.get(0).get("jibunAddr").asText();
        } catch (IOException e) {
            log.warning("Failed to parse JSON response : " + e.getMessage());
            return "";
        }
    }

    private static String fetchResponseFromApi(Cell cellToRead) {
        String apiUrl = setApiAddr(cellToRead);
        log.info("apiUrl : " + apiUrl);
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            log.warning("Failed to fetch from API. URL: " + apiUrl + ", Error: " + e.getMessage());
            return "";
        }
    }

    private static String setApiAddr(Cell cellToRead)  {
        String doroAddr = cellToRead.getStringCellValue();
        String currentPage = "&currentPage=1";
        String currentPerPage = "&currentPerPage=10";
        String keyword = "&keyword=" + URLEncoder.encode(doroAddr, StandardCharsets.UTF_8);
        String apiUrl = API + KEY + currentPage + currentPerPage + keyword + TYPE;
        log.info("apiUrl : " + apiUrl);

        return apiUrl;
    }

    private static void saveExcelFile(File file, Workbook workbook)  {
        File newFile = setNewFilePath(file);
        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            workbook.write(fos);
            log.info("File saved at : " + newFile.getAbsolutePath());
        } catch (IOException e) {
            log.warning("File not saved : " + e.getMessage());
        }
    }

    private static File setNewFilePath(File file) {
        return new File(file.getParent(), "converted_" + file.getName());
    }
}
