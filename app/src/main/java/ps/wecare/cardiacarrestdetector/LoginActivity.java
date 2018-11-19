package ps.wecare.cardiacarrestdetector;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ps.wecare.cardiacarrestdetector.db.Message;
import ps.wecare.cardiacarrestdetector.db.User;
import ps.wecare.cardiacarrestdetector.db.myDbAdapter;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {
    private App app;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private boolean blockLogin = false;
    private String tag = "LOGIN";

    // sign UP
    private AutoCompleteTextView mSignupPhoneView;
    private AutoCompleteTextView mSignupAgeView;
    private AutoCompleteTextView mSignupNameView;
    private EditText mSignupPasswordView;
    private Button mSigninButton ;
    private Button mSignupButton ;


    // Sign In
    private AutoCompleteTextView mSigninPhoneView;
    private EditText mSigninPasswordView;


    private View mProgressView;
    private View mLoginFormView;

    private myDbAdapter helper;

    private TabHost tabs;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (App.getInstance().isLoggedIn()) {
            Intent n = new Intent(LoginActivity.this, BelovedCircleActivity.class);
            LoginActivity.this.startActivity(n);
            finish();
        }
        // make the title centered
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.centered_title_layout);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitle)).setText(R.string.title_activity_login);
        // Set up tab host

        tabs=(TabHost)findViewById(R.id.tabHost);

        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("login");

        spec.setContent(R.id.SignIn);
        //spec.setIndicator(R.string.btn_sign_in);
        spec.setIndicator(getString(R.string.btn_sign_in) );

        tabs.addTab(spec);

        spec=tabs.newTabSpec("signup");
        spec.setContent(R.id.SignUp);
        spec.setIndicator(getString(R.string.btn_sign_up) );
        tabs.addTab(spec);



        // Sign Up data
        mSignupPhoneView = (AutoCompleteTextView) findViewById(R.id.signup_phone);
        mSignupAgeView = (AutoCompleteTextView) findViewById(R.id.signup_age);
        mSignupNameView = (AutoCompleteTextView) findViewById(R.id.signup_name);;
        mSignupPasswordView= (EditText) findViewById(R.id.signup_password);

        // Sign In data
        mSigninPhoneView = (AutoCompleteTextView) findViewById(R.id.signin_phone);
        mSigninPasswordView= (EditText) findViewById(R.id.signin_password);

        mSigninButton = (Button) findViewById(R.id.signin_button);
        mSignupButton = (Button) findViewById(R.id.signup_button);

        mSigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        helper = App.getInstance().getDbHelper();
    }
    private void attemptSignup() {
        if (blockLogin) {
            return;
        }

        blockLogin = true;

        // Store values at the time of the login attempt.
        final String phone = mSignupPhoneView.getText().toString();
        final String name = mSignupNameView.getText().toString();
        final String age = mSignupAgeView.getText().toString();
        final String password = mSignupPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid phone address.
        if (TextUtils.isEmpty(phone)) {
            mSignupPhoneView.setError(getString(R.string.error_field_required));
            focusView = mSignupPhoneView;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            mSignupPhoneView.setError(getString(R.string.error_invalid_phone));
            focusView = mSignupPhoneView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            blockLogin = false;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

                    try {
                        User user = new User(name,phone,password,age);
                        user = helper.insertUser(user);
                        Message.message(this,"user with id " + user.getId() + " Name "  + user.getName());
                        //App.getInstance().logIn(name ,phone, password);


                    } catch (Exception e) {
                            Message.message(this,""+e);
                    } finally {
                        showProgress(false);
                        blockLogin = false;
                    }


        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid phone, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (blockLogin) {
            return;
        }
        blockLogin = true;
        // Reset errors.
        mSigninPhoneView.setError(null);
        mSigninPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String phone = mSigninPhoneView.getText().toString();
        final String password = mSigninPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mSigninPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mSigninPasswordView;
            cancel = true;
        }

        // Check for a valid phone address.
        if (TextUtils.isEmpty(phone)) {
            mSigninPhoneView.setError(getString(R.string.error_field_required));
            focusView = mSigninPhoneView;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            mSigninPhoneView.setError(getString(R.string.error_invalid_phone));
            focusView = mSigninPhoneView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
            blockLogin = false;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
             User user = helper.getUser(phone);
            if (user != null){
                App.getInstance().logIn(user);
                Intent n = new Intent(LoginActivity.this, BelovedCircleActivity.class);
                LoginActivity.this.startActivity(n);
                finish();
            }else{
                mSigninPhoneView.setError("INCORRECT");
                showProgress(false);
            }

            //App.getInstance().getRequestQueue().add(strReqLogin);
        }
    }

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.length() > 3;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }




    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }


}

