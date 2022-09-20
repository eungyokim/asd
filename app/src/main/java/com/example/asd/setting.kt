package com.example.asd

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.http.SslError
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.ViewGroup
import android.webkit.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import com.example.asd.getCode.get_Code
import com.example.asd.SendUserInfo
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class setting : AppCompatActivity() {


    var gson= GsonBuilder().setLenient().create()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://asdapi.implude.kr/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val service = retrofit.create(SendUserInfo::class.java)

    private val retrofit1 = Retrofit.Builder()
        .baseUrl("https://open.neis.go.kr/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val getCode = retrofit1.create(SendSchoolName::class.java)

    lateinit var WhereSchool: String

    var gender: String? = "male"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)

        val sharedPreference = getSharedPreferences("uuid", 0)
        val editor  : SharedPreferences.Editor = sharedPreference.edit()

        val spinnerWhere = findViewById<Spinner>(R.id.spinner_SchoolWhereIs)
        val spinnerWhich = findViewById<Spinner>(R.id.spinner_SchoolWhichSort)
        val setting_button = findViewById<Button>(R.id.setting_button)

        spinnerWhere.adapter = ArrayAdapter.createFromResource(this, R.array.WhereIs, android.R.layout.simple_spinner_dropdown_item)
        spinnerWhich.adapter = ArrayAdapter.createFromResource(this, R.array.WhichSort, android.R.layout.simple_spinner_dropdown_item)


        // Check Box Control.
        var listener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                when (buttonView.id) {
                    R.id.setting_userinfo_gender_male -> {
                        gender = "male"
                        findViewById<CheckBox>(R.id.setting_userinfo_gender_female).isChecked = false
                    }
                    R.id.setting_userinfo_gender_female -> {
                        gender = "female"
                        findViewById<CheckBox>(R.id.setting_userinfo_gender_male).isChecked = false
                    }
                }
            }
        }

        findViewById<CheckBox>(R.id.setting_userinfo_gender_male).setOnCheckedChangeListener(listener)
        findViewById<CheckBox>(R.id.setting_userinfo_gender_female).setOnCheckedChangeListener(listener)



        setting_button.setOnClickListener {
            var name = findViewById<EditText>(R.id.setting_userinfo_name).getText().toString()
            var age = findViewById<EditText>(R.id.setting_userinfo_age).getText().toString()
            var uuid = findViewById<EditText>(R.id.setting_userinfo_code).getText().toString()
            var school = findViewById<EditText>(R.id.setting_userinfo_school).getText().toString()
            var whereIs = spinnerWhere.selectedItem.toString()
            var whichSort = spinnerWhich.selectedItem.toString()


            if(name.equals("") ||
                age.equals("") ||
                gender.equals("") ||
                uuid.equals("") ||
                school.equals("") || whereIs.equals("") || whichSort.equals("") == null){
                Toast.makeText(this, "입력되지 않은 부분이 있습니다. 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                getCode.SendUserInfo(whereIs, school, whichSort).enqueue(object :
                Callback<get_Code> {
                    override fun onResponse(
                        call: Call<get_Code>,
                        response: Response<get_Code>
                    ) {
                        response.body()?.let {
                            it.schoolInfo?.forEach{ book->
                                book!!.row?.forEach { it ->
                                    if (WhereSchool+' ' == it!!.oRGRDNZC.toString()){
                                        // 사용자 정보 저장
                                        service.SendUserInfo(name, age, gender, uuid, it!!.sDSCHULCODE.toString()).enqueue(object :
                                            Callback<getMsg> {
                                            override fun onResponse(
                                                call: Call<getMsg>,
                                                response: Response<getMsg>
                                            ) {
                                                Toast.makeText(this@setting, "성공적으로 사용자 정보가 등록되었습니다.", Toast.LENGTH_SHORT).show()
                                                editor.putString("uuid", uuid)
                                                editor.commit()
                                                val intent = Intent(this@setting, Main::class.java)
                                                startActivity(intent)
                                                overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit)
                                            }
                                            override fun onFailure(call: Call<getMsg>, t: Throwable) {
                                                Log.d("result",t.toString())
                                            }
                                    })
                                    }
                                }

                            }
                        }
                    }
                    override fun onFailure(call: Call<get_Code>, t: Throwable) {
                        Log.d("result",t.toString())
                    }
                })

            }
        }

        findViewById<TextView>(R.id.setting_userinfo_school).setOnClickListener {
            showKakaoAddressWebView()
        }
        navigationBar()
        findViewById<ConstraintLayout>(R.id.SampleLayoutView1).setOnTouchListener(object: OnSwipeTouchListener(this@setting) {
            override fun onSwipeRight() {
                val intent = Intent(this@setting, Main::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit)
            }
        })
    }

    private fun navigationBar() {
        val navigationbar_book = findViewById<ImageButton>(R.id.navigation_bar_book)
        val navigationbar_home = findViewById<ImageButton>(R.id.navigation_bar_home)

        navigationbar_book.setOnClickListener {
            val intent = Intent(this@setting, Calendar::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit)
        }
        navigationbar_home.setOnClickListener {
            val intent = Intent(this@setting, Main::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit)
        }
    }

    private val client: WebViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return false
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            handler?.proceed()
        }
    }

    private fun showKakaoAddressWebView() {
        val webView = findViewById<WebView>(R.id.webView)
        webView.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)
        }

        webView.apply {
            addJavascriptInterface(WebViewData(), "asd")
            webViewClient = client
            webChromeClient = chromeClient
            loadUrl("https://eks05.github.io/")
        }
    }

    private inner class WebViewData {
        @JavascriptInterface
        fun getAddress(zoneCode: String, roadAddress: String, buildingName: String) {

            CoroutineScope(Dispatchers.Default).launch {

                withContext(CoroutineScope(Dispatchers.Main).coroutineContext) {
                    findViewById<TextView>(R.id.setting_userinfo_school).setText("$buildingName")
                    WhereSchool = "$zoneCode"
                }
            }
        }
    }

    private val chromeClient = object : WebChromeClient() {

        override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {

            val newWebView = WebView(this@setting)

            newWebView.settings.javaScriptEnabled = true

            val dialog = Dialog(this@setting)

            dialog.setContentView(newWebView)

            val params = dialog.window!!.attributes

            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.attributes = params
            dialog.show()

            newWebView.webChromeClient = object : WebChromeClient() {
                override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                    super.onJsAlert(view, url, message, result)
                    return true
                }

                override fun onCloseWindow(window: WebView?) {
                    dialog.dismiss()
                }
            }

            (resultMsg!!.obj as WebView.WebViewTransport).webView = newWebView
            resultMsg.sendToTarget()

            return true
        }
    }

}

