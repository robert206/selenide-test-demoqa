import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.SelenideElement
import static com.codeborne.selenide.Selectors.*
import static com.codeborne.selenide.Selenide.*




class TooltipsAndClicks {

    SelenideElement tooltipsLink = $(byXpath("//a[@title='Tooltip and Double click' and @href='https://demoqa.com/tooltip-and-double-click/']")) //link that open tooltips page
    SelenideElement doubleClick = $(byAttribute("id","doubleClickBtn"))

    // right click
    SelenideElement rightClickBtn = $(byXpath("//button[@id='rightClickBtn']"))
    ElementsCollection rightClickItems = $$(byXpath("//div[@id='rightclickItem']/div")) //all elements in right click menu

    //mouse-over tooltip
    SelenideElement toolTipBtn = $(byXpath("//div[@class='tooltip' and @id='tooltipDemo']"))
    SelenideElement toolTipText = $(byXpath("*//span[@class='tooltiptext']"))
}
