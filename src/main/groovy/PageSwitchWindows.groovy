import com.codeborne.selenide.SelenideElement
import static com.codeborne.selenide.Selectors.*
import static com.codeborne.selenide.Selenide.*

class PageSwitchWindows {

    //new browser window
    SelenideElement pageLink = $(byLinkText("Automation Practice Switch Windows"))
    SelenideElement newBrowserWnd = $(byXpath("//button[@id='button1' and @onclick='newBrwWin()']"))



    // open new browser window by clicking on button
    def openNewBrowserWindow () {
        newBrowserWnd.click()
        switchTo().window(1) // switch to new window by index and check title
        def newWndTitle = title()
        assert newWndTitle.contains("Free QA Automation") : "New window is not opened"

        sleep(1000) //just for demo
        switchTo().defaultContent() // switch back to main window
        sleep(1000)
    }

    // new message window
    SelenideElement newMsgWnd = $(byText("New Message Window"))

    def openNewMsgWnd() {
        newMsgWnd.click() //open new msg window
        //switch to newly opened window by index
        switchTo().window(1)
        //switchTo().window(1).close() //close extra window
    }


}
