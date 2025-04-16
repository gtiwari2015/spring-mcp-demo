package com.mcp.mcpdemo;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JobApplicationProcessorTools {

    @Tool(description = "Schedule screening round with the candidate")
    String scheduleScreeningRound(@ToolParam(description = "the id of the job") int jobId, @ToolParam(description = "Description of found job") String jobDescirption){
        var screeningAppointment= Instant.now().plus(3, ChronoUnit.DAYS);
        System.out.println(" screening appointment confirmed for"+screeningAppointment);
        return screeningAppointment.toString();
    }
    @Tool(description = "submit job application ")
    void submitJobApplication(@ToolParam(description = "the id of the job") int jobId, @ToolParam(description = "Description of found job") String jobDescirption){
        System.out.println(" job application submitted");
    }
}
