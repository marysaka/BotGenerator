package eu.thog92.generator.twitter;


import eu.thog92.generator.api.Configuration;
import eu.thog92.generator.api.annotations.Module;
import eu.thog92.generator.api.annotations.SubscribeEvent;
import eu.thog92.generator.api.events.InitEvent;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Module(name = "twitter", version = "1.0")
public class TwitterModule
{
    private static TwitterModule instance;
    private Twitter twitter;

    public TwitterModule()
    {
        instance = this;
    }

    @SubscribeEvent
    public void init(InitEvent event)
    {
        Configuration configuration = new Configuration(event.getConfigDir(), "twitter");
        TwitterConfiguration config = configuration.readFromFile(TwitterConfiguration.class);
        if(config == null)
        {
            System.err.println("A new twitter config have been created! Complete it before restart.");
            configuration.saveToDisk(new TwitterConfiguration("", "", "", "", ""));
            System.exit(666);
        }



        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(config.debug)
                .setOAuthConsumerKey(config.consumerKey)
                .setOAuthConsumerSecret(config.consumerSecret)
                .setOAuthAccessToken(config.accessToken)
                .setOAuthAccessTokenSecret(config.accessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        this.twitter = tf.getInstance();
    }


    public static TwitterModule getInstance()
    {
        return instance;
    }

    public Twitter getTwitter()
    {
        return twitter;
    }
}
