package com.springai.polymodalaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
// 防止开发调试出现StoreVector冲突的情况, 当用PgVectorVectorsStoreConfig时要注释@Configuration
 @Configuration
public class AppVectorStoreConfig {

    @Resource
    private AppDocumentLoader appDocumentLoader;

    @Resource
    private MyKeywordEnricher myKeywordEnricher;

    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;

    @Bean
    VectorStore GameAppVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
//        simpleVectorStore.accept(appDocumentLoader.loadMarkdown());

        List<Document> documents = appDocumentLoader.loadMarkdown();
        // 文本分割
        documents = myTokenTextSplitter.splitCustomized(documents);
        // 元数据增强
        documents = myKeywordEnricher.enrichDocumentsByKeywords(documents);
        // 写入VectorStore
        simpleVectorStore.add(documents);

        return simpleVectorStore;
    }

}
