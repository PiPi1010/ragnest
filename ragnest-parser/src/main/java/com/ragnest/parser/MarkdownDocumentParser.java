package com.ragnest.parser;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Markdown 文档解析器。
 *
 * <p>Markdown 本质是纯文本，解析阶段直接读取文本内容。
 * 结构化切分（按标题分块）由后续的 {@link MarkdownHeaderSplitter} 负责。</p>
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
        String content;
        try (InputStream is = inputStream) {
            content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("读取 Markdown 文档失败: " + e.getMessage(), e);
        }

        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("filename", filename);
        metadata.put("fileType", "markdown");

        return ParsedDocument.builder()
                .content(content)
                .metadata(metadata)
                .build();
    }
}
