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


}
