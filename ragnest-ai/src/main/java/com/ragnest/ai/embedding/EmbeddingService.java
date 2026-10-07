package com.ragnest.ai.embedding;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.document.Document;

import java.util.List;

/**
 * 向量化服务。
 *
 * <p>封装 {@link EmbeddingModel}，提供面向业务场景的嵌入能力。</p>
 */
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;

    public EmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    /** 将文本转为向量 */
    public float[] embed(String text) {
        return embeddingModel.embed(text);
    }

    /** 批量将文本转为向量 */
    public List<float[]> embed(List<String> texts) {
        return embeddingModel.embed(texts);
    }

    /** 将文档转为向量（并写回 document 的 embedding 元数据） */
    public void embedDocument(Document document) {
        embeddingModel.embed(document);
    }

    /** 获取向量维度 */
    public int dimensions() {
        return embeddingModel.dimensions();
    }
}
