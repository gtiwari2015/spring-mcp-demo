package com.mcp.mcpdemo;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

@SpringBootApplication
public class McpDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpDemoApplication.class, args);
	}

    @Bean
    McpSyncClient mcpSyncClient(){
        var mcp = McpClient.sync(new HttpClientSseClientTransport("http://localhost:8081")).build();
        mcp.initialize();
        return mcp;
    }
    @Bean
    public EmbeddingModel embeddingModel() {
        // Can be any other EmbeddingModel implementation.
        return new OpenAiEmbeddingModel(new OpenAiApi.Builder().apiKey((System.getenv("SPRING_AI_OPENAI_API_KEY"))).build());
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder,McpSyncClient mcpSyncClient,JobPostingsRepository jobPostingsRepository, VectorStore vectorStore){
        jobPostingsRepository.findAll().forEach(jobs->{
            var jobPosting= new Document("id: %s,jobTitle: %s, jobdetails: %s".formatted(jobs.id(),jobs.job_title(),jobs.Job_description()));
            vectorStore.add(List.of(jobPosting));
        });

        var system = """
                You are an AI powered assistant to help people find a job from job posting engine names myjobsearch.
                MyJobSearch is personalised job search platform,which stores job postings of diverse fields and streams.
                Information about job will be presented below.If there is no matching job posting , then return a polite reponse 
                that no matching job found.
                """;
        return builder
                .defaultSystem(system)
                .build();
    }
}

record Job_postings (@Id int id, String job_title , String Job_description){}

interface JobPostingsRepository extends ListCrudRepository<Job_postings,Long> {}