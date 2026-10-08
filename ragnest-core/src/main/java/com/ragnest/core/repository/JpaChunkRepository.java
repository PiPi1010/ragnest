package com.ragnest.core.repository;

import com.ragnest.core.model.Chunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文档切片 JPA 仓储。
 */
@Repository
public interface JpaChunkRepository extends JpaRepository<Chunk, Long> {

    List<Chunk> findByDocumentId(Long documentId);

    List<Chunk> findByDocumentIdOrderBySequence(Long documentId);

    void deleteByDocumentId(Long documentId);
}
