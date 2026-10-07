package com.ragnest.parser;

import java.io.InputStream;

/**
 * Word 文档解析器（占位实现）。
 *
 * <p>Word（.docx）解析需引入 Apache POI 等依赖。接入时：</p>
 * <ol>
 *   <li>引入 Apache POI（poi-ooxml）</li>
 *   <li>处理 .doc（旧格式，需 poi-scratchpad）与 .docx 的差异</li>
 * </ol>
 *
 * <p>当前为占位，待明确 Word 解析方案后实现。</p>
 */
public class WordDocumentParser implements DocumentParser {

    @Override
    public boolean supports(String fileType) {
        return fileType != null && (fileType.equalsIgnoreCase("doc")
                || fileType.equalsIgnoreCase("docx"));
    }

    @Override
    public ParsedDocument parse(InputStream inputStream, String filename) {
        throw new UnsupportedOperationException("Word 解析尚未接入");
    }
}
