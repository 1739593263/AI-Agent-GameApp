package com.springai.polymodalaiagent.rag;

import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Component;

/**
 * 自定义Rag检索增强advisor
 */
public class AppRagCustomAdvisorFactory {
    /**
     * 创建一个自定义增强Advisor（RetrievalAugmentationAdvisor）
     * @param vectorStore
     * @param category
     * @return
     */
    public static Advisor createAppCustomAdvisor(VectorStore vectorStore, String category) {
        // Filter Expression 过滤元数据内容
        Filter.Expression filter = new FilterExpressionBuilder().eq("category", category).build();

        // VectorStoreDocumentRetriever
        VectorStoreDocumentRetriever vectorStoreDocRetriever = VectorStoreDocumentRetriever.builder()
                .filterExpression(filter)
                .similarityThreshold(0.8)
                .topK(5)
                .vectorStore(vectorStore)
                .build();


        // 使模型允许接收empty上下文：当文档中找不到Query相关的上下文时返回默认答案
        PromptTemplate promptTemplate = new PromptTemplate("\n当前知识库找不到相关内容\n");
        ContextualQueryAugmenter contextualQueryArgumenter = ContextualQueryAugmenter.builder()
                .allowEmptyContext(true)
                .emptyContextPromptTemplate(promptTemplate) // 自定义空上下文回复
                .build();

        // 整合Advisor
        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
//                .queryTransformers(RewriteQueryTransformer.builder()
//                        .chatClientBuilder(ChatClient.builder(dashscopeChatModel).build().mutate())
//                        .build())
                .documentRetriever(vectorStoreDocRetriever)
                .queryAugmenter(contextualQueryArgumenter)
                .build();

        return retrievalAugmentationAdvisor;
    }
}
