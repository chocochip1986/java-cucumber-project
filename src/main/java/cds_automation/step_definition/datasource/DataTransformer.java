package cds_automation.step_definition.datasource;

import cds_automation.data_helpers.datasource.datasource_file.mha.TestDataStruct.G1224TestData;
import cds_automation.data_helpers.datasource.datasource_file.mha.TestDataStruct.LoadTestData;
import cds_automation.enums.datasource.FileTypeEnum;
import io.cucumber.core.api.TypeRegistry;
import io.cucumber.core.api.TypeRegistryConfigurer;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableEntryTransformer;
import org.assertj.core.util.Strings;

import java.util.Locale;
import java.util.Map;

public class DataTransformer implements TypeRegistryConfigurer {
    public Locale locale() {
        return Locale.ENGLISH;
    }
    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        typeRegistry.defineDataTableType(new DataTableType(G1224TestData.class, (TableEntryTransformer<G1224TestData>) map ->
            G1224TestData.builder()
                .nric(initField(map, "nric"))
                .fin(initField(map, "fin"))
                .dateOfBirth(initField(map, "dateOfBirth"))
                .name(initField(map, "name"))
                .attainmentDate(initField(map, "attainmentDate"))
                .ceasedDate(initField(map, "ceasedDate"))
                .fileName(initField(map, "fileName"))
                .validFrom(initField(map, "validFrom"))
                .validTill(initField(map, "validTill"))
                .nationality(initField(map, "nationality"))
                .fileType(FileTypeEnum.fromString(initField(map, "fileType")))
            .build()));

        typeRegistry.defineDataTableType(new DataTableType(LoadTestData.class, (TableEntryTransformer<LoadTestData>) map ->
            LoadTestData.builder()
                    .fileName(initField(map, "fileName"))
                    .count(Integer.parseInt(initField(map, "count")))
                    .fileType(FileTypeEnum.fromString(initField(map, "fileType")))
                    .build()));
    }

    private String initField(Map<String, String> map, String key) {
        return containsKeyAndIsNullOrEmptyValue(map, key) ? "" : map.get(key);
    }

    private boolean containsKeyAndIsNullOrEmptyValue(Map<String, String> map, String key) {
        return map.containsKey(key) && Strings.isNullOrEmpty(map.get(key));
    }
}