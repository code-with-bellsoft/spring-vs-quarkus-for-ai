package dev.cyberjar.backendspring.controller;

import dev.cyberjar.backendspring.service.AiChatService;
import dev.cyberjar.backendspring.service.AiTranslationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class AiResponseController {

    private final AiChatService aiChatService;
    private final AiTranslationService aiTranslationService;


    public AiResponseController(AiChatService aiChatService, AiTranslationService aiTranslationService) {
        this.aiChatService = aiChatService;
        this.aiTranslationService = aiTranslationService;
    }


    @PostMapping(
            value = "/chat",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public Flux<String> chat(@RequestBody String request) {
        return aiChatService.reply(request);
    }

    @PostMapping(value = "/translate/{targetLanguage}",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public Flux<String> translate(@PathVariable String targetLanguage,
                                  @RequestBody String text) {
        return aiTranslationService.translate(targetLanguage, text);
    }

}
