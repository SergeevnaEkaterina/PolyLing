package api;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import model.response.AnalyseByLinkResponse;
import model.response.LaboratoryItem;
import model.response.MaterialsItemWithTitle;
import model.response.TextResponse;
import org.apache.http.HttpStatus;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.lessThan;

public class CommonService {
    protected Method requestedMethod;
    protected Map<String, String> headers;
    protected String body;
    protected File loadFile;


    public static CommonApiBuilder commonApiBuilder() {
        return new CommonApiBuilder();
    }

    public static class CommonApiBuilder {
        private final Map<String, String> headers = new HashMap<>();
        private Method requestedMethod = Method.GET;
        private String body = null;
        private File loadFile = null;

        public CommonApiBuilder setMethod(Method method) {
            requestedMethod = method;
            return this;
        }

        public CommonApiBuilder setBody(Object jsonBody) {
            body = new Gson().toJson(jsonBody);
            return this;
        }

        public CommonApiBuilder setReqHeader(String parameter, String value) {
            headers.put(parameter, value);
            return this;
        }

        public CommonApiBuilder setFile(File file) {
            loadFile = file;
            return this;
        }

        public CommonService buildCommonApiRequest() {
            return new CommonService(requestedMethod, headers, body, loadFile);
        }
    }

    public static ResponseSpecification okResponseSpecification() {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_OK)
                .expectContentType(ContentType.JSON)
                .expectResponseTime(lessThan(30000L))
                .build();
    }

    public static ResponseSpecification okHtmlResponseSpecification() {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_OK)
                .expectContentType(ContentType.HTML)
                .expectResponseTime(lessThan(30000L))
                .build();
    }

    public static ResponseSpecification serverErrorResponseSpecification() {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .expectContentType(ContentType.JSON)
                .expectResponseTime(lessThan(30000L))
                .build();
    }


    public Response sendRequestWithMultipart(URI url) {
        return RestAssured
                .given((requestSpecification(url))).log().all()
                .headers(headers)
                .multiPart(loadFile)
                .request(requestedMethod)
                .prettyPeek();
    }

    public Response sendRequest(URI url) {
        return RestAssured
                .given((requestSpecification(url))).log().all()
                .headers(headers)
                .body(body)
                .request(requestedMethod)
                .prettyPeek();
    }

    public Response sendGetRequest(URI url) {
        return RestAssured
                .given((requestSpecification(url))).log().all()
                .headers(headers)
                .request(requestedMethod)
                .prettyPeek();
    }

    public static RequestSpecification requestSpecification(URI url) {
        return new RequestSpecBuilder()
                .setBaseUri(url)
                .build();
    }

    public CommonService(Method requestedMethod, Map<String, String> headers, String body, File file) {
        this.requestedMethod = requestedMethod;
        this.headers = headers;
        this.body = body;
        this.loadFile = file;
    }

    public static AnalyseByLinkResponse extractAnalyseBodyFromJson(Response response) {
        return new Gson().fromJson(response.asString().trim(), new TypeToken<AnalyseByLinkResponse>() {
        }.getType());
    }

    public static TextResponse extractAnalyseTextBodyFromJson(Response response) {
        return new Gson().fromJson(response.asString().trim(), new TypeToken<TextResponse>() {
        }.getType());
    }

    public static List<MaterialsItemWithTitle> extractParticipantsBodyFromJson(Response response) {
        return new Gson().fromJson(response.asString().trim(), new TypeToken<List<MaterialsItemWithTitle>>() {
        }.getType());
    }

    public static List<LaboratoryItem> extractLaboratoryBodyFromJson(Response response) {
        return new Gson().fromJson(response.asString().trim(), new TypeToken<List<LaboratoryItem>>() {
        }.getType());
    }


    public static Response sendRequestAndGetResponse(CommonApiBuilder api,
                                                     ResponseSpecification resp, URI url, boolean isMultipart) {
        Response response = null;
        if(isMultipart) {
            response = api
                    .buildCommonApiRequest()
                    .sendRequestWithMultipart(url);
        } else{
            response = api
                    .buildCommonApiRequest()
                    .sendRequest(url);
        }

        response.then()
                .assertThat()
                .spec(resp);

        return response;
    }


    public static Response sendGetRequestAndGetResponse(CommonApiBuilder api,
                                                        ResponseSpecification resp, URI url, boolean isMultipart) {
        Response response = null;
        if(isMultipart) {
            response = api
                    .buildCommonApiRequest()
                    .sendRequestWithMultipart(url);
        } else{
            response = api
                    .buildCommonApiRequest()
                    .sendGetRequest(url);
        }

        response.then()
                .assertThat()
                .spec(resp);

        return response;
    }



}
