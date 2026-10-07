package com.ragnest.parser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 文档解析结果。
 *
 * <p>包含解析出的纯文本内容与元数据。</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParsedDocument {

    /** 解析出的纯文本内容 */
    private String content;

    /** 元数据（如文件名、标题、页码等） */
    @Builder.Default
    private Map<String, Object> metadata = new LinkedHashMap<>();

    /** 添加元数据 */
    public ParsedDocument addMetadata(String key, Object value) {
        this.metadata.put(key, value);
        return this;
    }
}
