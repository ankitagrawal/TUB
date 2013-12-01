package com.hk.web.validation;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.exception.StripesJspException;
import net.sourceforge.stripes.tag.DefaultPopulationStrategy;
import net.sourceforge.stripes.tag.InputTagSupport;
import net.sourceforge.stripes.tag.PopulationStrategy;

import com.akube.framework.stripes.population.CustomPopulationStrategy;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/20/12
 * Time: 7:50 PM
 * This is just beanFirstPopulationStrategy at ActionBean Level rather than global level.
 */
public class SelectivePopulationStrategy implements PopulationStrategy
{
    //private static final Log LOG = Log.getInstance(SelectivePopulationStrategy.class);

    private Configuration config;
    private PopulationStrategy defaultDelegate;
    private Map<Class<? extends PopulationStrategy>, PopulationStrategy> delegates =
            new HashMap<Class<? extends PopulationStrategy>, PopulationStrategy>();
    private Map<Class<? extends ActionBean>, PopulationStrategy> actionBeanStrategies =
            new HashMap<Class<? extends ActionBean>, PopulationStrategy>();

    protected PopulationStrategy getDelegate(InputTagSupport tag)
            throws StripesJspException
    {
        ActionBean actionBean = tag.getActionBean();
        if (actionBean == null)
            return defaultDelegate;

        // check cache
        Class<? extends ActionBean> beanType = actionBean.getClass();
        PopulationStrategy delegate = actionBeanStrategies.get(beanType);
        if (delegate != null)
            return delegate;

        CustomPopulationStrategy annotation =
                beanType.getAnnotation(CustomPopulationStrategy.class);
        if (annotation == null)
        {
            delegate = defaultDelegate;
        }
        else
        {
            Class<? extends PopulationStrategy> type = annotation.value();
            delegate = delegates.get(type);
            if (delegate == null)
            {
                try
                {
                    delegate = type.newInstance();
                    delegate.init(config);
                    delegates.put(type, delegate);
                }
                catch (Exception e)
                {
                    delegate = defaultDelegate;
                    /*LOG.info("Could not instantiate population strategy"
                            + " of name [" + type + "]", e);*/
                }
            }
        }

        // cache and return
        actionBeanStrategies.put(beanType, delegate);
        return delegate;
    }

    public Object getValue(InputTagSupport tag) throws StripesJspException
    {
        PopulationStrategy strategy = getDelegate(tag);
        Object value = (strategy).getValue(tag);
        return value;
    }

    public void init(Configuration configuration) throws Exception
    {
        this.config = configuration;
        defaultDelegate = new DefaultPopulationStrategy();
        defaultDelegate.init(config);
    }
}
