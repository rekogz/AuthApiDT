package spec;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class AuthUserSpec {
    public static RequestSpecification authRequestSpec = with()
            .log().uri()
            .log().method()
            .contentType(JSON);

    public static ResponseSpecification authResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(STATUS)
            .log(BODY)
            .expectBody(matchesJsonSchemaInClasspath("schemas/schemaAuthDT.json"))
            .build();
}
