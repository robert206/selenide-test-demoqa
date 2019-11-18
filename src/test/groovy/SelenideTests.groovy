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
import static com.codeborne.selenide.Selectors.*
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

    def "Interactions -Sortable-retrieve all elements" () {
        given :
            def pageInter = new PageInteractions()
            open(cfg.url)
        when: "we click Sortable link"
            pageInter.sortableLink.click()
            pageInter.printSortableElements()
        then:
            println "shit"

    }

    def "Interactions -Selectable" () {
        given:
            def pageInter = new PageInteractions()
            open(cfg.url)
        when:
            pageInter.selectableLink.click()
            //get all elements in collection and traverse them by clicking each one from top to bottom
            pageInter.traverseSelectValueCheck()
        then:
            println "All elements were traversed"
    }

// resizable
    def "Interactions-Resizable" () {
        given:
            def pageInter = new PageInteractions()
            open(cfg.url)
            def offsetX = 300
            def offsetY = 300
            def xpathString = "//*[@id='resizable' and @class='ui-widget-content ui-resizable' and @style='width: " + offsetX +"px; height: " + offsetY + "px;']"
            println "$xpathString"
        when: "we open page with resizable window"
            pageInter.resLink.click()
            pageInter.resizeWindow(offsetX,offsetY)
        then: "we check if window was actualy resized by finding changed element"
            SelenideElement expectedWindow = $(byXpath(xpathString))
            assert expectedWindow.shouldBe(visible) : "Window was not resized correctly"
    }


    def "Interactions - Drag and Drop into another element" () {
        given: "initial page"
            def pageInter = new PageInteractions()
            open(cfg.url)
        when: "we click link to go to subpage and drag and drop element into his position"
            pageInter.dropLink.click()
            //verify we are at page
            assert pageInter.dragMe.shouldBe(visible)
            assert pageInter.dropHere.shouldBe(visible)
            actions().dragAndDropBy(pageInter.dragMe,120,30).perform()
        then:
            sleep(2000)
            assert $(byText("Dropped!")).shouldBe(visible) : "Element was not dropped"
    }



    def "Upload File test with Alert Window" () {
        given:
            def keybrd = new KeyboardEvents()
            open(cfg.url)
            ClassLoader classLoader = getClass().getClassLoader()
            File file = new File(classLoader.getResource("FileUploadTestFile").getFile())
            assert file.exists()
        when:
            keybrd.keybrdLink.click()
            keybrd.uploadFileTest(file) // select file and submit
        then:
            assert $(withText("No file chosen")).shouldNotBe(visible)
            sleep(1000)
    }












}