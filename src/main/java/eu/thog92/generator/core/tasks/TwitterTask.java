package eu.thog92.generator.core.tasks;

import eu.thog92.generator.api.tasks.ScheduledTask;

public class TwitterTask extends ScheduledTask
{

    private GeneratorTask generatorTask;
    private int numTweets;

    public TwitterTask(GeneratorTask generatorTask)
    {
        this.generatorTask = generatorTask;
    }

    @Override
    public Boolean execute()
    {
        if (numTweets == 0 && !botGenerator.getConfig().sendTweetOnStartup)
        {
            System.out.println("Waiting " + delay + "s for the next tweet");
            numTweets++;
            return true;
        }
        String result = generatorTask.execute();
        if (result == null)
        {
            System.err.println("result is null! Abort...");
            return true;
        }
        if (botGenerator.getConfig().endOfSentense != null)
            result = result + " " + botGenerator.getConfig().endOfSentense;

        System.out.println(result);

        /*try
        {
            System.out.println("Sending to Twitter...");
            manager.getTwitter().updateStatus(result);
            System.out.println("Done. Waiting " + delay
                    + "s for the next tweet");
        } catch (StackOverflowError e)
        {
            System.err.println("No more drama available! Exiting!");
            System.exit(42);
        } catch (TwitterException e)
        {
            e.printStackTrace();
        }*/
        return true;
    }

}
