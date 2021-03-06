package rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.ContentType;

public class VerbosTest {

	@Test
	public void deveSalvarUsuario () {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Jose\",\"age\": \"50\"}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Jose"))
			.body("age", is("50"))
		;
	}
	
	@Test
	public void NaoDeveSalvarUsuarioSemNome () {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"age\": \"50\"}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(400)
			.body("id", is(nullValue()))
			.body("error", is("Name ? um atributo obrigat?rio"))
		;
	}
	
	@Test
	public void deveSalvarUsuarioXML () {
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body("<user><name>Jose</name><age>50</age></user>")
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Jose"))
			.body("user.age", is("50"))
		;
	}
	
	@Test
	public void deveAlterarUsuario () {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Usuario alterado\",\"age\": \"80\"}")
		.when()
			.put("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is("80"))
			.body("salary", is(1234.5678f))
		;
	}
	
	@Test
	public void devoCustomizarURL () {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Usuario alterado\",\"age\": \"80\"}")
			.pathParam("entidade", "users")
			.pathParam("userId", "1")
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is("80"))
			.body("salary", is(1234.5678f))
		;
	}
	
	@Test
	public void deveRemoverUsuario () {
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
		;
	}
	
	@Test
	public void naoDeveRemoverUsuario () {
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1000")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexistente"))
		;
	}
	
	@Test
	public void deveSalvarUsuarioMap () {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Usuario inserido via Map");
		params.put("age", "25");
		
		given()
			.log().all()
			.contentType("application/json")
			.body(params)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario inserido via Map"))
			.body("age", is(25))
		;
	}
	
	@Test
	public void deveSalvarUsuarioObjeto () {
		User user = new User("Usuario via objeto", 35); 
		
		given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via objeto"))
			.body("age", is(35))
		;
	}
	
	@Test
	public void deveDeserializarObjetoAoSalvar () {
		User user = new User("Usuario deserializado", 35); 
		
		User usuarioInserido = given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
		;
		
		System.out.println(usuarioInserido);
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertEquals("Usuario deserializado", usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getAge(), is(35));
	}
	
	@Test
	public void deveSalvarUsuarioXMLUsandoObjeto() {
		User user = new User("usuario xml objeto", 20);
		
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("usuario xml objeto"))
			.body("user.age", is("20"))
		;
	}
	
	@Test
	public void DeveDeserializarXMLAoSalvarUsuario() {
		User user = new User("usuario xml objeto", 20);
		
		User usuarioInserido = given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
		;
		
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertThat(usuarioInserido.getName(), is("usuario xml objeto"));
		Assert.assertThat(usuarioInserido.getAge(), is(20));
	}
	
}



