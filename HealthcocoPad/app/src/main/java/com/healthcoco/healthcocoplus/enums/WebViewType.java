package com.healthcoco.healthcocoplus.enums;

/**
 * Created by Shreshtha on 23-01-2017.
 */
public enum WebViewType {
    TERMS_OF_SERVICE("https://healthcoco.com/mobile_terms-of-use"),
    PRIVACY_POLICY("https://healthcoco.com/mobile_privacy-policy");
    private final String webLink;

    WebViewType(String webLink) {
        this.webLink = webLink;
    }

    public String getWebLink() {
        return webLink;
    }
}
