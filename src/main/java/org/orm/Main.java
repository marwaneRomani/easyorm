package org.orm;

import org.orm.framework.OrmApplication;
import org.orm.framework.transactionsmanager.Transaction;
import org.orm.models.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        OrmApplication.run();

        Filiere gl = new Filiere("GL", "GL is the best option you can choose");
        Filiere abd = new Filiere("ABD", "abd == hachemoud");
        Filiere asr = new Filiere("ASR", "asr == Khartouch");


        ChefFilliere ibriz = new ChefFilliere(1l, "Ibriz", List.of(gl, abd));
        ChefFilliere khartoch = new ChefFilliere(2l, "Khartoch", List.of(asr));


        Userr houssam = new Userr("123456789", "1234567", "Houssam", "houssam@example.com", "Houssam", 20, asr, List.of(asr, gl));
        Userr marwane = new Userr("134794630", "1234567", "Marwane", "marwane@example.com", "Romani", 20, gl, List.of(asr, gl));
        Userr enzeo = new Userr("185444554", "1234567", "Enzo", "enzo@example.com", "enzo", 20, abd, List.of(asr, abd));
        Userr oussama = new Userr("155778877", "1234567", "Oussama", "oussama@example.com", "Amrani", 20, gl, List.of(asr, abd, gl));

        List<Userr> users = new ArrayList<>();
        users.add(houssam);
        users.add(marwane);
        users.add(enzeo);
        users.add(oussama);



        Post post0 = new Post(1l, "HOW HTTP REQUEST HANDELED BY NODE", new Date(), marwane);
        Post post1 = new Post(2l, "ANDROID CONSTRAINT LAYOUT", new Date(), oussama);
        Post post2 = new Post(3l, "SHA-256 ALGORITHM IN CRYPTOGRAPHIE", new Date(), houssam);
        Post post3 = new Post(4l, "SHA-554 ALGORITHM", new Date(), oussama);
        Post post4 = new Post(5l, "SHA-545 ALGORITHM", new Date(), oussama);
        Post post5 = new Post(6l, "SHA-877 CRYPTOGRAPHIE", new Date(), oussama);


        List<Post> posts = new ArrayList<>();

        posts.add(post0);
        posts.add(post1);
        posts.add(post2);
        posts.add(post3);
        posts.add(post4);
        posts.add(post5);


        Message message0 = new Message(1l, "helle Oussama how are you", new Date() , true ,marwane, oussama);
        Message message1 = new Message(2l, "Afen akhay mzyan", new Date() , true ,houssam, oussama);
        Message message2 = new Message(3l, "hi how are you", new Date() , true ,enzeo, oussama);
        Message message3 = new Message(4l, "helle again", new Date() , true ,marwane, oussama);



// save data ----------------------------------------------------------------------
//        OrmApplication
//                .buildObject(Filiere.class)
//                .save(gl);
//
//        OrmApplication
//                .buildObject(Filiere.class)
//                .save(asr);
//
//        OrmApplication
//                .buildObject(Filiere.class)
//                .save(abd);
//
//
//        OrmApplication
//                .buildObject(ChefFilliere.class)
//                .save(ibriz);
//
//        OrmApplication
//                .buildObject(ChefFilliere.class)
//                .save(khartoch);
//
//
//        for (Userr user : users)
//            OrmApplication
//                    .buildObject(Userr.class)
//                    .save(user);
//
//
//        for (Post post : posts)
//            OrmApplication
//                    .buildObject(Post.class)
//                    .save(post);
//
//        OrmApplication
//                .buildObject(Message.class)
//                .save(message0);
//
//        OrmApplication
//                .buildObject(Message.class)
//                .save(message1);
//
//        OrmApplication
//                .buildObject(Message.class)
//                .save(message2);
//
//        OrmApplication
//                .buildObject(Message.class)
//                .save(message3);
//
//        Commentaire commentaire = new Commentaire(1, "very good topic", new Date(), post0, oussama, null);
//
//        SubCommentaire subCommentaire = new SubCommentaire(1l, "yes it is this is something we should look at", new Date(), commentaire, marwane);
//
//
//        OrmApplication
//                .buildObject(Commentaire.class)
//                .save(commentaire);
//
//        OrmApplication
//                .buildObject(Notification.class)
//                .save(new Notification(1, false, new Date(), "new comment on your post", "http://....", oussama));
//
//        OrmApplication
//                .buildObject(SubCommentaire.class)
//                .save(subCommentaire);

// find data -----------------------------------------------------------------------------

        Userr userr = OrmApplication
                .buildObject(Userr.class)
                .findOne()
                .where("name", "=", "Oussama")
                .execute()
                .get("filiere")
                .get("particapationFilieres")
                .get("sentMessages")
                .get("receivedMessages")
                .get("notifications")
                .get("posts")
                .get("commentaires")
                .get("subCommentaires")
                .get("lignePostReactions")
                .buildObject();


        sendMondey();
    }


    public static void sendMondey() {
        Transaction.wrapMethodInTransaction(() -> {
            Userr userr = OrmApplication
                    .buildObject(Userr.class, false)
                    .deleteById("TEST");

            if (userr != null)
                OrmApplication
                        .buildObject(Userr.class, false)
                        .save(new Userr("TEST", "Test", "test", "test", "test",20));
        });
    }
}