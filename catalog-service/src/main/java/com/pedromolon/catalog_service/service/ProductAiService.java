package com.pedromolon.catalog_service.service;

import com.pedromolon.catalog_service.dto.request.GenerateDescriptionRequest;
import com.pedromolon.catalog_service.dto.response.GenerateDescriptionResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductAiService {

    private final ProductDescriptionAiService productDescriptionAiService;

    public ProductAiService(ProductDescriptionAiService productDescriptionAiService) {
        this.productDescriptionAiService = productDescriptionAiService;
    }

    public GenerateDescriptionResponse generateDescription(GenerateDescriptionRequest request) {
        String description = productDescriptionAiService.generateDescription(
                request.name(),
                request.flavorNotes()
        );

        return new GenerateDescriptionResponse(description);
    }

}
