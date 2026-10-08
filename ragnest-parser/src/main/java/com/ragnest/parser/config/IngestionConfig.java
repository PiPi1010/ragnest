package com.ragnest.parser.config;

import com.ragnest.parser.DocumentParser;
import com.ragnest.parser.ExcelDocumentParser;
import com.ragnest.parser.MarkdownDocumentParser;
import com.ragnest.parser.MarkdownHeaderSplitter;
import com.ragnest.parser.PdfDocumentParser;
import com.ragnest.parser.PlainTextDocumentParser;
import com.ragnest.parser.WordDocumentParser;
import com.ragnest.parser.pipeline.DocumentIngestionPipeline;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 文档解析流水线装配。
 *
 * <p>注册各格式解析器、分块器与解析流水线。</p>
 */
@Configuration
public class IngestionConfig {

    @Bean
    public List<DocumentParser> documentParsers() {
        return List.of(
                new MarkdownDocumentParser(),
                new PlainTextDocumentParser(),
                new PdfDocumentParser(),
                new WordDocumentParser(),
                new ExcelDocumentParser()
        );
    }

    @Bean
    public TextSplitter textSplitter() {
        return new MarkdownHeaderSplitter();
    }

    @Bean
    public DocumentIngestionPipeline documentIngestionPipeline(List<DocumentParser> parsers,
                                                               TextSplitter textSplitter) {
        return new DocumentIngestionPipeline(parsers, textSplitter);
    }
}
