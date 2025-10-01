package org.dballesteros.filemanager.domain.model.search;


import lombok.Getter;

@Getter
public enum SortDirection {

    ASC("ASC"),

    DESC("DESC");

    private final String value;

    SortDirection(final String value) {
        this.value = value;
    }

    public static SortDirection fromValue(final String value) {
        for (final SortDirection b : SortDirection.values()) {
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
