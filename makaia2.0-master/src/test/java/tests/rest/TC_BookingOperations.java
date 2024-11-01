package tests.rest;

import static io.restassured.RestAssured.given;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import tests.properties.GetProperty;

public class TC_BookingOperations {
	public String token=null;
	public int bookingId=0;
	String value=GetProperty.server("server");
	@BeforeTest
	public void createToken() {
		RestAssured.baseURI=value;
		String response=given().log().all().header("Content-Type","application/json")
				.body("{\r\n"
						+ "    \"username\" : \"admin\",\r\n"
						+ "    \"password\" : \"password123\"\r\n"
						+ "}").when().post("auth")
				.then().
				//log().all().
				assertThat().statusCode(200)
				.extract().response().asString();
		JsonPath js=new JsonPath(response); //for Parsing Json
		token=js.getString("token");	
		System.out.println(token);
	}
	
	@Test
	public void createBooking() {
		String response=given().log().all().header("Content-Type","application/json")
				.body("{\r\n"
						+ "    \"firstname\" : \"Jim\",\r\n"
						+ "    \"lastname\" : \"Brown\",\r\n"
						+ "    \"totalprice\" : 111,\r\n"
						+ "    \"depositpaid\" : true,\r\n"
						+ "    \"bookingdates\" : {\r\n"
						+ "        \"checkin\" : \"2018-01-01\",\r\n"
						+ "        \"checkout\" : \"2019-01-01\"\r\n"
						+ "    },\r\n"
						+ "    \"additionalneeds\" : \"Breakfast\"\r\n"
						+ "}").when().post("booking").then().assertThat().statusCode(200)
				.extract().response().asString();
		JsonPath js=new JsonPath(response);
		bookingId=js.getInt("bookingid");
		System.out.println(bookingId);
	}
	@Test
	public void updateBooking() {
		given().log().all().headers("Content-Type","application/json","Authorization","Basic YWRtaW46cGFzc3dvcmQxMjM=")
				.body("{\r\n"
						+ "    \"firstname\" : \"James\",\r\n"
						+ "    \"lastname\" : \"Brown\",\r\n"
						+ "    \"totalprice\" : 111,\r\n"
						+ "    \"depositpaid\" : true,\r\n"
						+ "    \"bookingdates\" : {\r\n"
						+ "        \"checkin\" : \"2018-01-01\",\r\n"
						+ "        \"checkout\" : \"2019-01-01\"\r\n"
						+ "    },\r\n"
						+ "    \"additionalneeds\" : \"Breakfast\"\r\n"
						+ "}").when().put("booking/"+bookingId).then().assertThat().statusCode(200);
	}
	@Test
	public void getallBookingId() {
		given().log().all().when().get("booking").then().log().all().assertThat().statusCode(200);
	}
}
