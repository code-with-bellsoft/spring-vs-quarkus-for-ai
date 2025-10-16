package dev.rabauer.spring_vs_quarkus.quarkus;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;

@RegisterAiService
public interface TranslationService {

    @UserMessage("You are a translator: translate the following text to {targetLanguage}: {text}")
    Multi<String> translate(String targetLanguage, String text);

}
