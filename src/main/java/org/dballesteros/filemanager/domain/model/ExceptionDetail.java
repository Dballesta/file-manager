package org.dballesteros.filemanager.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class ExceptionDetail {
    private String code;
    private String message;
}
