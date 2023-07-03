package api.steps;

import io.restassured.http.Method;
import lombok.Getter;
import lombok.Setter;
import model.request.TextBody;
import model.response.TextResponse;

import java.net.URI;

import static api.CommonService.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Getter
@Setter
public class TextSteps {
    private TextResponse textResponse;
    public static final URI VISUAL_URL = URI.create("http://localhost:3000/evaluation/text");

    public void getResultAnalyseByText(TextBody body) {

        CommonApiBuilder api = commonApiBuilder()
                .setMethod(Method.POST)
                .setBody(body);
        api.setReqHeader("Content-Type", "application/json");
        api.setReqHeader("Host", "localhost:3000");
        api.setReqHeader("Accept", "application/json");
        api.setReqHeader("Accept-Encoding", "gzip, deflate, br");
        api.setReqHeader("Connection", "keep-alive");
        setTextResponse(extractAnalyseTextBodyFromJson(sendRequestAndGetResponse(api, okResponseSpecification(), VISUAL_URL, false)));
    }

    public void validateResponse(int result, String comment){
        assertEquals(getTextResponse().getResult(), result);
        assertEquals(getTextResponse().getComment(), comment);

    }
}
