package com.example.familysafe;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import com.example.familysafe.databinding.ActivitySignInBinding;
import com.example.familysafe.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private String email="",pass="";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog pD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        firebaseAuth=FirebaseAuth.getInstance();
        pD= new ProgressDialog(this);
        pD.setTitle("Please Wait");
       pD.setMessage("Creating Your Account...");
        pD.setCanceledOnTouchOutside(false);
        binding= ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            validation();
            }
        });
    }
    private void validation(){
    email= binding.txtEmail.getText().toString().trim();
    pass=binding.txtPass.getText().toString().trim();

    if(TextUtils.isEmpty(pass)&&TextUtils.isEmpty(email)){
        binding.txtEmail.setError("Enter Email");
        binding.txtPass.setError("Enter Password");
    }
    else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        binding.txtEmail.setError("Enter Valid Email");
    }
    else if(pass.length()<6){
        binding.txtPass.setError("Enter Minimum 6 Letter Password");
    }
    else{
        firebaseSignUp();
    }
    }
    private void firebaseSignUp(){
        pD.show();
        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        pD.dismiss();
                        Toast.makeText(SignUp.this, "SignUp Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(SignUp.this,MapDashboard.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pD.dismiss();
                Toast.makeText(SignUp.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}