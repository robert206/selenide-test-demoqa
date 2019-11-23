import com.codeborne.selenide.Condition
import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.SelenideElement
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.Select

import static com.codeborne.selenide.Selectors.*
import static com.codeborne.selenide.Selenide.*
import static com.codeborne.selenide.Selenide.actions


class PageSelectMenu {

    ElementsCollection continents = $$(byXpath("//select[@id='continents']/option"))//this is where we store all available options
    SelenideElement cont = $("#continents")


    SelenideElement selectLink = $(byXpath("//a[@href='https://demoqa.com/selectmenu/']"))
    ElementsCollection numbers = $$(byXpath("//select[@id='number']/option")) //we store all options
    SelenideElement number = $("#number-button")




    def traverseDropdownByText() {
        for (int i = 0; i < continents.size(); i++) {
            def currChoice = continents[i].text
            cont.selectOption(currChoice)//we set dropdown value here
            cont.shouldHave(Condition.text("$currChoice")) //check if value with text was selected
        }
    }


}