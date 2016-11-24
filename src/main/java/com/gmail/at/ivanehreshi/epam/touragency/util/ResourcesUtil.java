package com.gmail.at.ivanehreshi.epam.touragency.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class ResourcesUtil {
    private static Logger LOGGER = LogManager.getLogger(ResourcesUtil.class);

    public static File getResourceFile(String path) {
        URL url = ResourcesUtil.class.getClassLoader().getResource(path);

        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            LOGGER.warn("Bad path", e);
            return null;
        }


        if (file.exists()) {
            return file;
        }

        LOGGER.info("Cannot load resource");
        return null;
    }
}
