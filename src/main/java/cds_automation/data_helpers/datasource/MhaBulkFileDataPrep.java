package cds_automation.data_helpers.datasource;

import cds_automation.configuration.datasource.StepDefLevelTestContext;
import cds_automation.data_helpers.datasource.batch_entities.MhaBulkCitizenFileEntry;
import cds_automation.data_setup.Phaker;
import cds_automation.enums.datasource.AddressIndicatorEnum;
import cds_automation.enums.datasource.FileTypeEnum;
import cds_automation.enums.datasource.GenderEnum;
import cds_automation.enums.datasource.InvalidAddressTagEnum;
import cds_automation.enums.datasource.MhaAddressTypeEnum;
import cds_automation.enums.datasource.NationalityEnum;
import cds_automation.enums.datasource.NcaAddressTypeEnum;
import cds_automation.exceptions.TestFailException;
import cds_automation.utilities.StringUtils;
import io.cucumber.datatable.DataTable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class MhaBulkFileDataPrep extends BatchFileDataPrep {

    private static final String DEFAULT_DEATH_DATE = "00000000";
    private static final String DEFAULT_RESIDENTIAL_STATUS = "Singaporean";
    private static final String DEFAULT_ADDRESS = "MHAAddress";

    private static final String RANDOM_PEOPLE_OPTION = "RandomPeople";
    private static final String[] RESIDENTIAL_STATUS_OPTIONS = {"Singaporean", "PermanentResident", "DualCitizen"};
    private static final String[] ADDRESS_OPTIONS = {"MHAAddress", "NCAAddress", "Overseas", "InvalidAddressInd"};
    private static final String[] VALID_ADDRESS_OPTIONS = {"MHAAddress", "NCAAddress", "Overseas"};
    private static final String[] PERSON_OPTIONS = {"Alive", "Dead",
            "Male", "Female", "Unknown", "FutureBD", "FutureDD", "DeathDateBeforeBirthDate", "InvalidNric", "InvalidFin", ""};
    private static final String[] MHA_ADDRESS_OPTIONS = {"MhaAddrType", "InvalidMhaAddrType", "BlkNo", "StrtName", "FlrNo", "BuildName", "PostalCode", "NewPostalCode", "InvalidAddressTag", "InvalidInvalidAddressTag"};
    private static final String[] NCA_ADDRESS_OPTIONS = {"NcaAddrType", "InvalidNcaAddrType", "BlkNo", "StrCode", "LvlNo", "UnitNo", "PostalCode", "NewPostalCode"};

    public List<String> createBodyOfTestScenarios(List<String> listOfEntries, StepDefLevelTestContext testContext) {
        List<String> body = new ArrayList<>();

        for ( int i = 0 ; i < listOfEntries.size() ; i++ ) {
            body.addAll(mhaBulkFileEntryCreator(listOfEntries.get(i)));
        }
        return body;
    }

    private List<String> mhaBulkFileEntryCreator(String scenerioEntry) {
        if (scenerioEntry == null) {
            throw new TestFailException("There are no valid options to create an entry in the MHA Bulk File!");
        }
        List<String> optionsList = Arrays.stream(scenerioEntry.split(",")).collect(Collectors.toList());
        if (optionsList.size() == 0) {
            throw new TestFailException("There are no valid options to create an entry in the MHA Bulk File!");
        }

        return process(optionsList);
    }

    private List<String> process(List<String> optionsList) {
        int quantity = getQuantity(optionsList);
        List<String> listOfPpl = new ArrayList<>();
        String inputLine = null;
        for ( int i = 0 ; i < quantity ; i++ ) {
            if ( hasRandomPeopleOption(optionsList) ) {
                inputLine = createRandomPerson(optionsList);
//                listOfPpl.add(createRandomPerson(optionsList));
            }
            else {
                String residentialStatusOption = findResidentialStatusOption(optionsList);
                String addressOption = findAddressOptions(optionsList);
                String dob = createBirthDate(optionsList);
                inputLine = createPerson(optionsList, dob)+createAddress(addressOption, optionsList)+createResidentialStatus(optionsList, residentialStatusOption, LocalDate.parse(dob, Phaker.DATETIME_FORMATTER_YYYYMMDD));
                listOfPpl.add(inputLine);
            }
            batchFileDataWriter.chunkOrWrite(inputLine);
        }

        return listOfPpl;
    }

    private String createRandomPerson(List<String> optionsList) {
        String residentialStatusOption = RESIDENTIAL_STATUS_OPTIONS[new Random().nextInt(RESIDENTIAL_STATUS_OPTIONS.length)];
        String addressOption = VALID_ADDRESS_OPTIONS[new Random().nextInt(VALID_ADDRESS_OPTIONS.length)];
        String dob = createBirthDate(optionsList);

        return createPerson(optionsList, dob)+createAddress(addressOption, generateValidAddressOptions(addressOption))+createResidentialStatus(optionsList, residentialStatusOption, LocalDate.parse(dob, Phaker.DATETIME_FORMATTER_YYYYMMDD));
    }

    private String createResidentialStatus(List<String> optionsList, String status, LocalDate birthDate) {
        final ResidentialStatus residentialStatus = new ResidentialStatus(status, birthDate);
        List<String> usedOptions = new ArrayList<>();
        optionsList.stream()
            .map(option -> {
                if ( option.matches("^Nationality:([A-Z]{2})$")) {
                    usedOptions.add(option);
                    residentialStatus.nationality = option.substring(12);
                }
                if ( option.matches("^ctzIssDate:([\\d]{1,8})$")) {
                    usedOptions.add(option);
                    residentialStatus.ctzIssueDate = StringUtils.leftPad(option.substring(11), 8);
                }
                if ( option.matches("^dualCtz:[A-Z]$")) {
                    usedOptions.add(option);
                    residentialStatus.dualCtz = option.substring(8);
                }
                return residentialStatus;
            })
            .collect(Collectors.toList());
        optionsList.removeAll(usedOptions);
        return residentialStatus.toString();
    }

    private String createAddress(String addressOption, List<String> optionsList) {
        String line = "";
        switch(addressOption) {
            case "MHAAddress":
                line = AddressIndicatorEnum.MHA_Z.getValue()+mhaAddress(optionsList);
                break;
            case "NCAAddress":
                line = AddressIndicatorEnum.NCA.getValue()+ncaAddress(optionsList);
                break;
            case "Overseas":
                line = AddressIndicatorEnum.MHA_C.getValue()+mhaOverseasAddress(optionsList);
                break;
            case "InvalidAddressInd":
                break;
            default:
                throw new TestFailException("No such address option: "+addressOption);
        }
        return line;
    }

    private List<String> generateValidAddressOptions(String addressOption) {
        List<String> optionsList = new ArrayList<>();
        switch (addressOption) {
            case "NCAAddress":
                optionsList.addAll(generateValidNCAAddressOptions());
                break;
            case "MHAAddress":
            case "Overseas":
                optionsList.addAll(generateValidMHAAddressOptions());
                break;
            default:
                throw new TestFailException("No such address option: "+addressOption);
        }
        return optionsList;
    }

    private List<String> generateValidNCAAddressOptions() {
        String[] arrayOfNCAAddressOptions = new String[]{"BlkNo", "StrtCode", "LvlNo", "UnitNo", "OldPostalCode", "PostalCode"};
        List<String> results = new ArrayList<>();
        generateRandomValidAddressOptions(arrayOfNCAAddressOptions, results);
        return results;
    }

    private List<String> generateValidMHAAddressOptions() {
        String[] arrayOfMhaAddressOptions = new String[]{"UnitNo", "BlkNo", "StrtName", "FlrNo", "BuildingName", "PostalCode", "NewPostalCode"};
        List<String> results = new ArrayList<>();
        generateRandomValidAddressOptions(arrayOfMhaAddressOptions, results);
        return results;
    }

    private void generateRandomValidAddressOptions(String[] optionsList, List<String>results) {
        for ( int i = 0 ; i < optionsList.length ; i++ ) {
            if ( Math.random() >= 0.5 ) {
                results.add(optionsList[i]);
            }
        }
    }

    private String mhaOverseasAddress(List<String> optionsList) {
        return createMhaAddrType(optionsList, AddressIndicatorEnum.MHA_C.getValue())+createMhaAddress(optionsList);
    }

    private String mhaAddress(List<String> optionsList) {
        return createMhaAddrType(optionsList, AddressIndicatorEnum.MHA_Z.getValue())+createMhaAddress(optionsList);
    }

    private String ncaAddress(List<String> optionsList) {
        return createNcaAddress(optionsList);
    }

    private String createNcaAddress(List<String> optionsList) {
        String ncaAddrTypeRegex = "^NcaAddrType:("+String.join("|", NcaAddressTypeEnum.asList())+"|Invalid)?$";
        String invalidAddressTagRegex = "^InvalidAddressTag:("+String.join("|", InvalidAddressTagEnum.asList())+"|Invalid)?$";
        final NcaAddress ncaAddress = new NcaAddress();
        final List<String> usedOptions = new ArrayList<>();
        optionsList.stream().map(option -> {
            if (option.matches(ncaAddrTypeRegex)) {
                usedOptions.add(option);
                if(option.length() == 13) {
                    ncaAddress.ncaAddrType = option.substring(12);
                } else if ( option.matches("Invalid$") ){
                    ncaAddress.ncaAddrType = NcaAddressTypeEnum.unsupportedValue();
                }
            }
            if (option.matches("^BlkNo(:.{1,10})?$")) {
                usedOptions.add(option);
                if(option.length() == 5) {
                    ncaAddress.blkNo = StringUtils.leftPad(Phaker.validBlockNo(), 10);
                } else {
                    ncaAddress.blkNo = StringUtils.leftPad(option.substring(6), 10);
                }
            }
            if (option.matches("^StrtCode(:.{1,6})?$")) {
                usedOptions.add(option);
                if(option.length() == 8) {
                    ncaAddress.strtCode = Phaker.validNumber(6);
                } else {
                    ncaAddress.strtCode = StringUtils.leftPad(option.substring(9), 6);
                }
            }
            if(option.matches("^LvlNo(:.{1,3})?$")) {
                usedOptions.add(option);
                if(option.length() == 5) {
                    ncaAddress.lvlNo = Phaker.validNumber(3);
                } else {
                    ncaAddress.lvlNo = StringUtils.leftPad(option.substring(6), 3);
                }
            }
            if(option.matches("^UnitNo(:.{1,5})?$")) {
                usedOptions.add(option);
                if(option.length() == 6) {
                    ncaAddress.unitNo = Phaker.validNumber(5);
                } else {
                    ncaAddress.unitNo = StringUtils.leftPad(option.substring(7), 5);
                }
            }
            if(option.matches("^OldPostalCode(:.{4})?$")) {
                usedOptions.add(option);
                if(option.length() == 13) {
                    ncaAddress.oldPostalCode = Phaker.validOldPostalCode();
                } else {
                    ncaAddress.oldPostalCode = StringUtils.leftPad(option.substring(14), 4);
                }
            }
            if(option.matches("^PostalCode(:.{6})?$")) {
                usedOptions.add(option);
                if(option.length() == 10) {
                    ncaAddress.postalCode = Phaker.validPostalCode();
                } else {
                    ncaAddress.postalCode = StringUtils.leftPad(option.substring(11), 6);
                }
            }
            if (option.matches(invalidAddressTagRegex)) {
                usedOptions.add(option);
                if ( option.length() <= 19 ) {
                    ncaAddress.invalidAddressTag = InvalidAddressTagEnum.pick().toString();
                } else if ( option.substring(19).matches("Invalid$") ) {
                    ncaAddress.invalidAddressTag = InvalidAddressTagEnum.unsupportedValue();
                } else {
                    ncaAddress.invalidAddressTag = option.substring(19);
                }
            }
            return ncaAddress;
        }).collect(Collectors.toList());
        optionsList.removeAll(usedOptions);
        return ncaAddress.toString();
    }

    private String createMhaAddress(List<String> optionsList) {
        String invalidAddressTagRegex = "^InvalidAddressTag:("+String.join("|", InvalidAddressTagEnum.asList())+"|Invalid)$";
        final MhaAddress mhaAddress = new MhaAddress();
        final List<String> usedOptions = new ArrayList<>();
        optionsList.stream().map(option -> {
            if (option.matches("^UnitNo(:.{1,5})?$")) {
                usedOptions.add(option);
                if ( option.length() == 6 ) {
                    mhaAddress.unitNo = StringUtils.leftPad(Phaker.validUnitNo(), 5);
                } else {
                    mhaAddress.unitNo = StringUtils.leftPad(option.substring(7), 5);
                }
            }
            if (option.matches("^BlkNo(:.{1,10})?$")) {
                usedOptions.add(option);
                if ( option.length() == 5 ) {
                    mhaAddress.blkNo = StringUtils.leftPad(Phaker.validBlockNo(), 10);
                } else {
                    mhaAddress.blkNo = StringUtils.leftPad(option.substring(6), 10);
                }
            }
            if (option.matches("^StrtName(:.{1,32})?$")) {
                usedOptions.add(option);
                if ( option.length() == 8 ) {
                    mhaAddress.strtName = StringUtils.leftPad(Phaker.validStreetName(), 32);
                } else {
                    mhaAddress.strtName = StringUtils.leftPad(option.substring(9), 32);
                }
            }
            if (option.matches("^FlrNo(:.{1,2})?$")) {
                usedOptions.add(option);
                if ( option.length() == 5 ) {
                    mhaAddress.flrNo = StringUtils.leftPad(Phaker.validFloorNo(), 2);
                } else {
                    mhaAddress.flrNo = StringUtils.leftPad(option.substring(6), 2);
                }
            }
            if (option.matches("^BuildingName(:.{1,30})?$")) {
                usedOptions.add(option);
                if ( option.length() == 12 ) {
                    mhaAddress.buildName = StringUtils.leftPad(Phaker.validBuildingName(), 30);
                } else {
                    mhaAddress.buildName = StringUtils.leftPad(option.substring(13), 30);
                }
            }
            if (option.matches("^PostalCode(:[\\d]{4})?$")) {
                usedOptions.add(option);
                if ( option.length() == 10 ) {
                    mhaAddress.oldPostalCode = StringUtils.leftPad(Phaker.validOldPostalCode(), 4);
                } else {
                    mhaAddress.oldPostalCode = option.substring(11);
                }
            }
            if (option.matches("^NewPostalCode(:[\\d]{6})?$")) {
                usedOptions.add(option);
                if ( option.length() == 13 ) {
                    mhaAddress.postalCode = Phaker.validPostalCode();
                } else {
                    mhaAddress.postalCode = option.substring(14);
                }
            }
            if (option.matches(invalidAddressTagRegex)) {
                usedOptions.add(option);
                if ( option.substring(19).matches("Invalid$") ) {
                    mhaAddress.invalidAddressTag = InvalidAddressTagEnum.unsupportedValue();
                } else {
                    mhaAddress.invalidAddressTag = option.substring(19);
                }
            }
            return mhaAddress;
        }).collect(Collectors.toList());
        optionsList.removeAll(usedOptions);
        return mhaAddress.toString();
    }

    private String createMhaAddrType(List<String> optionsList, String mhaAddressIndicator) {
        String mhaAddrTypes = String.join("|", MhaAddressTypeEnum.asList());
        String mhaAddrTypesRegex = "^mhaAddrType:("+mhaAddrTypes+"|Invalid)$";
        return optionsList
                .stream()
                .filter(option -> option.matches(mhaAddrTypesRegex))
                .findFirst()
                .map(mhaAddrTypeOption -> {
                    optionsList.remove(mhaAddrTypeOption);
                    if(mhaAddrTypeOption.matches("Invalid$")) {
                        return MhaAddressTypeEnum.unsupportedValue();
                    } else {
                        return mhaAddrTypeOption.substring(12);
                    }
                })
                .orElse(mhaAddressIndicator.equals("C") ? MhaAddressTypeEnum.OVERSEAS_ADDRESS.getValue() : MhaAddressTypeEnum.pick().toString());
    }

    private String createPerson(List<String> optionsList, String dob) {
        return createNric(optionsList)+createFin(optionsList)+createName(optionsList)+dob+createDeathDate(optionsList, dob)+createGender(optionsList);
    }

    private String createFin(List<String> optionsList) {
        return optionsList
                .stream()
                .filter(option -> option.matches("^Fin:(.{9}|Invalid)$"))
                .findFirst()
                .map(option -> {
                    if ( option.substring(5).matches("Invalid") ) {
                        optionsList.remove(option);
                        return Phaker.invalidNric();
                    } else {
                        optionsList.remove(option);
                        return option.substring(4);
                    }
                })
                .orElse("         ");
    }

    private String createNric(List<String> optionsList) {
        return optionsList
                .stream()
                .filter(option -> option.matches("^Nric:(.{9}|Invalid)$"))
                .findFirst().map(option -> {
                    if ( option.substring(5).matches("Invalid") ) {
                        optionsList.remove(option);
                        return Phaker.invalidNric();
                    } else {
                        optionsList.remove(option);
                        return option.substring(5);
                    }
                })
                .orElse(Phaker.validNric());
    }

    private String createGender(List<String> optionsList) {
        return optionsList
                .stream()
                .filter(option -> option.matches("^(M|F|U|InvalidGender)$"))
                .findFirst()
                .map(option -> {
                    if ( option.equals("InvalidGender") ) {
                        optionsList.remove(option);
                        return "A";
                    } else {
                        GenderEnum genderEnum = GenderEnum.fromString(option);
                        optionsList.remove(option);
                        return genderEnum.getValue();
                    }
                })
                .orElse(GenderEnum.MALE.getValue());
    }

    private String createDeathDate(List<String> optionsList, String dob) {
        LocalDate birthDate = LocalDate.parse(dob, Phaker.DATETIME_FORMATTER_YYYYMMDD);
        return optionsList
                .stream()
                .filter(option -> option.matches("^DoD(:(DeathBeforeBirth|FutureDod|Spaces|[\\d]{8}))?$"))
                .findFirst().map(option -> {
                    try {
                        if ( option.length() == 4 ) {
                            optionsList.remove(option);
                            return Phaker.validDateFromRange(birthDate, dateUtils.now()).format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
                        } else if ( option.substring(4).matches("^DeathBeforeBirth$") ) {
                            optionsList.remove(option);
                            return birthDate.minusDays(1L).format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
                        } else if ( option.substring(4).matches("^FutureDod$") ) {
                            optionsList.remove(option);
                            return birthDate.plusDays(1L).format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
                        } else if ( option.substring(4).matches("^[\\d]{8}$") ) {
                            optionsList.remove(option);
                            return LocalDate.parse(option.substring(4), Phaker.DATETIME_FORMATTER_YYYYMMDD).format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
                        } else if ( option.substring(4).matches("^Spaces$") ) {
                            optionsList.remove(option);
                            return "        ";
                        } else {
                            return null;
                        }
                    } catch ( DateTimeParseException | IndexOutOfBoundsException e ) {
                        return null;
                    }
                })
                .orElse(DEFAULT_DEATH_DATE);
    }

    private String createBirthDate(List<String> optionsList) {
        return optionsList
                .stream()
                .filter(option -> option.matches("^DoB:([0-9]{8}|FutureDoB)$"))
                .findFirst().map(option -> {
                    try {
                        optionsList.remove(option);
                        if ( option.substring(4).equals("FutureDoB") ) {
                            return dateUtils.tomorrow().format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
                        } else {
                            return LocalDate.parse(option.substring(4), Phaker.DATETIME_FORMATTER_YYYYMMDD).format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
                        }
                    }
                    catch (DateTimeParseException e) {
                        optionsList.remove(option);
                        return null;
                    }
                }).orElse(Phaker.validDateFromRange(Phaker.defaultLowerBoundaryDate, dateUtils.yearsBeforeToday(13) ).format(Phaker.DATETIME_FORMATTER_YYYYMMDD));
    }

    private String createName(List<String> optionsList) {
        return optionsList
                .stream()
                .filter(option -> option.matches("^Name:(.{1,66})$"))
                .findFirst().map(option -> StringUtils.leftPad(option.substring(5), 66) ).orElse(StringUtils.leftPad(Phaker.validName(), 66));
    }

    private String findAddressOptions(List<String> optionsList) {
        for ( int i = 0 ;  i < ADDRESS_OPTIONS.length ; i++ ) {
            if ( optionsList.contains(ADDRESS_OPTIONS[i]) ) {
                optionsList.remove(ADDRESS_OPTIONS[i]);
                return ADDRESS_OPTIONS[i];
            }
        }
        return DEFAULT_ADDRESS;
    }

    private String findResidentialStatusOption(List<String> optionsList) {
        for ( int i = 0 ; i < RESIDENTIAL_STATUS_OPTIONS.length ; i++ ) {
            if ( optionsList.contains(RESIDENTIAL_STATUS_OPTIONS[i]) ) {
                optionsList.remove(RESIDENTIAL_STATUS_OPTIONS[i]);
                return RESIDENTIAL_STATUS_OPTIONS[i];
            }
        }
        return DEFAULT_RESIDENTIAL_STATUS;
    }

    private boolean hasRandomPeopleOption(List<String> optionsList) {
        if ( optionsList.contains(RANDOM_PEOPLE_OPTION)) {
            return true;
        }
        else {
            return false;
        }
    }

    private int getQuantity(List<String> optionsList) {
        String quantity = optionsList
                .stream()
                .filter(option -> option.matches("^Quantity:[1-9][0-9]*$"))
                .findFirst()
                .map(option -> {
                    optionsList.remove(option);
                    return option;
                })
                .orElse("Quantity:1");
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher match = pattern.matcher(quantity);
        if ( match.find() ) {
            return Integer.valueOf(match.group());
        }
        else {
            return 1;
        }
    }

    public List<String> formatEntries(DataTable dataTable) {
        List<Map<String, String>> dataRows = dataTable.asMaps(String.class, String.class);
        List<MhaBulkCitizenFileEntry> entries =
                dataRows.stream().map(MhaBulkCitizenFileEntry::new).collect(Collectors.toList());
        return entries.stream().map(MhaBulkCitizenFileEntry::toRawString).collect(Collectors.toList());
    }

    public void writeToFile(String dateStr, List<String> entries) {
        writeToFile(dateStr, dateStr, entries);
    }

    public void writeToFile(String dateOfRunStr, String cutOffDateStr, List<String> entries) {
        LocalDate dateOfRun = dateUtils.parse(dateOfRunStr);
        LocalDate cutOffDate = dateUtils.parse(cutOffDateStr);
        batchFileDataWriter.begin(generateDoubleHeader(dateOfRun, cutOffDate), FileTypeEnum.MHA_BULK_CITIZEN, 10);
        entries.forEach(entry -> batchFileDataWriter.chunkOrWrite(entry));
        batchFileDataWriter.end();
    }

    public String resolveDateString(String dateStr) {
        if (dateStr.equalsIgnoreCase("empty")) {
            return "";
        }

        if (dateStr.equalsIgnoreCase("space")) {
            return StringUtils.rightPad("", 8, StringUtils.SPACE);
        }

        if (dateStr.startsWith("CurrentDate")) {
            LocalDate result = LocalDate.now();

            String[] tokens = dateStr.split(StringUtils.SPACE);
            if (tokens.length == 1) {
                return result.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD);
            }

            long operand = Long.parseLong(tokens[2]);
            String operator = tokens[1];
            if (operator.matches("-")) {
                result = result.minusDays(operand);
            } else {
                result = result.plusDays(operand);
            }

            return result.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD);
        }

        return dateStr;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    private class NcaAddress {
       public String ncaAddrType;
       public String blkNo;
       public String strtCode;
       public String lvlNo;
       public String unitNo;
       public String oldPostalCode;
       public String postalCode;
       public String invalidAddressTag;

       public String toString(){
           return ncaAddrType()+blkNo()+strtCode()+lvlNo()+unitNo()+oldPostalCode()+postalCode()+invalidAddressTag()+whiteSpaces(55);
       }

       public String ncaAddrType(){
           return ncaAddrType == null ? NcaAddressTypeEnum.pick().getValue() : ncaAddrType;
       }

       public String blkNo() {
           return blkNo == null ? whiteSpaces(10) : blkNo;
       }

       public String strtCode() {
           return strtCode == null ? whiteSpaces(6) : strtCode;
       }

       public String lvlNo() {
           return lvlNo == null ? whiteSpaces(3) : lvlNo;
       }

       public String unitNo() {
           return unitNo == null ? whiteSpaces(5) : unitNo;
       }

       public String oldPostalCode() {
           return oldPostalCode == null ? whiteSpaces(4) : oldPostalCode;
       }

       public String postalCode() {
           return postalCode == null ? whiteSpaces(6) : postalCode;
       }

       public String invalidAddressTag() {
           if ( invalidAddressTag == null ) {
               invalidAddressTag = InvalidAddressTagEnum.pick().toString();
           }
           if ( invalidAddressTag.isEmpty() ) {
               return whiteSpaces(1);
           } else {
               return invalidAddressTag;
           }

       }

        private String whiteSpaces(int size){
            return String.format("%-"+String.valueOf(size)+"s", "");
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    private class MhaAddress {
        public String blkNo;
        public String strtName;
        public String flrNo;
        public String unitNo;
        public String buildName;
        public String oldPostalCode;
        public String postalCode;
        public String invalidAddressTag;

        public String toString() {
            return blkNo()+strtName()+flrNo()+unitNo()+buildName()+oldPostalCode()+postalCode()+invalidAddressTag();
        }

        public String invalidAddressTag() {
            if ( invalidAddressTag == null ) {
                invalidAddressTag = InvalidAddressTagEnum.pick().toString();
            }
            if ( invalidAddressTag.isEmpty() ) {
                return whiteSpaces(1);
            } else {
                return invalidAddressTag;
            }
        }

        public String postalCode() {
            return postalCode == null ? whiteSpaces(6) : postalCode;
        }

        public String oldPostalCode() {
            return oldPostalCode == null ? whiteSpaces(4) : oldPostalCode;
        }

        public String buildName() {
            return buildName == null ? whiteSpaces(30) : buildName;
        }

        public String unitNo () {
            return unitNo == null ? whiteSpaces(5) : unitNo;
        }

        public String flrNo() {
            return flrNo == null ? whiteSpaces(2) : flrNo;
        }

        public String blkNo() {
            return blkNo == null ? whiteSpaces(10) : blkNo;
        }

        public String strtName() {
            return strtName == null ? whiteSpaces(32) : strtName;
        }

        private String whiteSpaces(int size){
            return String.format("%-"+String.valueOf(size)+"s", "");
        }
    }

    private class ResidentialStatus {
        public String nationality;
        public String ctzIssueDate;
        public String dualCtz;

        public ResidentialStatus(String nationality, LocalDate birthDate) {
            if(nationality.equals("Singaporean")) {
                this.nationality = NationalityEnum.SG.getValue();
                this.ctzIssueDate = birthDate.format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
                this.dualCtz = "N";
            } else if(nationality.equals("PermanentResident")) {
                this.nationality = NationalityEnum.PR.getValue();
                this.ctzIssueDate = birthDate.format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
                this.dualCtz = "N";
            } else if(nationality.equals("DualCitizen")) {
                this.nationality = "  ";
                this.ctzIssueDate = birthDate.format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
                this.dualCtz = "Y";
            } else if (nationality.equals("Foreigner")) {
                this.nationality = NationalityEnum.AF.getValue();
                this.ctzIssueDate = whiteSpaces(8);
                this.dualCtz = "N";
            } else {
                this.nationality = NationalityEnum.SG.getValue();
                this.ctzIssueDate = birthDate.format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
                this.dualCtz = "N";
            }
        }

        public String toString() {
            return nationality()+ctzIssueDate()+dualCtz();
        }

        public String nationality() {
            return nationality == null ? whiteSpaces(2) : nationality;
        }

        public String ctzIssueDate() {
            return ctzIssueDate == null ? whiteSpaces(8) : ctzIssueDate;
        }

        public String dualCtz() {
            return dualCtz == null ? whiteSpaces(1) : dualCtz;
        }

        private String whiteSpaces(int size){
            return String.format("%-"+String.valueOf(size)+"s", "");
        }
    }
}
