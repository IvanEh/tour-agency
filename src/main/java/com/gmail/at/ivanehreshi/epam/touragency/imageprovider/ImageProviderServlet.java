package com.gmail.at.ivanehreshi.epam.touragency.imageprovider;

import org.apache.logging.log4j.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;

@WebServlet(urlPatterns = "/image-provider/*", initParams = {
        @WebInitParam(name = "location", value="~/prov"),
        @WebInitParam(name = "maxSize",value = "1")
})
@MultipartConfig
public class ImageProviderServlet extends HttpServlet{
    private static final Logger LOGGER = LogManager.getLogger(ImageProviderServlet.class);

    private static final String PARAM_LOCATION = "location";

    private static final String PARAM_MAX_SIZE = "maxSize";

    private static final String LINUX_HOME_DIR = "~";

    private static final String SYS_PROP_USER_HOME = "user.home";

    private static final int MEGABYTE = 1024*1024;

    private static final String FORM_IMAGES = "images[]";

    private ImageService imageService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String location = config.getInitParameter(PARAM_LOCATION).replace(LINUX_HOME_DIR,
                System.getProperty(SYS_PROP_USER_HOME)) + File.separator;

        long maxSize = Long.valueOf(config.getInitParameter(PARAM_MAX_SIZE))*MEGABYTE;

        imageService = new ImageServiceImpl(location, maxSize);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int index = req.getRequestURI().indexOf(req.getServletPath()) +
                req.getServletPath().length() + 1;
        String filename = req.getRequestURI().substring(index);

        if(!imageService.pipe(filename, resp.getOutputStream())) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            writeGetHeaders(resp, filename);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int filesCount = (int) req.getParts().stream()
                .filter(p -> p.getName().equals(FORM_IMAGES))
                .count();

        String[] names = new String[filesCount];

        int i = 0;
        for (Part part: req.getParts()) {
            if (!part.getName().equals(FORM_IMAGES))
                continue;

            String ext = getExtension(part.getContentType());

            String filename = imageService.save(part.getInputStream(), ext);

            if (filename != null) {
                names[i] = getUrl(req, filename);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        resp.setStatus(HttpServletResponse.SC_CREATED);

        String[] jsonArr = new String[names.length];
        for (int j = 0; j < jsonArr.length; j++) {
            jsonArr[j] = jsonArray(quote(names[j]), quote(thumbnail(names[j])));
        }

        resp.getOutputStream().print(jsonArray(jsonArr));
    }

    private String thumbnail(String filename) {
        int extIndex = filename.lastIndexOf('.');
        return filename.substring(0, extIndex) + "-thumb.png";
    }

    private String jsonArray(String... values) {
        return "[" +  String.join(",", values) + "]" ;
    }

    private String getUrl(HttpServletRequest request, String name) {
        return request.getRequestURL() + "/" + name;
    }

    private String quote(String name) {
        return "\"" + name + "\"";
    }


    private String getExtension(String contentType) {
        switch (contentType) {
            case "image/png":
                return ".png";
            case "image/jpeg":
                return ".jpg";
            default:
                return null;
        }
    }


    private void writeGetHeaders(HttpServletResponse resp, String filename) {
        String filetype = filename.substring(filename.indexOf(".") + 1);
        resp.setHeader("Content-Type", "image/" + filetype);
        resp.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
        resp.setHeader("Cache-Control", "max-age=11536000");
    }
}
