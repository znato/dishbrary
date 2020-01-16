package hu.gdf.szgd.dishbrary.transformer;

import java.util.HashSet;
import java.util.Set;

public class TransformerConfig {
	private Set<String> fieldsForExclusionSet;
	private Set<String> includeOnlyFieldSet;

	private TransformerConfig(String[] fieldNames, boolean isExclude) {

		Set<String> fieldNameSet = new HashSet<>(fieldNames.length);

		for (String fieldName : fieldNames) {
			fieldNameSet.add(fieldName);
		}

		if (isExclude) {
			fieldsForExclusionSet = fieldNameSet;
		} else {
			includeOnlyFieldSet = fieldNameSet;
		}
	}

	public static TransformerConfig includeOnlyFields(String... fieldNames) {
		return new TransformerConfig(fieldNames, false);
	}

	public static TransformerConfig excludeFields(String... fieldNames) {
		return new TransformerConfig(fieldNames, true);
	}

	public Set<String> getFieldsForExclusionSet() {
		return fieldsForExclusionSet;
	}

	public Set<String> getIncludeOnlyFieldSet() {
		return includeOnlyFieldSet;
	}

	public boolean isFieldExcluded(String fieldName) {
		boolean isFieldIncluded = includeOnlyFieldSet != null && includeOnlyFieldSet.contains(fieldName);

		if (isFieldIncluded) {
			return false;
		}

		return fieldsForExclusionSet == null || fieldsForExclusionSet.contains(fieldName);
	}

	public static boolean isFieldExcludedInConfig(TransformerConfig config, String fieldName) {
		return config != null && config.isFieldExcluded(fieldName);
	}
}
