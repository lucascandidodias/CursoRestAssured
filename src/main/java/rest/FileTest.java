package rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

public class FileTest {
	
	@Test
	public void deveObrigarEnviarArquivo () {
		given()
			.log().all()
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404)
			.body("error", is("Arquivo não enviado"))
		;
	}
	
	@Test
	public void deveFazerUploadDoArquivo () {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/users.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("users.pdf"))
			.body("md5", is(notNullValue()))
		;
	}
	
	@Test
	public void deveFazerUploadDoArquivoGrande () {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/users.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.time(lessThan(5000L)) //Limite de tempo para realizar a requisição
			.statusCode(200)
		;
	}
	
	@Test
	public void deveBaixarArquivo () throws IOException {
		byte[] image = given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/download")
		.then()
			.statusCode(200)
			.extract().asByteArray()
		;
		
		File imagem = new File("src/main/resources/file.jpg");
		OutputStream out = new FileOutputStream(imagem);
		out.write(image);
		out.close();
		
		Assert.assertThat(imagem.length(), lessThan(100000L));
	}

}
