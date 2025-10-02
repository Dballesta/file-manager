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
import org.springframework.core.io.ByteArrayResource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
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
    void uploadShouldReturnOkAndTransformByteToByteArrayResource() {
        final AssetDto asset = new AssetDto();
        asset.setFilename("pic.png");
        asset.setEncodedFile(new byte[]{1, 2, 3, 4});
        final Instant before = Instant.now();

        when(this.fileApi.binFilenamePost(eq(BIN_ID), eq("pic.png"), eq(CID), any(ByteArrayResource.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(this.adapter.uploadFile(asset))
                .assertNext(result -> {
                    assertThat(result.getUrl()).isEqualTo(BASE_URL + "/" + BIN_ID + "/pic.png");
                    assertThat(result.getUploadDateEnd()).isNotNull();
                    assertThat(!result.getUploadDateEnd().isBefore(before)).isTrue();
                })
                .verifyComplete();

        final ArgumentCaptor<ByteArrayResource> bodyCaptor = ArgumentCaptor.forClass(ByteArrayResource.class);
        verify(this.fileApi, times(1))
                .binFilenamePost(eq(BIN_ID), eq("pic.png"), eq(CID), bodyCaptor.capture());

        assertThat(bodyCaptor.getValue()).isEqualTo(new ByteArrayResource(asset.getEncodedFile()));

        verifyNoMoreInteractions(this.fileApi);
    }

    @Test
    void testUploadShouldMapErrorInsteadOfRetriesError() {
        final AssetDto asset = new AssetDto();
        asset.setFilename("doc.pdf");
        asset.setEncodedFile(new byte[]{9, 9, 9});

        when(this.fileApi.binFilenamePost(eq(BIN_ID), eq("doc.pdf"), eq(CID), any(ByteArrayResource.class)))
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

        verify(this.fileApi).binFilenamePost(eq(BIN_ID), eq("doc.pdf"), eq(CID), any(ByteArrayResource.class));
        verifyNoMoreInteractions(this.fileApi);
    }

    @Test
    void testUploadShouldRecoverAfterNRetriesAndThenSuccess() {
        final AssetDto asset = new AssetDto();
        asset.setFilename("retry.txt");
        asset.setEncodedFile(new byte[]{7, 7, 7});

        final AtomicInteger attempts = new AtomicInteger(0);

        when(this.fileApi.binFilenamePost(eq(BIN_ID), eq("retry.txt"), eq(CID), any(ByteArrayResource.class)))
                .thenAnswer(inv -> Mono.defer(() -> {
                    if (attempts.getAndIncrement() == 0) {
                        return Mono.error(new RuntimeException("transient"));
                    }
                    return Mono.empty();
                }));

        StepVerifier.create(this.adapter.uploadFile(asset))
                .expectNextMatches(a -> (BASE_URL + "/" + BIN_ID + "/retry.txt").equals(a.getUrl()))
                .verifyComplete();

        verify(this.fileApi, times(1)).binFilenamePost(eq(BIN_ID), eq("retry.txt"), eq(CID), any(ByteArrayResource.class));
        assertThat(attempts.get()).isGreaterThanOrEqualTo(2);
        verifyNoMoreInteractions(this.fileApi);
    }
}
