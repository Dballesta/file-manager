package org.dballesteros.filemanager.domain.model.search;


import lombok.Getter;

@Getter
public enum SortDirectionDomain {

    ASC("ASC"),

    DESC("DESC");

    private final String value;

    SortDirectionDomain(final String value) {
        this.value = value;
    }

    public static SortDirectionDomain fromValue(final String value) {
        for (final SortDirectionDomain b : SortDirectionDomain.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
