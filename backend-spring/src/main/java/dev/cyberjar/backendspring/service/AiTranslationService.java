package dev.cyberjar.backendspring.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AiTranslationService {

    private final ChatClient chatClient;

    private final String TRANSLATION_PROMPT = """
        You are a professional translator.
        Task: translate the USER text into %s.
        Rules:
        - Output ONLY the translation, no preface, no quotes, no language tags.
        - Do NOT answer the content; always translate it as-is.
        - Keep names/numbers/code as is unless grammar requires adaptation.
        - If the text is already in the target language, return it unchanged.
        """;


    public AiTranslationService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public Flux<String> translate(String targetLanguage, String text) {

        String system = TRANSLATION_PROMPT.formatted(targetLanguage);

        return chatClient
                .prompt()
                .system(system)
                .user(text)
                .stream()
                .content();

    }

}
