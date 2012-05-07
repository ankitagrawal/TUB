/* Copyright 2005-2006 Tim Fennell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.stripes.controller;

import java.lang.reflect.Method;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.exception.StripesServletException;

/**
 * <p>Uses Annotations on classes to identify the ActionBean that corresponds to the current
 * request.  ActionBeans are annotated with an {@code @UrlBinding} annotation, which denotes the
 * web application relative URL that the ActionBean should respond to.</p>
 *
 * <p>Individual methods on ActionBean classes are expected to be annotated with @HandlesEvent
 * annotations, and potentially a @DefaultHandler annotation.  Using these annotations the
 * Resolver will determine which method should be executed for the current request.</p>
 *
 * @see net.sourceforge.stripes.action.UrlBinding
 * @author Tim Fennell
 */
public class AnnotatedReloadableClassActionResolver extends AnnotatedClassActionResolver {

    /**
     * Returns the Method that is the default handler for events in the ActionBean class supplied.
     * If only one handler method is defined in the class, that is assumed to be the default. If
     * there is more than one then the method marked with @DefaultHandler will be returned.
     *
     * @param bean the ActionBean type bound to the request
     * @return Method object that should handle the request
     * @throws StripesServletException if no default handler could be located
     *
     * See if we have a less hacky way to do this
     *
     * PATCH : this method has been patched
     */
    public Method getDefaultHandler(Class<? extends ActionBean> bean) throws StripesServletException {

      // KANI PATCH:  this is to make the actionBeans instrumented by Guice AOP work
      // in case of AOP, the bean is instrumented by guice at runtime and a new subclass is created
      // and we do not get the appropriate handlers from the eventMappings hashmap in that case

      // for now comparing the classLoaders of ActionBean and its super class. in case the class loader is null
      // for a super class (can happen when the action bean does not extend the BaseAction class we have written)
      // we can assume that the class is not instrumented

      // this will not work as there were further problems, so have separated out a separate class for now
      // will come back to this in the future

      /*
      Class realBean;

      ClassLoader superClassLoader = bean.getSuperclass().getClassLoader();
      if (superClassLoader == null) {
        realBean = bean;
      } else {
        ClassLoader thisClassLoader = bean.getClassLoader();
        if (thisClassLoader.equals(superClassLoader)) {
          realBean = bean;
        } else {
          realBean = bean.getSuperclass();
        }
      }


        Map<String,Method> handlers = this.eventMappings.get(realBean);

      // KANI PATCH

        if (handlers.size() == 1) {
            return handlers.values().iterator().next();
        }
        else {
            Method handler = handlers.get(DEFAULT_HANDLER_KEY);
            if (handler != null) return handler;
        }

        // If we get this far, there is no sensible default!  Kaboom!
        throw new StripesServletException("No default handler could be found for ActionBean of " +
            "type: " + bean.getName());

       */

      return null;
    }

}
