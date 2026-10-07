package com.ragnest.core.repository;

import com.ragnest.core.model.Chunk;

import java.util.List;
import java.util.Optional;

/**
 * 文档切片仓储接口。
 */
public interface ChunkRepository {

    Chunk save(Chunk chunk);

    Optional<Chunk> findById(Long id);

    List<Chunk> findByDocumentId(Long documentId);

    List<Chunk> findByDocumentIdOrderBySequence(Long documentId);

    void deleteByDocumentId(Long documentId);
}
