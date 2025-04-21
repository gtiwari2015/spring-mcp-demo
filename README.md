# spring-mcp-demo

This Job search application is developed using Spring AI and will demonstrate below AI capabilities
1. Question Answer RAG
2. Agentic AI
2. MCP Client and server interaction
3. Uses AWS bedrcok LLM Models and openAI model

MCP Demo application with Java spring ai, aws bedrock
Job Search application is multi agentic which uses AWS bedrock for accessing models and uses direct Open AI models.
It aces MCP client and connect to another Spring AI based MCP Server ( which in my case is employer system) for scheduling first round.


API used for our job search engine application
http://localhost:8080/gaurav/inquire?question=%22any%20position%20for%20swifterst%20developer?%22

http://localhost:8080/gaurav/inquire?question=%22please%20tell%20me%20job%20description%20of%20swifterst%20developer?%22

http://localhost:8080/gaurav/inquire?question=%22please%20submit%20my%20job%20application%20for%20swifterst%20developer%20and%20%20shedule%20screening%20round%20r?%22

For MCP Use case

http://localhost:8080/gaurav/inquire?question=%22scehdule%20first%20round%20of%20interview%22

MCP server url
http://localhost:8081

I have used mysql database for creating database 

create database myjobsearch;

GRANT ALL PRIVILEGES ON myjobsearch.* TO 'root'@'localhost';

