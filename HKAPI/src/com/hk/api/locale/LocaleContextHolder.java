package com.hk.api.locale;

import com.hk.api.HkAPI;

public class LocaleContextHolder {


    private static final ThreadLocal<LocaleContext> localeContextHolder = new ThreadLocal<LocaleContext>();

    /**
     * Reset the LocaleContext for the current thread.
     */
    public static void resetLocaleContext() {
        localeContextHolder.remove();
    }

    /**
     * Associate the given LocaleContext with the current thread.
     *
     * @param localeContext the current LocaleContext, or <code>null</code> to reset the thread-bound context
     */
    public static void setLocaleContext(LocaleContext localeContext) {
        localeContextHolder.set(localeContext);
    }

    /**
     * Return the LocaleContext associated with the current thread, if any.
     *
     * @return the current LocaleContext, or <code>null</code> if none
     */
    public static LocaleContext getLocaleContext() {
        return (LocaleContext) localeContextHolder.get();
    }

    public static String getApiVersion() {
        String lang = HkAPI.CURRENT_VERSION;
        if (LocaleContextHolder.getLocaleContext() != null) {
            if (LocaleContextHolder.getLocaleContext().getApiVersion() != null) {
                lang = LocaleContextHolder.getLocaleContext().getApiVersion();
            }
        }
        return lang;
    }


}
