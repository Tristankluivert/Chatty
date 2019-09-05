package com.hybrid.chatty

import android.content.Intent
import android.media.AudioFocusRequest
import android.os.Bundle
import android.os.Handler
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User

class LoginActivity : AppCompatActivity() {

    lateinit var usernameEditText: EditText
    lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        findViewById<ImageButton>(R.id.backArrowImageButton).setOnClickListener { onBackPressed() }
        loginButton = findViewById(R.id.logInButton)
        loginButton.setOnClickListener { attemptLogin() }
        usernameEditText = findViewById(R.id.usernameEditText)
        usernameEditText.setOnEditorActionListener {
                _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                attemptLogin()
                true
            } else {
                false
            }
        }
    }

    private fun attemptLogin() {
        loggingInButtonState()
        val UID = usernameEditText.text.toString()
        CometChat.login(UID, GeneralConstants.API_KEY, object : CometChat.CallbackListener<User>() {
            override fun onSuccess(user: User?) {
                redirectToMainScreen()

            }

            override fun onError(p0: CometChatException?) {
                normalButtonState()
                Toast.makeText(this@LoginActivity, p0?.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun popDialog(){
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.loaderlayout,null)
        val message = dialogView.findViewById<TextView>(R.id.title)
        message.text = "Please wait...."
        builder.setView(dialogView)
        builder.setCancelable(true)
        val dialog = builder.create()
        dialog.show()
        Handler().postDelayed({dialog.dismiss()},2000)
    }

    private fun loggingInButtonState() {
     popDialog()
    }

    private fun normalButtonState() {

        loginButton.text = getString(R.string.log_in)
        loginButton.isEnabled = true
    }

    private fun redirectToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}

