package com.example.familysafe;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.familysafe.databinding.ActivitySignInBinding;
import com.example.familysafe.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signIn extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private ProgressDialog pD;
    private FirebaseAuth firebaseAuth;
    private String email="",pass="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        pD= new ProgressDialog(this);
        pD.setTitle("Please Wait");
        pD.setMessage("Logging In...");
        pD.setCanceledOnTouchOutside(false);
        binding= ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       getSupportActionBar().hide();
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              firebaseLogin();
            }
        });
        binding.signupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(signIn.this,SignUp.class);
                startActivity(intent);
            }
        });
    }
    private void firebaseLogin(){
        pD.show();
        email= binding.txtEmail.getText().toString().trim();
        pass=binding.txtPass.getText().toString().trim();
        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(signIn.this, "SignIn Successfully", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(signIn.this,MapDashboard.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                pD.dismiss();
                Toast.makeText(signIn.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}