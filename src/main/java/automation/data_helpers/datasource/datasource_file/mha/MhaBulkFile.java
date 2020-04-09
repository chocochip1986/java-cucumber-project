package automation.data_helpers.datasource.datasource_file.mha;

import automation.data_helpers.datasource.datasource_file.mha.DataFields.BodyContent.MhaBulkBodyDataField;
import automation.data_helpers.datasource.datasource_file.mha.DataFields.DateField;
import automation.data_helpers.datasource.datasource_file.mha.DataFields.FooterOfBodyCountField;
import automation.data_setup.Phaker;
import com.google.common.base.Joiner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MhaBulkFile extends MhaFile {
    public MhaBulkFile() {
        super();
    }
    public MhaBulkFile(int bodyCount) { super(bodyCount); }

    @Override
    protected void init(int rowCount) {
        super.fileName = "mha_bulk_citizen_e2e_file.txt";
        this.header = DateField.builder()
                .value(LocalDate.now().format(Phaker.DATETIME_FORMATTER_YYYYMMDD))
                .build();
        this.body = Collections.nCopies(rowCount, 0).stream().parallel().map(i-> new MhaBulkBodyDataField()).collect(Collectors.toList());
        this.footer = new FooterOfBodyCountField(this.body.size());
    }

    public void createLoadTestFile(int count) {
        try {
            final BufferedWriter fileWriter = new BufferedWriter(new FileWriter("output_artifacts/" + this.fileName));
            fileWriter.write(this.header.toRawString() + "\n");
            final int MAX_LINE = 1000;
            final int numIterations = count / MAX_LINE;
            final int numRemain = count % MAX_LINE;

            IntStream.range(0, numIterations).forEach(i -> {
                    try {
                        fileWriter.write(Joiner.on("\n").join(Collections.nCopies(MAX_LINE, 0).stream().map(j -> new MhaBulkBodyDataField().toRawString()).collect(Collectors.toList())));
                        fileWriter.write("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            });

            IntStream.range(0, numRemain).forEach(i -> {
                try {
                    fileWriter.write(Joiner.on("\n").join(Collections.nCopies(numRemain, 0).stream().map(j -> new MhaBulkBodyDataField().toRawString()).collect(Collectors.toList())));
                    fileWriter.write("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            fileWriter.write(this.footer.toRawString());
            fileWriter.close();
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}
