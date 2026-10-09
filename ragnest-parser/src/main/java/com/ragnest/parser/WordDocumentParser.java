package com.ragnest.parser;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Word 文档解析器。
 *
 * <p>基于 Apache POI 解析 {@code .docx} 文档，逐段提取文本。
 * 旧格式 {@code .doc} 需 poi-scratchpad，当前暂不支持（抛出明确提示）。</p>
 */
public class WordDocumentParser implements DocumentParser {

    @Override
    public boolean supports(String fileType) {
        return fileType != null && (fileType.equalsIgnoreCase("doc")
                || fileType.equalsIgnoreCase("docx"));
    }

    @Override
    public ParsedDocument parse(InputStream inputStream, String filename) {
        String content;
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            content = paragraphs.stream()
                    .map(XWPFParagraph::getText)
                    .filter(text -> text != null && !text.isBlank())
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new IllegalStateException("解析 Word 文档失败（仅支持 .docx）: " + e.getMessage(), e);
        }

        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("filename", filename);
        metadata.put("fileType", "docx");

        return ParsedDocument.builder()
                .content(content)
                .metadata(metadata)
                .build();
    }
}
