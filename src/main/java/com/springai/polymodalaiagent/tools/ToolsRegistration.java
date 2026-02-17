package com.springai.polymodalaiagent.tools;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolsRegistration {

    @Bean
    public ToolCallback[] allTools() {
        FileIOTool fileIOTool = new FileIOTool();
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        TerminateTool terminateTool = new TerminateTool();

        ToolCallback[] customerTools = ToolCallbacks.from(
                fileIOTool,
                pdfGenerationTool,
                resourceDownloadTool,
                webScrapingTool,
                terminateTool
        );
        return customerTools;
    }
}
