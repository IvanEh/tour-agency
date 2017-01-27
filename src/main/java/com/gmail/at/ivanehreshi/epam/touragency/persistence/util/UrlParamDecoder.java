package com.gmail.at.ivanehreshi.epam.touragency.persistence.util;

import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.util.*;

public class UrlParamDecoder {
    public static Optional<String> decode(String param) {
        return Optional.ofNullable(param)
                .flatMap(s -> TryOptionalUtil.of(() -> s.getBytes("iso-8859-1")))
                .flatMap(s -> TryOptionalUtil.of(() -> new String(s, "UTF-8")));
    }
}
