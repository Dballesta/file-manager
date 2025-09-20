package org.dballesteros.filemanager.infrastructure.config;

import openapi.client.api.FileApi;
import openapi.client.core.ApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {

    @Bean
    public FileApi fileApi() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("https://filebin.net");
        return new FileApi(apiClient);
    }
}
