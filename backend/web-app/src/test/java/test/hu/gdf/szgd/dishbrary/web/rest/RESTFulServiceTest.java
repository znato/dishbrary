package test.hu.gdf.szgd.dishbrary.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gdf.szgd.dishbrary.DishbraryApplication;
import hu.gdf.szgd.dishbrary.security.DishbraryUser;
import hu.gdf.szgd.dishbrary.web.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = DishbraryApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RESTFulServiceTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ObjectMapper om;

	//ObjectMapper is configured to ignore password, so post raw json String instead creating a java object
	private static final String newUserJson = "{\"username\":\"JamieOliver\",\"password\":\"qwe123\"}";
	private static final HttpEntity<String> loginRequest;
	static {
		HttpHeaders loginHeaders = new HttpHeaders();
		loginHeaders.setContentType(MediaType.APPLICATION_JSON);
		loginRequest = new HttpEntity<>(newUserJson, loginHeaders);
	}

	@Test
	public void testLoginUser() throws JsonProcessingException {
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/rest/user/login", loginRequest, String.class);

		DishbraryResponse<DishbraryUser> response = om.readValue(responseEntity.getBody(), ResponseTypeReferences.DISHBRARY_USER_RESPONSE_TYPE);

		Assert.assertFalse(response.isError());

		Assert.assertNotNull("Response user id is not filled. This indicates the login is not correctly performed!", response.getContent().getId());

		Assert.assertNull("Response password must be empty for security reasons!", response.getContent().getPassword());
	}

	@Test
	public void testLogoutUser() throws JsonProcessingException {
		ResponseEntity<String> loginResponse = restTemplate.postForEntity("/rest/user/login", loginRequest, String.class);
		String cookie = loginResponse.getHeaders().get("Set-Cookie").get(0);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Cookie", cookie);
		HttpEntity logoutEntity = new HttpEntity(headers);

		ResponseEntity<String> responseEntity = restTemplate.exchange("/rest/user/logout", HttpMethod.GET, logoutEntity, String.class);

		DishbraryResponse<String> response = om.readValue(responseEntity.getBody(), ResponseTypeReferences.STRING_RESPONSE_TYPE);

		Assert.assertFalse(response.isError());
	}

	@Test
	public void testRegisterUser() throws JsonProcessingException {
		//ObjectMapper is configured to ignore password, so post raw json String instead creating a java object
		String newUserJson = "{\"username\":\"newUser\",\"email\":\"newUser@email.com\",\"password\":\"qwe123\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(newUserJson, headers);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/rest/user/register", request, String.class);

		DishbraryResponse<DishbraryUser> response = om.readValue(responseEntity.getBody(), ResponseTypeReferences.DISHBRARY_USER_RESPONSE_TYPE);

		Assert.assertFalse(response.isError());

		Assert.assertNotNull("Response user id is not filled. This indicates it has not been saved or mapped properly!", response.getContent().getId());

		Assert.assertNull("Response password must be empty for security reasons!", response.getContent().getPassword());
	}

	@Test
	public void testFetchAllCategory() throws JsonProcessingException {
		String body = restTemplate.getForObject("/rest/recipe/category/all", String.class);

		DishbraryCollectionResponse<CategoryRestModel> response = om.readValue(body, ResponseTypeReferences.ALL_CATEGORY_RESPONSE_TYPE);

		Assert.assertFalse(response.isError());
		Assert.assertTrue(response.getContent().size() > 0);
	}

	@Test
	public void testFetchAllCuisines() throws JsonProcessingException {
		String body = restTemplate.getForObject("/rest/recipe/cuisine/all", String.class);

		DishbraryCollectionResponse<CuisineRestModel> response = om.readValue(body, ResponseTypeReferences.ALL_CUISINE_RESPONSE_TYPE);

		Assert.assertFalse(response.isError());
		Assert.assertTrue(response.getContent().size() > 0);
	}

	@Test
	public void testFetchAllIngredient() throws JsonProcessingException {
		String body = restTemplate.getForObject("/rest/recipe/ingredient/all", String.class);

		DishbraryCollectionResponse<IngredientRestModel> response = om.readValue(body, ResponseTypeReferences.ALL_INGREDIENT_RESPONSE_TYPE);

		Assert.assertFalse(response.isError());
		Assert.assertTrue(response.getContent().size() > 0);
	}
}
