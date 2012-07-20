package com.akube.framework.stripes.population;

import net.sourceforge.stripes.tag.DefaultPopulationStrategy;
import net.sourceforge.stripes.tag.PopulationStrategy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/20/12
 * Time: 7:53 PM
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomPopulationStrategy
{
    Class<? extends PopulationStrategy> value()
            default DefaultPopulationStrategy.class;
}
