package com.gdgocbu.tabulation.backend.utilities;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class RequestHeader {
    @Autowired
    private HttpServletRequest request;

    public String getHeader(String name) {
        return request.getHeader(name);
    }
}
