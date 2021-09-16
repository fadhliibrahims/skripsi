package com.example.testui

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [KompresFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KompresFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val MY_REQUEST_CODE_PERMISSION = 1000
    private val MY_RESULT_CODE_FILECHOOSER = 2000
    private val LOG_TAG = "AndroidExample"

    private lateinit var buttonBrowse: Button
    private lateinit var editTextPath: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_kompres, container, false)
        editTextPath = view.findViewById(R.id.editText_path)
        buttonBrowse = view.findViewById(R.id.button_browse)

        buttonBrowse.setOnClickListener {
            askPermissionAndBrowseFile()
        }

        return view
    }

    private fun askPermissionAndBrowseFile() {
        if (askForPermissions()) {
            browseFile()
        }
    }

    private fun askForPermissions(): Boolean {
        if (!isPermissionsAllowed()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this as Activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(this as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_REQUEST_CODE_PERMISSION)
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
            .setPositiveButton("App Settings", DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            })
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
                if (grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    browseFile()
                } else {
                    askForPermissions()
                }
                return
            }
        }
    }

    private fun browseFile() {

    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment KompresFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            KompresFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}