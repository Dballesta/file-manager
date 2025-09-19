package org.dballesteros.filemanager.domain.model;


import lombok.Getter;

@Getter
public enum SortDirection {

    ASC("ASC"),

    DESC("DESC");

    private final String value;

    SortDirection(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static SortDirection fromValue(String value) {
        for (SortDirection b : SortDirection.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
