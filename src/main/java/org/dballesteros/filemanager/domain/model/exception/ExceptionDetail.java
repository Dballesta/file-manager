package org.dballesteros.filemanager.domain.model.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class ExceptionDetail extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1931367157172675039L;

    private final String code;

    public ExceptionDetail(final String code, final String message) {
        super(message);
        this.code = code;
    }

    public ExceptionDetail(final String code, final String message, final Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }
}
