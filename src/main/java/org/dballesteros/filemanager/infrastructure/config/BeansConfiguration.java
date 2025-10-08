package org.dballesteros.filemanager.infrastructure.config;

import lombok.AllArgsConstructor;
import openapi.client.api.FileApi;
import openapi.client.core.ApiClient;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@AllArgsConstructor
@Configuration
@ConfigurationPropertiesScan
@EnableAsync
public class BeansConfiguration {
    FilebinConfigProps filebinConfigProps;

    @Bean(name = "uploadExecutor")
    public static Executor uploadExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public FileApi fileApi() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(this.filebinConfigProps.getBaseUrl());
        return new FileApi(apiClient);
    }
}
