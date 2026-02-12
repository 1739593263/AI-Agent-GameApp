package com.springai.polymodalaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppVectorStoreConfig {

    @Resource
    private AppDocumentLoader appDocumentLoader;

    @Bean
    VectorStore GameAppVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        simpleVectorStore.accept(appDocumentLoader.loadMarkdown());
        return simpleVectorStore;
    }

}
