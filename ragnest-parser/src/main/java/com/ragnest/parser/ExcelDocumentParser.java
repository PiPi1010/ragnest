package com.ragnest.parser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Excel 文档解析器。
 *
 * <p>基于 Apache POI 解析 {@code .xlsx} / {@code .xls}，逐 sheet 逐行提取单元格内容，
 * 每行用制表符分隔，便于后续分块与向量化。{@code .csv} 直接按文本读取。</p>
 */
public class ExcelDocumentParser implements DocumentParser {

    @Override
    public boolean supports(String fileType) {
        return fileType != null && (fileType.equalsIgnoreCase("xls")
                || fileType.equalsIgnoreCase("xlsx")
                || fileType.equalsIgnoreCase("csv"));
    }

    @Override
    public ParsedDocument parse(InputStream inputStream, String filename) {
        String content;
        if (filename != null && filename.toLowerCase().endsWith(".csv")) {
            content = parseCsv(inputStream);
        } else {
            content = parseWorkbook(inputStream);
        }

        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("filename", filename);
        metadata.put("fileType", "excel");

        return ParsedDocument.builder()
                .content(content)
                .metadata(metadata)
                .build();
    }

    private String parseWorkbook(InputStream inputStream) {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            DataFormatter formatter = new DataFormatter();
            StringBuilder sb = new StringBuilder();
            int sheetCount = workbook.getNumberOfSheets();
            for (int i = 0; i < sheetCount; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                if (sheetCount > 1) {
                    sb.append("【").append(sheet.getSheetName()).append("】\n");
                }
                for (Row row : sheet) {
                    StringBuilder line = new StringBuilder();
                    for (Cell cell : row) {
                        String value = formatter.formatCellValue(cell);
                        if (value != null && !value.isBlank()) {
                            if (line.length() > 0) {
                                line.append('\t');
                            }
                            line.append(value);
                        }
                    }
                    if (line.length() > 0) {
                        sb.append(line).append('\n');
                    }
                }
            }
            return sb.toString().trim();
        } catch (Exception e) {
            throw new IllegalStateException("解析 Excel 文档失败: " + e.getMessage(), e);
        }
    }

    private String parseCsv(InputStream inputStream) {
        try (InputStream is = inputStream) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("解析 CSV 文档失败: " + e.getMessage(), e);
        }
    }
}
