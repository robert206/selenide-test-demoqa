import com.codeborne.selenide.SelenideElement
import static com.codeborne.selenide.Selectors.*
import static com.codeborne.selenide.Selenide.*

//import static com.codeborne.selenide.Selectors.byXpath
//import static com.codeborne.selenide.Selenide.*

class PageInteractions {

    SelenideElement interLink = $(byXpath("//li[@class='menu-item'][2]"))
    SelenideElement interLabel = $(byXpath("//h1[@class='entry-title']"))

//input[@type='text' and @class='firstname']

}
