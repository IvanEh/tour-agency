package com.gmail.at.ivanehreshi.epam.touragency.security;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class SecurityContextTest {
    private SecurityContext securityContext;

    @Before
    public void setUp() throws Exception {
        SecurityContext.INSTANCE.reset();
        securityContext = SecurityContext.INSTANCE;
    }

    @Test
    public void testAddedConstraintCheckExact() {
        securityContext.addSecurityConstraint("/index\\.html", Role.CUSTOMER);
        assertTrue(securityContext.allowed("/index.html", Arrays.asList(Role.CUSTOMER)));
    }

    @Test
    public void testAddedConstraintCheckNull() {
        securityContext.addSecurityConstraint("/index\\.html", Role.CUSTOMER);
        assertFalse(securityContext.allowed("/index.html", null));
    }

    @Test
    public void testAddedConstraintCheckNoRoles() {
        securityContext.addSecurityConstraint("/index\\.html", Role.CUSTOMER);
        assertFalse(securityContext.allowed("/index.html", Collections.EMPTY_LIST));
    }

    @Test
    public void testAddedConstraintCheckNoPermission() throws Exception {
        securityContext.addSecurityConstraint("/index\\.html", Role.CUSTOMER);
        assertFalse(securityContext.allowed("/index.html",
                Arrays.asList(Role.TOUR_AGENT)));
    }

    @Test
    public void testAddedConstraintCheckMorePermissions() throws Exception {
        securityContext.addSecurityConstraint("/index\\.html", Role.CUSTOMER);
        assertTrue(securityContext.allowed("/index.html",
                Arrays.asList(Role.TOUR_AGENT, Role.CUSTOMER)));
    }

    @Test
    public void testAddedConstraintCheckRepeatedRoles() throws Exception {
        securityContext.addSecurityConstraint("/index\\.html", Role.CUSTOMER);
        assertTrue(securityContext.allowed("/index.html", Arrays.asList(Role.TOUR_AGENT,
                Role.TOUR_AGENT, Role.CUSTOMER, Role.CUSTOMER)));
    }

    @Test
    public void testAddedConstraintMissGivenRole() {
        securityContext.addSecurityConstraint("/index\\.html", Role.CUSTOMER);
        assertTrue(securityContext.allowed("/hello.html", Arrays.asList(Role.CUSTOMER)));
    }

    @Test
    public void testAddedConstraintMissGivenNoRole() {
        securityContext.addSecurityConstraint("/index\\.html", Role.CUSTOMER);
        assertTrue(securityContext.allowed("/hello.html", Collections.EMPTY_LIST));
    }

    @Test
    public void testAuthConstraintGivenNoRole() {
        securityContext.addSecurityConstraint("/index\\.html");
        assertTrue(securityContext.allowed("/index.html", Collections.EMPTY_LIST));
    }

    @Test
    public void testAuthConstraintGivenRoles() {
        securityContext.addSecurityConstraint("/index\\.html");
        assertTrue(securityContext.allowed("/index.html",
                Arrays.asList(Role.CUSTOMER, Role.TOUR_AGENT)));
    }
}
