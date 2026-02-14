package com.springai.polymodalaiagent;

import org.springframework.ai.vectorstore.pgvector.autoconfigure.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {
    PgVectorStoreAutoConfiguration.class
})
public class PolyModalAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(PolyModalAiAgentApplication.class, args);
    }

}
