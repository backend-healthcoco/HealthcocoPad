package com.healthcoco.healthcocopad.utilities;

/**
 * Created by Shreshtha on 20-01-2017.
 */
public enum BuildType {
    DEV("http://52.66.106.88/dpdocter/api/v1/", "healthcoco@16:1GWLF9ORMK9B8QFV", true, true),
    QA("https://app.health3.in/dpdocter/api/v1/", "healthcoco@16:1GWLF9ORMK9B8QFV", false, true),
    PROD("https://api.healthcoco.com/dpdocter/api/v1/", "hlthco@48#$:03USULTRS0MR7362P9F5", false, false);

    private final String OAUTH_KEY = "Authorization";
    private final boolean loggingEnabled;
    private final boolean preFilledForm;
    private final String serverUrl;
    private final String OAuthValue;

    BuildType(String serverUrl, String OAuthValue, boolean preFilledForm, boolean loggingEnabled) {
        this.serverUrl = serverUrl;
        this.OAuthValue = OAuthValue;
        this.preFilledForm = preFilledForm;
        this.loggingEnabled = loggingEnabled;
    }

    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    public String getOAUTH_KEY() {
        return OAUTH_KEY;
    }

    public boolean isPreFilledForm() {
        return preFilledForm;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getOAuthValue() {
        return OAuthValue;
    }
}