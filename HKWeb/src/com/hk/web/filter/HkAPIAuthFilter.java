package com.hk.web.filter;


/**
 * @author vaibhav.adlakha
 */
public class HkAPIAuthFilter 
//implements Filter 
{

    /*private static Logger      logger            = LoggerFactory.getLogger(HkAPIAuthFilter.class);

    public static final String JSON_CONTENT_TYPE = "application/json; charset=UTF-8";

    private HkAuthService      hkAuthService;

    

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) arg0;
        HttpServletResponse resp = (HttpServletResponse) arg1;

        String authToken = req.getParameter("authToken");
        String apiVersion = req.getParameter("apiVersion");
        String apiKey = req.getParameter("apiKey");

        // check for a not null authToken
        if (StringUtils.isEmpty(authToken)) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "NO_AUTH_TOKEN_PASSED").build());
            return;
        }

        // check for a not null apiKey
        if (StringUtils.isEmpty(apiKey)) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "NO_API_KEY_PASSED").build());
            return;
        }

        if (!HkAPIUser.containsApiKey(apiKey)) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "INVALID_APP_KEY").build());
            return;
        }

        if (StringUtils.isEmpty(apiVersion)) {
            apiVersion = HkAPI.CURRENT_VERSION;
        }

        LocaleContextHolder.getLocaleContext().setApiVersion(apiVersion);

        *//**
         * validate token and api key in auth service, do all in memory
         *//*
        try {
            getHkAuthService().validateToken(authToken, apiKey, false);
        } catch (Throwable t) {
            writeExceptionResponse(req, resp, new JSONResponseBuilder().addField("code", "INVALID_AUTH_TOKEN").build());
            return;
        }

    }

    public HkAuthService getHkAuthService() {
        if (hkAuthService == null) {
            hkAuthService = (HkAuthService) ServiceLocatorFactory.getService(HkAuthService.class);
        }
        return hkAuthService;
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

    @Override
    public void destroy() {

    }*/

}
