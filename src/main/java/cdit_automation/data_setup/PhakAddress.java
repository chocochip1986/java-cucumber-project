package cdit_automation.data_setup;

import cdit_automation.data_setup.data_setup_address.PhakAbstractAddress;
import cdit_automation.data_setup.data_setup_address.PhakIslandAddress;
import cdit_automation.enums.PropertyTypeEnum;
import cdit_automation.exceptions.TestFailException;

import java.util.Random;

public class PhakAddress {
    /*
    Postal district         | Postal sector (1st 2 digits of 6-digit postal codes) | General location
    01                      | 01, 02, 03, 04, 05, 06                               | Raffles Place, Cecil, Marina, People's Park
    02                      | 07, 08                                               | Anson, Tanjong Pagar
    03                      | 14, 15, 16                                           | Bukit Merah, Queenstown, Tiong Bahru
    04                      | 09, 10                                               | Telok Blangah, Harbourfront
    05                      | 11, 12, 13                                           | Pasir Panjang, Hong Leong Garden, Clementi New Town
    06                      | 17                                                   | High Street, Beach Road (part)
    07                      | 18, 19                                               | Middle Road, Golden Mile
    08                      | 20, 21                                               | Little India, Farrer Park, Jalan Besar, Lavender
    09                      | 22, 23                                               | Orchard, Cairnhill, River Valley
    10                      | 24, 25, 26, 27                                       | Ardmore, Bukit Timah, Holland Road, Tanglin
    11                      | 28, 29, 30                                           | Watten Estate, Novena, Thomson
    12                      | 31, 32, 33                                           | Balestier, Toa Payoh, Serangoon
    13                      | 34, 35, 36, 37                                       | Macpherson, Braddell
    14                      | 38, 39, 40, 41                                       | Geylang, Eunos
    15                      | 42, 43, 44, 45                                       | Katong, Joo Chiat, Amber Road
    16                      | 46, 47, 48                                           | Bedok, Upper East Coast, Eastwood, Kew Drive
    17                      | 49, 50, 81                                           | Loyang, Changi
    18                      | 51, 52                                               | Simei, Tampines, Pasir Ris
    19                      | 53, 54, 55, 82                                       | Serangoon Garden, Hougang, Punggol
    20                      | 56, 57                                               | Bishan, Ang Mo Kio
    21                      | 58, 59                                               | Upper Bukit Timah, Clementi Park, Ulu Pandan
    22                      | 60, 61, 62, 63, 64                                   | Jurong, Tuas
    23                      | 65, 66, 67, 68                                       | Hillview, Dairy Farm, Bukit Panjang, Choa Chu Kang
    24                      | 69, 70, 71                                           | Lim Chu Kang, Tengah
    25                      | 72, 73                                               | Kranji, Woodgrove, Woodlands
    26                      | 77, 78                                               | Upper Thomson, Springleaf
    27                      | 75, 76                                               | Yishun, Sembawang
    28                      | 79, 80                                               | Seletar
     */

    public static final String[] POSTAL_SECTOR = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
            "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
            "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
            "61", "62", "63", "64", "65", "66", "67", "68", "69", "70",
            "71", "72", "73", "75", "76", "77", "78", "79", "80"};

    public String validPostalCode() {
        return POSTAL_SECTOR[new Random().nextInt(POSTAL_SECTOR.length)]+Phaker.validNumber(4);
    }

    public static PhakAbstractAddress suggestAnAddress(PropertyTypeEnum propertyTypeEnum) {
        switch (propertyTypeEnum) {
            case HDB:
            case CONDO:
            case CHALET:
            case ISLAND:
                return PhakIslandAddress.fakeItTillYouMakeIt();
            case LANDED:
            case PRISON:
            case KAMPONG:
            case NURSING:
            case BUSINESS:
            case HOSPITAL:
            case EDUCATION:
            case ORPHANAGE:
            case INDUSTRIAL:
            case EXECUTIVE_CONDO:
            default:
                throw new TestFailException("Unsupport address type to create a fake address");
        }
    }
}
