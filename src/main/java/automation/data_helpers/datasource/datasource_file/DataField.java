package automation.data_helpers.datasource.datasource_file;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class DataField {
    public String value;

    protected final Faker faker = Faker.instance();

    public abstract String name();
    public abstract int length();

    public abstract String toRawString();
}
