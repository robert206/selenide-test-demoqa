import com.codeborne.selenide.Condition
import com.codeborne.selenide.SelenideElement
import org.openqa.selenium.By


import static com.codeborne.selenide.Selectors.byText
import static com.codeborne.selenide.Selectors.byXpath
import static com.codeborne.selenide.Selenide.*
//import org.openqa.selenium.By

class PageHome {

    SelenideElement homeBtn = $(By.xpath("//a[@title='Home']"))
    SelenideElement homeTitle = $(By.xpath("//h1[@class='entry-title']"))

    SelenideElement inter = $(byText("Interactions"))
    SelenideElement interLink = $(byXpath("//li[@class='menu-item'][2]"))

    def goHomePage () {
        homeBtn.click()
        assert homeTitle.shouldBe(Condition.visible)
    }


    def clickInteractions () {
        interLink.click()
    }
}
