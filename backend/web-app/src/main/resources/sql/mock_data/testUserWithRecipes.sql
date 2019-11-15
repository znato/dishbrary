-- test user, password: qwe123
INSERT INTO USER (ID, BANNED, EXPIRED, EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, ROLE_ID)
VALUES (9999, false, false, 'test@test.com', 'FirstName', 'LastName', '$2a$10$hkYYE/B9uGMNKLeaRCrEXunO.ePdWeu1yLiTb28X3WQIOOgq2TIm.', 'test', 2);

INSERT INTO RECIPE(ID, CREATION_DATE, MODIFICATION_DATE, ADDITIONAL_INFO, COOK_TIME_IN_MILLIS, COVER_IMAGE_FILE_NAME, INSTRUCTION, NAME, POPULARITY_INDEX, PORTION, PREPARATION_TIME_IN_MILLIS, TAGS, VIDEO_FILE_NAME, OWNER_ID) VALUES
(1, TIMESTAMP '2019-11-15 13:59:43.976', TIMESTAMP '2019-11-15 13:59:49.133', '{"energyKcal":"0.32","protein":"0.0555","fat":"0.0093","carbohydrate":"0.0000"}', NULL, 'Austria.png', STRINGDECODE('<ul>\n  <li>erfer</li>\n  <li>erer</li>\n  <li>ert</li>\n  <li>ert</li>\n</ul>\n<p>hytht4y<strong>64536</strong></p>\n<h1><strong>4365456</strong></h1>\n<p><strong>64356464356</strong></p>\n<ol>\n  <li><strong>43645</strong></li>\n  <li>4356</li>\n  <li>4</li>\n</ol>\n<ul>\n  <li>reer</li>\n</ul>\n<ol>\n  <li>e23423234</li>\n</ol>\n<p><br></p>'), '5435', NULL, 4, NULL, NULL, NULL, 9999);

INSERT INTO RECIPE_ADDITIONAL_IMAGES_FILE_NAMES(RECIPE_ID, ADDITIONAL_IMAGES_FILE_NAMES) VALUES
(1, 'China.png'),
(1, 'France.png'),
(1, 'Germany.png'),
(1, 'Greece.png'),
(1, 'Hungary.png'),
(1, 'Italy.png'),
(1, 'Japan.png'),
(1, 'Poland.png'),
(1, 'Romania.png');

INSERT INTO RECIPE_INGREDIENT(ID, CREATION_DATE, MODIFICATION_DATE, QUANTITY, SELECTED_UNIT, INGREDIENT_ID, RECIPE_ID) VALUES
(1, TIMESTAMP '2019-11-15 13:59:43.99', TIMESTAMP '2019-11-15 13:59:43.99', 1, 'g', 2, 1);