package com.hk.web.servlet;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.hk.util.json.JSONResponseBuilder;

@SuppressWarnings("serial")
public class HKSingleLoginServlet extends HttpServlet {

    private static Logger      logger            = LoggerFactory.getLogger(HKSingleLoginServlet.class);

    public static final String JSON_CONTENT_TYPE = "application/json; charset=UTF-8";

    // TODO: change this to post
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String appId = req.getParameter("appId");
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        if (StringUtils.isBlank(appId)) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "NO_APP_ID_PASSED").build());
            return;
        }
        
        if (StringUtils.isBlank(login)) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "NO_LOGIN_PASSED").build());
            return;
        }
        
        if (StringUtils.isBlank(password)) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "NO_PWD_PASSED").build());
            return;
        }

    }

    private void writeExceptionResponse(HttpServletRequest req, HttpServletResponse resp, String jsonResponse) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        writeJSONResponse(req, resp, jsonResponse);

    }

    private void writeJSONResponse(HttpServletRequest req, HttpServletResponse resp, String jsonResponse) {
        resp.setContentType(JSON_CONTENT_TYPE);
        String callback = req.getParameter("callback");
        if (callback != null) {
            jsonResponse = callback + "(" + jsonResponse + ")";
        }
        writeResponse(req, resp, jsonResponse);
    }

    private void writeResponse(HttpServletRequest req, HttpServletResponse resp, String response) {
        try {
            resp.getWriter().write(response);
        } catch (IOException e) {
            logger.error("IO Exception occured in writing response :" + response, e);
        }
    }

}
