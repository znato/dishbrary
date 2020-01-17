package hu.gdf.szgd.dishbrary;

public enum RecipeSearchContextType {
	ALL_RECIPE("all"),
	USER_OWN_RECIPE("my"),
	USER_FAVOURITE_RECIPES("favourites");

	private String contextName;

	RecipeSearchContextType(String contextName) {
		this.contextName = contextName;
	}

	public static RecipeSearchContextType fromContextName(String contextName) {
		for (RecipeSearchContextType contextType : values()) {
			if (contextType.contextName.equals(contextName)) {
				return contextType;
			}
		}

		throw new IllegalArgumentException("Invalid contextName: " + contextName);
	}
}
