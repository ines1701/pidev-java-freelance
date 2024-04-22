package org.example;
import entities.Groupe;
import entities.Post;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import services.GroupeService;
import services.PostService;


public class Main {
    public static void main(String[] args) {
        PostService postService = new PostService();
        GroupeService groupeService = new GroupeService();

        // Création d'un groupe
//        Groupe groupe = new Groupe();
//        groupe.setGroupe("Groupe de test");
//        groupe.setDescription("Description du groupe de test");
//        groupe.setDateDeCreation(LocalDate.now());
//        groupe.setNom("Nom du groupe de test");
//        groupe.setImage("image.jpg");
//
        // Ajout du groupe
        //   groupeService.create(groupe);


//  List<Groupe> groupeList = groupeService.getAll();
//    System.out.println("List of Groupes:");
//    for (Groupe g : groupeList) {
//        System.out.println(g);
//    }


//    int groupId = 100; // Replace 1 with the ID of the group you want to retrieve
//        Groupe retrievedGroupe = groupeService.getById(groupId);
//         if (retrievedGroupe != null) {
//            System.out.println("Retrieved Groupe: " + retrievedGroupe);
//        } else {
//            System.out.println("Groupe not found with ID: " + groupId);
//        }



//        int groupId = 1;
//        Groupe groupeToUpdate = groupeService.getById(groupId);
//        if (groupeToUpdate != null) {
//            groupeToUpdate.setGroupe("Updated Groupe Name");
//            groupeToUpdate.setDescription("Updated Description");
//            groupeService.update(groupeToUpdate);
//        } else {
//            System.out.println("Groupe not found with ID: " + groupId);
//        }

        //groupeService.delete(3);

/*******************************************************************************get alll********************************************************************************************/
//Groupe groupe = groupeService.getById((int) 1L);
//
//// Vérification si le groupe existe
//if (groupe != null) {
//
//    Post post = new Post();
//    post.setDescription("Description du post de test");
//    post.setDatePublication(LocalDate.now());
//    post.setNom("Nom du post de test");
//    post.setGroupe(groupe);
//
//    // Appel de la méthode create de PostService
//    postService.create(post);
//} else {
//    System.out.println("Le groupe avec l'ID spécifié n'existe pas.");
//}

//  Post retrievedPost = postService.getById(1);
//            if (retrievedPost != null) {
//                System.out.println("Retrieved Post: " + retrievedPost);
//            } else {
//                System.out.println("Post not found with ID: 1");
//            }
//
//  List<Post> allPosts = postService.getAll();
//            if (!allPosts.isEmpty()) {
//                System.out.println("All Posts:");
//                for (Post post : allPosts) {
//                    System.out.println(post);
//                }
//            } else {
//                System.out.println("No posts found.");
//            }



        // postService.delete(10000);

//    Post retrievedPost = postService.getById(2000);
//if (retrievedPost != null) {
//    System.out.println("Retrieved Post: " + retrievedPost);
//    // Test update method
//    retrievedPost.setDescription("Updated description");
//    postService.update(retrievedPost);
//} else {
//    System.out.println("Post not found with ID: 2000");
//}


//List<Groupe> topGroups = groupeService.getTopActiveGroups(5); // Get top 5 active groups
//for (Groupe groupe : topGroups) {
//    System.out.println(groupe.getNom() + " - Number of Posts: " );
//}
//   Month mostCommonMonth = postService.getMostCommonMonth();
//        if (mostCommonMonth != null) {
//            System.out.println("The most common month for posts is: " + mostCommonMonth);
//        } else {
//            System.out.println("No posts found.");
//        }



    }}
