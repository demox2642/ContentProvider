package com.skilbox.contentprovider

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.fragment_file_share.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class FileShareFragment : Fragment(R.layout.fragment_file_share) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // saveFileWithTextToExternalStorage()

        shareFile()

        shared_button_view.setOnClickListener {
            sharedText()
        }
    }

    private fun sharedText() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, "Отправленный текст")
            type = "text/plain"
        }

        val sharedIntent = Intent.createChooser(intent, null)
        startActivity(sharedIntent)
    }

    private fun saveFileWithTextToExternalStorage() = lifecycleScope.launch(Dispatchers.IO) {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED)return@launch
        val dir = requireContext().getExternalFilesDir("saved_files")
        val file = File(dir, "textfile.txt")
        if (file.exists())return@launch
        try {
            file.outputStream().buffered().use { stream ->
                (0..1000).forEach {
                    stream.write(it.toString().toByteArray())
                }
            }
            Log.e("FileShareFragment", "File save success")
        } catch (t: Throwable) {
            Log.e("FileShareFragment", "File save error:$t")
        }
    }

    private fun shareFile() {
        lifecycleScope.launch(Dispatchers.IO) {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) return@launch
            val dir = requireContext().getExternalFilesDir("saved_files")
            val file = File(dir, "textfile.txt")
            if (file.exists().not()) return@launch

            val uri = FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.file_provider", file)
            Log.e("FileShareFragment", "file uri: $uri")

            val intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_STREAM, uri)
                type = requireContext().contentResolver.getType(uri)
                setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val sharedIntent = Intent.createChooser(intent, null)
            startActivity(sharedIntent)
        }
    }
}
