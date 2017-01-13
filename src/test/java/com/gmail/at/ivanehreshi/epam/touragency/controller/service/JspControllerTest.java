package com.gmail.at.ivanehreshi.epam.touragency.controller.service;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import javax.servlet.http.*;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JspControllerTest {
    private JspController jspController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private RequestService requestService;

    @Before
    public void setUp() throws Exception {
        jspController = new JspController("/pages/", ".html", ".jsp");
        requestService = new RequestService(request, response, null);
    }

    @After
    public void tearDown() throws Exception {
        jspController = null;
        requestService = null;
    }

    @Test
    public void shouldRedirectPage() {
        given(request.getPathInfo()).willReturn("/path1/path2/page.html");
        given(request.getMethod()).willReturn("GET");

        jspController.execute(requestService);

        assertEquals("/pages/path1/path2/page.jsp", requestService.getRenderPage());
    }

    @Test
    public void shouldNotRedirectWhenWithoutSuffix() {
        given(request.getPathInfo()).willReturn("/path1/path2/page.xyz");
        given(request.getMethod()).willReturn("GET");

        jspController.execute(requestService);

        assertEquals(null, requestService.getRenderPage());
    }

    @Test
    public void shouldNotRedirectWhenSuffixInMiddle() {
        given(request.getPathInfo()).willReturn("/path1/path.html/page.jsp");
        given(request.getMethod()).willReturn("GET");

        jspController.execute(requestService);

        assertEquals(null, requestService.getRenderPage());
    }

}
