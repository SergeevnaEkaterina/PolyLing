package pages;

import io.qameta.allure.Step;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = false)
@Data
public class MainPage extends AbstractPage {
    private List<String> logsText = new ArrayList<>();
    @FindBy(id = "message")
    private WebElement inputField;

    @FindBy(xpath = "//*[@name=\"link\"]")
    private WebElement inputLinkField;

    @FindBy(xpath = "//button[contains(text(),'Анализировать')]")
    private WebElement runAnalyseButton;

    @FindBy(xpath = "//label[contains(text(),'Анализ docx-файла')]")
    private WebElement docSwitcher;

    @FindBy(xpath = "//b[contains(text(),'Выбрать')]")
    private WebElement chooseFileButton;

//    @FindBy(id = "native")
//   // @FindBy(xpath = "//*[@id=\"app\"]/div/main/div/div/div[3]/div/div[2]/div[1]/div[2]/label")
//    private WebElement isLanguageNativeSwitcher;

    @FindBy(css = "div.analysisText-style__input_container--TgiHQ label")
    private List<WebElement> isLanguageForeignSwitcher;

    @FindBy(id = "text")
    private WebElement mainHeader;

    @FindBy(css = "div progress")
    private WebElement scoreProgressBar;

    @FindBy(css = "div:nth-child(2) > div  input[type=checkbox]")
    private WebElement visualPerceptionSwitcher;

    @FindBy(css = "div.resultBlock-style__grade__result_block--xfaHf em")
    private WebElement scoreDescription;

    @FindBy(xpath = "//h3[contains(text(),'Оценка')]/parent::div/following-sibling::div/h3")
    private WebElement score;

    @FindBy(xpath = "//h3[contains(text(),'Оценка')]/parent::div/parent::div/following-sibling::div/p")
    private List<WebElement> scoreAttributes;

    @FindBy(css = "div  div > div h1")
    private WebElement header;

    @FindBy(css = "placeholder")
    private WebElement placeholder;

    @FindBy(css = "div p")
    private WebElement counter;

    @FindBy(id = "link")
    private List<WebElement> linkInput;

    @FindBy(css = "div.visualAnalysis-style__visual__choose--bRp36 label")
    private List<WebElement> analyseByFileLabel;

    @FindBy(css = "div.main-style__layout__burger--gH2oQ input")
    private WebElement burgerButton;

    @FindBy(css = "div.main-style__layout__navbar--UPRyF.main-style__layout__open--beJZr div div a")
    private List<WebElement> informationHeaders;


    @Step("Input text {text} to analyse into message area")
    public MainPage inputTextToAnalysisArea(String inputText) {
        inputField.clear();
        inputField.sendKeys(inputText);
        return this;
    }


    public String getHeader() {
        return header.getText();
    }

    public String getCounter() {
        return counter.getText();
    }

    public String getPlaceholder() {
        return placeholder.getText();
    }

    @Step("Input link {link} to analyse into link area")
    public MainPage inputLinkToAnalysisArea(String inputLink) {
        WebElement l = webDriver.findElement(By.id("link"));
        System.out.println("find by");
        // Javascript executor
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);", l);
        System.out.println("scrolled");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        inputLinkField.sendKeys(inputLink);
        return this;
    }

    @Step("Press button with text {text} to run analyse")
    public MainPage runTextAnalyse() throws InterruptedException {
        WebElement l = webDriver.findElement(By.id("foreign"));
        System.out.println("find by");
        // Javascript executor
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);", l);
        System.out.println("scrolled");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        runAnalyseButton.click();
        return this;
    }


    @Step("Switch language nativity")
    public MainPage switchLanguageNativity(String mode) {
        if(Objects.equals(mode, "foreign")) {
            WebElement l = webDriver.findElement(By.id("foreign"));
            System.out.println("find by");
            // Javascript executor
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", l);
            System.out.println("scrolled");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            isLanguageForeignSwitcher.get(1).click();
        } else   if(Objects.equals(mode, "native")) {
            WebElement l = webDriver.findElement(By.id("native"));
            System.out.println("find by");
            // Javascript executor
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", l);
            System.out.println("scrolled");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            isLanguageForeignSwitcher.get(0).click();
        }
        return this;
    }

    public void open(String url) {
        webDriver.navigate().to(url);
    }

    public String getScoreWithAttributes() {
        String result;
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(120L));
        wait.until(ExpectedConditions.visibilityOf(score));
        result = score.getText();
//        scoreAttributes.forEach(e->result.add(e.getText()));
        return result;
    }

    public String getScoreDescription() {

        return scoreDescription.getText();
    }

    public String getProgressBarColor() {
        String color = scoreProgressBar.getAttribute("class");
        String result = "";
        if(color.contains("two_class")) {
           result = "yellow";
        } else if(color.contains("five_class")) {
            result = "green";
        } else if(color.contains("four_class")) {
            result = "orange";
        }else if(color.contains("one_class")) {
            result = "red";
        }else if(color.contains("three_class")) {
            result = "redOrange";
        }
        return result;
    }

    public MainPage switchToVisualOrTextPerception() {
            WebElement l = webDriver.findElement(By.id("native"));
            System.out.println("find by");
            // Javascript executor
            ((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);", l);
            System.out.println("scrolled");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            visualPerceptionSwitcher.click();
            return this;
        }

    public MainPage switchToDocInput() {
        // Javascript executor
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);", docSwitcher);
        System.out.println("scrolled");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        docSwitcher.click();
        return this;
    }

//    @FindBy(css = ".checkbox-row .label-radio")
//    private List<WebElement> radio;
//    @FindBy(css = ".uui-form-element")
//    private List<WebElement> dropdownList;
//    @FindBy(css = ".uui-form-element > option")
//    private List<WebElement> dropdownElement;
//    @FindBy(css = ".info-panel-body-log .panel-body-list > li")
//    private List<WebElement> logs;
//
    public MainPage(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);

    }
//
//    @Step("Select checkboxes with indexes {index}")
//    public MainPage selectCheckBoxes(int index) {
//        checkBoxes.get(index).click();
//        return this;
//    }
//
//    @Step("Select radio with index {index}")
//    public MainPage selectRadio(int index) {
//        radio.get(index).click();
//        return this;
//    }
//
//    @Step("Select dropdown with index {outerIndex}, {innerIndex}")
//    public MainPage selectDropDown(int outerIndex, int innerIndex) {
//        dropdownList.get(outerIndex).click();
//        dropdownElement.get(innerIndex).click();
//        return this;
//    }
//
//    @Step("Get logs text")
//    public MainPage collectLogs() {
//        for (WebElement element : logs) {
//            System.out.println(element.getText());
//            logsText.add(element.getText().substring(9));
//        }
//        return this;
//    }
//
//    public boolean isCheckBoxSelected(int index) {
//        return getCheckBoxes().get(index).isSelected();
//    }
//
//    public boolean isRadioEnabled(int index) {
//        return getRadio().get(index).isEnabled();
//    }
//
//    public boolean isDropDownSelected(int index) {
//        return getDropdownElement().get(index).isSelected();
//    }
}
