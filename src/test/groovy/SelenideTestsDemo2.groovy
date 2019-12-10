import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Configuration
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.openqa.selenium.Keys
import spock.lang.Specification
import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.SelenideElement

import java.awt.Robot

import static com.codeborne.selenide.Condition.*
import static com.codeborne.selenide.Selectors.*
import static com.codeborne.selenide.Selenide.*
import static com.codeborne.selenide.WebDriverRunner.*



class SelenideTestsDemo2 extends Specification {

    def ENV_NAME = "Test02"
    def utils = new SelenideUtils()
    def cfg = utils.readXmlConfig("Config",ENV_NAME)

    def setup () {

        def configuration = new Configuration()
        configuration.browser = cfg.browser
        configuration.browserSize = cfg.browserRes
        configuration.startMaximized = false
        //System.setProperty("selenide.browser", "Chrome")
        //ChromeOptions options = new ChromeOptions()
    }


    def "Add /Remove elements" () {
        given:
            open(cfg.url)
            SelenideElement link = $(byLinkText("Add/Remove Elements"))
            link.click()
            def iterate = 5
        when: "we click Add elements btn multiple times"
            SelenideElement addElement = $(byText("Add Element"))
            iterate.times {
                addElement.click()
            }
        then: "we check if 5 elements were added "
            ElementsCollection deleteBtns = $$(byXpath("//div[@id='elements']/button"))
            assert deleteBtns.shouldBe(CollectionCondition.size(iterate)) //should have 5 delete btns
            for (btn in deleteBtns) {
                assert btn.text == "Delete" : "Incorrect btn label"
            }
        when: "we remove all Delete btns now by clicking them"
            for (btn in deleteBtns) {
                btn.click()
            }
        then: "we check if all btns were removed"
            assert $(byText("Delete")).shouldNotBe(visible)
    }



    def "Authorization using chrome trick by passing username/password in url" () {
        when:
            open("https://admin:admin@the-internet.herokuapp.com/basic_auth")// with this url directly
        then:
            def loggedInLabel = $("p").text
            assert loggedInLabel.contains("Congratulations")
    }


    def "Authorization using Selenide builtin that supports all browsers" () {
        when:
            open("http://the-internet.herokuapp.com/","/basic_auth","admin","admin")
        then:
            println "This was too easy"

    }


    def "Broken Images test by just opening each one" () {
        given:
            open(cfg.url)
            SelenideElement link = $(byPartialLinkText("Broken Images"))
            link.click()
        when: "we get all images on subpage"
            ElementsCollection images = $$(byXpath("//*[@id='content']/div/img"))
            for (SelenideElement image in images) {
                def imageLink = image.getAttribute("src")
                HttpClient client = HttpClientBuilder.create().build();
                HttpGet request = new HttpGet("$imageLink")
                HttpResponse response = client.execute(request)
                if (response.getStatusLine().getStatusCode() != 200) {
                    println "Broken image : $imageLink"
                }
            }
        then:
         println "checked for all invalid images"
    }



    def "CheckBoxes" () {
        given:
            open(cfg.url)
            SelenideElement link = $(byLinkText("Checkboxes"))
            link.click()
        when: "we check try to check first checkbox to (selected =true)"
            SelenideElement checkbox1 = $(byXpath("//*[@id='checkboxes']/input[1]"))
            if (!checkbox1.isSelected()) {
                checkbox1.setSelected(true)
            }
        then: "we check that elements was checked"
            assert checkbox1.shouldBe(selected,visible) : "Checkbox was not selected"
        when: "we try to check checkbox2 (by default its already checked"
            SelenideElement checkbox2 = $(byXpath("//*[@id='checkboxes']/input[2]"))
            if (checkbox2.isSelected()) {
                checkbox2.setSelected(false)
            }
        then: "we check that checkbox was unchecked"
            assert checkbox2.shouldNotBe(selected) : "Checkbox was not unchecked"
    }

    def "Right Click in the box which triggers Alert window" () {
        given:
            open(cfg.url)
            SelenideElement link =$(byLinkText("Context Menu"))
            link.click()
        when: "we right click in the small frame"
            SelenideElement frame = $(byXpath("//*[@id='hot-spot']"))
            frame.contextClick()
        then:
            def alertText = switchTo().alert().text
            assert alertText == "You selected a context menu" : "Error:No alert window triggered or no context click made."
            switchTo().alert().accept() //close alert by confirming it
    }


