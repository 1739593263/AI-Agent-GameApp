package com.springai.polymodalaiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppDocumentLoaderTest {

    @Resource
    AppDocumentLoader appDocumentLoader;

    @Test
    void testLoadMarkdown() {
        List<Document> documents = appDocumentLoader.loadMarkdown();
        System.out.println(documents.get(0).getText());
    }
}