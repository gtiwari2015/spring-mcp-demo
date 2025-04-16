package com.mcp.mcpdemo;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.model.Model;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@ResponseBody
public class JobSearchController {

    private final ChatClient singularity;
    private final QuestionAnswerAdvisor questionAnswerAdvisor;

    @Autowired
    McpSyncClient mcpSyncClient;
    @Autowired
    JobApplicationProcessorTools jobApplicationProcessorTools;
    private final Map<String, PromptChatMemoryAdvisor> advisorMap = new ConcurrentHashMap<>();
    public JobSearchController(ChatClient singularity, VectorStore vectorStore,JobApplicationProcessorTools jobApplicationProcessorTools) {
        this.singularity = singularity;
        this.questionAnswerAdvisor =new QuestionAnswerAdvisor(vectorStore);
        this.jobApplicationProcessorTools= jobApplicationProcessorTools;
    }

    @GetMapping("/{user}/inquire")
    String inquireJobs(@PathVariable("user") String user,
                       @RequestParam String question){
     var advisor=   this.advisorMap.computeIfAbsent(user,k-> PromptChatMemoryAdvisor.builder(new InMemoryChatMemory()).build());
        return this.singularity
                .prompt()
                .user(question)
                .tools(new SyncMcpToolCallbackProvider(mcpSyncClient))
                .advisors(advisor,this.questionAnswerAdvisor)
                .call()
                .content();
    }
}
