package eu.thog92.generator.api.twitter;


import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterModule
{
    private static TwitterModule instance = new TwitterModule();

    private TwitterModule() {}

    public static TwitterModule getInstance()
    {
        return instance;
    }


    public Object createTwitterInstance(boolean debug, String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret)
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
