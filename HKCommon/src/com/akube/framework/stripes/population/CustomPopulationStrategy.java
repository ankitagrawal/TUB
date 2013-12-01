package com.akube.framework.stripes.population;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.sourceforge.stripes.tag.DefaultPopulationStrategy;
import net.sourceforge.stripes.tag.PopulationStrategy;

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
