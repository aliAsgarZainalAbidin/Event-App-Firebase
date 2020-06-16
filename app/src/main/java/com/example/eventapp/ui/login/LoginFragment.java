package com.example.eventapp.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventapp.R;
import com.example.eventapp.ui.home.HomeActivity;
import com.example.eventapp.ui.login.registrasi.RegistrasiFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    private FirebaseAuth mFirebaseAuth;
    private Button btnLogin;
    private TextView tvRegistrasi;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions gso;
    private  FirebaseUser mFirebaseUser;
    public static final int RC_SIGN_IN = 100;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogin = view.findViewById(R.id.button_login);
        tvRegistrasi = view.findViewById(R.id.tv_login_regis);
        etEmail = view.findViewById(R.id.et_login_email);
        etPassword = view.findViewById(R.id.et_login_password);
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser!=null){
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        tvRegistrasi.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    private void signIn(){
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            mFirebaseUser = mFirebaseAuth.getCurrentUser();
                            if (mFirebaseUser!=null){
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult();
            firebaseAuthWithGoogle(account);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_login : {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                boolean isEmpty = false;

                if (email.isEmpty()){
                    etEmail.setError("Masukkan email Anda");
                    isEmpty = true;
                }

                if (password.isEmpty()){
                    etPassword.setError("Masukkan password Anda");
                    isEmpty = true;
                }

                if (!isEmpty){
                    mFirebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                    if (user!=null){
                                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                }
                            });
                }

                break;
            }
            case R.id.tv_login_regis : {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_main_container, new RegistrasiFragment())
                        .commit();
                break;
            }
        }
    }
}
