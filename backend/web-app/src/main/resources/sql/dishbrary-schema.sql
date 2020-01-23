
create table category (id bigint, creation_date timestamp, modification_date timestamp, name varchar(255), primary key (id));
create sequence category_seq start with 1 increment by 1;

create table cuisine (id bigint, creation_date timestamp, modification_date timestamp, icon_file_name varchar(255), name varchar(255), primary key (id));
create sequence cuisine_seq start with 1 increment by 1;

create table favourite_recipe (id bigint, creation_date timestamp, modification_date timestamp, recipe_id bigint, user_id bigint, primary key (id));
create sequence favourite_recipe_seq start with 1 increment by 1;
alter table favourite_recipe add constraint UK_FavRecipeUserAndRecipeId unique (user_id, recipe_id);

create table ingredient (id bigint, creation_date timestamp, modification_date timestamp, carbohydrate decimal(19,2), energykcal integer, fat decimal(19,2), image_file_name varchar(255), name varchar(255) not null unique , protein decimal(19,2), unit varchar(255), primary key (id));
create sequence ingredient_seq start with 1 increment by 1;

create table recipe (id bigint, creation_date timestamp, modification_date timestamp, additional_info varchar(255), cook_time_in_millis bigint, cover_image_file_name varchar(255), instruction clob, name varchar(255) not null, popularity_index bigint, portion integer, preparation_time_in_millis bigint, video_file_name varchar(255), owner_id bigint, primary key (id));
create sequence recipe_seq start with 3 increment by 1;

create table recipe_additional_images_file_names (recipe_id bigint not null, additional_images_file_names varchar(255));

create table recipe_categories (recipe_id bigint not null, categories_id bigint not null, primary key(recipe_id, categories_id) );

create table recipe_cuisines (recipe_id bigint not null, cuisines_id bigint not null, primary key(recipe_id, cuisines_id) );

create table recipe_ingredient (id bigint, creation_date timestamp, modification_date timestamp, quantity integer not null, selected_unit varchar(255), ingredient_id bigint, recipe_id bigint, primary key (id, recipe_id, ingredient_id));
create sequence recipe_ingredient_seq start with 1 increment by 1;

create table right (id bigint, creation_date timestamp, modification_date timestamp, name varchar(255) not null unique, primary key (id));
create sequence right_seq start with 13 increment by 1;

create table role (id bigint, creation_date timestamp, modification_date timestamp, name varchar(255) not null unique , primary key (id));
create sequence role_seq start with 3 increment by 1;

create table role_rights (roles_id bigint not null, rights_id bigint not null, primary key(roles_id, rights_id) );

create table user (id bigint, creation_date timestamp, modification_date timestamp, banned boolean, email varchar(255), expired boolean, first_name varchar(255), last_login_date date, last_name varchar(255), password varchar(255), profile_image_file_name varchar(255), username varchar(255), role_id bigint, primary key (id));
create sequence user_seq start with 1 increment by 1;

-- *************************************************************
alter table favourite_recipe add constraint FK_FavRecipeRecipe foreign key (recipe_id) references recipe;
alter table favourite_recipe add constraint FK_FavRecipeUser foreign key (user_id) references user;

alter table recipe add constraint FK_RecipeUser foreign key (owner_id) references user;

alter table recipe_additional_images_file_names add constraint FK_RecipeImagesRecipe foreign key (recipe_id) references recipe;

alter table recipe_categories add constraint FK_RecipeCatCategory foreign key (categories_id) references category;
alter table recipe_categories add constraint FK_RecipeCatRecipe foreign key (recipe_id) references recipe;

alter table recipe_cuisines add constraint FK_RecipeCuisineCuisine foreign key (cuisines_id) references cuisine;
alter table recipe_cuisines add constraint FK_RecipeCuisineRecipe foreign key (recipe_id) references recipe;

alter table recipe_ingredient add constraint FK_RecipeIngredientIngredient foreign key (ingredient_id) references ingredient;
alter table recipe_ingredient add constraint FK_RecipeIngredientRecipe foreign key (recipe_id) references recipe;

alter table role_rights add constraint FK_RoleRightsRight foreign key (rights_id) references right;
alter table role_rights add constraint FK_RoleRightsRole foreign key (roles_id) references role;

alter table user add constraint FK_UserRole foreign key (role_id) references role;