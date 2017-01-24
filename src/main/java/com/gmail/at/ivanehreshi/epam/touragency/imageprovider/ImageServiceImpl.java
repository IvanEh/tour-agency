package com.gmail.at.ivanehreshi.epam.touragency.imageprovider;

import org.apache.logging.log4j.*;
import org.imgscalr.*;

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ImageServiceImpl implements ImageService {
    private static final Logger LOGGER = LogManager.getLogger(ImageServiceImpl.class);

    private static final int BUFFER_SIZE = 16384;

    private static final int THREAD_NUM = 8;

    private ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUM);

    private String location;

    private long maxSize;

    public ImageServiceImpl(String location, long maxSize) {
        this.location = location;
        this.maxSize = maxSize;
    }

    @Override
    public String save(InputStream is, String ext) {
        UUID uuid = UUID.randomUUID();
        String filename = uuid + ext;

        File file = new File(location + filename);

        try {
            if(!file.exists()) {
                file.createNewFile();
            }

            try (BufferedOutputStream os =
                         new BufferedOutputStream(new FileOutputStream(file))) {
                byte[] bytes = new byte[BUFFER_SIZE];

                int n;
                while ((n = is.read(bytes)) != -1) {
                    os.write(bytes, 0, n);
                }
            }

            executor.execute(() -> createThumbnail(file, uuid));
        } catch (IOException e) {
            LOGGER.error("Cannot save image", e);
            return null;
        }

        return filename;
    }

    private void createThumbnail(File file, UUID uuid) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);

            BufferedImage thumbnail = Scalr.resize(bufferedImage, Scalr.Method.BALANCED,
                    Scalr.Mode.AUTOMATIC, 64, 64);

            ImageIO.write(thumbnail, "png", new File(location + uuid + "-thumb.png"));
        } catch (IOException e) {
            LOGGER.info("Cannot create thumbnail", e);
        }
    }

    @Override
    public boolean pipe(String filename, OutputStream os) {
        String path = location + filename;
        File file = new File(path);

        try {
            try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
                byte[] bytes = new byte[BUFFER_SIZE];
                int n;
                while ((n = is.read(bytes)) != -1) {
                    os.write(bytes, 0, n);
                }
            }
        } catch (IOException e) {
            LOGGER.info("Cannot retrieve image", e);
            return false;
        }

        return true;
    }
}
