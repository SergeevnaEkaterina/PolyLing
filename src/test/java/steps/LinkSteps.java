package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import model.request.LinkBody;
import model.response.AnalyseByLinkResponse;
import model.response.Comment;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static api.CommonService.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Getter
@Setter
public class LinkSteps {

    private AnalyseByLinkResponse linkResponse;
    public static final URI VISUAL_URL = URI.create("http://localhost:3000/evaluation/linkVisual");
    static Comment serif = new Comment("serif_font", false, "Шрифты с засечками", "Шрифты с засечками (serif fonts) читаются легче, поэтому лучше использовать их");
    static Comment useble = new Comment("useble_list", false, "Использование списков", "Вертикальные списки в тексте повышают воспринимаемость");
    static Comment paragraph = new Comment("paragraph_len", false, "Размер абзаца", "Размер абзаца не должен превышать 750 символов");
    static Comment spacing = new Comment("line_spacing", false, "Межстрочный интервал", "Межстрочный интервал должен быть в диапазоне от 120 % до 180% от размера кегля использованного шрифта");
    static Comment string = new Comment("string_len", false, "Длина строки", "Длина строки не должна превышать 65-75 символов и должна занимать не менее 30% и не более 80% страницы");
    static Comment caps = new Comment("not_caps", false, "Использование капслока", "Капслок должен использоваться только для аббревиатур");
    static Comment bold = new Comment("bold_count", false, "Полужирное начертание", "Объем полужирных начертаний не должен превышать 15%");
    static Comment img = new Comment("img_tree", false, "Расположение изображений", "Изображения должны находиться между блоками текста. При этом желательно, чтобы между блоками было не более одного изображения");
    static Comment font = new Comment("font_set", false, "Набор шрифтов", "Не стоит использовать более двух типов шрифтов");
    static Comment italic = new Comment("italic_count", false, "Курсивные начертания", "Объем курсивных начертаний не должен превышать 15%");
    static Comment size = new Comment("font_size", false, "Размер шрифта", "Размер шрифта должен быть не менее 12 и не более 14");


    public static final List<Comment> SCORE_ATTRIBUTES = Arrays.asList(serif, useble, paragraph, spacing, string, caps, bold, img, font, italic, size);

    @Given("Upload text to analyse by link {string} via API")
    @Step("Upload text to analyse by link {string} via API")
    public void getResultAnalyseByLink(String link) {
        LinkBody body = new LinkBody(link);
        CommonApiBuilder api = commonApiBuilder()
                .setMethod(Method.POST)
                .setBody(body);
        api.setReqHeader("Content-Type", "application/json");
        api.setReqHeader("Host", "localhost:3000");
        api.setReqHeader("Accept", "application/json");
        api.setReqHeader("Accept-Encoding", "gzip, deflate, br");
        api.setReqHeader("Connection", "keep-alive");
       setLinkResponse(extractAnalyseBodyFromJson(sendRequestAndGetResponse(api, okResponseSpecification(), VISUAL_URL, false)));

    }
    @Then("Upload text by not enabled link {string} via API and should get error")
    @Step("Upload text by not enabled link {string} via API and should get error")

    public void getErrorAnalyseByLink(String link) {
        LinkBody body = new LinkBody(link);
        CommonApiBuilder api = commonApiBuilder()
                .setMethod(Method.POST)
                .setBody(body);
        api.setReqHeader("Content-Type", "application/json");
        api.setReqHeader("Host", "localhost:3000");
        api.setReqHeader("Accept", "application/json");
        api.setReqHeader("Accept-Encoding", "gzip, deflate, br");
        api.setReqHeader("Connection", "keep-alive");
        Response r = sendRequestAndGetResponse(api, serverErrorResponseSpecification(), VISUAL_URL, false);
        System.out.println(r.body().toString());
        System.out.println(r.getStatusCode());
        System.out.println(r.getStatusLine());

    }

    @When("Grade by link is {string} with overallComment {string}")
    @Step("Grade by link is {string} with overallComment {string}")
    public void validateResponse(String result, String overallComment) {
        //   assertEquals(getLinkResponse().getSuccess(), success);
        assertEquals(getLinkResponse().getResult(), Integer.parseInt(result));
        assertEquals(getLinkResponse().getOverallComment(), overallComment);

    }

    @When("The following criteria by link in true mode: {string}")
    @Step("The following criteria by link in true mode: {string}")
    public void validateResponseCriteria(String commentNumbers) throws InterruptedException {
        List<Comment> comments = SCORE_ATTRIBUTES;
        comments.forEach(elem->elem.setValue(false));
        String[] num = commentNumbers.split(",");
        for (int i = 0; i < comments.size(); i++) {
            for (String s : num) {
                int number = Integer.parseInt(s);
                if (i == number) {
                    comments.get(i).setValue(true);
                }
            }
        }
        assertEquals(getLinkResponse().getComments(), comments);
        Thread.sleep(1000);

    }





}
