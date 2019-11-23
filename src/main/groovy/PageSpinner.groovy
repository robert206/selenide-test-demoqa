import com.codeborne.selenide.SelenideElement
import static com.codeborne.selenide.Condition.visible
import static com.codeborne.selenide.Selectors.*
import static com.codeborne.selenide.Selenide.$
import static com.codeborne.selenide.Selenide.actions


class PageSpinner {


    SelenideElement linkSpinner = $(byXpath("//a[@href='https://demoqa.com/spinner/']"))

    SelenideElement spinnerField =$(byXpath("//input[@id='spinner' and @name='value']")) //inputField
    SelenideElement incValue = $(byXpath("*//span[@class='ui-button-icon ui-icon ui-icon-triangle-1-n']")) //arrow inc
    SelenideElement decValue = $(byXpath("//span[@class='ui-button-icon ui-icon ui-icon-triangle-1-s']")) // arrow for decrease
    SelenideElement enableDisable = $(byText("Toggle disable/enable"))
    SelenideElement toggleWidget = $(byId("destroy"))
    SelenideElement getValue = $(byText("Get value"))
    SelenideElement setValue = $(byId("setvalue"))

    // Slider test
    SelenideElement sliderLink  = $(byLinkText("Slider"))
    SelenideElement sliderBtn = $(byXpath("//div[@id='slider']/span[@tabindex='0']")) // btn we hold to slide across the bar
    SelenideElement sliderBar = $("#slider")

    // clicking arrows to increase or decrease
    def incValue (max) {
       if (incValue.shouldBe(visible)) {
           max.times {
               incValue.click()
           }
       }
    }
    def decValue (i) {
        if (decValue.shouldBe(visible)) {
            i.times {
                decValue.click()
            }
        }
    }

    //slide using moveByOffset
    def slideLeftToRightOffset () {
        //we click and hold first
        actions().clickAndHold(sliderBtn).perform()
        //def width = sliderBar.getSize().getWidth() //this is how you retrieve element width
        for (def i=0; i < 5; i++) {
            actions().moveByOffset(50,0).perform()
        }
        actions().release()
    }
}
