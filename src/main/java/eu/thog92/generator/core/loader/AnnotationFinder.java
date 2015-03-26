package eu.thog92.generator.core.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class AnnotationFinder
{

    private final List<String> blackListedPackage = new ArrayList<>();

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
        return this.start(targetClass);
    }

    private List<Class> start(Class<? extends Annotation> targetClass)
    {
        List<Class> classes = new ArrayList<>();
        try
        {
            for (String classpathEntry : System.getProperty("java.class.path").split(System.getProperty("path.separator"))) {
                System.out.println("Scanning " + classpathEntry);
                File entryFile = new File(classpathEntry);
                if (classpathEntry.endsWith(".jar")) {
                    File jar = new File(classpathEntry);

                    // Don't scan internal libs
                    if(jar.getPath().contains("jre" + File.separator + "lib"))
                        continue;

                    JarInputStream is = new JarInputStream(new FileInputStream(jar));

                    JarEntry entry;
                    while( (entry = is.getNextJarEntry()) != null) {
                        if(entry.getName().endsWith(".class") && !this.isNotBlackListed(entry.getName())) {
                            try
                            {
                                Class clazz = Class.forName(entry.getName().substring(0, entry.getName().length() - 6).replace("/", "."));
                                if (this.containAnnotInClass(clazz, targetClass))
                                    classes.add(clazz);

                            } catch (Exception ignored)
                            {
                            }
                        }
                    }
                }

                // Support IDE and Gradle class dirs
                else if(entryFile.exists() && entryFile.isDirectory())
                {
                    for(File file : entryFile.listFiles())
                    {
                        classes.addAll(findClasses(entryFile, "", targetClass));
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
        for(String entry : blackListedPackage)
        {
            if(name.startsWith(entry))
                return true;
        }
        return false;
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
