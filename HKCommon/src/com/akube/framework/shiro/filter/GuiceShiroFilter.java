package com.akube.framework.shiro.filter;

import org.apache.shiro.web.servlet.ShiroFilter;
import com.akube.framework.shiro.filter.GuiceWebConfiguration;

/**
 * <p>Extension of ShiroFilter that uses {@link GuiceWebConfiguration} to configure the Shiro instance.</p>
 *
 * @author Animesh Jain
 */
public class GuiceShiroFilter extends ShiroFilter {

    public GuiceShiroFilter() {
        this.configClassName = GuiceWebConfiguration.class.getName();
    }
}
