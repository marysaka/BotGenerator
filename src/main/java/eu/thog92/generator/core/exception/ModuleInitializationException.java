package eu.thog92.generator.core.exception;

import java.io.IOException;

public class ModuleInitializationException extends IOException
{
    public ModuleInitializationException(String s)
    {
        super(s);
    }

    public ModuleInitializationException(Exception e)
    {
        super(e);
    }
}
