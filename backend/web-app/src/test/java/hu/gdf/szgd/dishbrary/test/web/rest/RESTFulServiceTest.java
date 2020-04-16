package hu.gdf.szgd.dishbrary.test.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gdf.szgd.dishbrary.DishbraryApplication;
import hu.gdf.szgd.dishbrary.db.entity.RecipeIngredient;
import hu.gdf.szgd.dishbrary.security.DishbraryUser;
import hu.gdf.szgd.dishbrary.test.AbstractTest;
import hu.gdf.szgd.dishbrary.web.model.*;
import hu.gdf.szgd.dishbrary.web.model.request.RecipeSearchCriteriaRestModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = DishbraryApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RESTFulServiceTest extends AbstractTest {

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
		HttpEntity logoutEntity = new HttpEntity(prepareHeadersForAuthenticatedSession());

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

	@Test
	public void testFetchRandomRecipes() throws JsonProcessingException {
		HttpEntity request = new HttpEntity(prepareHeadersForAuthenticatedSession());

		ResponseEntity<String> responseEntity = restTemplate.exchange("/rest/recipe/recipes/random", HttpMethod.GET, request, String.class);

		DishbraryResponse<PageableRestModel<RecipeRestModel>> response = om.readValue(responseEntity.getBody(), ResponseTypeReferences.PAGEABLE_RECIPE_RESPONSE_TYPE);

		Assert.assertFalse(response.isError());

		Assert.assertTrue(response.getContent().getTotalElements() > 0);
		Assert.assertTrue(response.getContent().getElements().size() > 0);
	}

	@Test
	public void testFetchRecipeById() throws JsonProcessingException {
		ResponseEntity<String> responseEntity = restTemplate.getForEntity("/rest/recipe/" + 1, String.class);

		DishbraryResponse<RecipeRestModel> response = om.readValue(responseEntity.getBody(), ResponseTypeReferences.RECIPE_RESPONSE_TYPE);

		Assert.assertFalse(response.isError());

		Assert.assertEquals((long) response.getContent().getId(), 1);
	}

	@Test
	public void testFetchMyRecipesWithAuthenticatedUser() throws JsonProcessingException {
		HttpEntity request = new HttpEntity(prepareHeadersForAuthenticatedSession());

		ResponseEntity<String> responseEntity = restTemplate.exchange("/rest/recipe/my-recipes", HttpMethod.GET, request, String.class);

		DishbraryResponse<PageableRestModel<RecipeRestModel>> response = om.readValue(responseEntity.getBody(), ResponseTypeReferences.PAGEABLE_RECIPE_RESPONSE_TYPE);

		Assert.assertFalse(response.isError());

		Assert.assertTrue(response.getContent().getTotalElements() > 0);
		Assert.assertTrue(response.getContent().getElements().size() > 0);
	}

	@Test
	public void testFetchMyRecipesWithAnonymousUser() throws JsonProcessingException {
		ResponseEntity<String> responseEntity = restTemplate.getForEntity("/rest/recipe/my-recipes", String.class);

		DishbraryResponse<PageableRestModel<RecipeRestModel>> response = om.readValue(responseEntity.getBody(), ResponseTypeReferences.PAGEABLE_RECIPE_RESPONSE_TYPE);

		Assert.assertTrue(response.isError());

		Assert.assertEquals(response.getStatus(), 401);
	}

	@Test
	public void testCreateRecipe() throws JsonProcessingException {
		RecipeRestModel recipe = new RecipeRestModel();
		recipe.setName("Test Recipe");
		recipe.setInstruction("test instruction");
		IngredientRestModel ingredient = new IngredientRestModel();
		ingredient.setId(1l);
		recipe.setIngredients(Arrays.asList(new RecipeIngredientRestModel(null, null, ingredient, 1, RecipeIngredient.SelectableUnit.kg)));
		recipe.setPortion(2);

		HttpEntity request = new HttpEntity(recipe, prepareHeadersForAuthenticatedSession());

		ResponseEntity<String> responseEntity = restTemplate.exchange("/rest/recipe/create", HttpMethod.POST, request, String.class);

		DishbraryResponse<RecipeRestModel> response = om.readValue(responseEntity.getBody(), ResponseTypeReferences.RECIPE_RESPONSE_TYPE);

		Assert.assertFalse(response.isError());

		Assert.assertNotNull(response.getContent().getId());
	}

	@Test(expected = ResourceAccessException.class)
	public void testCreateRecipeWithAnonymousUser() {
		RecipeRestModel recipe = new RecipeRestModel();
		recipe.setName("Test Recipe");
		recipe.setInstruction("test instruction");
		IngredientRestModel ingredient = new IngredientRestModel();
		ingredient.setId(1l);
		recipe.setIngredients(Arrays.asList(new RecipeIngredientRestModel(null, null, ingredient, 1, RecipeIngredient.SelectableUnit.kg)));
		recipe.setPortion(2);

		HttpEntity request = new HttpEntity(recipe);

		ResponseEntity<String> responseEntity = restTemplate.exchange("/rest/recipe/create", HttpMethod.POST, request, String.class);

//		DishbraryResponse<RecipeRestModel> response = om.readValue(responseEntity.getBody(), ResponseTypeReferences.RECIPE_RESPONSE_TYPE);
//
//		Assert.assertTrue(response.isError());
//
//		Assert.assertEquals(response.getStatus(), 401);
	}

	@Test
	public void testDeleteRecipeWithAnonymousUser() throws JsonProcessingException {
		ResponseEntity<String> responseEntity = restTemplate.exchange("/rest/recipe/delete/" + 2, HttpMethod.DELETE, null, String.class);

		DishbraryResponse<String> response = om.readValue(responseEntity.getBody(), ResponseTypeReferences.STRING_RESPONSE_TYPE);

		Assert.assertTrue(response.isError());

		Assert.assertEquals(response.getStatus(), 401);
	}

	@Test
	public void testDeleteOtherUsersRecipeWithAuthenticatedUser() throws JsonProcessingException {
		HttpEntity request = new HttpEntity(prepareHeadersForAuthenticatedSession());
		ResponseEntity<String> responseEntity = restTemplate.exchange("/rest/recipe/delete/" + 6, HttpMethod.DELETE, request, String.class);

		DishbraryResponse<String> response = om.readValue(responseEntity.getBody(), ResponseTypeReferences.STRING_RESPONSE_TYPE);

		Assert.assertTrue(response.isError());

		Assert.assertEquals(response.getStatus(), 400);
	}

	@Test
	public void testDeleteOwnRecipeWithAuthenticatedUser() throws JsonProcessingException {
		HttpEntity request = new HttpEntity(prepareHeadersForAuthenticatedSession());
		ResponseEntity<String> responseEntity = restTemplate.exchange("/rest/recipe/delete/" + 3, HttpMethod.DELETE, request, String.class);

		DishbraryResponse<String> response = om.readValue(responseEntity.getBody(), ResponseTypeReferences.STRING_RESPONSE_TYPE);

		Assert.assertFalse(response.isError());
	}

	@Test
	public void testSearchByIngredient() throws JsonProcessingException {
		RecipeSearchCriteriaRestModel criteria = new RecipeSearchCriteriaRestModel();
		IngredientRestModel ingredient = new IngredientRestModel();
		ingredient.setId(11l);
		criteria.setIngredientList(Arrays.asList(ingredient));

		HttpEntity request = new HttpEntity(criteria);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/rest/recipe/search/all", HttpMethod.POST, request, String.class);

		DishbraryResponse<RecipeSearchResponseRestModel> response = om.readValue(responseEntity.getBody(), ResponseTypeReferences.RECIPE_SEARCH_RESPONSE_TYPE);

		Assert.assertFalse(response.isError());

		Assert.assertEquals((long) response.getContent().getElements().get(0).getId(), 4);
	}

	@Test
	public void testSearchByPlainText() throws JsonProcessingException {
		RecipeSearchCriteriaRestModel criteria = new RecipeSearchCriteriaRestModel();
		criteria.setPlainTextSearch("csirke");

		HttpEntity request = new HttpEntity(criteria);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/rest/recipe/search/all", HttpMethod.POST, request, String.class);

		DishbraryResponse<RecipeSearchResponseRestModel> response = om.readValue(responseEntity.getBody(), ResponseTypeReferences.RECIPE_SEARCH_RESPONSE_TYPE);

		Assert.assertFalse(response.isError());

		Assert.assertEquals(response.getContent().getTotalElements(), 2);

		List<RecipeRestModel> recipes = response.getContent().getElements();

		Assert.assertTrue(recipes.get(0).getId() == 1 || recipes.get(0).getId() == 10);
		Assert.assertTrue(recipes.get(1).getId() == 1 || recipes.get(1).getId() == 10);
	}

	@Test
	public void testSearchByWhatIsInTheFridge() throws JsonProcessingException {
		RecipeSearchCriteriaRestModel criteria = new RecipeSearchCriteriaRestModel();
		IngredientRestModel ingredient1 = new IngredientRestModel();
		ingredient1.setId(127l);
		IngredientRestModel ingredient2 = new IngredientRestModel();
		ingredient2.setId(789l);
		IngredientRestModel ingredient3 = new IngredientRestModel();
		ingredient3.setId(620l);
		IngredientRestModel ingredient4 = new IngredientRestModel();
		ingredient4.setId(704l);
		IngredientRestModel ingredient5 = new IngredientRestModel();
		ingredient5.setId(682l);
		criteria.setIngredientList(Arrays.asList(ingredient1, ingredient2, ingredient3, ingredient4, ingredient5));

		HttpEntity request = new HttpEntity(criteria);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/rest/recipe/search/fridge", HttpMethod.POST, request, String.class);

		DishbraryResponse<RecipeSearchResponseRestModel> response = om.readValue(responseEntity.getBody(), ResponseTypeReferences.RECIPE_SEARCH_RESPONSE_TYPE);

		Assert.assertFalse(response.isError());

		Assert.assertTrue(response.getContent().getTotalElements() == 1);

		Assert.assertEquals((long) response.getContent().getElements().get(0).getId(), 8);
	}

	private HttpHeaders prepareHeadersForAuthenticatedSession() {
		ResponseEntity<String> loginResponse = restTemplate.postForEntity("/rest/user/login", loginRequest, String.class);
		String cookie = loginResponse.getHeaders().get("Set-Cookie").get(0);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Cookie", cookie);
		headers.setContentType(MediaType.APPLICATION_JSON);

		return headers;
	}
}
