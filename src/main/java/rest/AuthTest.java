package rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import groovyjarjarantlr4.v4.parse.ANTLRParser.id_return;
import io.restassured.http.ContentType;

public class AuthTest {

	@Test
	public void deveAcessarSWAPI() {
		given()
			.log().all()
		.when()
			.get("https://swapi.dev/api/people/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Luke Skywalker"))
		;
	}
	
	@Test
	public void deveObterClima () {
		given()
			.log().all()
			.queryParam("q", "Belo Horizonte,BR")
			.queryParam("appid", "c8c2ac98d97b07a0efa4f144e38819d1")
			.queryParam("units", "metric")
		.when()
			.get("http://api.openweathermap.org/data/2.5/weather")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Belo Horizonte"))
			.body("coord.lon", is(-43.9378F))
			.body("coord.lat", is(-19.9208F))
		;
	}
	
	@Test
	public void deveFazerAutenticacaoComToken() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "wagner@aquino");
		login.put("senha", "123456");
		
		//Login na api
		//Receber o Token
		String token =given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when()
			.post("http://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token");
		
		
		//Obter Contas
		given()
			.log().all()
			.header("Authorization", "JWT " + token)
		.when()
			.get("http://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", hasItem("Conta para saldo"))
		;
	}
}
