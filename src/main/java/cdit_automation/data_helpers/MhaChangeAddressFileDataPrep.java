package cdit_automation.data_helpers;

import cdit_automation.configuration.StepDefLevelTestContext;
import cdit_automation.data_helpers.batch_entities.AddressFileEntry;
import cdit_automation.data_helpers.batch_entities.MhaAddressFileEntry;
import cdit_automation.data_helpers.batch_entities.MhaChangeAddressFileEntry;
import cdit_automation.data_helpers.batch_entities.MhaOverseasFileEntry;
import cdit_automation.data_helpers.batch_entities.NcaAddressFileEntry;
import cdit_automation.data_setup.PhakAddress;
import cdit_automation.data_setup.Phaker;
import cdit_automation.data_setup.data_setup_address.PhakAbstractAddress;
import cdit_automation.enums.AddressIndicatorEnum;
import cdit_automation.enums.FormatType;
import cdit_automation.enums.automation.PropertyTypeEnum;
import cdit_automation.exceptions.TestDataSetupErrorException;
import cdit_automation.models.PersonId;
import cdit_automation.models.PropertyDetail;
import cdit_automation.utilities.DateUtils;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MhaChangeAddressFileDataPrep extends BatchFileDataPrep {

    private static final String PERSON = "person";
    private static final String PREVIOUS_ADDRESS = "previous_address";
    private static final String CURRENT_ADDRESS = "current_address";
    private static final String ADDRESS_CHANGE_DATE = "address_change_dte";
    
    private static final int NO_OF_IDENTIFIERS_FOR_EXISTING_ADDR = 1;
    private static final int NO_OF_IDENTIFIERS_FOR_NEW_ADDR = 7;
    private static final String IDENTIFIER_DELIMITER = ",";
    private static final String KEY_VALUE_DELIMITER = ":";
    
    // Address identifiers
    private static final String EXISTING = "Existing";
    private static final String INDICATOR_TYPE = "IndType";
    private static final String BLOCK = "Block";
    private static final String STREET = "Street";
    private static final String UNIT = "Unit";
    private static final String FLOOR = "Floor";
    private static final String BUILDING = "Building";
    private static final String POSTAL = "Postal";
    
    public void createBodyOfTestScenarios(List<Map<String, String>> changeAddressList, StepDefLevelTestContext testContext) {
        
        for (Map<String, String> changeAddress : changeAddressList) {
            
            String personKey = changeAddress.get(PERSON);
            String previousAddressString = changeAddress.get(PREVIOUS_ADDRESS);
            String currentAddressString = changeAddress.get(CURRENT_ADDRESS);
            String addressChangeDateString = changeAddress.get(ADDRESS_CHANGE_DATE);

            PersonId personId = testContext.get(personKey);
            
            Pair<AddressIndicatorEnum, PropertyDetail> prevPropertyDetailPair = processAddressString(previousAddressString, testContext);
            Pair<AddressIndicatorEnum, PropertyDetail> curPropertyDetailPair = processAddressString(currentAddressString, testContext);
            
            AddressIndicatorEnum prevIndType = prevPropertyDetailPair.getKey();
            PropertyDetail prevPropertyDetail = prevPropertyDetailPair.getValue();

            AddressIndicatorEnum curIndType = curPropertyDetailPair.getKey();
            PropertyDetail curPropertyDetail = curPropertyDetailPair.getValue();
            
            LocalDate addressChangeDate = new DateUtils().parse(addressChangeDateString);
            
            createLineInBody(personId, prevIndType, prevPropertyDetail, curIndType, curPropertyDetail, addressChangeDate);
        }
    }
    
    private Pair<AddressIndicatorEnum, PropertyDetail> processAddressString(String addressString, StepDefLevelTestContext testContext) {

        List<String> optionsList = 
                Arrays.stream(addressString.split(IDENTIFIER_DELIMITER)).collect(Collectors.toList());
        
        int size = optionsList.size();
        
        if (NO_OF_IDENTIFIERS_FOR_EXISTING_ADDR == size) {
            
            String[] keyValue = optionsList.get(0).split(KEY_VALUE_DELIMITER);

            if (!EXISTING.equals(keyValue[0])) {
                throw new TestDataSetupErrorException("'" + EXISTING + "' must always be the parameter name when retrieving current address.");
            }

            PropertyDetail propertyDetail = testContext.get(keyValue[1]);
            AddressIndicatorEnum addressIndicator = (FormatType.NCA.equals(propertyDetail.getFormatType())) ? AddressIndicatorEnum.NCA : AddressIndicatorEnum.MHA_Z;
            
            return new Pair<>(addressIndicator, propertyDetail);
           
        } else if (NO_OF_IDENTIFIERS_FOR_NEW_ADDR == size) {
            
            String[] keyValue = optionsList.get(0).split(KEY_VALUE_DELIMITER);
            
            if (!INDICATOR_TYPE.equals(keyValue[0])) {
                throw new TestDataSetupErrorException("'" + INDICATOR_TYPE + "' must always be the first parameter.");
            }
            
            AddressIndicatorEnum addressIndicatorEnum = AddressIndicatorEnum.fromString(keyValue[1]);
            PropertyDetail propertyDetail = new PropertyDetail();
            
            String option;
            String key;
            String value;
            
            for (int i = 1; i < size; i ++) {
                
                option = optionsList.get(i);
                keyValue = option.split(KEY_VALUE_DELIMITER);
                
                key = keyValue[0].trim();
                value = keyValue[1].trim();

                switch (key) {
                    case BLOCK:
                        propertyDetail.setBlockNumber(value);
                        break;
                    case STREET:
                        if (AddressIndicatorEnum.NCA.equals(addressIndicatorEnum)) {
                            propertyDetail.setStreetCode(value);
                            propertyDetail.setFormatType(FormatType.NCA);
                        } else {
                            propertyDetail.setStreetName(value);
                            propertyDetail.setFormatType(FormatType.MHA);
                        }
                        break;
                    case UNIT:
                        propertyDetail.setUnit(value);
                        break;
                    case FLOOR:
                        propertyDetail.setFloor(value);
                        break;
                    case BUILDING:
                        propertyDetail.setBuildingName(value);
                        break;
                    case POSTAL:
                        propertyDetail.setNewPostalCode(value);
                        break;
                    default:
                        throw new TestDataSetupErrorException("Error in creating new address in test data file.");
                }
            }
            
            return new Pair<>(addressIndicatorEnum, propertyDetail);
            
        } else {
            throw new TestDataSetupErrorException("Incorrect number of address parameters.");
        }
    }    

    public void createLineInBody(PersonId personId, AddressIndicatorEnum prevIndType, PropertyDetail prevPropertyDetail, AddressIndicatorEnum curIndType, PropertyDetail curPropertyDetail, LocalDate addressChangeDate) {
        MhaChangeAddressFileEntry mhaChangeAddressFileEntry = new MhaChangeAddressFileEntry(personId.getNaturalId(),
                prevIndType,
                getAddressFileEntryFrom(prevIndType, prevPropertyDetail),
                curIndType,
                getAddressFileEntryFrom(curIndType, curPropertyDetail),
                addressChangeDate.format(Phaker.DATETIME_FORMATTER_YYYYMMDD));
        batchFileDataWriter.chunkOrWrite(mhaChangeAddressFileEntry.toString());
    }

    public void createLineInBodyWithNewCurAddress(PersonId personId, AddressIndicatorEnum prevIndType, PropertyDetail prevPropertyDetail, AddressIndicatorEnum curIndType, PhakAbstractAddress phakAbstractAddress, LocalDate addressChangeDate) {
        MhaChangeAddressFileEntry mhaChangeAddressFileEntry = new MhaChangeAddressFileEntry(personId.getNaturalId(),
                prevIndType,
                getAddressFileEntryFrom(prevIndType, prevPropertyDetail),
                curIndType,
                getAddressFileEntryFrom(curIndType, phakAbstractAddress),
                addressChangeDate.format(Phaker.DATETIME_FORMATTER_YYYYMMDD));
        batchFileDataWriter.chunkOrWrite(mhaChangeAddressFileEntry.toString());
    }

    public void createLineInBodyWithNewPrevAddress(PersonId personId, AddressIndicatorEnum prevIndType, PropertyTypeEnum prevPropertyTypeEnum, AddressIndicatorEnum curIndType, PropertyDetail propertyDetail, LocalDate addressChangeDate) {
        PhakAbstractAddress phakAddress = PhakAddress.suggestAnAddress(prevPropertyTypeEnum);
        MhaChangeAddressFileEntry mhaChangeAddressFileEntry = new MhaChangeAddressFileEntry(personId.getNaturalId(),
                prevIndType,
                getAddressFileEntryFrom(curIndType, phakAddress),
                curIndType,
                getAddressFileEntryFrom(prevIndType, propertyDetail),
                addressChangeDate.format(Phaker.DATETIME_FORMATTER_YYYYMMDD));
        batchFileDataWriter.chunkOrWrite(mhaChangeAddressFileEntry.toString());
    }

    public void createLineInBodyWithNewPrevAndCurAddress(PersonId personId, AddressIndicatorEnum prevIndType, PropertyTypeEnum prevPropertyTypeEnum, AddressIndicatorEnum curIndType, PropertyTypeEnum curPropertyTypeEnum, LocalDate addressChangeDate) {
        PhakAbstractAddress prevPhakAddress = PhakAddress.suggestAnAddress(prevPropertyTypeEnum);
        PhakAbstractAddress curPhakAddress = PhakAddress.suggestAnAddress(curPropertyTypeEnum);
        MhaChangeAddressFileEntry mhaChangeAddressFileEntry = new MhaChangeAddressFileEntry(personId.getNaturalId(),
                prevIndType,
                getAddressFileEntryFrom(curIndType, prevPhakAddress),
                curIndType,
                getAddressFileEntryFrom(prevIndType, curPhakAddress),
                addressChangeDate.format(Phaker.DATETIME_FORMATTER_YYYYMMDD));
        batchFileDataWriter.chunkOrWrite(mhaChangeAddressFileEntry.toString());
    }

    private AddressFileEntry getAddressFileEntryFrom(AddressIndicatorEnum addressIndicatorEnum, PhakAbstractAddress phakAbstractAddress) {
        if ( addressIndicatorEnum.equals(AddressIndicatorEnum.NCA) ) {
            return new NcaAddressFileEntry(phakAbstractAddress.getUnitNo(),
                    phakAbstractAddress.getFloorNo(),
                    phakAbstractAddress.getBlockNo(),
                    Phaker.validNumber(6),
                    phakAbstractAddress.getOldPostalCode(),
                    phakAbstractAddress.getPostalCode());
        } else if ( addressIndicatorEnum.equals(AddressIndicatorEnum.MHA_C) ) {
            return new MhaOverseasFileEntry(phakAbstractAddress.getUnitNo(),
                    phakAbstractAddress.getFloorNo(),
                    phakAbstractAddress.getBlockNo(),
                    phakAbstractAddress.getStreetName(),
                    phakAbstractAddress.getBuildingName(),
                    phakAbstractAddress.getOldPostalCode(),
                    phakAbstractAddress.getPostalCode());
        } else {
            return new MhaAddressFileEntry(phakAbstractAddress.getUnitNo(),
                    phakAbstractAddress.getFloorNo(),
                    phakAbstractAddress.getBlockNo(),
                    phakAbstractAddress.getStreetName(),
                    phakAbstractAddress.getBuildingName(),
                    phakAbstractAddress.getOldPostalCode(),
                    phakAbstractAddress.getPostalCode());
        }
    }

    private AddressFileEntry getAddressFileEntryFrom(AddressIndicatorEnum addressIndicatorEnum, PropertyDetail propertyDetail) {
        if ( addressIndicatorEnum.equals(AddressIndicatorEnum.NCA) ) {
            return new NcaAddressFileEntry(propertyDetail.getUnit(),
                    propertyDetail.getFloor(),
                    propertyDetail.getBlockNumber(),
                    propertyDetail.getStreetCode(),
                    propertyDetail.getPostalCode(),
                    propertyDetail.getNewPostalCode());
        } else if ( addressIndicatorEnum.equals(AddressIndicatorEnum.MHA_C) ) {
            return new MhaOverseasFileEntry(propertyDetail.getUnit(),
                    propertyDetail.getFloor(),
                    propertyDetail.getBlockNumber(),
                    propertyDetail.getStreetName(),
                    propertyDetail.getBuildingName(),
                    propertyDetail.getPostalCode(),
                    propertyDetail.getNewPostalCode());
        } else {
            return new MhaAddressFileEntry(propertyDetail.getUnit(),
                    propertyDetail.getFloor(),
                    propertyDetail.getBlockNumber(),
                    propertyDetail.getStreetName(),
                    propertyDetail.getBuildingName(),
                    propertyDetail.getPostalCode(),
                    propertyDetail.getNewPostalCode());
        }
    }
}
