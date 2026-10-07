package com.ragnest.parser.pipeline;

import com.ragnest.parser.DocumentParser;
import com.ragnest.parser.ParsedDocument;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 文档解析流水线。
 *
 * <p>串联"解析 → 分块"两个阶段，产出可供向量化与存储的 Spring AI Document 列表。</p>
 */
public class DocumentIngestionPipeline {

    private final List<DocumentParser> parsers;
    private final TextSplitter textSplitter;

    public DocumentIngestionPipeline(List<DocumentParser> parsers, TextSplitter textSplitter) {
        this.parsers = parsers;
        this.textSplitter = textSplitter;
    }

    /**
     * 执行解析流水线。
     *
     * @param inputStream 文件流
     * @param filename    文件名
     * @param fileType    文件类型
     * @return 分块后的 Document 列表
     */
    public List<Document> ingest(InputStream inputStream, String filename, String fileType) {
        // 1. 找到匹配的解析器
        DocumentParser parser = parsers.stream()
                .filter(p -> p.supports(fileType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的文件类型: " + fileType));

        // 2. 解析
        ParsedDocument parsed = parser.parse(inputStream, filename);

        // 3. 分块
        Document sourceDoc = Document.builder()
                .text(parsed.getContent())
                .metadata(parsed.getMetadata())
                .build();

        return new ArrayList<>(textSplitter.split(sourceDoc));
    }
}
