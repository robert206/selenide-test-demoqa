import com.codeborne.selenide.Condition
import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.SelenideElement

import static com.codeborne.selenide.Selectors.byId
import static com.codeborne.selenide.Selectors.byLinkText
import static com.codeborne.selenide.Selectors.byPartialLinkText
import static com.codeborne.selenide.Selectors.byText
import static com.codeborne.selenide.Selectors.byXpath
import static com.codeborne.selenide.Selenide.$
import static com.codeborne.selenide.Selenide.$$



class PageRadioCheckBox {


    SelenideElement radioCheckBoxLink = $(byLinkText("Checkboxradio"))

    //radio btns
    SelenideElement radio1 = $(byId("radio-1"))
    SelenideElement radio2 = $(byText("Paris"))
    SelenideElement radio3 = $(byText("London"))

    ElementsCollection checkBoxes = $$(byXpath("//input[@type='checkbox']"))

}
