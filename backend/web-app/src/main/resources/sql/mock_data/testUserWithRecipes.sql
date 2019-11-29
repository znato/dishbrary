-- test user, password: qwe123
INSERT INTO USER (ID, BANNED, EXPIRED, EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, ROLE_ID)
VALUES (9999, false, false, 'test@test.com', 'FirstName', 'LastName', '$2a$10$hkYYE/B9uGMNKLeaRCrEXunO.ePdWeu1yLiTb28X3WQIOOgq2TIm.', 'test', 2);

-- 1st recipe ==========================================================================================================

INSERT INTO RECIPE(ID, CREATION_DATE, MODIFICATION_DATE, ADDITIONAL_INFO, COOK_TIME_IN_MILLIS, COVER_IMAGE_FILE_NAME, INSTRUCTION, NAME, POPULARITY_INDEX, PORTION, PREPARATION_TIME_IN_MILLIS, VIDEO_FILE_NAME, OWNER_ID) VALUES
(1, TIMESTAMP '2019-11-15 13:59:43.976', TIMESTAMP '2019-11-15 13:59:49.133', '{"energyKcal":"0.32","protein":"0.0555","fat":"0.0093","carbohydrate":"0.0000"}', NULL, 'Austria.png', STRINGDECODE('<ul>\n  <li>erfer</li>\n  <li>erer</li>\n  <li>ert</li>\n  <li>ert</li>\n</ul>\n<p>hytht4y<strong>64536</strong></p>\n<h1><strong>4365456</strong></h1>\n<p><strong>64356464356</strong></p>\n<ol>\n  <li><strong>43645</strong></li>\n  <li>4356</li>\n  <li>4</li>\n</ol>\n<ul>\n  <li>reer</li>\n</ul>\n<ol>\n  <li>e23423234</li>\n</ol>\n<p><br></p>'), '5435', NULL, 4, NULL, NULL, 9999);

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

-- 2nd recipe ==========================================================================================================

INSERT INTO RECIPE(ID, CREATION_DATE, MODIFICATION_DATE, ADDITIONAL_INFO, COOK_TIME_IN_MILLIS, COVER_IMAGE_FILE_NAME, INSTRUCTION, NAME, POPULARITY_INDEX, PORTION, PREPARATION_TIME_IN_MILLIS, VIDEO_FILE_NAME, OWNER_ID) VALUES
(2, TIMESTAMP '2019-11-22 10:20:06.079', TIMESTAMP '2019-11-22 10:20:41.245', '{"energyKcal":"307.68","protein":"56.6285","fat":"6.8308","carbohydrate":"2.2970"}', 1800000, 'Hungary.png', STRINGDECODE('<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque &nbsp;posuere arcu nunc, tempor feugiat sem convallis a. Curabitur sed &nbsp;consequat ante. Vivamus tempus, ipsum eget commodo rutrum, sem metus &nbsp;sodales elit, ac eleifend turpis nulla sit amet diam. Vivamus nec porta &nbsp;arcu. Nam ut convallis arcu. Suspendisse lacinia congue felis, ac rutrum &nbsp;augue dapibus quis. Etiam nibh risus, eleifend ac finibus nec, &nbsp;tincidunt quis nisi. Vivamus fermentum ipsum ut rutrum tincidunt. Etiam &nbsp;luctus molestie mi a porttitor. Pellentesque malesuada facilisis orci &nbsp;vitae dictum. Morbi sed scelerisque nisi, eu placerat lorem. Duis vel &nbsp;dolor sed nulla efficitur tincidunt tristique vitae turpis.&nbsp;</p>\n<p>Quisque auctor augue odio, vel imperdiet dui iaculis non. Nam interdum &nbsp;quis leo nec feugiat. Mauris lacinia neque et ultricies pellentesque. Ut &nbsp;sit amet iaculis est. Cras dignissim dapibus mauris. Vivamus et eros &nbsp;laoreet, condimentum ipsum in, ultrices erat. Pellentesque eget interdum &nbsp;libero, non eleifend augue. Aliquam vestibulum tincidunt varius. &nbsp;Suspendisse vitae lobortis nulla. Phasellus quis eleifend risus. Donec &nbsp;et pharetra velit. Integer porttitor mauris sed odio feugiat eleifend. &nbsp;Pellentesque nec risus sed odio iaculis auctor. Integer eleifend dui &nbsp;lorem. Etiam et magna porttitor, pellentesque ligula in, imperdiet &nbsp;tellus. Morbi in nisi aliquam, luctus lectus lobortis, congue sem.&nbsp;</p>\n<p>Pellentesque habitant morbi tristique senectus et netus et malesuada &nbsp;fames ac turpis egestas. Donec maximus quam vitae libero rutrum ornare. &nbsp;Curabitur molestie magna sapien, ut convallis velit volutpat vitae. Sed &nbsp;nec porta orci, nec varius eros. Nullam in efficitur risus. Vivamus &nbsp;tristique nulla sit amet leo molestie, eget blandit sapien hendrerit. &nbsp;Aenean sed risus eu massa semper dignissim vel sed turpis. Suspendisse &nbsp;vehicula finibus dolor vel suscipit. Sed convallis ac elit vel eleifend. &nbsp;Morbi viverra consectetur lobortis. Fusce maximus metus enim, ac &nbsp;lacinia sem vehicula imperdiet. Sed facilisis blandit elit, sed &nbsp;condimentum arcu dictum ut. Suspendisse lobortis cursus lorem quis &nbsp;vestibulum. Nunc nisl elit, tristique sed tempus ac, pellentesque quis &nbsp;risus.&nbsp;</p>'), 'Teszt recept videoval', NULL, 4, 600000, 'Big_Buck_Bunny_1080_10s_1MB.mp4', 9999);

INSERT INTO RECIPE_ADDITIONAL_IMAGES_FILE_NAMES(RECIPE_ID, ADDITIONAL_IMAGES_FILE_NAMES) VALUES
(2, 'Austria.png'),
(2, 'China.png'),
(2, 'France.png'),
(2, 'Germany.png'),
(2, 'Greece.png'),
(2, 'Italy.png'),
(2, 'Japan.png'),
(2, 'Poland.png'),
(2, 'Romania.png'),
(2, 'Russia.png'),
(2, 'Spain.png'),
(2, 'Switzerland.png'),
(2, 'Turkey.png'),
(2, 'UnitedKingdom.png'),
(2, 'UnitedStates.png');

INSERT INTO RECIPE_CATEGORIES(RECIPE_ID, CATEGORIES_ID) VALUES
(2, 2),
(2, 3);

INSERT INTO RECIPE_CUISINES(RECIPE_ID, CUISINES_ID) VALUES
(2, 16);

INSERT INTO RECIPE_INGREDIENT(ID, CREATION_DATE, MODIFICATION_DATE, QUANTITY, SELECTED_UNIT, INGREDIENT_ID, RECIPE_ID) VALUES
(2, TIMESTAMP '2019-11-22 10:20:06.099', TIMESTAMP '2019-11-22 10:20:06.099', 1, 'kg', 539, 2),
(3, TIMESTAMP '2019-11-22 10:20:06.103', TIMESTAMP '2019-11-22 10:20:06.103', 1, 'g', 704, 2),
(4, TIMESTAMP '2019-11-22 10:20:06.104', TIMESTAMP '2019-11-22 10:20:06.104', 1, 'g', 682, 2),
(5, TIMESTAMP '2019-11-22 10:20:06.105', TIMESTAMP '2019-11-22 10:20:06.105', 10, 'g', 700, 2);

