package com.springai.polymodalaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PDFGenerationToolTest {

    @Test
    void generatePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "GameApp.pdf";
        String content = "Github https://github.com/1739593263/AI-Agent-GameApp";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}