package org.dballesteros.filemanager.infrastructure.config;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@AllArgsConstructor
@Getter
@ConfigurationProperties(prefix = "app.filebin")
public class FilebinConfigProps {
    @NotBlank
    private final String baseUrl;

    @NotBlank
    private final String binId;

    @NotBlank
    private final String cid;
}
