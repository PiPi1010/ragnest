package com.ragnest.parser;

import java.io.InputStream;

/**
 * PDF 文档解析器（占位实现）。
 *
 * <p>PDF 解析需引入 PDFBox 等依赖。Spring AI 2.0 对 PDF 文档读取做了精简，
 * 接入时需：</p>
 * <ol>
 *   <li>引入 {@code spring-ai-pdf-document-reader} 或直接使用 Apache PDFBox</li>
 *   <li>处理扫描件（需 OCR）与文本型 PDF 的差异</li>
 * </ol>
 *
 * <p>当前为占位，待明确 PDF 解析方案后实现。</p>
 */
public class PdfDocumentParser implements DocumentParser {

    @Override
    public boolean supports(String fileType) {
        return fileType != null && (fileType.equalsIgnoreCase("pdf")
                || fileType.equalsIgnoreCase("application/pdf"));
    }

    @Override
    public ParsedDocument parse(InputStream inputStream, String filename) {
        throw new UnsupportedOperationException("PDF 解析尚未接入");
    }
}
