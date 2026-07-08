package com.app.arcadeaproject.ui.main.profile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.app.arcadeaproject.R
import com.app.arcadeaproject.data.remote.ApiClient
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etBio: EditText
    private lateinit var ivAvatar: ImageView
    private lateinit var tvInitial: TextView
    private lateinit var btnSave: Button

    private var selectedImageUri: Uri? = null

    // Launcher untuk memilih gambar dari galeri
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            // Tampilkan preview gambar yang dipilih
            tvInitial.visibility = View.GONE
            Glide.with(this)
                .load(uri)
                .circleCrop()
                .into(ivAvatar)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profil_edit)

        etName = findViewById(R.id.et_edit_name)
        etEmail = findViewById(R.id.et_edit_email)
        etBio = findViewById(R.id.et_edit_bio)
        ivAvatar = findViewById(R.id.iv_edit_avatar)
        tvInitial = findViewById(R.id.tv_avatar_initial)
        btnSave = findViewById(R.id.btn_save_profile)

        val btnBack = findViewById<ImageButton>(R.id.btn_back_edit_profile)
        btnBack.setOnClickListener {
            finish()
        }

        loadCurrentData()

        btnSave.setOnClickListener {
            saveProfileChanges()
        }

        findViewById<ImageView>(R.id.btn_change_avatar).setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun loadCurrentData() {
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val name = sharedPref.getString("user_name", "")
        val email = sharedPref.getString("user_email", "")
        val bio = sharedPref.getString("user_bio", "")
        val imageUrl = sharedPref.getString("user_image", "")

        etName.setText(name)
        etEmail.setText(email)
        etBio.setText(bio)

        updateAvatarUI(name, imageUrl)
    }

    private fun updateAvatarUI(name: String?, imageUrl: String?) {
        val initial = name?.take(1)?.uppercase() ?: "U"
        tvInitial.text = initial

        if (!imageUrl.isNullOrEmpty()) {
            val fullUrl = if (imageUrl.startsWith("http")) imageUrl else "${ApiClient.BASE_URL}${imageUrl.removePrefix("/")}"
            tvInitial.visibility = View.GONE
            Glide.with(this)
                .load(fullUrl)
                .circleCrop()
                .placeholder(R.drawable.circle_avatar_bg)
                .into(ivAvatar)
        } else {
            tvInitial.visibility = View.VISIBLE
            ivAvatar.setImageResource(R.drawable.circle_avatar_bg)
        }
    }

    private fun saveProfileChanges() {
        val newName = etName.text.toString().trim()
        val newEmail = etEmail.text.toString().trim()
        val newBio = etBio.text.toString().trim()

        if (newName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Name and Email cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)

        if (userId == -1) return

        lifecycleScope.launch {
            try {
                btnSave.isEnabled = false
                btnSave.text = "Saving..."

                val namaBody = newName.toRequestBody("text/plain".toMediaType())
                val emailBody = newEmail.toRequestBody("text/plain".toMediaType())
                val bioBody = newBio.toRequestBody("text/plain".toMediaType())

                var imagePart: MultipartBody.Part? = null
                selectedImageUri?.let { uri ->
                    val file = uriToFile(uri, this@ProfileEditActivity)
                    if (file != null) {
                        val requestFile = file.asRequestBody("image/*".toMediaType())
                        imagePart = MultipartBody.Part.createFormData("foto_profile", file.name, requestFile)
                    }
                }

                val response = ApiClient.instance.updateProfile(
                    userId,
                    namaBody,
                    emailBody,
                    bioBody,
                    imagePart
                )

                if (response.isSuccessful && response.body()?.success == true) {
                    val updatedUser = response.body()?.user
                    with(sharedPref.edit()) {
                        putString("user_name", newName)
                        putString("user_email", newEmail)
                        putString("user_bio", newBio)
                        updatedUser?.gambar?.let { putString("user_image", it) }
                        apply()
                    }
                    Toast.makeText(this@ProfileEditActivity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val errorMsg = response.body()?.message ?: "Failed to update profile"
                    Toast.makeText(this@ProfileEditActivity, errorMsg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileEditActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                btnSave.isEnabled = true
                btnSave.text = "Save Changes"
            }
        }
    }

    private fun uriToFile(uri: Uri, context: Context): File? {
        val contentResolver = context.contentResolver
        val tempFile = File(context.cacheDir, getFileName(uri, context))

        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val outputStream = FileOutputStream(tempFile)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getFileName(uri: Uri, context: Context): String {
        var name = "temp_image.jpg"
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) name = it.getString(nameIndex)
            }
        }
        return name
    }
}