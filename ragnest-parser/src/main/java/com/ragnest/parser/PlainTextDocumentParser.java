package com.ragnest.parser;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 纯文本解析器（兜底实现）。
 *
 * <p>用于 txt 及无法识别格式的纯文本文件。</p>
 */
public class PlainTextDocumentParser implements DocumentParser {

    @Override
    public boolean supports(String fileType) {
        return fileType != null && (fileType.equalsIgnoreCase("txt")
                || fileType.equalsIgnoreCase("text/plain"));
    }

    @Override
    public ParsedDocument parse(InputStream inputStream, String filename) {
        String content;
        try (InputStream is = inputStream) {
            content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("读取文本文档失败: " + e.getMessage(), e);
        }

        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("filename", filename);
        metadata.put("fileType", "txt");

        return ParsedDocument.builder()
                .content(content)
                .metadata(metadata)
                .build();
    }
}
