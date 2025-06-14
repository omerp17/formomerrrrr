package com.example.formomerpeled.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.formomerpeled.models.Dish;
import com.example.formomerpeled.models.Restaurant;
import com.example.formomerpeled.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/// a service to interact with the Firebase Realtime Database.
/// this class is a singleton, use getInstance() to get an instance of this class
///
/// @see #getInstance()
/// @see FirebaseDatabase
public class DatabaseService {

    /// tag for logging
    private static final String TAG = "DatabaseService";

    /// callback interface for database operations
    ///
    /// @param <T> the type of the object to return
    /// @see DatabaseCallback#onCompleted(Object)
    /// @see DatabaseCallback#onFailed(Exception)
    public interface DatabaseCallback<T> {
        /// called when the operation is completed successfully


        public void onCompleted(T object);

        /// called when the operation fails with an exception
        public void onFailed(Exception e);
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
    private void writeData(@NotNull final String path, @Nullable final Object data, final @Nullable DatabaseCallback<Void> callback) {
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
    ///
    /// @param path     the path to remove the data from
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


    public <T> void getDataList(String path, Class<T> clazz, @NotNull final DatabaseCallback<List<T>> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<T> list = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                T restaurant = dataSnapshot.getValue(clazz);
                list.add(restaurant);
            });

            callback.onCompleted(list);
        });
    }


    public String generateRestaurantId() {
        return generateNewId("Restaurant/");
    }

    public String generateDishId() {
        return generateNewId("dishes/");
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
        writeData("Users/" + user.getId(), user, callback);
    }


    public void createNewRestaurant(@NotNull final Restaurant restaurant, @Nullable final DatabaseCallback<Void> callback) {
        writeData("Restaurants/" + restaurant.getId(), restaurant, callback);
    }

    /// get a restaurant from the database
    public void getRestaurant(@NotNull final String rid, @NotNull final DatabaseCallback<Restaurant> callback) {
        getData("Restaurants/" + rid, Restaurant.class, callback);
    }

    /// get all the restaurants from the database
    public void getRestaurants(@NotNull final DatabaseCallback<List<Restaurant>> callback) {
        getDataList("Restaurants", Restaurant.class, callback);
    }

    /// get all the users from the database
    public void getUsers(@NotNull final DatabaseCallback<List<User>> callback) {
        getDataList("Users", User.class, callback);
    }


    public void deleteUser(@NotNull final String userId, @Nullable final DatabaseCallback<Void> callback) {
        deleteData("Users/" + userId, callback);
    }

    public void deleteDish(@NotNull final String dishId, @Nullable final DatabaseCallback<Void> callback) {
        deleteData("dishes/" + dishId, callback);
    }

    /// delete a restaurant from the database
    ///
    /// @param restaurantId the id of the restaurant to delete
    /// @param callback     the callback to call when the operation is completed
    public void deleteRestaurant(@NotNull final String restaurantId, @Nullable final DatabaseCallback<Void> callback) {
        deleteData("/Restaurants/" + restaurantId, callback);
    }


    public void createNewDish(@NotNull final Dish dish, @NotNull final DatabaseCallback<Void> callback) {
        writeData("dishes/" + dish.getId(), dish, callback);
        writeData("restaurantDishes/" + dish.getResId() + "/" + dish.getId(), dish, callback);
    }


    public void updateDish(@NotNull final Dish dish, @NotNull final DatabaseCallback<Void> callback) {
        writeData("dishes/" + dish.getId(), dish, callback);
        writeData("restaurantDishes/" + dish.getResId() + "/" + dish.getId(), dish, callback);
    }


    /// get all the restaurants from the database
    public void getAllDishes(@NotNull final DatabaseCallback<List<Dish>> callback) {
        getDataList("dishes", Dish.class, callback);
    }

    /// get all the restaurants from the database
    public void getRestaurantDishes(@NotNull final String resId, @NotNull final DatabaseCallback<List<Dish>> callback) {
        getDataList("restaurantDishes/"+ resId , Dish.class, callback);
    }

    public void deleteRestaurantDish(@NotNull final String resId, @NotNull final String dishId, @NotNull final DatabaseCallback<Void> callback) {
        deleteData("restaurantDishes/" + resId + "/" + dishId, callback);
    }


}
