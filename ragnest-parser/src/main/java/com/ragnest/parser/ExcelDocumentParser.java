package com.ragnest.parser;

import java.io.InputStream;

/**
 * Excel 文档解析器（占位实现）。
 *
 * <p>Excel（.xlsx）解析需引入 Apache POI 等依赖，通常用于表格数据的提取。</p>
 *
 * <p>当前为占位，待明确 Excel 解析方案后实现。</p>
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
        throw new UnsupportedOperationException("Excel 解析尚未接入");
    }
}
