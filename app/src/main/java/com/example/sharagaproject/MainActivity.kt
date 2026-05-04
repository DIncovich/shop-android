package com.example.sharagaproject

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userLogin: EditText = findViewById(R.id.user_login)
        val userMail: EditText = findViewById(R.id.user_mail)
        val userPass: EditText = findViewById(R.id.user_pass)
        val btnReg: Button = findViewById(R.id.button)

        btnReg.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val email = userMail.text.toString().trim()
            val pass = userPass.text.toString().trim()

            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

            if (login.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else if (!email.matches(emailPattern.toRegex())) {
                Toast.makeText(this, "Некорректный Email", Toast.LENGTH_SHORT).show()
            } else if (pass.length < 8) {
                Toast.makeText(this, "Пароль слишком короткий (мин. 8)", Toast.LENGTH_SHORT).show()
            } else {
                val db = DBHelper(this, null)
                if (db.checkUserExists(login, email)) {
                    юthis, "Логин или почта уже заняты", Toast.LENGTH_SHORT).show()
                } else {
                    db.addUser(User(login, email, pass))
                    Toast.makeText(this, "Успешно!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, AuthActivity::class.java))
                }
            }
        }

        val linkToLogin: Button = findViewById(R.id.link_to_login)
        linkToLogin.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }
}