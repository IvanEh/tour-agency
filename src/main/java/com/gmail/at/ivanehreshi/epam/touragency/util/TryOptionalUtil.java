package com.gmail.at.ivanehreshi.epam.touragency.util;

import java.util.Optional;

public class TryOptionalUtil {
    public static <R> Optional<R> of(FailableSupplier<R> f) {
        try {
            return Optional.ofNullable(f.get());
        } catch (Throwable e) {
            return Optional.empty();
        }
    }

    public interface FailableSupplier<R> {
        R get() throws Throwable;
    }
}
