class ConfigurationSelenide {

    // environment name
    String env
    String getEnv() {
        return env
    }

    void setEnv() {
        this.env = env
    }

    //test site url
    String url
    String getUrl() {
        return url
    }
    void setUrl(String url) {
        this.url = url
    }

    // Browsers
    String browser
    String getBrowser() {
        return browser
    }

    void setBrowser (String browser) {
        this.browser = browser
    }

    // browser Resolution
    String browserRes
    String getBrowserRes () {
        return browserRes
    }
    void setBrowserRes(String browserRes) {
        this.browserRes = browserRes
    }


}
