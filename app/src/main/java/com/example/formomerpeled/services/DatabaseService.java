package com.example.formomerpeled.services;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.formomerpeled.R;
import com.example.formomerpeled.models.Restaurant;
import com.example.formomerpeled.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/// a service to interact with the Firebase Realtime Database.
/// this class is a singleton, use getInstance() to get an instance of this class
/// @see #getInstance()
/// @see FirebaseDatabase
public class DatabaseService {

    /// tag for logging
    private static final String TAG = "DatabaseService";

    /// callback interface for database operations
    /// @param <T> the type of the object to return
    /// @see DatabaseCallback#onCompleted(Object)
    /// @see DatabaseCallback#onFailed(Exception)
    public interface DatabaseCallback<T> {
        /// called when the operation is completed successfully
        void onCompleted(T object);

        /// called when the operation fails with an exception
        void onFailed(Exception e);
    }

    /// the instance of this class
    private static DatabaseService instance;

    /// the reference to the database
    private final DatabaseReference databaseReference;

    /// use getInstance() to get an instance of this class
    private DatabaseService() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    /// get an instance of this class
    public static synchronized DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    // private generic methods to write and read data from the database

    /// write data to the database at a specific path
    private void writeData(@NotNull final String path, @NotNull final Object data, final @Nullable DatabaseCallback<Void> callback) {
        databaseReference.child(path).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (callback == null) return;
                callback.onCompleted(task.getResult());
            } else {
                if (callback == null) return;
                callback.onFailed(task.getException());
            }
        });
    }

    /// read data from the database at a specific path
    private DatabaseReference readData(@NotNull final String path) {
        return databaseReference.child(path);
    }

    /// get data from the database at a specific path
    private <T> void getData(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<T> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            T data = task.getResult().getValue(clazz);
            callback.onCompleted(data);
        });
    }


    /// remove data from the database at a specific path
    /// @param path the path to remove the data from
    /// @param callback the callback to call when the operation is completed
    /// @see DatabaseCallback
    private void deleteData(@NotNull final String path, @Nullable final DatabaseCallback<Void> callback) {
        readData(path).removeValue((error, ref) -> {
            if (error != null) {
                if (callback == null) return;
                callback.onFailed(error.toException());
            } else {
                if (callback == null) return;
                callback.onCompleted(null);
            }
        });
    }


    /// generate a new id for a new object in the database
    private String generateNewId(@NotNull final String path) {
        return databaseReference.child(path).push().getKey();
    }





    public String generateRestaurantId() {
        return generateNewId("Restaurant/");
    }

    // end of private methods for reading and writing data

    // public methods to interact with the database

    /// create a new user in the database
    public void createNewUser(@NotNull final User user, @Nullable final DatabaseCallback<Void> callback) {
        writeData("Users/" + user.getId(), user, callback);
    }

    /// get a user from the database
    public void getUser(String userId, DatabaseCallback<User> callback) {

        getData("Users/" + userId, User.class, callback);


    }


    public void updateUser(User user, DatabaseCallback<Void> callback) {
        // עדכון המידע במסד הנתונים
        // לדוגמה, אם אתה עובד עם Firebase:
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // שימוש ב-uid של המשתמש על מנת לעדכן את הנתונים
        databaseReference.child(user.getId()).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onCompleted(null); // הצלחה
                    } else {
                        callback.onFailed(task.getException()); // שגיאה
                    }
                });
    }


    public void createNewRestaurant(@NotNull final Restaurant restaurant, @Nullable final DatabaseCallback<Void> callback) {
        writeData("Restaurants/" + restaurant.getId(), restaurant, callback);
    }

    public void userFavoriteRestaurant(@NotNull final String uid,@NotNull final Restaurant restaurant, @Nullable final DatabaseCallback<Void> callback) {
        writeData("UserFavorite/" + uid+"/"+restaurant.getId(), restaurant, callback);
    }




    /// get a restaurant from the database
    public void getUserFavoriteRestaurant(@NotNull final String uid, @NotNull final DatabaseCallback<List<Restaurant>> callback) {


        readData("UserFavorite/" + uid).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<Restaurant> restaurants = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                Log.d(TAG, "Got restaurant: " + restaurant);
                restaurants.add(restaurant);
            });

            callback.onCompleted(restaurants);
        });
    }



    /// get a restaurant from the database
    public void getRestaurant(@NotNull final String rid, @NotNull final DatabaseCallback<Restaurant> callback) {
        getData("Restaurants/" + rid, Restaurant.class, callback);
    }

    /// get all the restaurants from the database
    public void getRestaurants(@NotNull final DatabaseCallback<List<Restaurant>> callback) {
        readData("Restaurants").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<Restaurant> restaurants = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                Log.d(TAG, "Got restaurant: " + restaurant);
                restaurants.add(restaurant);
            });

            callback.onCompleted(restaurants);
        });
    }

    /// get all the users from the database
    public void getUsers(@NotNull final DatabaseCallback<List<User>> callback) {
        readData("Users").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<User> users = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                User user = dataSnapshot.getValue(User.class);
                Log.d(TAG, "Got user: " + user);
                users.add(user);
            });

            callback.onCompleted(users);
        });
    }


    public void deleteUser(@NotNull final String userId, @Nullable final DatabaseCallback<Void> callback) {
        deleteData("Users/" + userId, callback);
    }

    /// delete a restaurant from the database
    /// @param restaurantId the id of the restaurant to delete
    /// @param callback the callback to call when the operation is completed
    public void deleteRestaurant(@NotNull final String restaurantId, @Nullable final DatabaseCallback<Void> callback) {
        deleteData("/Restaurants/" + restaurantId, callback);
//        writeData("/Restaurants/" + restaurantId, null, callback);
    }

}
