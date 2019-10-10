package hu.gdf.szgd.dishbrary.db.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gdf.szgd.dishbrary.db.entity.Recipe;

import javax.persistence.AttributeConverter;
import java.io.IOException;

public class RecipeAdditionalInfoJsonConverter implements AttributeConverter<Recipe.AdditionalInfo, String> {

	private ObjectMapper jsonMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(Recipe.AdditionalInfo additionalInfo) {
		try {
			return jsonMapper.writeValueAsString(additionalInfo);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("AdditionalInfoJson cannot be converted to json string", e);
		}
	}

	@Override
	public Recipe.AdditionalInfo convertToEntityAttribute(String jsonString) {
		try {
			return jsonMapper.readValue(jsonString, Recipe.AdditionalInfo.class);
		} catch (IOException e) {
			throw new IllegalArgumentException("Json string cannot be converted to AdditionalInfoJson", e);
		}
	}

}
