package eu.thog92.generator.api.events;

import eu.thog92.generator.api.annotations.SubscribeEvent;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Thog92 on 25/03/2015.
 */
public class EventBus
{

    private HashMap<Class<Event>, List<Method>> eventMethodHashMap = new HashMap<Class<Event>, List<Method>>();
    private HashMap<Class, List<Object>> objectHashMap = new HashMap<>();
    /*
            List<Object> instanciedModules = new ArrayList<>();
        for(Class module : modules)
        {
            try
            {
                instanciedModules.add(module.newInstance());
            } catch (InstantiationException e)
            {
            } catch (IllegalAccessException e)
            {
                //e.printStackTrace();
            }
        }
     */

    public boolean post(Object object)
    {
        List<Method> methodList = this.eventMethodHashMap.get(object.getClass());

        if(methodList == null)
            return false;

        for (Method method : methodList)
        {
            try
            {
                for(Object toCall : objectHashMap.get(method.getDeclaringClass()))
                {
                    method.invoke(toCall, object);
                }

            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
                continue;
            } catch (InvocationTargetException e)
            {
                e.printStackTrace();
                continue;
            }
        }
        return true;
    }


    public void register(Object object)
    {
        Class clazz = object.getClass();
        for(Method method : clazz.getDeclaredMethods())
        {
            if(method.getParameterCount() != 1)
                continue;

            for(Annotation annotation : method.getDeclaredAnnotations())
            {
                if(annotation.annotationType() == SubscribeEvent.class)
                {
                    Class param = method.getParameterTypes()[0];
                    if(param.getSuperclass().isAssignableFrom(Event.class))
                    {
                        if(eventMethodHashMap.get(param) == null)
                        {
                            eventMethodHashMap.put(param, new ArrayList<Method>());
                        }
                        if(objectHashMap.get(clazz) == null)
                        {
                            objectHashMap.put(clazz, new ArrayList<Object>());
                        }

                        if(!objectHashMap.get(clazz).contains(object))
                            objectHashMap.get(clazz).add(object);

                        eventMethodHashMap.get(param).add(method);

                    }
                }
            }
        }
    }
}
