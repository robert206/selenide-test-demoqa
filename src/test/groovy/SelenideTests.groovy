import com.codeborne.selenide.Configuration
import com.codeborne.selenide.ElementsCollection
import org.openqa.selenium.By
import org.openqa.selenium.Capabilities
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import com.codeborne.selenide.SelenideElement
import org.openqa.selenium.support.ui.Select

import java.util.concurrent.locks.Condition

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
            pageContactForm.commentInput.val("Hier steht ein kommentar")
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
        then: "we check if dropBox label has changed indicating success"
            sleep(2000)
            assert $(byText("Dropped!")).shouldBe(visible) : "Element was not dropped"
    }



    def "Upload File test with Alert Window" () {
        given: "file used for test in resource dir"
            def keybrd = new KeyboardEvents()
            open(cfg.url)
            ClassLoader classLoader = getClass().getClassLoader()
            File file = new File(classLoader.getResource("FileUploadTestFile").getFile())
            assert file.exists()
        when: "we move to subpage and upload file"
            keybrd.keybrdLink.click()
            keybrd.uploadFileTest(file) // select file and submit
        then: "we expect that if file is uploaded label of select is changed "
            assert $(withText("No file chosen")).shouldNotBe(visible)
            sleep(1000)
    }



    def "Open new window and switch to it,then Close it" () {
        given:
            def ps = new PageSwitchWindows()
            open(cfg.url)
        when:
            // move to correct page and check if we are landed correctly by url check
            ps.pageLink.click()
            def subUrl = cfg.url + "automation-practice-switch-windows/"
            assert url() == subUrl : "Not landed on automation practice for windows"
            //opens new browser window and checks if it correct
            ps.openNewBrowserWindow()
        then:
            //close newly opened window-considering this test ends here its not really needed as whole driver is quit automatically anyway
            switchTo().window(1).close()
    }



    def "Open new message window" () {
        when: "we open page"
            def ps = new PageSwitchWindows()
            open(cfg.url)
        then: "we open new window by clickin on btn"
            ps.pageLink.click()
            ps.openNewMsgWnd()
    }



    def "Double click and Alert window handling" () {
        given:
            def tc = new TooltipsAndClicks()
            open(cfg.url)
        when: "we open subpage"
            tc.tooltipsLink.click()
            assert url().contains("tooltip-and-double-click") : "This is not Tooltips and Double click page "// are we on correct page
            // actual double click -easy
            tc.doubleClick.doubleClick()
        then: "We check Alert Window that pops -up"
            def alertText = switchTo().alert().getText()
            assert alertText.contains("Double Click Alert") //if specific text in alert then correct one is displayed
            switchTo().alert().accept() //we accept OK in alert ..we could also call just .dismiss
    }




    def "Right Click (context menu) with various items without checking each and one" () {
        given:
            def tc = new TooltipsAndClicks()
            open(cfg.url)
        when: "we go to subpage and click right click btn"
            tc.tooltipsLink.click()
            assert url().contains("tooltip-and-double-click") : "This is not Tooltips and Double click page "// are we on correct page
            tc.rightClickBtn.contextClick() //we click right click on btn
        then:
            // all items in menu are saved into ElementsCollection and now we traverse them
            assert tc.rightClickItems.size() == 5 : "Expected:5 options in dropdown.Actual :$tc.rightClickItems.size()"
            for ( menuItem in tc.rightClickItems) {
                println "$menuItem.text"
            }
        }


    def "Check all right click options with handling also alert window" () {
        when: "we go to subpage and right click btn"
            def tc = new TooltipsAndClicks()
            open(cfg.url)
            tc.tooltipsLink.click()
            assert url().contains("tooltip-and-double-click")

        then: "we open subpage and right click btn"
            tc.rightClickBtn.contextClick()
            assert tc.rightClickItems.size() == 5 : "Expected:5 options in dropdown.Actual :$tc.rightClickItems.size()"

            //workaround below as test site's 2nd option in context menu overlaps tooltip link
            for(int i=2;i < tc.rightClickItems.size();i++) {
                def menuItem = tc.rightClickItems[i]
                menuItem.click() //click menu item
                def alertText = switchTo().alert().getText() //this opens alert window so we get the text of it
                assert alertText.contains("You have selected") //and check it
                switchTo().alert().accept() //accept/close alert
                tc.rightClickBtn.contextClick()
            }
            //this below would actually be normal way to go to test all items in contextmenu
           /* for (menuItem in tc.rightClickItems) {
                menuItem.click() //click menu item
                def alertText = switchTo().alert().getText() //this opens alert window so we get the text of it
                assert alertText.contains("You have selected") //and check it
                switchTo().alert().accept() //accept/close alert
                tc.rightClickBtn.contextClick()
            }*/

    }


    def "Tooltip on mouse-over/hover" () {
        given:
            def tc = new TooltipsAndClicks()
            open(cfg.url)
            tc.tooltipsLink.click()
        when: "we mouse over on element that triggers tooltip"
            tc.toolTipBtn.hover() //yes its so simple and short as opposed to using pure Selenium
        then:
            def txt = tc.toolTipText.text
            assert txt.contains("statistical purposes")

    }


    def "Spinner increase value" () {
        given:
            def ps = new PageSpinner()
            open(cfg.url)
            ps.linkSpinner.click()
            def maxIterations = 10
            assert url().contains("spinner") : "Not on correct url -spinner page"

        when: "we increase value in field by clicking arrow up"
            ps.incValue(maxIterations)
        then:
            def Xpath = "*//input[@id='spinner' and @aria-valuenow='" + maxIterations + "']"
            assert $(byXpath(Xpath)).shouldBe(visible)

        when: "we decrease value again back to 0"
            ps.decValue(maxIterations)

        then: "we check again if it is back to 0"
            def XpathDec = "*//input[@id='spinner' and @aria-valuenow='0']"
            assert $(byXpath(XpathDec)).shouldBe(visible)
    }


    def "Spinner Get/Set Value" () {
        given:
            def ps = new PageSpinner()
            open(cfg.url)
            ps.linkSpinner.click()
            assert url().contains("spinner") : "Not on correct url -spinner page"

        when: "we click Set Value to 5"
            ps.setValue.click()

        then: "check if value was correctly set"
            assert $(byXpath("*//input[@id='spinner' and @aria-valuenow='5']")).shouldBe(visible)

        when: "we click Get Value"
            ps.getValue.click()

        then: "we check if alert window value is correct"
            sleep(1000)
            def alertText = switchTo().alert().text
            assert alertText == "5"
            sleep(1000)
            switchTo().alert().accept()
    }


    def "Spinner Toogle Disable/Enable" () {
        given:
            def ps = new PageSpinner()
            open(cfg.url)
            ps.linkSpinner.click()
            assert url().contains("spinner") : "Not on correct url."

        when: "we disable input field"
            ps.enableDisable.click()
            sleep(1000)
        then : "this is how disable btn affects input field"
            def disabledField = $(byXpath("*//input[@id='spinner' and @disabled]")) //this is how clicking disable affects its xpath
            //assert disabledField.shouldBe(visible)
            assert ps.spinnerField.shouldBe(disabled)

        when: "we enable field again"
            ps.enableDisable.click()
        then: "and check if it really is"
            ps.spinnerField.shouldBe(enabled)
    }



    def "Slide left to right" () {
        given:
            def ps = new PageSpinner()
            open(cfg.url)
        when:
            ps.sliderLink.click()
            assert url().contains("slider") : "Not on correct url"
            ps.slideLeftToRightOffset()
        then:
            println "this is just one of the way to move slider"
    }


    def "Traverse options Continents Dropdown By Text" () {
        given:
            open("http://toolsqa.com/automation-practice-form/")
            def ps = new PageSelectMenu()
        when: "we traverse dropdown by text and check every option is selected"
            ps.traverseDropdownByText()
        then:
            println "We traversed all option"
     }


    def "Numbers options select" () {
        given:
            open(cfg.url)
            def ps = new PageSelectMenu()
            SelenideElement numberTwo = $(byXpath("//div[@id='ui-id-2']"))
        when: "we select number 2"
            ps.selectLink.click()
            ps.number.click()
            numberTwo.waitUntil(visible,2000) //explicit wait
            numberTwo.click()
        then:
            assert $(byXpath("//span[@class='ui-selectmenu-text' and text()='2']")).exists() // if it is selected then this exists ..doesnt work with .visible,selected..?? Why ?

    }


    def "Mouse-over open menu" () {
        given:
            open("https://demoqa.com/menu/")
        when:
            SelenideElement music = $(byXpath(".//div[contains(text(),'Music')]"))
            SelenideElement rock = $(byXpath(".//div[contains(text(),'Rock')]"))
            SelenideElement classic = $(byXpath(".//div[contains(text(),'Classic')]"))
            //music.hover()
            actions().moveToElement(music).perform()
            rock.waitUntil(visible,5000)
            actions().moveToElement(rock).perform()
            classic.waitUntil(appears,2000).click()
        then:
            println "We selected Classical Rock"
    }





















}