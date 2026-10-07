package com.pedromolon.catalog_service.service;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

import java.util.List;

@AiService
public interface ProductDescriptionAiService {

    @UserMessage("""
            Você é um especialista em marketing gastronômico e sommelier.
            Gere uma descrição atraente, sucinta e vendedora (máximo 3 frases) para um produto com as seguintes características:
            
            Nome do produto: {{name}}
            Notas de sabor/características: {{flavorNotes}}
            
            Response APENAS com o texto da descrição, sem introduções ou explicações adicionais.
            """)
    String generateDescription(@V("name") String name, @V("flavorNotes")List<String> flavorNotes);

}
