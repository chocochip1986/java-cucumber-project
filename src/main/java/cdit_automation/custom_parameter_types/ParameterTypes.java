package cdit_automation.custom_parameter_types;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.BatchStatusEnum;
import io.cucumber.core.api.TypeRegistry;
import io.cucumber.core.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.CaptureGroupTransformer;
import io.cucumber.cucumberexpressions.ParameterType;

import java.util.Locale;

public class ParameterTypes implements TypeRegistryConfigurer {
    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        typeRegistry.defineParameterType(new ParameterType(
                "nricValidation",
                "[S|T|F|G][0-9]{7}[A-Z]",
                String.class,
                new CaptureGroupTransformer() {
                    @Override
                    public String transform(String[] strings) throws Throwable {
                        String rawString = strings[0];
                        rawString = rawString.substring(0, rawString.length()-1);
                        String checksum = Phaker.obtainValidNricChecksum(rawString.substring(0,1), rawString.substring(1));
                        return rawString;
                    }
                }
        ));
    }
}
