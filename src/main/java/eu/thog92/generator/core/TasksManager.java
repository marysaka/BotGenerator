package eu.thog92.generator.core;

import eu.thog92.generator.api.tasks.ITaskManager;
import eu.thog92.generator.api.tasks.ScheduledTask;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TasksManager implements ITaskManager
{

    private Config config;
    private Twitter twitter;
    private Dictionary dictionary;
    private ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(4);

    private HashMap<ScheduledTask, ScheduledFuture<?>> activeTasks = new HashMap<ScheduledTask, ScheduledFuture<?>>();


    public TasksManager() throws IOException
    {
        this.dictionary = Dictionary.getInstance();
        this.dictionary.setDir(new File("data"));
        this.dictionary.loadCombinations();
        this.dictionary.loadBlackList();
    }

    public void setConfig(Config config)
    {
        this.config = config;
        try
        {
            this.loadTwitterCredentials();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void loadTwitterCredentials() throws IOException
    {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(config.debugTwitter)
                .setOAuthConsumerKey(config.consumerKey)
                .setOAuthConsumerSecret(config.consumerSecret)
                .setOAuthAccessToken(config.accessToken)
                .setOAuthAccessTokenSecret(config.accessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        this.twitter = tf.getInstance();
    }

    public void reload() throws IOException
    {
        System.out.println("Reloading Config...");
        this.loadTwitterCredentials();
        System.out.println("Reloading Dictionary...");
        this.dictionary.reload();
        System.out.println("Config Reloaded");
    }

    public void scheduleTask(ScheduledTask task)
    {
        System.out.println("Scheduling " + task.getName() + "...");
        this.activeTasks.put(task, scheduler.scheduleAtFixedRate(task, 0,
                task.getDelay(), TimeUnit.SECONDS));
    }

    public void resetExecutorService()
    {
        scheduler.shutdownNow();
        scheduler = Executors.newScheduledThreadPool(100);
    }

    public void onFinishTask(ScheduledTask task)
    {
        if (task.isCancelled())
        {
            if (this.activeTasks.get(task) != null)
            {
                this.activeTasks.remove(task).cancel(true);
            }
        }
    }

    public Twitter getTwitter()
    {
        return twitter;
    }

    public Config getConfig()
    {
        return config;
    }
}
