import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.SelenideElement
import static com.codeborne.selenide.Selectors.*
import static com.codeborne.selenide.Selenide.*
import static com.codeborne.selenide.Condition.*


class PageInteractions {

    SelenideElement interLink = $(byXpath("//li[@class='menu-item'][2]"))
    SelenideElement interLabel = $(byXpath("//h1[@class='entry-title']"))

    // Sortable
    SelenideElement sortableLink = $(byXpath("//a[@href='https://demoqa.com/sortable/' and text()='Sortable']"))
    ElementsCollection sortableElements = $$(byXpath("//ul[@id='sortable' and @class='ui-sortable']/li"))

    def printSortableElements () {
        for (item in sortableElements) {
            println "$item.text"
        }
    }

    //Selectable
    SelenideElement selectableLink = $(byXpath("//a[@href='https://demoqa.com/selectable/' and text()='Selectable']"))
    ElementsCollection selectableList = $$(byXpath("//ol[@id='selectable' and @class='ui-selectable']/li"))


    def traverseSelectValueCheck () {
        for (int i=0;i < selectableList.size();i++) {
            selectableList[i].click()
            def value = selectableList[i].text
            def expectedValue = "Item " + "${i+1}"
            println "Expected:$expectedValue Actual:$value"
            assert value == expectedValue : "Expected:$expectedValue Actual:$value"
        }
    }
    def traverseSelect () {
        for (SelenideElement item in selectableList) {
            item.click()
            item.text
            sleep(1000)
            assert item.shouldBe(selected) : "Failed at : $item.text"
        }
    }


    //Resizable window
    SelenideElement resLink = $(byLinkText("Resizable"))

    SelenideElement resizeDrag = $(byXpath("//div[@class='ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se']")) // resize icon pull
    SelenideElement resWindow = $(byXpath("//*[@id='resizable' and @class='ui-widget-content ui-resizable']"))

    def resizeWindow(int xOffset, int yOffset) {
        actions().dragAndDropBy(resizeDrag,xOffset,yOffset).perform()
        sleep (1000)
    }


    // Dropable elements
    SelenideElement dropLink = $(byXpath("//a[@href='https://demoqa.com/droppable/' and text()='Droppable']"))
    SelenideElement dragMe = $(byText("Drag me to my target"))
    SelenideElement dropHere = $(byText("Drop here"))





}
