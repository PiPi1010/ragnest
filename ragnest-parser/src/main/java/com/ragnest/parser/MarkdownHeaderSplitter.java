package com.ragnest.parser;

import org.springframework.ai.transformer.splitter.TextSplitter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Markdown 标题分块器。
 *
 * <p>按 Markdown 的标题层级（# / ## / ### ...）将文档切分为逻辑段落，
 * 相比纯 token 切分更能保留语义结构，适合技术文档类知识库。</p>
 *
 * <p>扩展自 Spring AI 的 {@link TextSplitter} 抽象类。</p>
 */
public class MarkdownHeaderSplitter extends TextSplitter {

    /** 匹配 markdown 标题行（# 开头） */
    private static final Pattern HEADER_PATTERN = Pattern.compile("(?m)^(#{1,6})\\s+(.+)$");

    /** 最小标题层级（1 表示从一级标题开始切分） */
    private final int minHeaderLevel;

    public MarkdownHeaderSplitter() {
        this(1);
    }

    public MarkdownHeaderSplitter(int minHeaderLevel) {
        this.minHeaderLevel = minHeaderLevel;
    }

    @Override
    protected List<String> splitText(String text) {
        List<String> chunks = new ArrayList<>();
        Matcher matcher = HEADER_PATTERN.matcher(text);

        int lastIndex = 0;
        String currentHeader = "";

        while (matcher.find()) {
            int level = matcher.group(1).length();
            String header = matcher.group(2).trim();

            // 只按 >= minHeaderLevel 的标题切分（更深的标题也切）
            if (level >= minHeaderLevel) {
                // 记录上一个 chunk（标题前的正文）
                if (lastIndex < matcher.start()) {
                    addChunk(chunks, text, lastIndex, matcher.start(), currentHeader);
                }
                currentHeader = header;
                lastIndex = matcher.start();
            }
        }

        // 最后一个 chunk
        if (lastIndex < text.length()) {
            addChunk(chunks, text, lastIndex, text.length(), currentHeader);
        }

        // 若无标题，整体作为一个 chunk
        if (chunks.isEmpty() && !text.isBlank()) {
            chunks.add(text.trim());
        }

        return chunks;
    }

    private void addChunk(List<String> chunks, String text, int start, int end, String header) {
        String content = text.substring(start, end).trim();
        if (!content.isEmpty()) {
            chunks.add(content);
        }
    }
}
