package com.dicoding.mysharedpreferences

import UserPreference
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.mysharedpreferences.databinding.ActivityFormUserPreferenceBinding

class FormUserPreferenceActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var userModel: UserModel
    private lateinit var binding: ActivityFormUserPreferenceBinding

    companion object {
        const val EXTRA_TYPE_FORM = "extra_type_form"
        const val EXTRA_RESULT = "extra_result"
        const val RESULT_CODE = 101

        const val TYPE_ADD = 1
        const val TYPE_EDIT = 2

        private const val FIELD_REQUIRED = "field tidak boleh kosong"
        private val FIELD_DIGIT_ONLY = "Hanya boleh terisi numerik"
        private val FIELD_IS_NOT_VALID = "Email tidak valid"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFormUserPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_form_user_preference)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnSave.setOnClickListener(this)

        userModel = intent.getParcelableExtra<UserModel>("USER") as UserModel
        val formType = intent.getIntExtra(EXTRA_TYPE_FORM, 0)

        var actionBarTitle = ""
        var btnTitle = ""
        when (formType) {
            TYPE_ADD -> {
                actionBarTitle = "Tambah User"
                btnTitle = "Simpan"
            }

            TYPE_EDIT -> {
                actionBarTitle = "Edit User"
                btnTitle = "Update"
                showPreferenceInform()
            }
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnSave.text = btnTitle
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showPreferenceInform() {
        binding.edtName.setText(userModel.name)
        binding.edtEmail.setText(userModel.email)
        binding.edtAge.setText(userModel.age.toString())
        binding.edtPhone.setText(userModel.phoneNumber)
        if (userModel.isLove) binding.rbYes.isChecked = true else binding.rbNo.isChecked = true
    }


    override fun onClick(v: View) {
        if (v.id == R.id.btn_save) {
            val name = binding.edtName.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val age = binding.edtAge.text.toString().trim()
            val phoneNo = binding.edtPhone.text.toString().trim()
            val isLoveMU = binding.rgLoveMu.checkedRadioButtonId == R.id.rb_yes

            if (name.isEmpty()) {
                binding.edtName.error = FIELD_REQUIRED
                return
            }

            if (email.isEmpty()) {
                binding.edtEmail.error = FIELD_REQUIRED
                return
            }
            if (!isValidEmail(email)) {
                binding.edtEmail.error = FIELD_IS_NOT_VALID
                return
            }

            if (age.isEmpty()) {
                binding.edtAge.error = FIELD_REQUIRED
                return
            }

            if (!age.matches("-?\\d+(\\.\\d+)?".toRegex())) {
                binding.edtAge.error = FIELD_DIGIT_ONLY
                return
            }

            if (phoneNo.isEmpty()) {
                binding.edtPhone.error = FIELD_REQUIRED
                return
            }

            if (!TextUtils.isDigitsOnly(phoneNo)) {
                binding.edtPhone.error = FIELD_DIGIT_ONLY
                return
            }

            saveUser(name, email, age, phoneNo, isLoveMU)
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_RESULT, userModel)
            setResult(RESULT_CODE, resultIntent)

            finish()

        }
    }

    private fun saveUser(
        name: String,
        email: String,
        age: String,
        phoneNo: String,
        loveMU: Boolean
    ) {
        val userPreference = UserPreference(this)

        userModel.name = name
        userModel.email = email
        userModel.age = age.toInt()
        userModel.phoneNumber = phoneNo
        userModel.isLove = loveMU

        userPreference.setUser(userModel)
        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show()
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}