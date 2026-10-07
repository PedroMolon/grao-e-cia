package com.pedromolon.catalog_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record GenerateDescriptionRequest(
        @NotBlank(message = "Name is required")
        String name,
        @NotEmpty(message = "Flavor notes are required")
        List<String> flavorNotes
) {
}
