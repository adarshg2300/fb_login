package com.trainee.appinventiv.facebook_login
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var fireBaseAuth: FirebaseAuth? = null
    var callBackManager: CallbackManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fireBaseAuth = FirebaseAuth.getInstance()
        callBackManager = CallbackManager.Factory.create()

        login_button.setReadPermissions("email")

        login_button.setOnClickListener {
            signIn()

        }

    }


    private fun signIn() {
        login_button.registerCallback(callBackManager, object : FacebookCallback<LoginResult> {

            override fun onCancel() {
                Toast.makeText(this@MainActivity , "signUp can" , Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(result: LoginResult) {
               Toast.makeText(this@MainActivity , "login success" , Toast.LENGTH_SHORT).show()
                Log.e("on success" , "on succ")
                handleAccessToken(result!!.accessToken)
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_LONG).show()
            }

        })
    }
    private fun handleAccessToken(accessToken: AccessToken?) {
        //Get Credential
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        fireBaseAuth!!.signInWithCredential(credential)
            .addOnFailureListener {
                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
            }.addOnSuccessListener { authResult ->

                val email = authResult.user?.email
                Toast.makeText(this@MainActivity,"Successful log in by :"+email, Toast.LENGTH_LONG).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callBackManager!!.onActivityResult(requestCode,resultCode,data)
    }
}