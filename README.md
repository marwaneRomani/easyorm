# EasyORM documentation

help you to develop any kind of application that uses databases - from small applications with a few tables to large scale enterprise applications with multiple databases

EasyORM is highly influenced by other ORMs, such as [Hibernate](http://hibernate.org/orm/), [TypeORM](https://typeorm.io/) and [Entity Framework](https://www.asp.net/entity-framework) 

# Features

- Supports DataMapper pattern
- Entities and columns.
- Entity manager.
- Java Beans as models
- Associations (relations).
- Eager and lazy load based on user demand.
- Support any kind of SQL relations.
- Cascades.
- Transactions.
- Automatic migrations generation.
- Connection pooling.
- Working with relational databases.
- Cross-database and cross-schema queries and Dialect.
- Elegant-syntax, flexible and powerful QueriesBuilders.
- Proper pagination for queries using joins.
- Streaming raw results.
- Configuration in Json.
- Supports MySQL / Postgres / H2 dbms
- Produced performant, flexible, clean and maintainable code.

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

    // relations with Personne table
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

# Configuration file

In EasyORM we are supporting a Json file as a configuration file, this file is used to specify the application models path, database infos and extra informations, you can use this example and personalise it with your own config :

```json
{
  "models": "org.easyORM.models",

  "database": {
    "url": "jdbc:mysql://localhost/messagingApp",
    "username": "root",
    "password": "",
    "strategy": "create",
    "dialect" : "mysql"
  },
  "connectionPool": {
    "maxSize": 10
  }
}
```

This file should be named config.json and it should be put in the resources directory in a java project.

 

- "models": This attribute specifies the package name where the application models are located.
- "database": This attribute contains details about the the database to be used such as :
    - The URL to connect to the database,
    - The username and password for authentication
    - The strategy to use it can be
        - “create” : create tables for the first time in the database
        - “create-drop” : drops all database tables and re-create them
        - “update” : this strategy keeps all tables as they are in the database, and give the user the ability to interact with those tables
        - “drop” : this strategy drops the database tables
    
- "connectionPool": This attribute sets the maximum size of the connection pool that will be used to manage connections to the database. The connection pool is a mechanism used to improve performance by reusing existing database connections rather than creating a new connection for each database request.

**As simple as this your config is done no more complex steps are needed**

# Map Models

As we show before we are using the [JavaBeans](https://en.wikipedia.org/wiki/JavaBeans) convention, so it’s very very simple and easy to  set the structure of your tables based on something you already use and familiar with, so there is no need to use [JPA](https://en.wikipedia.org/wiki/Jakarta_Persistence) to declare tables and columns and map relations between tables, but if you want to customize your tables and columns you have a minimum of useful [annotations](https://en.wikipedia.org/wiki/Java_annotation) 

      1- Primary Key :

By default the first attribute of the model is the primary key of the table

```java
public class User {
    private Integer id;
    // other columns... .	
    // add getters and setters... .
}
```

you can customize the primary key by adding the @Id annotation

```java
public class User {
    private String name;
    @Id(name = "userId", autoIncrement = true)
    private Integer id;
    // other columns... .	
    // add getters and setters... .
}
```

now the order doesn't matter because the id is specified with the @Id annotation, 

you can also change the name of this column, set it as auto incremented value, control it length if **~~it’s a String primary key~~** 

And it’s also possible to customize other attributes by using @Column annotation

```java
public class User {
    @Id(name = "userId", autoIncrement = true)
    private Integer id;

    @Column(name = "national_card", unique = true)
    private String nic;

    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;

    // other columns... .	
    // add getters and setters... .
}
```

You can change the column name, set it value to be unique, set it as non nullable value and control it length if it is a String attribute

You can also ignore an attribute if you don’t want it to be part of the created table using @Ignore annotation

```java
public class User {
    @Id(name = "userId", autoIncrement = true)
    private Integer id;

    @Column(name = "national_card", unique = true)
    private String nic;

    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;
    
    @Ignore
    private Integer age;
    // other columns... .	
    // add getters and setters... .
}
```

The age attribute will not be part of the user table in the database

That’s all → with EasyORM it’s so easy to map java classes to relational database tables.

# Map Relations

To map relationship between two classes in java we are using this convention

One-to-One Relationship :

A one-to-one relationship between two classes means that each instance of one class is associated with exactly one instance of the other class. In Java, this relationship is usually implemented using an instance variable of the target class as an attribute of the source class.

One-to-Many Relationship :

A one-to-many relationship between two classes means that each instance of one class is associated with zero or more instances of the other class. In Java, this relationship is usually implemented using a collection (e.g. List) of the target class as an attribute of the source class.

Many-to-Many Relationship :

A many-to-many relationship between two classes means that each instance of one class is associated with zero or more instances of the other class, and vice versa. In Java, this relationship is usually implemented using a collection (e.g. List) of the target class as an attribute of both classes.

So if you respect those conventions in creating relations between your classes, with EasyORM it’s all done.

Let’s take an example :

Each user will have a list of sent and received messages and 

each message will have a sender and a receiver

So between those two tables there is two one-to-many relations, the User class shpud two Lists of messages (sentMessages, receivedMessages) and the Message class should two users objects (sender, receiver).

```java
public class User {
    @Id(name = "userId")
    private Integer id;
    @Column(name = "national_card", unique = true)
    private String nic;
    @Column(name = "firstname", nullable = false, length = 255)

    // relations
    private List<Message> sentMessages;
    private List<Message> receivedMessages;

    // other columns... .	
    // add getters and setters... .
}
```

```java
public class Message {
    private Long id;
    private String content;
    private Date date;
    private Boolean seen;

    private User sender;
    private User receiver;

    // other columns... .	
    // add getters and setters... .
}
```

In the database, the message table will have 2 foreign keys on the user table (sender and receiver)

**Note :**  

In the Message class the first User object will correspand to the first Message object in the User class, the second one will correspand to the second one and vice versa.

This is a very important  convention to respect when creating you relations 

to map them correctly in the database

sender ←→ sentMessages

receiver ←→ receivedMessages
