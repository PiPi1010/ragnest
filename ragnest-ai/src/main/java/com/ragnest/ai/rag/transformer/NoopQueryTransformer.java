package com.ragnest.ai.rag.transformer;

import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;

/**
 * 恒等 Query 转换器。
 *
 * <p>不做任何改写，直接返回原查询。Spring AI 2.0 无内置 Noop 实现，
 * 当不需要 LLM 改写查询时使用。</p>
 */
public class NoopQueryTransformer implements QueryTransformer {

    @Override
    public Query transform(Query query) {
        return query;
    }
}
