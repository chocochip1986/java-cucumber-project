package cdit_automation.data_helpers.datasource_file.mha;

import cdit_automation.configuration.TestManager;
import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.BodyContent.MhaBodyDataField;
import com.github.javafaker.Faker;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

@Getter
@Setter
public abstract class MhaFile {
    public DataField header;
    public List<MhaBodyDataField> body;
    public DataField footer;

    public String fileName;

    public final Faker faker = Faker.instance();

    public MhaFile() {
        init(1);
    }

    public MhaFile(int rowCount) {
        init(rowCount);
    }

    /**
     * Initialize this.body
     * @param rowCount number of rows in file
     */
    protected abstract void init(int rowCount);

    public String toRawString() {
        return new StringBuilder()
                .append(header.toRawString())
                .append(body.stream().map(i -> i.toRawString()).reduce("", (partialString, element) -> partialString + "\n" + element))
                .append("\n")
                .append(footer.toRawString()).toString();
    }

    public void createFile() {
        try {
            final BufferedWriter fileWriter = new BufferedWriter(new FileWriter("output_artifacts/" + this.fileName));
            fileWriter.write(this.toRawString());
            fileWriter.close();
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}
