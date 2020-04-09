package automation.configuration.datasource;

import automation.models.datasource.Address;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AddressJacksonObjectMapper {

  private Random random = new Random();
  private List<Address> businessAddresses;
  private List<Address> condominiumAddresses;
  private List<Address> educationAddresses;
  private List<Address> hdbAddresses;
  private List<Address> hospitalAddresses;
  private List<Address> industrialAddresses;
  private List<Address> islandAddresses;
  private List<Address> kampongAddresses;
  private List<Address> nursingAddresses;
  private List<Address> orphanageAddresses;
  private List<Address> prisonAddresses;

  public AddressJacksonObjectMapper() throws IOException {
    /*
     * Avoid loading unnecessary addresses which will affect testing performance
     */
//    condominiumAddresses = loadAddressesOf("condo");
    //    hdbAddresses = loadAddressesOf("hdb");
    //    kampongAddresses = loadAddressesOf("kampong");
    //    businessAddresses = loadAddressesOf("business");
    //    educationAddresses = loadAddressesOf("education");
    //    hospitalAddresses = loadAddressesOf("hospital");
    //    industrialAddresses = loadAddressesOf("industrial");
    //    islandAddresses = loadAddressesOf("island");
    //    nursingAddresses = loadAddressesOf("nursing");
    //    orphanageAddresses = loadAddressesOf("orphanage");
    //    prisonAddresses = loadAddressesOf("prison");
  }

  private List<Address> loadAddressesOf(String fileName) throws IOException {
    byte[] jsonData =
        Files.readAllBytes(Paths.get("src/main/resources/artifacts/" + fileName + ".json"));
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    Address[] result = objectMapper.readValue(jsonData, Address[].class);
    return new ArrayList<>(Arrays.asList(result));
  }

  public Address getRandomBusinessAddress() {
    return (Address) businessAddresses.get(random.nextInt(businessAddresses.size())).clone();
  }

  public Address getRandomCondominiumAddress() {
    return (Address) condominiumAddresses.get(random.nextInt(condominiumAddresses.size())).clone();
  }

  public Address getRandomEducationAddress() {
    return (Address) educationAddresses.get(random.nextInt(educationAddresses.size())).clone();
  }

  public Address getRandomHdbAddress() {
    return (Address) hdbAddresses.get(random.nextInt(hdbAddresses.size())).clone();
  }

  public Address getRandomHospitalAddress() {
    return (Address) hospitalAddresses.get(random.nextInt(hospitalAddresses.size())).clone();
  }

  public Address getRandomIndustrialAddress() {
    return (Address) industrialAddresses.get(random.nextInt(industrialAddresses.size())).clone();
  }

  public Address getRandomIslandAddress() {
    return (Address) islandAddresses.get(random.nextInt(islandAddresses.size())).clone();
  }

  public Address getRandomKampongAddress() {
    return (Address) kampongAddresses.get(random.nextInt(kampongAddresses.size())).clone();
  }

  public Address getRandomNursingAddress() {
    return (Address) nursingAddresses.get(random.nextInt(nursingAddresses.size())).clone();
  }

  public Address getRandomOrphanageAddress() {
    return (Address) orphanageAddresses.get(random.nextInt(orphanageAddresses.size())).clone();
  }

  public Address getRandomPrisonAddress() {
    return (Address) prisonAddresses.get(random.nextInt(prisonAddresses.size())).clone();
  }
}
