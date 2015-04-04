package eu.thog92.generator.twitter;

import eu.thog92.generator.api.tasks.GeneratorTask;
import eu.thog92.generator.api.tasks.ScheduledTask;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterTask extends ScheduledTask
{

    private final boolean sendTweetOnStartup;
    private final String endOfSentence;
    private final GeneratorTask generatorTask;
    private final Twitter twitter;
    private int numTweets;

    public TwitterTask(Twitter twitter, GeneratorTask generatorTask, boolean sendTweetOnStartup, String endOfSentence)
    {
        this.twitter = twitter;
        this.generatorTask = generatorTask;
        this.sendTweetOnStartup = sendTweetOnStartup;
        this.endOfSentence = endOfSentence;
    }

    @Override
    public Boolean execute()
    {
        if (numTweets == 0 && !sendTweetOnStartup)
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
        if (endOfSentence != null)
            result = result + " " + endOfSentence;

        System.out.println(result);

        try
        {
            System.out.println("Sending to Twitter...");
            twitter.updateStatus(result);
            System.out.println("Done. Waiting " + delay
                    + "s for the next tweet");
        } catch (StackOverflowError e)
        {
            System.err.println("No more drama sentences available! Stopping task!");
            return false;
        } catch (TwitterException e)
        {
            e.printStackTrace();
        }
        return true;
    }

}
