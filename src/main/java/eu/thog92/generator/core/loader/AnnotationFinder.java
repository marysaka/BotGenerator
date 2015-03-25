package eu.thog92.generator.core.loader;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class AnnotationFinder
{

    public AnnotationFinder()
    {
    }


    private List<Class> findClasses(File directory, String packageName, Class<? extends Annotation> targetClass)
    {
        if (packageName.startsWith("."))
            packageName = packageName.substring(1);

        List<Class> classes = new ArrayList<>();

        if (!directory.exists())
        {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files)
        {
            if (file.isDirectory())
            {
                classes.addAll(findClasses(file, packageName + "." + file.getName(), targetClass));
            } else if (file.getName().endsWith(".class"))
            {
                try
                {
                    String name = file.getName().substring(0, file.getName().length() - 6);
                    Class clazz = Class.forName(packageName + '.' + name);
                    if (this.containAnnotInClass(clazz, targetClass))
                        classes.add(clazz);

                } catch (ClassNotFoundException ignored)
                {
                }
            }
        }
        return classes;
    }

    public List<Class> search(Class<? extends Annotation> targetClass)
    {
        return this.searchOnPackage("", targetClass);
    }

    private List<Class> searchOnPackage(String packageName, Class<? extends Annotation> targetClass)
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<Class> classes = new ArrayList<>();
        try
        {
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);

            while (resources.hasMoreElements())
            {
                File file = new File(resources.nextElement().getFile());
                if (file.isDirectory())
                {
                    classes.addAll(findClasses(file, packageName, targetClass));
                } else if (file.getName().endsWith(".class"))
                {
                    try
                    {
                        Class clazz = Class.forName(packageName + "." + file.getName().substring(0, file.getName().length() - 6));
                        if (this.containAnnotInClass(clazz, targetClass))
                            classes.add(clazz);

                    } catch (ClassNotFoundException ignored)
                    {
                    }
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return classes;
    }

    private boolean containAnnotInClass(Class clazz, Class<? extends Annotation> targetClass)
    {
        for (Annotation annot : clazz.getDeclaredAnnotations())
        {
            if (annot.annotationType() == targetClass)
            {
                return true;
            }
        }
        return false;
    }
}
