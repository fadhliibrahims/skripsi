package com.example.skripsi

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import java.io.*
import kotlin.system.measureTimeMillis

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DekompresFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DekompresFragment : Fragment() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    private val MY_REQUEST_CODE_PERMISSION = 1000
    private val MY_RESULT_CODE_FILECHOOSER = 2000
    private val MY_WRITE_REQUEST_CODE_PERMISSION = 3000
    private var algorithm = 0
    private var originalText = ""
    private var compressedText = ""
    private var filePath = ""
    private var originalSize = 0L
    private var compressedSize = 0L
//    private var compressionRatio = 0F


    private lateinit var buttonBrowse: Button
    private lateinit var textViewPath: TextView
    private lateinit var textBox: TextView
    private lateinit var textViewSize: TextView
    private lateinit var textViewResultSize: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var buttonDekompres: Button
//    private lateinit var textViewResultSize: TextView
//    private lateinit var textViewCR: TextView
    private lateinit var textViewWaktuDekompresi: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dekompres, container, false)
        textViewPath = view.findViewById(R.id.textView_pathDekompresi)
        buttonBrowse = view.findViewById(R.id.button_browseDekompresi)
        textBox = view.findViewById(R.id.textBoxDekompresi)

        buttonBrowse.setOnClickListener {
            askPermissionAndBrowseFile()
        }
        textBox.movementMethod = ScrollingMovementMethod()
        textViewSize = view.findViewById(R.id.textView_sizeDekompresi)
        textViewResultSize = view.findViewById(R.id.textView_resultSizeDekompresi)

        radioGroup = view.findViewById(R.id.radioGroupDekompresi)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.stoutCodeDekompresi -> algorithm = 0
                R.id.fibonacciCodeDekompresi -> algorithm = 1
            }
        }


        buttonDekompres = view.findViewById(R.id.button_dekompres)
        buttonDekompres.setOnClickListener {
            val time = measureTimeMillis {
                dekompresTeks()
            }
            saveHasil()
            textViewWaktuDekompresi.text = "%o ms".format(time)
        }

//        textViewResultSize = view.findViewById(R.id.textView_resultSize)
//        textViewCR = view.findViewById(R.id.textView_cr)
        textViewWaktuDekompresi = view.findViewById(R.id.textView_waktuDekompresi)

        return view
    }

    //region Asking Permission
    private fun askPermissionAndBrowseFile() {
        if (askForPermissions()) {
            doBrowseFile()
        }
    }

    private fun askForPermissions(): Boolean {
        if (!isPermissionsAllowed()) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showPermissionDeniedDialog()
            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_REQUEST_CODE_PERMISSION)
            }
            return false
        }
        return true
    }

    private fun isPermissionsAllowed(): Boolean {
        return context?.let {
            ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE)
        } == PackageManager.PERMISSION_GRANTED
    }

    private fun showPermissionDeniedDialog() {
        context?.let { AlertDialog.Builder(it)
            .setTitle("Permission Ditolak")
            .setMessage("Silahkan izinkan permission melalui App Settings.")
            .setPositiveButton("App Settings") { dialog, which ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_REQUEST_CODE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    doBrowseFile()
                } else {
                    askForPermissions()
                }
                return
            }
            MY_WRITE_REQUEST_CODE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    doSaveFile()
                } else {
                    saveHasil()
                }
                return
            }
        }
    }
    //endregion AskingPermission

    private fun doBrowseFile() {
        var chooseFileIntent = Intent(Intent.ACTION_GET_CONTENT)
        chooseFileIntent.setType("*/*")

        //Only Return URI that can be opened with ContentResolver
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE)

        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a File")
        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            MY_RESULT_CODE_FILECHOOSER -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        val fileUri = data.getData()
                        filePath = getPath(requireContext(), fileUri!!)!!
                        compressedText = readText(filePath)
                        compressedSize = File(filePath).length()

                        textBox.text = compressedText
                        textViewPath.text = filePath
                        textViewSize.text = "%.2f kb".format(compressedSize / 1024.0)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textViewPath.setTextAppearance(R.style.TextAppearance_AppCompat_Body1)
                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun readText(path: String): String {
        val inputStream: InputStream
        var text = ""
        try {
            inputStream = FileInputStream(path)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            text = String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return text
    }

    //region Get Path from URI
    fun getPath(context: Context, uri: Uri): String? {
        val isKitKatorAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKatorAbove && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.getContentResolver().query(uri!!, projection, selection, selectionArgs,null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            if (cursor != null) cursor.close()
        }
        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
    //endregion

//    fun onRadioButtonClicked(view: View) {
//        if (view is RadioButton) {
//            // Is the button now checked?
//            val checked = view.isChecked
//
//            // Check which radio button was clicked
//            when (view.getId()) {
//                R.id.stoutCode ->
//                    if (checked) {
//                        algorithm = 0
//                    }
//                R.id.fibonacciCode ->
//                    if (checked) {
//                        algorithm = 1
//                    }
//            }
//        }
//    }

    private fun dekompresTeks() {
        val dekompres = Dekompres()
        originalText = dekompres.dekompresText(compressedText, algorithm)
        textBox.text = originalText
    }

    private fun askPermissionsToSave(): Boolean {
        if(Build.VERSION.SDK_INT>22){
            if (!isWritingPermissionsAllowed()) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showPermissionDeniedDialog()
                } else {
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_WRITE_REQUEST_CODE_PERMISSION)
                }
                return false
            }
        }
        return true
    }

    private fun saveHasil() {
        if (askPermissionsToSave()) {
            doSaveFile()
        }
    }

    private fun isWritingPermissionsAllowed(): Boolean {
        return context?.let {
            ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } == PackageManager.PERMISSION_GRANTED
    }

    private fun doSaveFile() {
        var directory = Environment.getExternalStorageDirectory().absolutePath
        var fullName = filePath.substring(filePath.lastIndexOf("/")+1)
        var fileName = fullName.substringBeforeLast(".") + ".txt"
        var externalFile = File(directory, fileName)
        return try {
            val fileOutPutStream = FileOutputStream(externalFile)
            fileOutPutStream.write(originalText.toByteArray())
            originalSize = File(directory + "/$fileName").length()
//            compressionRatio = originalSize * 1F / compressedSize
            textViewResultSize.text = "%.2f kb".format(compressedSize / 1024.0)
//            textViewCR.text = "%.2f".format(compressionRatio)
            fileOutPutStream.close()
            Toast.makeText(requireContext(), "file saved to" + directory + "/$fileName", Toast.LENGTH_LONG).show()
        } catch(e: IOException) {
            e.printStackTrace()
        }
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment DekompresFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            DekompresFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}