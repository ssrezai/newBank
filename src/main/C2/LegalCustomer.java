package C2;

/**
 * Created by DOTIN SCHOOL 3 on 2/19/2015.
 */
public class LegalCustomer extends Customer {
    private String name;
    private String registrationDate;
    private String economicCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getEconomicCode() {
        return economicCode;
    }

    public void setEconomicCode(String economicCode) {
        this.economicCode = economicCode;
    }
}
