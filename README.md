# EasyORM documentation

help you to develop any kind of application that uses databases - from small applications with a few tables to large scale enterprise applications with multiple databases

EasyORM is highly influenced by other ORMs, such as [Hibernate](http://hibernate.org/orm/), [TypeORM](https://typeorm.io/) and [Entity Framework](https://www.asp.net/entity-framework) 

# Features

- Supports [DataMapper](https://typeorm.io/active-record-data-mapper#what-is-the-data-mapper-pattern) pattern
- Entities and columns.
- Entity manager.
- Java Beans as models
- Associations (relations).
- Eager and lazy relations based on user demand.
- Support any kind of SQL relations.
- Cascades.
- Transactions.
- Automatic migrations generation.
- Connection pooling.
- Working with melatuonal databases .
- Cross-database and cross-schema queries and Dialect.
- Elegant-syntax, flexible and powerful QueryBuilder.
- Proper pagination for queries using joins.
- Streaming raw results.
- Connection configuration in Json.
- Supports MySQL / Postgres / H2 dbms
- Produced code is performant, flexible, clean and maintainable.

# Models creation

With EasyORM your can keep your models just like this :

```java
public class User {
    private Integer id;
    private String nic;
    private String firstName;
    private String lastName;
    private Date birthDay;

    public User() {
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
```

Or you customize you fields with specific annotaions

```java
public class User {
    @Id(name = "userId", autoIncrement = true)
    private Integer id;
    @Column(name = "national_card", unique = true)
    private String nic;
    @Column(name = "firstname", nullable = false)
    private String firstName;
    private String lastName;
    private Date birthDay;

    public User() {
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
```

The only requirement is to respect the convention of JavaBeans 

The class must have a default constructor with no params.

Private attributes of the class (instance variables) must be accessed publicly through accessor methods built with "get" or "set" followed by the capitalized name of the attribute. The pair of accessors is called a property.

The class must not be declared final.

The class must be "Serializable" to be able to save and restore the state of instances of this class.

You can add relations with other tables, in this example we will use another class Message

```java
public class Message {
    private Long id;
    private String content;
    private Date date;
    private Boolean seen;

    // relations with User table
    private User sender;
    private User receiver;

    public Message() {

    }

    // we can add other constructors
    public Message(Long id, String content, Date date, Boolean seen, User sender, User receiver) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.seen = seen;
        this.sender = sender;
        this.receiver = receiver;
    }
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
```

this class have 2 relations with the user table both of type one-to-many, we need to modify the User class to identify those relations 

one-to-many → …..

so we need to add 2 lists in the User class sentMessages and receivedMessages :

```java
public class User {
    @Id(name = "userId", autoIncrement = true)
    private Integer id;
    @Column(name = "national_card", unique = true)
    private String nic;
    @Column(name = "firstname", nullable = false)
    private String firstName;
    private String lastName;
    private Date birthDay;

    // relations
    private List<Message> sentMessages;
    private List<Message> receivedMessages;

    public User() {
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
```

Now our models are correctly formed and respect our conventions in EasyORM, we can now create all tables with it relations.

```java
EasyORM.run();
```

# Objects persistence

Now we are ready to persist our object and execute some queries 

```java
// the user id is auto increment value
User Oussama = new User();

     Oussama.setNic("Abcd123");
     Oussama.setFirstName("Oussama");
     Oussama.setLastName("EL-Amrani");
     Oussama.setBirthDay(new Date(2002,9, 1));
        
User Marwane = new User();
     Marwane.setNic("Abcd123");
     Marwane.setFirstName("Marwane");
     Marwane.setLastName("Romani");
     Marwane.setBirthDay(new Date(2002, 9, 9));

// save new users to the database
EasyORM
     .buildObject(User.class)
     .save(Oussama);

EasyORM
     .buildObject(User.class)
     .save(Marwane);
```

we can also save new messages and each message will have a sender and receiver

```java
Message message = new Message(1l, "helle Oussama how are you", new Date() , true ,Marwane, Oussama);

EasyORM
     .buildObject(Message.class)
     .save(message);
```

A new message will be saved in the database with relations to the sender and receiver

# Search Queries

We can execute find queries 

We can look for a user by his id

```java
User userFromDb = EasyORM
	                 .buildObject(User.class)
	                 .findById(1)
	                 .buildObject();
```

Or

```java
User userFromDb = EasyORM
	                .buildObject(User.class)
                        .findOne()
	                .where("name", "=" ,"Oussama")
	                .execute()
	                .buildObject();
```

We can also look for all users 

```java
List<User> listOfUsersFromDB = EasyORM
                              .buildObject(User.class)
                              .findAll()
                              .buildObjects();
```

Or we can add a search criteria to our query

```java
List<Message> messages = EasyORM
                          .buildObject(User.class)
                          .findMany()
                          .where("content", "like","%Marwane%")
                          .execute()
                          .buildObjects();
```
