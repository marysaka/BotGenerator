package eu.thog92.generator.twitter;

public class TwitterConfiguration
{
    public boolean sendTweetOnStartup;
    public String consumerKey;
    public String consumerSecret;
    public String accessToken;
    public String accessTokenSecret;
    public String endOfSentence;
    public boolean debug;


    public TwitterConfiguration()
    {

    }

    public TwitterConfiguration(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, String endOfSentence)
    {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
        this.endOfSentence = endOfSentence;
    }
}
