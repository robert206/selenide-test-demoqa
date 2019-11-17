import com.codeborne.selenide.Configuration
import org.openqa.selenium.By
import org.openqa.selenium.Capabilities
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import com.codeborne.selenide.SelenideElement
import static com.codeborne.selenide.Selenide.*
import static com.codeborne.selenide.Condition.*
import static com.codeborne.selenide.WebDriverRunner.*

import com.codeborne.selenide.Selectors.ByText

import static com.codeborne.selenide.Condition.*
import spock.lang.Shared
import spock.lang.Specification


class SelenideTests extends Specification {

def ENV_NAME = "Test01"
def utils = new SelenideUtils()
def cfg = utils.readXmlConfig("Config",ENV_NAME)


    def setup () {

        def configuration = new Configuration()
        configuration.browser = cfg.browser
        configuration.browserSize = cfg.browserRes
        configuration.startMaximized = false
        //System.setProperty("selenide.browser", "Chrome")
        //ChromeOptions options = new ChromeOptions()

    }


    def "Home_page" () {
        given: "home page address"
            open("https://demoqa.com/")
        when: "traaa"
            SelenideElement title = $(By.xpath("//img[@class='preload-me lazyloading']"))
        then: "tralala"
        assert title.shouldBe(visible) : "Naslov se vidi"
    }


    def "Go_home" () {
        given: "home page"
           open("https://demoqa.com/")
        when: "we click home btn"
            def homePage = new PageHome()
            homePage.goHomePage()
        then:
         println "tralalala"

    }

    def "Check Home page labels and links" () {

        given: "page"
            open("https://demoqa.com/")
        when: "we have home page displayed"
            def homePage = new PageHome()
            homePage.clickInteractions()
        then:
            assert homePage.inter.text() == "Interactions"
    }


    def "Go to Interactions and check links" () {
        given: "page"
            open("$cfg.url")
        when:
            def interPage = new PageInteractions()
            interPage.interLink.click()
        then:
            assert interPage.interLabel.shouldBe(visible)
    }


    def "Html Page Submit Form" () {

        given: "open main page"
            def pageContactForm = new PageHtmlContactForm()
            open("$cfg.url")

        when: "we click html contact form link "
            pageContactForm.linkHtmlPage.click()

        then: "Form is opened"
            assert pageContactForm.firstNameInput.shouldBe(visible)

        when: "we input all required fields"
            pageContactForm.firstNameInput.val("Robert")
            pageContactForm.lastNameInput.val("Leskovsek")
            pageContactForm.countryInput.val("Slovenia")
            pageContactForm.commentInput.val("hier steht ein kommentar")
            pageContactForm.submitBtn.click()
        then: "we check if information was submitted"
            pageContactForm.afterSubmit.shouldBe(visible)

    }

    def "Html Page Submit form Links" () {
        given:
            def pageContactForm = new PageHtmlContactForm()
            open(cfg.url)
        when: "we go to contact form"
            pageContactForm.linkHtmlPage.click()
            pageContactForm.partLink1.click()
        then: "we should land at google site"
            assert url() == "https://www.google.com/?gws_rd=ssl" : "Not landed at google site"
            assert title() == "Google" : "Not landed at Google site"

    }


}