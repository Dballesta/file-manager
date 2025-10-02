package org.dballesteros.filemanager.infrastructure.client;

import openapi.client.api.FileApi;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.domain.model.exception.ExceptionDetail;
import org.dballesteros.filemanager.infrastructure.config.FilebinConfigProps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilebinUploadAdapterTest {

    private static final String BIN_ID = "bin123";
    private static final String BASE_URL = "https://filebin.net";
    private static final String CID = "client-1";
    @Mock
    FileApi fileApi;
    @Mock
    FilebinConfigProps props;
    @InjectMocks
    FilebinUploadAdapter adapter;

    @BeforeEach
    void setUp() {
        when(this.props.getBinId()).thenReturn(BIN_ID);
        when(this.props.getBaseUrl()).thenReturn(BASE_URL);
        when(this.props.getCid()).thenReturn(CID);
    }

    @Test
    void upload_success_setsUrlAndEnd_andEncodesBase64() {
        final AssetDto asset = new AssetDto();
        asset.setFilename("pic.png");
        asset.setEncodedFile(new byte[]{1, 2, 3, 4});
        final Instant before = Instant.now();

        when(this.fileApi.binFilenamePost(eq(BIN_ID), eq("pic.png"), eq(CID), anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(this.adapter.uploadFile(asset))
                .assertNext(result -> {
                    assertThat(result.getUrl()).isEqualTo(BASE_URL + "/" + BIN_ID + "/pic.png");
                    assertThat(result.getUploadDateEnd()).isNotNull();
                    assertThat(!result.getUploadDateEnd().isBefore(before)).isTrue();
                })
                .verifyComplete();

        final ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(this.fileApi, times(1))
                .binFilenamePost(eq(BIN_ID), eq("pic.png"), eq(CID), bodyCaptor.capture());

        final String expectedB64 = Base64.getEncoder().encodeToString(new byte[]{1, 2, 3, 4});
        assertThat(bodyCaptor.getValue()).isEqualTo(expectedB64);

        verifyNoMoreInteractions(this.fileApi);
    }

    @Test
    void upload_error_mapsToExceptionDetail() {
        final AssetDto asset = new AssetDto();
        asset.setFilename("doc.pdf");
        asset.setEncodedFile(new byte[]{9, 9, 9});

        when(this.fileApi.binFilenamePost(eq(BIN_ID), eq("doc.pdf"), eq(CID), anyString()))
                .thenReturn(Mono.error(new RuntimeException("boom")));

        StepVerifier.create(this.adapter.uploadFile(asset))
                .expectErrorSatisfies(err -> {
                    assertThat(err).isInstanceOf(ExceptionDetail.class);
                    final ExceptionDetail ex = (ExceptionDetail) err;
                    assertThat(ex.getCode()).isEqualTo("ERROR_UPLOADING");
                    assertThat(ex.getMessage()).isEqualTo("Error Uploading to filebin");
                    assertThat(ex.getCause()).isNotNull();
                    assertThat(ex.getCause().getMessage()).isEqualTo("boom");
                })
                .verify();

        verify(this.fileApi).binFilenamePost(eq(BIN_ID), eq("doc.pdf"), eq(CID), anyString());
        verifyNoMoreInteractions(this.fileApi);
    }

    @Test
    void upload_retries_then_succeeds() {
        final AssetDto asset = new AssetDto();
        asset.setFilename("retry.txt");
        asset.setEncodedFile(new byte[]{7, 7, 7});

        final AtomicInteger attempts = new AtomicInteger(0);

        when(this.fileApi.binFilenamePost(eq(BIN_ID), eq("retry.txt"), eq(CID), anyString()))
                .thenAnswer(inv -> Mono.defer(() -> {
                    if (attempts.getAndIncrement() == 0) {
                        return Mono.error(new RuntimeException("transient"));
                    }
                    return Mono.empty();
                }));

        StepVerifier.create(this.adapter.uploadFile(asset))
                .expectNextMatches(a -> (BASE_URL + "/" + BIN_ID + "/retry.txt").equals(a.getUrl()))
                .verifyComplete();

        verify(this.fileApi, times(1)).binFilenamePost(eq(BIN_ID), eq("retry.txt"), eq(CID), anyString());
        assertThat(attempts.get()).isGreaterThanOrEqualTo(2);
        verifyNoMoreInteractions(this.fileApi);
    }
}