    def "Drag And drop Columns" () {
        given:
            open(cfg.url)
            SelenideElement link = $(byLinkText("Drag and Drop"))
            link.click()
        when : "We drag A to B"
            SelenideElement columnA = $(byXpath("//div[@class='column' and @id='column-a']"))
            SelenideElement columnB = $(byXpath("//div[@class='column' and @id='column-b']"))
            actions().clickAndHold(columnA).moveToElement(columnB).release().build().perform()
        then: "we check if drag and drop was ok "
            assert columnA.text == "A"
            assert columnB.text == "B"
    }


    def "Dropdown Menu" () {
        given:
            open(cfg.url)
            SelenideElement link = $(byLinkText("Dropdown"))
            link.click()
        when: "we select dropdown options and evaluate them"
            ElementsCollection dropDownOptions = $$(byXpath("//*[@id='dropdown']/option"))
            SelenideElement dropDown = $("#dropdown")
            for (int i=1;i < dropDownOptions.size();i++) {
                def option = dropDownOptions[i].text
                def selected = dropDownOptions[i].getAttribute("selected")
                dropDown.selectOption(option)
                sleep(1000)
            }
        then:
            println "We traversed options (all except first one not selectable)"
    }


    def "Dynamic controls Remove/Add" () {
        given:
            open(cfg.url)
            SelenideElement link = $(byLinkText("Dynamic Controls"))
            link.click()
        when: "we checkbox selected=true"
            SelenideElement checkbox =$(byXpath("//*[@id='checkbox']/input"))
            checkbox.setSelected(true)
        then: "is it checked?"
            assert checkbox.shouldBe(selected) : "Checkbox was not selected"
        when: "we click remove btn"
            SelenideElement addRemoveBtn = $(byXpath("//*[@id='checkbox-example']/button"))
            addRemoveBtn.click()
        then: "Btn changes to Add and label its gone appears"
            SelenideElement label = $(byXpath("//p[@id='message']"))
            assert label.text == "It's gone!"
            assert addRemoveBtn.text == "Add"
        when: "we click Add btn now"
            addRemoveBtn.click()
        then: "checkbox should be back,label changes,btn is now Remove again"
            assert label.text == "It's back!"
            assert addRemoveBtn.text == "Remove"
            SelenideElement checkboxAfter =$(byXpath("//*[@id='checkbox']"))  //element has changed in between
            assert checkboxAfter.shouldBe(visible)
    }


    def "Dynamic controls Enable/Disable" () {
        given:
            open(cfg.url)
            SelenideElement link = $(byLinkText("Dynamic Controls"))
            link.click()
        when: "we click Enable btn"
            SelenideElement enableDisableBtn = $(byXpath("//*[@id='input-example']/button"))
            enableDisableBtn.click()
        then: "text input field should be enabled and label should appear,btn label changes"
            enableDisableBtn.waitUntil(text("Disable"),5000) //implicit wait needed here as it takes more then default 4s we could also do just this ..
            assert enableDisableBtn.text == "Disable" : "Invalid state: Btn is not disabled"
            //label that appears
            SelenideElement label = $(byXpath("//p[@id='message']"))
            assert label.text == "It's enabled!" : "Btn was not enabled"

            //we check if text input field is enabled -has no attribute 'disabled'
            SelenideElement textInput = $(byXpath("//input[@type='text']"))
            assert !textInput.has(attribute("disabled"))
            textInput.val("Testing text for testing motherfuckers")
        when: "We disable input field again"
            enableDisableBtn.click()
        then: "we check if correct label appears and field is actually disabled"
            enableDisableBtn.waitUntil(text("Enable"),5000)
            assert enableDisableBtn.text == "Enable" : "Invalid btn state"
            assert label.text == "It's disabled!" : "Btn was not disabled"
    }


    def "Dynamically loaded example1" () {
        given:
            open(cfg.url)
            SelenideElement link = $(byLinkText("Dynamic Loading"))
            link.click()
            SelenideElement exampleLink = $(byLinkText("Example 1: Element on page that is hidden")).click()
        when : "we click Start btn "
            SelenideElement startBtn = $(byText("Start"))
            startBtn.click()
        //*[contains(text(),'Hello World')]
        then :"and wait for Hello world text to appear"
            SelenideElement hello = $(byXpath("//*[contains(text(),'Hello World')]"))
            hello.waitUntil(visible,5000)
    }


    def "Exit Intent -mouse out of window triggers alert" () {
        given:
            open(cfg.url)
            SelenideElement link = $(byLinkText("Exit Intent"))
            link.click()
        when: "we move mouse out of the window using Robot -Selenide/Selenium does not support moving out of browser"
            Robot robot = new Robot()
            robot.mouseMove(0,0)
        then:
           SelenideElement modalWindow = $(byXpath("//div[@class='modal-title']/h3"))
            $(byXpath("//*[contains(text(),'Close')]")).click() // we click Close
    }


