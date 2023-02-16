package uz.mahmudovdev.cameraandpermissions

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import uz.mahmudovdev.cameraandpermissions.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val CAMERA_REQUST_CODE = 1
    val IMAGE_CAPTURE_IMG_CODE = 100
    var bitmapImage: Bitmap? = null
    var name: String? = null
    private lateinit var db: MyDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db= MyDB(binding.root.context)

        binding.takePhoto.setOnClickListener {
            if (checkPermissionIsgranted()) {
                takePhoto()
            } else {
                requestPermission()
            }
        }
        binding.saveBtn.setOnClickListener {
            name = binding.nameEditTxt.text.toString()
            if (bitmapImage != null) {
                saveUserDetail()
            }
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, IMAGE_CAPTURE_IMG_CODE)
        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "Xato bor", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_IMG_CODE && resultCode == RESULT_OK) {
            val image = data?.extras?.get("data") as Bitmap
            bitmapImage = image
            binding.image.setImageBitmap(image)
        }
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.CAMERA
            )
        ) {
            val dialog = AlertDialog.Builder(binding.root.context)
            dialog.setTitle("Diqqat")
            dialog.setMessage("Jarayonni amalga oshirish uchun ruxsat bering")
            dialog.setNegativeButton("Cancel") { _, i ->

            }
            dialog.setPositiveButton("OK") { _, _ ->
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUST_CODE
                )
            }

            dialog.create().show()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUST_CODE
            )
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()

            }
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserDetail() {
        val byteArray = ByteArrayOutputStream()
        bitmapImage?.compress(Bitmap.CompressFormat.PNG, 100, byteArray)
        val img = byteArray.toByteArray()
        val insertData = db.insertData(name,img)
        if (insertData==-1L){
            Toast.makeText(this, "Xatolik sodir bo'ldi", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Saqlandi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissionIsgranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            binding.root.context, android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    }
}