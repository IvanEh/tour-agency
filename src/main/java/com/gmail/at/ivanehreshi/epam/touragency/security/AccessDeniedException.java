package com.gmail.at.ivanehreshi.epam.touragency.security;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super("Access denied to the given resource");
    }
}
