package steps;

import api.CommonService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import io.restassured.http.Method;
import lombok.Getter;
import lombok.Setter;
import model.response.AnalyseByLinkResponse;
import model.response.Comment;
import model.response.LaboratoryItem;
import model.response.MaterialsItemWithTitle;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Objects;

import static api.CommonService.*;
import static data.FileAnalyseDataProvider.SCORE_ATTRIBUTES;
import static data.FileAnalyseDataProvider.getFilePathByExpectedMark;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Getter
@Setter
public class FileSteps {


    public static final URI VISUAL_URL = URI.create("http://localhost:3000/evaluation/fileVisual");
    //    public static final URI PARTICIPANTS_FRONT_URL = URI.create("http://localhost/projectTeam");
    public static final URI PARTICIPANTS_URL = URI.create("http://localhost:3000/participants");
    //    public static final URI MATERIALS_FRONT_URL = URI.create("http://localhost/aboutProject");
    public static final URI MATERIALS_URL = URI.create("http://localhost:3000/materials");
    public static final URI PROJECT_URL = URI.create("http://localhost/aboutProject");
    public String page;


    private AnalyseByLinkResponse linkResponse;
    private List<MaterialsItemWithTitle> materialsResponse;
    private List<LaboratoryItem> laboratoryItem;

    @Given("Upload generated file with {string} mark with the following criteria considered {string} via API")
    @Step("Upload generated file with {string} mark with the following criteria considered {string} via API")
    public void getResultAnalyseByFile(String mark, String criteria) throws InterruptedException {
        String filePath = getFilePathByExpectedMark(mark, criteria);

        File file = new File(filePath);

        CommonService.CommonApiBuilder api = commonApiBuilder()
                .setMethod(Method.POST)
                .setBody(null)
                .setFile(file);
        api.setReqHeader("Content-Type", "application/json");
        api.setReqHeader("Host", "localhost:3000");
        api.setReqHeader("Accept", "application/json");
        api.setReqHeader("Accept-Encoding", "gzip, deflate, br");
        api.setReqHeader("Connection", "keep-alive");
        api.setReqHeader("Content-Type", "multipart/form-data");
        setLinkResponse(extractAnalyseBodyFromJson(sendRequestAndGetResponse(api, okResponseSpecification(), VISUAL_URL, true)));

    }

    @Given("User successfully gets {string} page of PolyLing application")
    public void getApplicationPages(String pagename) {
//        String filePath = getFilePathByExpectedMark(mark, criteria);
page = pagename;
//        File file = new File(filePath);
        URI url = switch (pagename) {
            case ("participants") -> PARTICIPANTS_URL;
            case ("project") -> PROJECT_URL;
            default -> MATERIALS_URL;
        };

        CommonService.CommonApiBuilder api = commonApiBuilder()
                .setMethod(Method.GET);
        api.setReqHeader("Host", "localhost:3000");
        api.setReqHeader("Accept", "application/json");
        api.setReqHeader("Accept-Encoding", "gzip, deflate, br");
        api.setReqHeader("Connection", "keep-alive");
        if (pagename.equals("materials")) {
            setMaterialsResponse(extractParticipantsBodyFromJson(sendGetRequestAndGetResponse(api, okResponseSpecification(), url, false)));
        } else if (pagename.equals("participants")) {
            setLaboratoryItem(extractLaboratoryBodyFromJson(sendGetRequestAndGetResponse(api, okResponseSpecification(), url, false)));
        }
    }

    @When("Response body is valid")
    public void validateResponseMaterialsBody() {
        if(Objects.equals(page, "materials")) {
            assertEquals(materialsResponse.get(0).getTitle(), "Презентация по материалам исследования");
            assertEquals(materialsResponse.get(1).getTitle(), "Статьи");
        }else if(Objects.equals(page, "participants")){
            assertEquals(laboratoryItem.get(0).getDescription(), "Высшая школа лингводидактики и перевода Гуманитарного института Санкт-Петербургского политехнического университета Петра Великого");
            assertTrue(laboratoryItem.get(1).getDescription().contains("Промышленные системы потоковой обработки данных"));
        }
    }

    @When("Grade is {string} with overallComment {string}")
    @Step("Grade is {string} with overallComment {string}")

    public void validateResponse(String result, String overallComment) {
        //   assertEquals(getLinkResponse().getSuccess(), success);
        assertEquals(getLinkResponse().getResult(), Integer.parseInt(result));
        assertEquals(getLinkResponse().getOverallComment(), overallComment);

    }

    @When("The following criteria in true mode: {string}")
    @Step("The following criteria in true mode: {string}")
    public void validateResponseCriteria(String commentNumbers) {
        List<Comment> comments = SCORE_ATTRIBUTES;
        comments.forEach(elem -> elem.setValue(false));
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

    }


}
