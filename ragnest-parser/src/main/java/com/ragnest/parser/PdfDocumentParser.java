package com.ragnest.parser;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * PDF 文档解析器。
 *
 * <p>基于 Spring AI 的 {@link PagePdfDocumentReader}（底层 Apache PDFBox）按页提取文本。</p>
 *
 * <p>注意：仅支持文本型 PDF。扫描件（图片型 PDF）需额外接入 OCR，
 * 当前会得到空文本，后续可扩展。</p>
 */
public class PdfDocumentParser implements DocumentParser {

    @Override
    public boolean supports(String fileType) {
        return fileType != null && (fileType.equalsIgnoreCase("pdf")
                || fileType.equalsIgnoreCase("application/pdf"));
    }

    @Override
    public ParsedDocument parse(InputStream inputStream, String filename) {
        List<Document> pages;
        try {
            PagePdfDocumentReader reader = new PagePdfDocumentReader(new InputStreamResource(inputStream));
            pages = reader.get();
        } catch (Exception e) {
            throw new IllegalStateException("解析 PDF 文档失败: " + e.getMessage(), e);
        }

        String content = pages.stream()
                .map(Document::getText)
                .filter(text -> text != null && !text.isBlank())
                .collect(Collectors.joining("\n\n"));

        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("filename", filename);
        metadata.put("fileType", "pdf");
        metadata.put("pageCount", pages.size());

        return ParsedDocument.builder()
                .content(content)
                .metadata(metadata)
                .build();
    }
}
