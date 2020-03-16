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
import cdit_automation.enums.automation.PropertyTypeEnum;
import cdit_automation.models.PersonId;
import cdit_automation.models.PropertyDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MhaChangeAddressFileDataPrep extends BatchFileDataPrep {
    public void createBodyOfTestScenarios(List<Map<String, String>> listOfAddresses, StepDefLevelTestContext testContext) {
        for(Map<String, String> changeAddressRow : listOfAddresses) {

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
