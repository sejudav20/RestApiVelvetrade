package com.Velvetrade.Velvetrade.DAO;

import com.Velvetrade.Velvetrade.Model.Posting;
import com.Velvetrade.Velvetrade.Model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository("UserDAO")
public class FakeUserDAO implements UserDAO {
      @Override
    public int addNewUser(User user) {
        User u =new User(user.getId(),user.getUserName(),user.getPassword(),user.getEmail(),user.getState(),user.getStreetAddress(),user.getZip(),user.getTin(),user.isOnline(),user.getFriends(),user.getItemId(),user.getNotifications(),user.getGroups());
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> ndoc = dbFirestore.collection("Users").document(user.getId()).set(u);
        }catch (Exception e){
            return 0;
        }
        return 1;
    }

    @Override
    public List<Posting> getAllPostingsPerUser(String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        User u=getUserByID(id);
        List<Posting> l= new ArrayList<>();
        for(String ids:u.getGroups()){
            l.addAll( dbFirestore.collection("Groups").document(ids).collection("Postings").
                    whereEqualTo("userId",id).get().get().toObjects(Posting.class));
        }
        return l;
    }
    @Override
    public User authenticateUser(String username, String password) {
        List<User> users=findUserByName(username);
        for(User user:users){
            if(user.getPassword().equals(password)){
                System.out.println("user password "+user.getPassword()+" "+password);
                return user;
            }
        }
        return null;
    }

    @Override
    public int updateUserByID(String id,User user) {
        try {
            User u= getUserByID(id);
            User e= new User(id,(user.getUserName()==null?u.getUserName():user.getUserName()),
                    (user.getPassword()==null?u.getPassword():user.getPassword()),
                    (user.getEmail()==null?u.getEmail():user.getEmail()),
                    (user.getState()==null?u.getState():user.getState()),
                    (user.getStreetAddress()==null?u.getStreetAddress():user.getStreetAddress()),
                    (user.getZip()==0?u.getZip():user.getZip()),
                    (user.getTin()==null?u.getTin():user.getTin()),user.isOnline(),
                    user.getFriends(),user.getItemId(),
                    user.getNotifications(),user.getGroups());
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> ndoc = dbFirestore.collection("Users").document(id).set(e);

        }catch (Exception e){
            return 0;
        }
        return 1;
    }

    @Override
    public int deleteUserByID(String id) {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> ndoc = dbFirestore.collection("Users").document(id).delete();
        }catch (Exception e){
            return 0;
        }

        return 1;
    }

    @Override
    public User getUserByID(String id) {
        DocumentReference dr=FirestoreClient.getFirestore().collection("Users").document(id);
        ApiFuture<DocumentSnapshot> future = dr.get();
        try {
            DocumentSnapshot ds=future.get();
            if(ds.exists()){
                return ds.toObject(User.class);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findUserByName(String name) {
        CollectionReference cr=FirestoreClient.getFirestore().collection("Users");
        Query q= cr.whereEqualTo("userName",name);
        ApiFuture<QuerySnapshot> a = q.get();
        List<User> users=new ArrayList<>();
        try {
            for(DocumentSnapshot ds:a.get().getDocuments()){
                users.add(ds.toObject(User.class));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return users;
    }
}
