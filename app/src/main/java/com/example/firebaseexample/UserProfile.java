package com.example.firebaseexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        FirebaseUser currentUser =
                FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, FirebaseAuthActivity.class));
            finish();
            return;
        }

        TextView email = findViewById(R.id.email_text_view);
        TextView displayName = findViewById(R.id.display_name);
        ImageView profilePhoto = findViewById(R.id.profile_image);

            // Name, email address, and profile photo Url
            if(currentUser.getPhotoUrl() != null) {
                String photoUrl = currentUser.getPhotoUrl().toString();
                Glide.with(this) //1
                        .load(photoUrl)
                        .placeholder(R.drawable.ic_profile)
                        .error(R.drawable.ic_profile)
                        .skipMemoryCache(true) //2
                        .diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop()
                        .into(profilePhoto);

            }

            if(currentUser.getDisplayName() != null) {
                displayName.setText(currentUser.getDisplayName());
            }

            if(currentUser.getEmail() != null) {
                email.setText(currentUser.getEmail());
            }

        Button signOut = findViewById(R.id.sign_out_button);
        Button deleteAccount = findViewById(R.id.delete_button);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(UserProfile.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(UserProfile.this,
                                            FirebaseAuthActivity.class));
                                    finish();
                                } else {
                                    // Report error to user
                                }
                            }
                        });
            }
        });
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .delete(UserProfile.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(UserProfile.this,
                                            FirebaseAuthActivity.class));
                                    finish();
                                } else {
                                    // Notify user of error
                                }
                            }
                        });
            }
        });
    }

    }

