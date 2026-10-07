package com.ragnest.parser;

import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.document.Document;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Markdown 文档解析器。
 *
 * <p>基于 Spring AI 的 {@link MarkdownDocumentReader} 实现。</p>
 */
public class MarkdownDocumentParser implements DocumentParser {

    @Override
    public boolean supports(String fileType) {
        return fileType != null && (fileType.equalsIgnoreCase("md")
                || fileType.equalsIgnoreCase("markdown")
                || fileType.equalsIgnoreCase("text/markdown"));
    }

    @Override
    public ParsedDocument parse(InputStream inputStream, String filename) {
        // Markdown 本质是纯文本，直接读取
        String content = readAll(inputStream);

        // 复用 Spring AI 的 MarkdownDocumentReader 做结构化解析
        Document doc = new MarkdownDocumentReader(content).read().stream()
                .findFirst()
                .orElse(Document.builder().text(content).build());

        return ParsedDocument.builder()
                .content(doc.getText())
                .metadata(doc.getMetadata())
                .build()
                .addMetadata("filename", filename)
                .addMetadata("fileType", "markdown");
    }

    private String readAll(InputStream inputStream) {
        try (InputStream is = inputStream) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("读取 Markdown 文档失败: " + e.getMessage(), e);
        }
    }
}
