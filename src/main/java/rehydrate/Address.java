package rehydrate;

import java.util.Objects;

public class Address {
    private String streetName;
    private Integer buildingNumber;

    public Address() {
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Integer getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(Integer buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(streetName, address.streetName) &&
                Objects.equals(buildingNumber, address.buildingNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(streetName, buildingNumber);
    }

    @Override
    public String toString() {
        return "Address{" +
                "streetName='" + streetName + '\'' +
                ", buildingNumber=" + buildingNumber +
                '}';
    }
}
