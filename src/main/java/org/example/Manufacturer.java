package org.example;

public class Manufacturer {
    private String name;
    private String country;
    private String address;
    private String phoneNumber;
    private String fax;

    public Manufacturer(String name, String country, String address, String phoneNumber, String fax) {
        this.name = name;
        this.country = country;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.fax = fax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Override
    public String toString() {
        return "Manufacturer{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", fax='" + fax + '\'' +
                '}';
    }
}
