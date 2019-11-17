import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.Selenide
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
    ElementsCollection selectableList = $$(byXpath("//ol[@id='selectable' and @class='ui-selectable']"))

    def traverseSelect () {
        for (SelenideElement item in selectableList) {
            item.click()
            assert item.shouldBe(selected) : "Failed at : $item.text"
        }
    }



}
