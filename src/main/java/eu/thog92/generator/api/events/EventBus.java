package eu.thog92.generator.api.events;

import eu.thog92.generator.api.annotations.SubscribeEvent;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventBus
{

    private final HashMap<Class<IEvent>, List<Method>> eventMethodHashMap = new HashMap<>();
    private final HashMap<Class, List<Object>> objectHashMap = new HashMap<>();

    public boolean post(Object object)
    {
        List<Method> methodList = this.eventMethodHashMap.get(object.getClass());

        if (methodList == null)
            return false;

        for (Method method : methodList)
        {
            try
            {
                for (Object toCall : objectHashMap.get(method.getDeclaringClass()))
                {
                    method.invoke(toCall, object);
                }

            } catch (IllegalAccessException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }


    public void register(Object object)
    {
        Class clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods())
        {
            if (method.getParameterCount() != 1)
                continue;

            for (Annotation annotation : method.getDeclaredAnnotations())
            {
                if (annotation.annotationType() == SubscribeEvent.class)
                {
                    Class param = method.getParameterTypes()[0];
                    if (param.getSuperclass().isAssignableFrom(IEvent.class))
                    {
                        if (eventMethodHashMap.get(param) == null)
                        {
                            eventMethodHashMap.put(param, new ArrayList<>());
                        }
                        if (objectHashMap.get(clazz) == null)
                        {
                            objectHashMap.put(clazz, new ArrayList<>());
                        }

                        if (!objectHashMap.get(clazz).contains(object))
                            objectHashMap.get(clazz).add(object);

                        eventMethodHashMap.get(param).add(method);

                    }
                }
            }
        }
    }
}
