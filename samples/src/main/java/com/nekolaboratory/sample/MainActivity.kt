package com.nekolaboratory.sample

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nekolaboratory.Lilium.DefaultAttestCallback
import com.nekolaboratory.Lilium.Lilium
import com.nekolaboratory.Lilium.network.LiliumConfig
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        apkcertificatedigestsha256.setText(getSignatureHash())
        button.setOnClickListener {
            val attestCallback = DefaultAttestCallback { response ->
                val sampleRequestCallback = object : SampleRequestCallback() {
                    override fun onSuccess(response: String) {
                        check_text.setText(check_text.text.toString() + "\n" + response)
                    }
                }
                check_text.setText(response)
                // post(baseUri.text.toString(), response, sampleRequestCallback)
            }
            Lilium().attest(this, baseUri.text.toString(), userId.text.toString(), attestCallback, LiliumConfig("{ attest_report : { package_name : \"packageName\", user_id : \"userUniqueId\", ver : \"ver\", atn : \"atn\", atn_error : \"atnError\", atn_error_msg : \"atnErrorMsg\" }, parameter_request : { package_name : \"packageName\", user_id : \"userUniqueId\" }, parameter_response : { api_key : \"apiKey\", nonce : \"nonce\" } }"))
        }
    }

    private fun getSignatureHash(): String {
        return getApplicationSignature().get(0)
    }

    @SuppressLint("PackageManagerGetSignatures")
    private fun getApplicationSignature(): List<String> {
        val signatureList: List<String>
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val sig = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES).signingInfo
                signatureList = if (sig.hasMultipleSigners()) {
                    sig.apkContentsSigners.map {
                        bytesToHex(MessageDigest.getInstance("SHA-256").digest(it.toByteArray()))
                    }
                } else {
                    sig.signingCertificateHistory.map {
                        bytesToHex(MessageDigest.getInstance("SHA-256").digest(it.toByteArray()))
                    }
                }
            } else {
                val sig = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures
                signatureList = sig.map {
                    bytesToHex(MessageDigest.getInstance("SHA-256").digest(it.toByteArray()))
                }
            }

            return signatureList
        } catch (e: Exception) {
        }
        return emptyList()
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
        val hexChars = CharArray(bytes.size * 3 - 1)
        var v: Int
        for (j in bytes.indices) {
            v = bytes[j].toInt() and 0xFF
            hexChars[j * 3] = hexArray[v / 16]
            hexChars[j * 3 + 1] = hexArray[v % 16]
            if (j < bytes.size - 1) {
                hexChars[j * 3 + 2] = ':'
            }
        }
        return String(hexChars)
    }

    private fun post(baseUri: String, request: String, callback: SampleRequestCallback) {
        object : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void): String? {
                val urlConnection: HttpURLConnection
                try {
                    val url = URL(baseUri + "/report")
                    urlConnection = url.openConnection() as HttpURLConnection
                    urlConnection.requestMethod = "POST"
                    urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                    val outputStream = urlConnection.outputStream
                    val printStream = PrintStream(outputStream)
                    printStream.print(request)
                    printStream.close()
                } catch (e: Exception) {
                    return null
                }
                val buffer: String
                try {
                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream, "UTF-8"))
                    buffer = reader.readLine()
                } catch (e: Exception) {
                    return null
                } finally {
                    urlConnection.disconnect()
                }
                return buffer
            }

            override fun onPostExecute(response: String) {
                super.onPostExecute(response)
                callback.onSuccess(response)
            }
        }.execute()
    }

    open class SampleRequestCallback {
        open fun onSuccess(response: String) {}
    }
}
