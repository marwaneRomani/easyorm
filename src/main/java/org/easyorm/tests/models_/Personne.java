package org.easyorm.tests.models_;


import org.easyorm.modelsmapper.annotations.Ignore;
import org.easyorm.modelsmapper.annotations.Column;
import org.easyorm.modelsmapper.annotations.Id;

import java.util.Date;
import java.util.List;

public class Personne {
    @Id(name = "userId", autoIncrement = true)
    private Integer id;

    @Column(name = "national_card", unique = true)
    private String nic;

    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;

    @Ignore
    private Integer age;
    private String lastName;
    private Date birthDay;

    private List<Message> sentMessages;
    private List<Message> recievedMessages;
    public Personne() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

}