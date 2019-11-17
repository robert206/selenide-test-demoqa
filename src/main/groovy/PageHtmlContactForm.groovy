import com.codeborne.selenide.SelenideElement
import static com.codeborne.selenide.Selectors.*
import static com.codeborne.selenide.Selenide.*

class PageHtmlContactForm {

    SelenideElement linkHtmlPage = $(byXpath("//a[@href='https://demoqa.com/html-contact-form/' and text()='HTML contact form']"))
    SelenideElement firstNameInput = $(byXpath("//input[@type='text' and @class='firstname']"))
    SelenideElement lastNameInput = $(byXpath("//input[@type='text' and @id='lname']"))
    SelenideElement countryInput = $(byXpath("//input[@type='text' and @name='country']"))
    SelenideElement commentInput = $(byXpath("//*[@id='subject' and @name='subject']"))
    SelenideElement partLink1 = $(byPartialLinkText("Link"))
    SelenideElement partLink2 = $(byPartialLinkText("here"))
    SelenideElement submitBtn = $(byXpath("*//input [@type='submit' and @value='Submit']"))
    SelenideElement afterSubmit = $(byXpath("//h1[@class='page-title' and text()='Oops! That page can’t be found.']"))


}
