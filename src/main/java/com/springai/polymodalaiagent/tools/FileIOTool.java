package com.springai.polymodalaiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.springai.polymodalaiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class FileIOTool {
    public String FILE_DIR = FileConstant.FILE_SAVE_DIR+"/files";

    @Tool(description = "read a file")
    public String readFile(@ToolParam(description = "the name of file to read") String fileName) {
        String filePath = FILE_DIR+"/"+fileName;
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "Error reading file "+e.getMessage();
        }
    }

    @Tool(description = "write content to a file")
    public String writeFile(@ToolParam(description = "the name of file to write") String fileName,
                            @ToolParam(description = "the content to be wrote in the file") String content) {
        String filePath = FILE_DIR+"/"+fileName;
        try {
            FileUtil.touch(filePath);
            FileUtil.writeUtf8String(content, filePath);
            return "File written successfully to "+filePath;
        } catch (Exception e) {
            return "Error writing file "+e.getMessage();
        }
    }
}
