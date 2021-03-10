package rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
 
public class OlaMundoTest {

	@Test
	public void testOlaMundo() {
		Response response = request(Method.GET, "http://restapi.wcaquino.me/ola"); 
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertEquals(200, response.statusCode());
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}
	
	@Test
	public void devoConhecerOutrasFormasRestAssured() {
		get("http://restapi.wcaquino.me/ola").then().statusCode(200);
		
		given()
		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then()
			.assertThat()
			.statusCode(200);
	}
	
	@Test
	public void devoConhecerMatchersHamcrest () {
		assertThat("Maria", Matchers.is("Maria"));
		assertThat(128, Matchers.is(128));
		assertThat(128, Matchers.isA(Integer.class));
		assertThat(128d, Matchers.isA(Double.class));
		assertThat(128, Matchers.greaterThan(127));
		assertThat(128, Matchers.lessThan(129));
		
		
		List<Integer> impares = Arrays.asList(1,2,3,4,5);
		assertThat(impares, hasSize(5));
		assertThat(impares, contains(1,2,3,4,5));
		assertThat(impares, containsInAnyOrder(5,4,3,2,1));
		assertThat(impares, hasItem(3));
		assertThat(impares, hasItems(1,5));
		
		assertThat("Maria", is(not("João")));
		assertThat("Maria", not("João"));
		assertThat("Joaquina", anyOf(is("Maria"), is("Joaquina")));
		assertThat("Joaquina", allOf(is("Joaquina"), is("Joaquina")));
		assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui")));
	}
	
	@Test
	public void devoValiarBody() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then()
			.assertThat()
			.statusCode(200)
			.body(is("Ola Mundo!"))
			.body(containsString("Mundo"))
			.body(is(not(nullValue())));
	}
}
