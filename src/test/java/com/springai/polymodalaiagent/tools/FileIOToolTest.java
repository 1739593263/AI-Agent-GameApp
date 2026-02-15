package com.springai.polymodalaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileIOToolTest {

    @Test
    void readFile() {
        FileIOTool fileIOTool = new FileIOTool();
        String file = "test.txt";
        String res = fileIOTool.readFile(file);
        System.out.println(res);
        Assertions.assertNotNull(res);
    }

    @Test
    void writeFile() {
        FileIOTool fileIOTool = new FileIOTool();
        String file = "test.txt";
        String content = "LoVE as Much AS WE CAN";
        String res = fileIOTool.writeFile(file, content);
        Assertions.assertNotNull(res);
    }
}