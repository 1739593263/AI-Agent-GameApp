package com.springai.polymodalaiagent.rag;

import com.knuddels.jtokkit.api.EncodingType;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.BatchingStrategy;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;


import java.util.List;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

//@Configuration
public class PgVectorVectorsStoreConfig {

    @Resource
    private AppDocumentLoader documentLoader;

    @Bean
    public VectorStore PgVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel dashscopeEmbeddingModel) {
//        System.out.println("EmbeddingModel.dimension() = " + dashscopeEmbeddingModel.dimensions());
        VectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, dashscopeEmbeddingModel)
                .dimensions(1024)
                .distanceType(COSINE_DISTANCE)       // Optional: defaults to COSINE_DISTANCE
                .indexType(HNSW)                     // Optional: defaults to HNSW
//                .initializeSchema(true)              // Optional: defaults to false
                .schemaName("public")                // Optional: defaults to "public"
                .vectorTableName("vector_store")     // Optional: defaults to "vector_store"
                .maxDocumentBatchSize(10000)         // Optional: defaults to 10000
                .build();
        // 加载本地的文档并写入vectorstore
//        List<Document> documents = documentLoader.loadMarkdown();
//
//        int batchsize = 10;
//        for (int i=0; i<documents.size(); i+=batchsize) {
//            int end = Math.min(documents.size(), i+batchsize);
//            vectorStore.add(documents.subList(i,end));
//        }
        return vectorStore;
    }
}
