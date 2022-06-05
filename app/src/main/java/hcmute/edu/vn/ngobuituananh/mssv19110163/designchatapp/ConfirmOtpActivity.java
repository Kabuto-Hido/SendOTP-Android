package hcmute.edu.vn.ngobuituananh.mssv19110163.designchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ConfirmOtpActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    PhoneAuthProvider.ForceResendingToken token;
    Button btn_confirmOtp,btn_resendCode;
    TextView nofi3;
    EditText editTextInput1,editTextInput2,editTextInput3,
            editTextInput4,editTextInput5,editTextInput6;

    String mphone, mverificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_otp);

        btn_confirmOtp = (Button) findViewById(R.id.btn_confirmOtp);
        btn_resendCode = (Button) findViewById(R.id.btn_resendCode);
        nofi3 = (TextView) findViewById(R.id.nofi3);
        editTextInput1 = (EditText) findViewById(R.id.editTextInput1);
        editTextInput2 = (EditText) findViewById(R.id.editTextInput2);
        editTextInput3 = (EditText) findViewById(R.id.editTextInput3);
        editTextInput4 = (EditText) findViewById(R.id.editTextInput4);
        editTextInput5 = (EditText) findViewById(R.id.editTextInput5);
        editTextInput6 = (EditText) findViewById(R.id.editTextInput6);

        firebaseAuth = FirebaseAuth.getInstance();

        mphone = getIntent().getStringExtra("phone");
        mverificationId = getIntent().getStringExtra("verificationId");

        nofi3.setText("We have sent you an SMS with the code to "+mphone);

        btn_confirmOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextInput1.getText().toString().isEmpty()){
                    editTextInput1.setError("Required");
                    return;
                }
                if(editTextInput2.getText().toString().isEmpty()){
                    editTextInput2.setError("Required");
                    return;
                }
                if(editTextInput3.getText().toString().isEmpty()){
                    editTextInput3.setError("Required");
                    return;
                }
                if(editTextInput4.getText().toString().isEmpty()){
                    editTextInput4.setError("Required");
                    return;
                }
                if(editTextInput5.getText().toString().isEmpty()){
                    editTextInput5.setError("Required");
                    return;
                }
                if(editTextInput6.getText().toString().isEmpty()){
                    editTextInput6.setError("Required");
                    return;
                }

                String code=
                        editTextInput1.getText().toString()+
                        editTextInput2.getText().toString()+
                        editTextInput3.getText().toString()+
                        editTextInput4.getText().toString()+
                        editTextInput5.getText().toString()+
                        editTextInput6.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mverificationId, code);
                signInWithPhoneAuthCredential(credential);
            }
        });
        btn_resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthOptions options =  PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(mphone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setForceResendingToken(token)
                        .setActivity(ConfirmOtpActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(ConfirmOtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                mverificationId = verificationId;
                token = forceResendingToken;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ConfirmOtpActivity.this,
                                    "signInWithCredential:success", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            startActivity(new Intent(ConfirmOtpActivity.this,ListMessageActivity.class));
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(ConfirmOtpActivity.this, "failure", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}