package dev.demoai.frontend;

import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.Map;

@Route("")
public class MainView extends VerticalLayout {

    @Value("${backend.url}")
    private String backendUrl;

    private final WebClient webClient;

    private final TextArea input = new TextArea("Question");
    private final TextArea translateInput = new TextArea("Text to translate");
    private final TextArea answer = new TextArea("Answer");

    private final ComboBox<String> target = new ComboBox<>("Target language");

    private final Button ask = new Button("Ask");
    private final Button translate = new Button("Translate");

    private Disposable activeStream;

    public MainView(@Value("${backend.url}") String backendUrl, WebClient.Builder builder) {
        this.backendUrl = backendUrl;
        this.webClient = builder.baseUrl(backendUrl).build();
        createViewElements();
    }

    private void createViewElements() {

        setWidthFull();
        setPadding(true);
        setSpacing(true);

        input.setWidthFull();
        input.setPlaceholder("Type a question for Llama");

        translateInput.setWidthFull();
        translateInput.setPlaceholder("Type a text for translation for Llama");

        answer.setWidthFull();
        answer.setReadOnly(true);

        target.setItems(
                "English", "German", "Italian", "Spanish",
                "French", "Portuguese", "Dutch",
                "Chinese (Simplified)", "Chinese (Traditional)", "Japanese", "Korean", "Russian"
        );
        target.setPlaceholder("Pick target…");
        target.setClearButtonVisible(true);
        target.setWidth("260px");

        ask.addClickListener(_ -> startStream(
                onAsk(input.getValue())));

        translate.addClickListener(_ -> startStream(
                onTranslate(translateInput.getValue(), target.getValue())));

        HorizontalLayout actions = new HorizontalLayout(ask, translate);
        actions.setWidthFull();

        HorizontalLayout languages = new HorizontalLayout(target);
        languages.setWidthFull();

        add(input, translateInput, languages, actions, answer);

    }




    private Flux<String> onTranslate(String rawText, String languageTo) {
        String txt = trimOrNull(rawText);
        if (txt == null) {
            Notification.show("Enter some text to translate.");
        }
        String targetLanguage = trimOrNull(languageTo);
        if (targetLanguage == null) {
            Notification.show("Pick a target language.");
        }

        return webClient
                .post()
                .uri("/translate/" + targetLanguage)
                .contentType(MediaType.TEXT_PLAIN)
                .accept(MediaType.TEXT_PLAIN)
                .bodyValue(txt)
                .retrieve()
                .bodyToFlux(String.class);
    }

    private Flux<String> onAsk(String message) {
        String txt = trimOrNull(message);
        if (txt == null) {
            Notification.show("Please enter a question.");
        }

        return webClient.post()
                .uri("/chat")
                .contentType(MediaType.TEXT_PLAIN)
                .accept(MediaType.TEXT_PLAIN)
                .bodyValue(txt)
                .retrieve()
                .bodyToFlux(String.class);
    }


    private void startStream(Flux<String> stringFlux) {
        if (activeStream != null) {
            activeStream.dispose();
            activeStream = null;
        }

        answer.setValue("");
        setBusy(true);

        UI ui = UI.getCurrent();
        activeStream = stringFlux
                .doOnError(err -> ui.access(() ->
                        Notification.show("Error: " + err.getMessage(), 6000, Notification.Position.MIDDLE)))
                .doFinally(sig -> ui.access(() -> {
                    setBusy(false);
                    activeStream = null;
                }))
                .subscribe(chunk -> ui.access(() ->
                        answer.setValue(answer.getValue() + chunk)));
    }

    private void setBusy(boolean busy) {
        ask.setEnabled(!busy);
        translate.setEnabled(!busy);
    }

    private String trimOrNull(String value) {
        if (value == null) return null;
        String text = value.trim();
        return text.isEmpty() ? null : text;
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Avoid leaks: cancel any ongoing stream when view is detached
        if (activeStream != null) {
            activeStream.dispose();
            activeStream = null;
        }
        super.onDetach(detachEvent);
    }

}
