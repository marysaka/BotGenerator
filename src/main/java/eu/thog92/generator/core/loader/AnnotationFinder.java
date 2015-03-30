package eu.thog92.generator.core.loader;

import eu.thog92.generator.api.annotations.Module;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class AnnotationFinder
{

    private final List<String> blackListedPackage = new ArrayList<>();

    private final HashMap<String, Module> annotCache = new HashMap<>();

    public AnnotationFinder()
    {
        blackListedPackage.add("oracle");
        blackListedPackage.add("com/oracle");
        blackListedPackage.add("com/sun");
        blackListedPackage.add("org/w3c");
        blackListedPackage.add("org/xml");
        blackListedPackage.add("org/omg");
        blackListedPackage.add("org/jcp");
        blackListedPackage.add("sun/");
        blackListedPackage.add("java/");
        blackListedPackage.add("javafx/");
        blackListedPackage.add("javax/");
        blackListedPackage.add("jdk/");
        blackListedPackage.add("twitter4j/");
        blackListedPackage.add("com/intellij");
    }

    private Map<String, Class> findClasses(File directory, String packageName)
    {
        if (packageName.startsWith("."))
            packageName = packageName.substring(1);

        Map<String, Class> classes = new HashMap<>();

        if (!directory.exists())
        {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files)
        {
            if (file.isDirectory())
            {
                classes.putAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class"))
            {
                try
                {
                    String name = file.getName().substring(0, file.getName().length() - 6);
                    Class clazz = Class.forName(packageName + '.' + name);
                    if (this.containAnnotInClass(clazz))
                    {
                        Module module = (Module) clazz.getAnnotation(Module.class);
                        classes.put(module.name(), clazz);
                        this.annotCache.put(module.name(), module);
                    }

                } catch (ClassNotFoundException ignored)
                {
                }
            }
        }
        return classes;
    }

    public Map<String, Class> search()
    {
        return this.start();
    }

    public Module getAnnotFromClass(String name)
    {
        return this.annotCache.get(name);
    }

    private Map<String, Class> start()
    {
        // Add modules dir to classpath
        URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> classLoaderClass = URLClassLoader.class;
        File moduleDir = new File("modules");
        if(!moduleDir.exists())
            moduleDir.mkdirs();
        for(File file : moduleDir.listFiles())
        {
            if(file.getName().endsWith(".jar"))
            {
                try {
                    Method method = classLoaderClass.getDeclaredMethod("addURL", new Class[]{URL.class});
                    method.setAccessible(true);
                    method.invoke(systemClassLoader, new Object[]{file.toURI().toURL()});
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
        Map<String, Class> classes = new HashMap<>();

        try
        {
            for (URL url : systemClassLoader.getURLs())
            {
                String classpathEntry = url.getPath().replace("%20", " ");
                System.out.println("Scanning " + classpathEntry);
                File entryFile = new File(classpathEntry);
                if (classpathEntry.endsWith(".jar"))
                {
                    File jar = new File(classpathEntry);

                    // Don't scan internal libs
                    if (jar.getPath().contains("jre" + File.separator + "lib"))
                        continue;

                    JarInputStream is = new JarInputStream(new FileInputStream(jar));

                    JarEntry entry;
                    while ((entry = is.getNextJarEntry()) != null)
                    {
                        if (entry.getName().endsWith(".class") && !this.isNotBlackListed(entry.getName()))
                        {
                            try
                            {
                                Class clazz = Class.forName(entry.getName().substring(0, entry.getName().length() - 6).replace("/", "."));
                                if (this.containAnnotInClass(clazz))
                                {
                                    Module module = (Module) clazz.getAnnotation(Module.class);
                                    classes.put(module.name(), clazz);

                                    this.annotCache.put(module.name(), module);
                                }


                            } catch (Exception ignored)
                            {
                            }
                        }
                    }
                }

                // Support IDE and Gradle class dirs
                else if (entryFile.exists() && entryFile.isDirectory())
                {
                    for (File file : entryFile.listFiles())
                    {
                        classes.putAll(findClasses(entryFile, ""));
                    }
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return classes;
    }

    private boolean isNotBlackListed(String name)
    {
        for (String entry : blackListedPackage)
        {
            if (name.startsWith(entry))
                return true;
        }
        return false;
    }

    private boolean containAnnotInClass(Class clazz)
    {
        for (Annotation annot : clazz.getDeclaredAnnotations())
        {
            if (annot.annotationType() == Module.class)
            {
                return true;
            }
        }
        return false;
    }
}
