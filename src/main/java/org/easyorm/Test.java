package org.easyorm;



import org.easyorm.tests.models__.models.Car;
import org.easyorm.tests.models__.models.Driver;
import org.easyorm.tests.models__.models.Person;

import java.util.Date;
import java.util.List;

public class Test {
    public static void main(String[] args) {

//        EasyOrm.bootstrap();

//        Person marwane = new Person("1234", "Marwane", "Romani", new Date(2002, 9, 9), 20, null);
//        Person oussama = new Person("1235", "Oussama", "El-Amrani", new Date(2002, 9, 9), 20, null);
//
//        EasyOrm
//            .buildObject(Person.class)
//            .save(marwane);
//
//        EasyOrm
//            .buildObject(Person.class)
//            .save(oussama);
//
//        Driver newDriver = new Driver("A", new Date(), null);
//        newDriver.setNic("1234");
//        newDriver.setFirstName("Marwane");
//        newDriver.setLastName("Romani");
//        newDriver.setBirthDay(new Date(2002,9,9));
//        newDriver.setAge(20);
//
//        EasyOrm
//                .buildObject(Driver.class)
//                .save(newDriver);
//
//
//        Car bmw = new Car(null, 4, "BMW", 10000, newDriver, null);
//
//        EasyOrm
//                .buildObject(Car.class)
//                .save(bmw);
//
//
//        Driver newDriver1 = new Driver("A", new Date(), null);
//        newDriver1.setNic("12345");
//        newDriver1.setFirstName("Salah");
//        newDriver1.setLastName("Bouasria");
//        newDriver1.setBirthDay(new Date(2002,9,9));
//        newDriver1.setAge(20);
//        newDriver1.setCars(List.of(bmw));
//
//        EasyOrm
//                .buildObject(Driver.class)
//                .save(newDriver1);

        Car bmw = EasyOrm
                .buildObject(Car.class)
                .findOne()
                .where("car_brand", "=", "bmw")
                .and("kilometrage", ">", 1000)
                .execute()
                .get("owner")
                .buildObject();





        Driver driver = EasyOrm
                .buildObject(Driver.class)
                .findOne()
                .where("firstName", "=", "Salah")
                .execute()
                .get("drivingCars")
                .get("driver_cars")
                .buildObject();

        System.out.println(driver);
    }
}
