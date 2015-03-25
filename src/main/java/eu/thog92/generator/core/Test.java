package eu.thog92.generator.core;

import eu.thog92.generator.api.annotations.Module;
import eu.thog92.generator.api.annotations.SubscribeEvent;
import eu.thog92.generator.api.events.InitEvent;

/**
 * Created by Thog92 on 23/03/2015.
 */

@Module(name = "Test", version = "0.1")
public class Test
{

    @SubscribeEvent
    public void init(InitEvent event)
    {
        System.out.println("INIT!");
    }
}
