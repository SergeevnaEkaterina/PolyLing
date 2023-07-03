package api.steps;//package api.steps;
//
//import api.CommonService;
//import io.cucumber.java.en.Then;
//import io.restassured.http.Method;
//import io.restassured.response.Response;
//import lombok.Getter;
//import lombok.Setter;
//import model.request.LinkBody;
//import model.response.AnalyseByLinkResponse;
//import model.response.Comment;
//
//import java.net.URI;
//import java.util.List;
//
//import static api.CommonService.*;
//import static api.CommonService.okResponseSpecification;
//import static data.FileAnalyseDataProvider.SCORE_ATTRIBUTES;
//import static org.testng.Assert.assertEquals;
//
//@Getter
//@Setter
//public class LinkSteps {
//
//    private AnalyseByLinkResponse linkResponse;
//    public static final URI VISUAL_URL = URI.create("http://localhost:3000/evaluation/linkVisual");
//
//    @Then("Upload text to analyse by link {string} via API")
//    public void getResultAnalyseByLink(String link) {
//        LinkBody body = new LinkBody(link);
//        CommonService.CommonApiBuilder api = commonApiBuilder()
//                .setMethod(Method.POST)
//                .setBody(body);
//        api.setReqHeader("Content-Type", "application/json");
//        api.setReqHeader("Host", "localhost:3000");
//        api.setReqHeader("Accept", "application/json");
//        api.setReqHeader("Accept-Encoding", "gzip, deflate, br");
//        api.setReqHeader("Connection", "keep-alive");
//       setLinkResponse(extractAnalyseBodyFromJson(sendRequestAndGetResponse(api, okResponseSpecification(), VISUAL_URL, false)));
//
//    }
//    @Then("Upload text by not enabled link {string} via API and should get error")
//    public void getErrorAnalyseByLink(String link) {
//        LinkBody body = new LinkBody(link);
//        CommonService.CommonApiBuilder api = commonApiBuilder()
//                .setMethod(Method.POST)
//                .setBody(body);
//        api.setReqHeader("Content-Type", "application/json");
//        api.setReqHeader("Host", "localhost:3000");
//        api.setReqHeader("Accept", "application/json");
//        api.setReqHeader("Accept-Encoding", "gzip, deflate, br");
//        api.setReqHeader("Connection", "keep-alive");
//        sendRequestAndGetResponse(api, serverErrorResponseSpecification(), VISUAL_URL, false);
//
//    }
//
//    @Then("Grade by link is {string} with overallComment {string}")
//    public void validateResponse(String result, String overallComment) {
//        //   assertEquals(getLinkResponse().getSuccess(), success);
//        assertEquals(getLinkResponse().getResult(), Integer.parseInt(result));
//        assertEquals(getLinkResponse().getOverallComment(), overallComment);
//
//    }
//
//    @Then("The following criteria by link in true mode: {string}")
//    public void validateResponseCriteria(String commentNumbers) throws InterruptedException {
//        List<Comment> comments = SCORE_ATTRIBUTES;
//        comments.forEach(elem->elem.setValue(false));
//        String[] num = commentNumbers.split(",");
//        for (int i = 0; i < comments.size(); i++) {
//            for (String s : num) {
//                int number = Integer.parseInt(s);
//                if (i == number) {
//                    comments.get(i).setValue(true);
//                }
//            }
//        }
//        assertEquals(getLinkResponse().getComments(), comments);
//        Thread.sleep(1000);
//
//    }
//
//
//
//
//
//}
