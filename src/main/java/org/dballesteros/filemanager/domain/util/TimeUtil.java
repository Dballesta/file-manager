package org.dballesteros.filemanager.domain.util;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.util.Optional;

@UtilityClass
public class TimeUtil {
    public static Instant stringToInstant(String dateString) {
        return Optional.ofNullable(dateString)
                .map(Instant::parse)
                .orElse(null);
    }

    public static String instantToString(Instant instant) {
        return Optional.ofNullable(instant)
                .map(Instant::toString)
                .orElse(null);
    }
}
