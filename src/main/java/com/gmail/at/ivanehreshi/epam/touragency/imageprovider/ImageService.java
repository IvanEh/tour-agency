package com.gmail.at.ivanehreshi.epam.touragency.imageprovider;

import java.io.*;

public interface ImageService {
    String save(InputStream is, String ext);

    boolean pipe(String filename, OutputStream os);
}
