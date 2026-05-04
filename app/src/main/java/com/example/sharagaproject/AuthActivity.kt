package com.example.sharagaproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)

        val userLogin: EditText = findViewById(R.id.user_login)
        val userPassword: EditText = findViewById(R.id.user_pass)
        val button: Button = findViewById(R.id.button)
        val linkToLog: TextView = findViewById(R.id.link_to_reg)


        linkToLog.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val password = userPassword.text.toString().trim()

            if (login == "" || password == "") {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                val db = DBHelper(this, null)

                val userId = db.getUserId(login, password)

                if (userId != -1) {
                    val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putInt("USER_ID", userId)
                        apply()
                    }

                    Toast.makeText(this, "Успешный вход!", Toast.LENGTH_LONG).show()

                    val intent = Intent(this, ItemsActivity::class.java)
                    startActivity(intent)

                    userLogin.text.clear()
                    userPassword.text.clear()
                    finish()
                } else {
                    Toast.makeText(this, "Пользователь $login не найден или пароль неверен",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}