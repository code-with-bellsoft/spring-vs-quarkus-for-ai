package dev.rabauer.spring_vs_quarkus.quarkus;

import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class MainResource {

    @Inject
    ConversionService conversionService;

    @Inject
    TranslationService translationService;
    
    @POST
    @Path("/chat")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Multi<String> chat(String request)
    {
        return conversionService.chat(request);
    }

    @POST
    @Path("/translate/{targetLanguage}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Multi<String> translate(@PathParam("targetLanguage") String targetLanguage, String textToTranslate)
    {
        return translationService.translate(targetLanguage, textToTranslate);
    }
}
