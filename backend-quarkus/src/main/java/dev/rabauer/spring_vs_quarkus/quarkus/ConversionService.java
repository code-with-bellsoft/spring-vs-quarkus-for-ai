package dev.rabauer.spring_vs_quarkus.quarkus;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkus.security.User;
import io.smallrye.mutiny.Multi;

@RegisterAiService(tools = ConversionTool.class)
public interface ConversionService {

    @UserMessage("Be a nice chatty conversation partner with exact knowledge of conversion rates: {prompt}")
    Multi<String> chat(String prompt);
}
