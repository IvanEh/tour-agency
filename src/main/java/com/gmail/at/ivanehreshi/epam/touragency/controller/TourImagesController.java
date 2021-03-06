package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.imageprovider.*;
import com.gmail.at.ivanehreshi.epam.touragency.security.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;
import org.apache.logging.log4j.*;

import javax.servlet.http.*;
import java.net.*;

public class TourImagesController extends Controller {
    private static final Logger LOGGER = LogManager.getLogger(TourImagesController.class);

    private TourImageService tourImageService =
            ServiceLocator.INSTANCE.get(TourImageService.class);

    private ImageService imageService = ServiceLocator.INSTANCE.get(ImageService.class);

    @Override
    public void post(RequestService reqService) {
        long tourId = reqService.getLong("tourId").get();
        URL imageUrl = null;
        URL thumbnailUrl = null;

        try {
            imageUrl = new URL(reqService.getString("imageUrl"));
            thumbnailUrl = new URL(reqService.getString("thumbnailUrl"));
        } catch (MalformedURLException e) {
            reqService.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        TourImage tourImage = new TourImage();
        tourImage.setTour(new Tour(tourId));
        tourImage.setImageUrl(imageUrl);
        tourImage.setThumbnailUrl(thumbnailUrl);

        tourImageService.create(tourImage);
    }

    @Override
    public void delete(RequestService reqService) {
        long id = reqService.getLong("id").get();
        TourImage tourImage = tourImageService.read(id);
        tourImageService.delete(id);

        if (!reqService.getRequest().isUserInRole(Role.TOUR_AGENT.toString())) {
            throw new AccessDeniedException();
        }

        String filename = reqService.getString("file");
        imageService.delete(getFilename(tourImage.getImageUrl()));
    }

    private String getFilename(URL url) {
        String s = url.getFile();
        return s.substring(s.lastIndexOf("/") + 1);
    }
}