    def "Selenide File Download" () {
        given:
            open(cfg.url)
            SelenideElement link = $(byLinkText("File Download"))
            link.click()
        when : "we download some files-files are by default stored in reports folder"
            File screenshot = $(byLinkText("Screenshot_2.png")).download()
            File textFile = $(byLinkText("Test.txt")).download()
        then: "we check if files were downloaded"
            assert screenshot.exists() && textFile.exists() : "File are not downloaded properly"


    }


    def "Selenide File upload" () {
        given:
            open(cfg.url)
            $(byLinkText("File Upload")).click()
            ClassLoader classLoader = getClass().getClassLoader()
            File file = new File(classLoader.getResource("FileUploadTestFile").getFile())
            println "${file.getPath()}"
            assert file.exists()
        when: "Upload file"
            SelenideElement uploadBtn = $(byXpath("//input[@id='file-submit']"))
            SelenideElement fileBrowse = $(byXpath("//*[@id='file-upload']"))
            fileBrowse.val(file.getPath())
            uploadBtn.click()
        then:
            SelenideElement confirmation = $(byXpath("//div[@class='example']/h3"))
            println "${confirmation.text}"
            assert confirmation.text.contains("File Uploaded!")
    }


    def "Forgot password" () {
        given:
            open(cfg.url)
            $(byLinkText("Forgot Password")).click()
            SelenideElement emailInput = $("#email")
            SelenideElement retrievePassword = $(byXpath("//button[@id='form_submit']"))
        when: "situation with no email entered"
            retrievePassword.click()

        then: "we land on internal server error page"
            assert $(byXpath("//*[contains(text(),'Internal Server Error')]")).shouldBe(appear)

        when: "we enter email"
            back() // browser back command
            emailInput.val("posranegate@yahoo.com")
            retrievePassword.click()

        then: "we should get notification that email was sent"
            assert $(byXpath("//*[contains(text(),'been sent')]"))
    }


    def "Form Authentication example" () {
        given:
            open(cfg.url)
            $(byLinkText("Form Authentication")).click()
            SelenideElement usernameField = $("#username")
            SelenideElement passwordField = $("#password")
            SelenideElement loginBtn = $(byXpath("//*[@id='login']/button"))
            SelenideElement notification = $(byXpath("//*[@id='flash']"))
            SelenideElement logoutBtn = $(byXpath("//a[@class='button secondary radius']"))
        when: "we enter invalid credentials"
            usernameField.val("BogusUser")
            passwordField.val("boguspassword")
            loginBtn.click()

        then: "we receive invalid email notification"
            assert notification.getAttribute("class") == "flash error" : "Login server is down or notification is missing"

        when: "we login with normal"
            refresh() // reload page
            usernameField.val("tomsmith")
            passwordField.val("SuperSecretPassword!")
            loginBtn.click()
        then:
            assert notification.getAttribute("class") == "flash success" // attribute class of notification changes his value according to valid/invalid
        when: "we click logout btn"
            logoutBtn.click()
        then: "we should be logged out"
            assert $(byXpath("//*[contains(text(),'logged out')]")) : "You were not logged out."
    }



    def "Horizontal slider" () {
        given:
            open(cfg.url)
            $(byLinkText("Horizontal Slider")).click()
            SelenideElement slider = $(byXpath("//input[@type='range']"))
            SelenideElement rangeIndicator = $("#range")
            def sliderLocation = slider.getLocation() // left upper corner click ,we do this by getting slider location

        when: "We click element and then use arrow keys to increase value up to max -5 by step 0.5"
            actions().moveToElement(slider,sliderLocation.getX(),sliderLocation.getY()).click().perform()
            def expectedValue = 0
            def actualValue = 0
            10.times {
                actualValue = rangeIndicator.text.toDouble() // we get current position from attribute in code
                assert expectedValue == actualValue
                slider.sendKeys(Keys.ARROW_RIGHT)
                expectedValue = expectedValue + 0.5
            }
        then:
            println "Sliding from Left to Right done"
        when: "we slide back from right to left"
            expectedValue = 5
            actualValue = 5
            10.times {
                actualValue = rangeIndicator.text.toDouble()
                assert expectedValue == actualValue
                slider.sendKeys(Keys.ARROW_LEFT)
                expectedValue = expectedValue - 0.5
            }
        then:
            println "Sliding from Right to Left done"
    }


