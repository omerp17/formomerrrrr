package com.example.formomerpeled.screens;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.formomerpeled.R;
import com.example.formomerpeled.Utils.ImageUtil;
import com.example.formomerpeled.models.Restaurant;
import com.example.formomerpeled.models.User;
import com.example.formomerpeled.services.AuthenticationService;
import com.example.formomerpeled.services.DatabaseService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddRestaurant extends AppCompatActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {
    FirebaseDatabase database;
    DatabaseService databaseService = DatabaseService.getInstance();
    AuthenticationService authenticationService = AuthenticationService.getInstance();

    EditText etName, etRestaurantType, etPhoneNumber, etAddress, etDomain, etGlutenFreeItems;
    String Name, RestaurantType, PhoneNumber, City, Address, Domain, GlutenFreeItems, imageCode;

    RatingBar ratingBar;


    private final String TAG = "AddRestaurant";
    Spinner spCity;
    Button btnAdd, btnGallery;
    ImageView ivResImage;
    float rating = 0.0F;

    /// Activity result launcher for selecting image from gallery
    private ActivityResultLauncher<Intent> selectImageLauncher;


    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    private float rating2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_restaurant);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        ImageUtil.requestPermission(this);

        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        ivResImage.setImageURI(selectedImage); // הצגת התמונה שנבחרה
                    }
                });
    }

    private void initViews() {
        btnAdd = findViewById(R.id.btnAdd);
        btnGallery = findViewById(R.id.btnGalleryD);


        etName = findViewById(R.id.etName);
        etRestaurantType = findViewById(R.id.etRestaurantType);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        spCity = findViewById(R.id.spCity);
        etAddress = findViewById(R.id.etAddress);
        etDomain = findViewById(R.id.etDomain);
        etGlutenFreeItems = findViewById(R.id.etGlutenFreeItems);
        ratingBar = findViewById(R.id.ratingBar);
        ivResImage = findViewById(R.id.ivAddRes);

        ratingBar.setOnRatingBarChangeListener(this);



        btnAdd.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {


        if (view == btnGallery) {
            selectImageFromGallery(); // קורא לפונקציה לבחירת תמונה
        }
        if (view == btnAdd) {
            // אוספת את כל הנתונים מהטופס
            RestaurantType = etRestaurantType.getText().toString();
            Name = etName.getText().toString();
            PhoneNumber = etPhoneNumber.getText().toString();
            City = spCity.getSelectedItem().toString();
            Address = etAddress.getText().toString();
            Domain = etDomain.getText().toString();
            GlutenFreeItems = etGlutenFreeItems.getText().toString();
            imageCode = ImageUtil.convertTo64Base(ivResImage); // המרת התמונה ל-Base64
            //rating = ratingBar.getRating();


            // בדוק אם התמונה קיימת
            if (!isValid(Name, RestaurantType, PhoneNumber, Address, Domain, GlutenFreeItems, imageCode)) {
                return; // לא מוסיפים אם יש שדות ריקים
            }


            // יצירת אובייקט מסעדה
            Restaurant restaurant = new Restaurant(databaseService.generateRestaurantId(), authenticationService.getCurrentUserId(), Name, RestaurantType, Address, City,
                    PhoneNumber, GlutenFreeItems, Domain, imageCode, new ArrayList<>());
            databaseService.createNewRestaurant(restaurant, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {
                    // הפעלת פעולה אחרי שהמסעדה נוצרה בהצלחה
                    etName.setText("");
                    etRestaurantType.setText("");
                    etPhoneNumber.setText("");
                    etAddress.setText("");
                    etDomain.setText("");
                    etGlutenFreeItems.setText("");
                    ratingBar.setRating(0); // מאפס את ה-RatingBar ל-0 (אם תרצה)

                }

                @Override
                public void onFailed(Exception e) {
                    // טיפול בשגיאה במקרה של כשל
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            });
        }
    }

    private void selectImageFromGallery() {
        // פתיחת הגלריה לבחירת תמונה


        imageChooser();
    }

    // פונקציה לבדוק אם כל השדות מלאים
    private boolean isValid(String name, String restaurantType, String phoneNumber, String address, String domain, String glutenFreeItems, String imageBase64) {
        if (name.isEmpty()) {
            etName.setError("דרוש שם");
            return false;
        }
        if (restaurantType.isEmpty()) {
            etRestaurantType.setError("דרוש סוג מסעדה");
            return false;
        }
        if (phoneNumber.isEmpty()) {
            etPhoneNumber.setError("דרוש מספר");
            return false;
        }

        // בדיקה שמספר הטלפון כולל בדיוק 10 ספרות
        if (phoneNumber.length() != 10 || !phoneNumber.matches("\\d{10}")) {
            etPhoneNumber.setError("יש להזין מספר טלפון תקין (10 ספרות)");
            return false;
        }

        if (address.isEmpty()) {
            etAddress.setError("דרושה כתובת");
            return false;
        }
        if (domain.isEmpty()) {
            etDomain.setError("דרוש קישור לאתר");
            return false;
        }
        if (glutenFreeItems.isEmpty()) {
            etGlutenFreeItems.setError("דרוש להכניס מאכלים ללא גלוטן");
            return false;
        }
        if (imageBase64 == null || imageBase64.isEmpty()) {
            Toast.makeText(this, "תמונה דרושה", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    ivResImage.setImageURI(selectedImageUri);
                }
            }
        }
    }


    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        rating2 = rating;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_admin){
            databaseService.getUser(authenticationService.getCurrentUserId(), new DatabaseService.DatabaseCallback<User>() {
                @Override
                public void onCompleted(User user) {
                    if(user.isAdmin())
                    {
                        Intent go = new Intent(AddRestaurant.this, AdminPage.class);
                        startActivity(go);
                    }
                    else
                        Toast.makeText(AddRestaurant.this, "אינך מנהל", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(AddRestaurant.this, "שגיאה באחזור פרטים", Toast.LENGTH_SHORT).show();
                    Log.e("AfterPageMenu", "tried to log into admin page and ecountered: " + e.getMessage());
                }
            });
        }
        else if(id == R.id.action_home){
            Intent go = new Intent(AddRestaurant.this, AfterPage.class);
            startActivity(go);
        }
        else if (id == R.id.action_update) {
            Intent go = new Intent(AddRestaurant.this, Profile.class);
            startActivity(go);
        }
        else if (id == R.id.action_guide) {
//            Intent go = new Intent(AddRestaurant.this, Guide.class);
//            startActivity(go);
        }
        else if (id == R.id.action_about) {
            Intent go = new Intent(AddRestaurant.this, Odot.class);
            startActivity(go);
        }
        else if (id == R.id.action_logout) {
            authenticationService.signOut();
            Intent go = new Intent(AddRestaurant.this, MainActivity2.class);
            startActivity(go);
        }


        return true;
    }
}



