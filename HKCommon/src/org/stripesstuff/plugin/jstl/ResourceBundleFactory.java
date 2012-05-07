package org.stripesstuff.plugin.jstl;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * Bundle factory to retrieve the resource bundles with.
 * <p>
 * Normally, one would implement this interface as well as
 * {@link net.sourceforge.stripes.localization.LocalizationBundleFactory Stripes' LocalizationBundleFactory} to provide
 * all localization for your application.
 * </p>
 *
 * @author <a href="mailto:kindop@xs4all.nl">Oscar Westra van Holthe - Kind</a>
 * @version $Id: ResourceBundleFactory.java,v 1.1 2008-10-22 10:18:20 Kani Exp $
 * @see JstlBundleInterceptor
 */
public interface ResourceBundleFactory
{
	/**
	 * Returns the ResourceBundle from which to messages for the specified locale by default.
	 *
	 * @param locale the locale that is in use for the current request
	 * @return the default resource bundle
	 * @throws MissingResourceException when a bundle that is expected to be present cannot be located
	 */
	ResourceBundle getDefaultBundle(Locale locale) throws MissingResourceException;
}