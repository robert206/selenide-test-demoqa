import org.apache.commons.mail.*;




class SelenideUtils {

// reads config.file parameters into class To
    def readXmlConfig (String fileName,String envName) {
        // conf.data is parsed from xml and stored in Class
        ConfigurationSelenide reqCfg = new ConfigurationSelenide()
        ClassLoader classLoader = getClass().getClassLoader()
        File file = new File(classLoader.getResource(fileName).getFile())

        assert file.exists() : "Configuration file $file not found"
        assert file.canRead() : "Configuration file $file has no read permission"
        //parse Xml file and store the specified envType config into class structure

        def config = new XmlSlurper().parse(file)
        for(def i=0;i<config.env.size();i++) {
            if (config.env[i].@name == envName) {
                reqCfg.setUrl((String)config.env[i].url)
                reqCfg.setBrowser((String)config.env[i].browser)
                reqCfg.setBrowserRes((String)config.env[i].browserRes)
            }
        }
        return reqCfg
    }





    def sendReportToEmail () {
        // Create the attachment
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath("/build/spock-reports/SelenideTests.html");
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("Picture of John");
        attachment.setName("SelenideHtml");

        // Create the email message
        MultiPartEmail email = new MultiPartEmail();
        email.setAuthenticator(new DefaultAuthenticator("robert.leskovsek@gmail.com","navigate206"))
        email.setSmtpPort(465)
        email.setSSLOnConnect(true)

        email.setHostName("smtp.gmail.com");
        email.addTo("robert.leskovsek@gmail.com", "Robert Leskovsek");
        email.setFrom("robert.leskovsek@gmail.com", "Me");
        email.setSubject("The picture");
        email.setMsg("Here is the picture you wanted");

        // add the attachment
        email.attach(attachment);

        // send the email
        email.send();
    }
}
