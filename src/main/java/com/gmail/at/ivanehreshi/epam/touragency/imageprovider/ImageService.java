package com.gmail.at.ivanehreshi.epam.touragency.imageprovider;

import java.io.*;

public interface ImageService {
    String save(InputStream is, String ext);

    boolean load(String filename, OutputStream os);

    boolean delete(String filename);

    String thumbnailName(String filename);
}
