package com.example.formomerpeled.screens;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

import com.example.formomerpeled.R;
import com.example.formomerpeled.Utils.ImageUtil;
import com.example.formomerpeled.models.Restaurant;
import com.example.formomerpeled.services.DatabaseService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddRestaurant extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase database;
    DatabaseService databaseService = DatabaseService.getInstance();
    DatabaseReference myRef;
    Bitmap bitmap;

    EditText etName, etRestaurantType, etPhoneNumber, etAddress, etDomain, etGlutenFreeItems;
    String Name, RestaurantType, PhoneNumber, City, Address, Domain, GlutenFreeItems, imageCode;

    RatingBar ratingBar;
    float rating;

    private final String TAG="AddRestaurant";
    Spinner spCity;
    Button btnAdd, btnBackAddRestaurant, btnGallery;
    ImageView ivResImage;
    public static final int GALLERY_INTENT = 2;

    /// Activity result launcher for selecting image from gallery
    private ActivityResultLauncher<Intent> selectImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_restaurant);
        initViews();

        // בדוק הרשאות גישה לאחסון
        checkPermissions();

        // אתחול של ה-ActivityResultLauncher לבחירת תמונה מהגלריה
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
        ratingBar=findViewById(R.id.ratingBar);

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
            float rating = ratingBar.getRating();


            // בדוק אם התמונה קיימת
            if (!isValid(Name, RestaurantType, PhoneNumber, Address, Domain, GlutenFreeItems, imageCode)) {
                return; // לא מוסיפים אם יש שדות ריקים
            }


                // יצירת אובייקט מסעדה
            Restaurant restaurant = new Restaurant(databaseService.generateRestaurantId(), Name, RestaurantType, Address, City, PhoneNumber, GlutenFreeItems, Domain, imageCode, rating);
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
                    ivResImage.setImageResource(0); // מנקה את התמונה
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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");  // רק תמונות
        selectImageLauncher.launch(intent); // שימוש ב-ActivityResultLauncher
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


    // בקשת הרשאות בזמן ריצה (למערכות Android 6 ומעלה)
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_INTENT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GALLERY_INTENT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // הרשאה ניתנה, אפשר להפעיל את הגלריה
                selectImageFromGallery();
            } else {
                // הרשאה לא ניתנה, תוכל להראות הודעת שגיאה
                Toast.makeText(this, "אין הרשאות לגישה לאחסון", Toast.LENGTH_SHORT).show();
            }
        }
    }
}