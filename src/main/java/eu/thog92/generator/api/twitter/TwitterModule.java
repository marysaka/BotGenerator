package eu.thog92.generator.api.twitter;


import eu.thog92.generator.api.annotations.Module;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Module(name = "twitter", version = "1.1")
public class TwitterModule
{
    private static TwitterModule instance;

    public TwitterModule()
    {
        instance = this;
    }

    public static TwitterModule getInstance()
    {
        return instance;
    }


    public Twitter createTwitterInstance(boolean debug, String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret)
    {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(debug)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }
}