    def "Hover over images" () {
        given:
            open(cfg.url)
            $(byLinkText("Hovers")).click()
            ElementsCollection images = $$(byXpath("//div[@class='figure']/img"))
            assert images.size() == 3 : "Not all images are detected"
            ElementsCollection captions = $$(byXpath("//div[@class='figcaption']/h5"))
            assert captions.size() == 3

        when: " we mouse over every single image"
            for (int i=0; i < images.size();i++) {
                images[i].hover() //mouse over image
                captions[i].waitUntil(visible,2000)
                println "${captions[i].text}"
                assert captions[i].text == "name: user" + "${i+1}" : "Caption is not correct."
            }
        then:
            println "we checked all images on mouse over"
    }


    def "Java Script different Alert types" () {
        given:
            open(cfg.url)
            $(byLinkText("JavaScript Alerts")).click()
            SelenideElement alertBtn = $(byXpath("//button[@onclick='jsAlert()']"))
            SelenideElement confirmBtn = $(byXpath("//button[@onclick='jsConfirm()']"))
            SelenideElement promptBtn = $(byXpath("//button[@onclick='jsPrompt()']"))
            SelenideElement result = $("#result")

        when: "we click Alert btn"
            alertBtn.click()
        then: "we check if alert window was opened and has correct text"
            def alertText = switchTo().alert().text
            assert alertText == "I am a JS Alert"
            switchTo().alert().accept() // confirm ok and close window
            switchTo().defaultContent() // switch back to main window
            assert result.text == "You successfuly clicked an alert"

        when: "we trigger confirm alert window"
            confirmBtn.click()
        then: "check again"
            assert switchTo().alert().text == "I am a JS Confirm"
            switchTo().alert().accept()
            switchTo().defaultContent()
            assert result.text == "You clicked: Ok"

        when: "we trigger prompt alert window"
            promptBtn.click()
            switchTo().alert().sendKeys("Robert")
        then:
            switchTo().alert().accept()
            switchTo().defaultContent()
            assert result.text == "You entered: Robert"
    }



    def "Sites that return different return code" () {
        given:
            open("http://the-internet.herokuapp.com/status_codes")
            SelenideElement link200 = $(byLinkText("200"))
            SelenideElement link301 = $(byLinkText("301"))
        when: "200"
            link200.click()
            def currentUrl = url()
        then:
            def getRequest = new URL("$currentUrl")
            def connection = getRequest.openConnection()
            assert connection.responseCode == 200
        when: "301"
            link301.click()
            currentUrl = url()
        then:
            def getRequest1 = new URL("$currentUrl")
            def connection1 = getRequest1.openConnection()
            assert connection1.responseCode == 301
    }


    def "Table 1 LastNames" () {
        given:
            open(cfg.url)
            $(byLinkText("Sortable Data Tables")).click()
            SelenideElement sortLastName = $(byXpath("//th[@class='header']/span[text()='Last Name'][1]"))
        when: "we click sort by last name and collect the whole row in Collection"
            sortLastName.click() //sorted alphabetically ascending at this point
            ElementsCollection lastNames = $$(byXpath("//*[@id='table1']/tbody/tr/td[1]"))
            def expectedList = lastNames.texts() //all names
            expectedList.sort() // we sort it alone
        then: "we compare expected list and actual retrieved"
            for (int i=0; i < lastNames.size();i++) {
                assert lastNames[i].text == expectedList[i]
            }
        when: "we click last name header again and it should be descending sorted"
            sortLastName.click()
            lastNames = $$(byXpath("//*[@id='table1']/tbody/tr/td[1]"))
            expectedList = lastNames.texts()
            expectedList.sort().reverse()
        then:
            for (int i=0; i < lastNames.size();i++) {
                assert lastNames[i].text == expectedList[i]
            }
            def listTable = []
            ElementsCollection table1 = $$(byXpath("//*[@id='table1']/tbody/tr")) //collection of all everything in table1
            def listOfUsers = table1.texts() // returns list of elements in text form so we can assign it to list ex:listOfUsers[0]-first row/user
            for (int i=0;i < listOfUsers.size();i++) {
                def tmpTable = []
                tmpTable = listOfUsers[i].tokenize(" ")
                for (int j=0; j < tmpTable.size()-2;j++) {
                    println "${tmpTable[j]}"
                    listTable.add(j,tmpTable[j])
                }
            }
        listTable.each {
            println "item = $it"
        }
        //sort last names ascending
        def lastNamesTbl = []
        for (int i=0;i < listTable.size();i=i+5) {
            lastNamesTbl.add(listTable[i])
        }
        lastNamesTbl.sort()
        lastNamesTbl.each {
            println "last = $it"
        }




    }







}