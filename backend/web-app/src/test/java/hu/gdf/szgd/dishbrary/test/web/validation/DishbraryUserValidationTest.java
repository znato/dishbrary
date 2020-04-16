package hu.gdf.szgd.dishbrary.test.web.validation;

import hu.gdf.szgd.dishbrary.security.DishbraryUser;
import hu.gdf.szgd.dishbrary.service.exception.DishbraryValidationException;
import hu.gdf.szgd.dishbrary.test.AbstractTest;
import hu.gdf.szgd.dishbrary.web.validation.DishbraryUserValidationUtil;
import org.junit.Test;

public class DishbraryUserValidationTest extends AbstractTest {

	@Test(expected = DishbraryValidationException.class)
	public void testEmptyUsername() {
		DishbraryUser user = new DishbraryUser();
		user.setUsername("    ");
		user.setEmail("user@email.com");
		user.setPassword("qwe123");
		user.setFirstName("firstname");
		user.setLastName("lastname");

		DishbraryUserValidationUtil.validateUser(user, true);
	}

	@Test(expected = DishbraryValidationException.class)
	public void testEmptyEmail() {
		DishbraryUser user = new DishbraryUser();
		user.setUsername("username");
		user.setEmail("   ");
		user.setPassword("qwe123");
		user.setFirstName("firstname");
		user.setLastName("lastname");

		DishbraryUserValidationUtil.validateUser(user, true);
	}

	@Test(expected = DishbraryValidationException.class)
	public void testInvalidEmailFormat() {
		DishbraryUser user = new DishbraryUser();
		user.setUsername("username");
		user.setEmail("invalidemailformat.email.com");
		user.setPassword("qwe123");
		user.setFirstName("firstname");
		user.setLastName("lastname");

		DishbraryUserValidationUtil.validateUser(user, true);
	}

	@Test(expected = DishbraryValidationException.class)
	public void testXSSAttackInEmail() {
		DishbraryUser user = new DishbraryUser();
		user.setUsername("username");
		user.setEmail("invalidem<script>alert('attack')</script>ailformat@email.com");
		user.setPassword("qwe123");
		user.setFirstName("firstname");
		user.setLastName("lastname");

		DishbraryUserValidationUtil.validateUser(user, true);
	}

	@Test(expected = DishbraryValidationException.class)
	public void testXSSAttackInUsername() {
		DishbraryUser user = new DishbraryUser();
		user.setUsername("user<script>alert('attack')</script>name");
		user.setEmail("user@email.com");
		user.setPassword("qwe123");
		user.setFirstName("firstname");
		user.setLastName("lastname");

		DishbraryUserValidationUtil.validateUser(user, true);
	}

	@Test(expected = DishbraryValidationException.class)
	public void testXSSAttackInFirstname() {
		DishbraryUser user = new DishbraryUser();
		user.setUsername("username");
		user.setEmail("user@email.com");
		user.setPassword("qwe123");
		user.setFirstName("first<script>alert('attack')</script>name");
		user.setLastName("lastname");

		DishbraryUserValidationUtil.validateUser(user, true);
	}

	@Test(expected = DishbraryValidationException.class)
	public void testXSSAttackInLastname() {
		DishbraryUser user = new DishbraryUser();
		user.setUsername("username");
		user.setEmail("user@email.com");
		user.setPassword("qwe123");
		user.setFirstName("firstname");
		user.setLastName("last<script>alert('attack')</script>name");

		DishbraryUserValidationUtil.validateUser(user, true);
	}

	@Test
	public void testValidUser() {
		DishbraryUser user = new DishbraryUser();
		user.setUsername("username");
		user.setEmail("user@email.com");
		user.setPassword("qwe123");
		user.setFirstName("firstname");
		user.setLastName("lastname");

		DishbraryUserValidationUtil.validateUser(user, true);
	}
}
