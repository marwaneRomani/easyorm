package org.orm;

import org.orm.framework.OrmApplication;
import org.orm.models.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        OrmApplication.run();

        Filiere gl = new Filiere("GL", "GL is the best option you can choose");

//        OrmApplication
//                .buildObject(Filiere.class)
//                .save(gl);

        ChefFilliere ibriz = new ChefFilliere(1l, "Ibriz", gl);

//        OrmApplication
//                .buildObject(ChefFilliere.class)
//                .save(ibriz);



        Post post0 = new Post(1l, "HOW HTTP REQUEST HANDELED BY NODE", new Date());
        Post post1 = new Post(2l, "ANDROID CONSTRAINT LAYOUT", new Date());
        Post post2 = new Post(3l, "SHA-256 ALGORITHM IN CRYPTOGRAPHIE", new Date());
        List<Post> posts = new ArrayList<>();

        posts.add(post0);
        posts.add(post1);
        posts.add(post2);

//        for (Post post : posts) {
//            OrmApplication
//                    .buildObject(Post.class)
//                    .save(post);
//        }

        User user0 = new User("123456789", "1234567", "John", "john@example.com", "Doe", 30, gl,List.of(posts.get(0)));
        User user1 = new User("134794630", "1234567", "Marwane", "marwane@example.com", "Doe", 20, gl, List.of(posts.get(1)));
        User user2 = new User("185444554", "1234567", "Oussama", "Oussama@example.com", "Doe", 20, gl, List.of(posts.get(2)));
        User user3 = new User("155778877", "1234567", "Amine", "Amine@example.com", "Doe", 20, gl);

        List<User> users = new ArrayList<>();
        users.add(user0);
        users.add(user1);
        users.add(user2);
        users.add(user3);

//        for (User user : users) {
//            OrmApplication
//                    .buildObject(User.class)
//                    .save(user);
//        }

        Message message = new Message(1l, "helle Oussama how are you", new Date() , true ,user1, user0);

//        OrmApplication
//                .buildObject(Message.class)
//                .save(message);


        SubCommentaire springFramework = new SubCommentaire();
        springFramework.setContent("can you use spring with kotlin");
        springFramework.setDate(new Date());
        springFramework.setUser(user1);

//        SubCommentaire springFrameworkPersisted = OrmApplication
//                .buildObject(SubCommentaire.class)
//                .save(springFramework);
//
//        System.out.println(springFrameworkPersisted + "  springFrameworkPersisted)");
//        System.out.println(springFramework+"  springFramework");


        User user = OrmApplication
                    .buildObject(User.class)
                    .findOne()
                    .where("age", "<>", 30)
                    .and("name", "=", "Marwane")
                    .and("cin", "=", "134794630")
                    .or("cin", "=", "1234567")
                    .execute()
                    .buildObject();

        System.out.println("hahahhahahhahahhhhahahahahahaa");
        System.out.println(user);

        List<User> users1 = OrmApplication
                .buildObject(User.class)
                .findMany()
                .where("age", "equals", 20)
                .or("name", "=", "Marwane")
                .limit(1)
                .execute()
                .buildObjects();
//
//        System.out.println("hahahahhahhhahahahhahahahahha");
        System.out.println(users1);
    }

}


