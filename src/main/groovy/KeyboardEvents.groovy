import com.codeborne.selenide.SelenideElement
import static com.codeborne.selenide.Selectors.byLinkText
import static com.codeborne.selenide.Selectors.byTitle
import static com.codeborne.selenide.Selenide.$
import static com.codeborne.selenide.Selenide.sleep

class KeyboardEvents {

    SelenideElement keybrdLink =$(byLinkText("Keyboard Events"))
    //file Upload functionality
    SelenideElement browseBtn = $(byTitle("Click here to browse"))
    SelenideElement uploadBtn = $("#uploadButton") // css Selector by Id


    def uploadFileTest(File file) {
        browseBtn.uploadFile(file)
        sleep(1000)
        uploadBtn.click()
    }


}
