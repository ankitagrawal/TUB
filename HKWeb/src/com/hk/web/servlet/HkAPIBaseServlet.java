package com.hk.web.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hk.util.json.JSONResponseBuilder;

/**
 * @author vaibhav.adlakha
 */
@SuppressWarnings("serial")
public class HkAPIBaseServlet extends HttpServlet {

    private static Logger      logger            = LoggerFactory.getLogger(HkAPIBaseServlet.class);

    public static final String JSON_CONTENT_TYPE = "application/json; charset=UTF-8";

    protected void writeExceptionResponse(HttpServletRequest req, HttpServletResponse resp, String jsonResponse) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        writeJSONResponse(req, resp, jsonResponse);
    }

    protected void writeJSONResponse(HttpServletRequest req, HttpServletResponse resp, String jsonResponse) {
        resp.setContentType(JSON_CONTENT_TYPE);
        String callback = req.getParameter("callback");
        if (callback != null) {
            jsonResponse = callback + "(" + jsonResponse + ")";
        }
        writeResponse(req, resp, jsonResponse);
    }

    protected void writeResponse(HttpServletRequest req, HttpServletResponse resp, String response) {
        try {
            resp.getWriter().write(response);
        } catch (IOException e) {
            logger.error("IO Exception occured in writing response :" + response, e);
        }
    }

    protected void writeSuccessResponse(HttpServletRequest req, HttpServletResponse resp, String response) {
        writeJSONResponse(req, resp, response);
    }

    protected void writeInvalidParametersException(HttpServletRequest req, HttpServletResponse resp) {
        writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "INVALID_PARAMETER").build());
    }

    protected void writeMissingParametersException(HttpServletRequest req, HttpServletResponse resp, String msg) {
        writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("msg", msg).addField("code", "MISSING_PARAMETER").build());
    }

}
