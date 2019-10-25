<img src="https://github.com/mofneko/Lilium/raw/master/art/LILIUM.jpg" alt="Lilium" style="width:300px;"/>

=============================================

[![Release](https://jitpack.io/v/mofneko/Lilium.svg)](https://jitpack.io/#mofneko/Lilium)

LiliumはGoogleによって提供されているSafetyNet Attestation APIをより効果的かつ，簡単に使用するための軽量なラッパーライブラリです. Unityと互換性があります.

# Liliumの哲学

このライブラリは主にアプリケーションの改竄対策に有効です．
実際にはSafetyNet APIを実行することによって返却されるReportファイルに記載されている以下の項目：

```
nonce
タイムスタンプ
APK証明書のSHA-256ハッシュ
APKのSHA-256ハッシュ
```

を検証することでAPKファイルが悪意のあるユーザーによって改竄された状態で実行されていることを検出可能にします．
Reportファイルに記載されている，それぞれの項目に対しては下記を参照してください．

```
 ・nonce
nonceはLiliumの初期化処理実行時に引数として渡すバイト配列で，この値がそのまま，SafetyNetReport内部に内包されて入ってきます．この値の存在意義としては，例えば悪意のあるユーザーが，事前に正常な非改竄アプリで検証処理を実行して，Report内容をコピーし，改竄アプリでサーバーに送信するReportを正常系のものに置換することでチェックをパスするハックをnonceをワンタイムトークンとして使用することで阻止することができます．
 ・timestamp
SafetyNetによってReportが作成された時間が記録されています．この値は通常，LiliumのReport作成時刻と相違がない時間となっています．あまりにかけはなれている場合は，上記の不正が施されている可能性があるので，ライブラリのユーザーはこの値も検証するようにしてください．
 ・APK証明書のSHA-256ハッシュ
通常，APKをAndroid端末で実行するには開発者の秘密鍵で署名された証明書が必要です．改竄者がAPKファイルを改竄した場合にもそれは同様で，ディベロッパーの秘密鍵が改竄者に流出でもするような事態がない限り，改竄者の秘密鍵で署名がされるので非改竄APKと改竄APKで署名ファイルのハッシュが異なります．当ライブラリはこの値をチェックすることによって改竄の検証を行うことをメインにしています．
 ・APKのSHA-256ハッシュ
APKファイル本体のハッシュになります．これは説明するまでもなく，改竄前と改竄後でAPKファイルのハッシュが異なる性質を利用して改竄の判定を行うことができます．しかし，リリース毎にこの値が変動するので運用のコストがかかります．APK証明書のSHA-256ハッシュの説明のようにディベロッパーの秘密鍵が流出している可能性でもない限りはこの値のチェックをスキップしても問題ないと考えられます．
```

SafetyNetから発行される完全なReportファイルの内容を下記に示します．

```
{
  "timestampMs": 9860437986543,
  "nonce": "R2Rra24fVm5xa2Mg",
  "apkPackageName": "com.package.name.of.requesting.app",
  "apkCertificateDigestSha256": ["base64 encoded, SHA-256 hash of the
                                  certificate used to sign requesting app"],
  "ctsProfileMatch": true,
  "basicIntegrity": true,
}
```

### LiliumReportの構造

LiliumはSafetyNet APIから発行されるReportファイルをラップしたReportを作成し，実行元アプリに返却します．
これはオリジナルのReportと比較して，より詳細をサーバーに報告するという目的があります．ライブラリ実行元アプリケーションは戻り値として返却されるReportをアプリ内部で解釈しないでください．これは改竄者に改竄の余地を与えてしまうリスクがあります．
一度，完全なReportをサーバーに送信してください．Report送信後にサーバーからアプリに対して適切なアプローチを指示することを推奨します．

注意： Liliumから生成されるReportの値はJsonなので内包しているSafetyNetのReport以外は改竄される可能性があります．LiliumはSafetyNetが失敗した際にエラーReportをサーバーに送信して，悪意のないユーザーに発生したエラーに対してクライアントアプリケーションが適切なアプローチを提供するためのエラー発生時の状況判断のための材料を取得する目的で使用する範疇を逸脱しないように扱ってください．

# SafetyNet Attestation APIとは?

The SafetyNet Attestation API helps you assess the security and compatibility of the Android environments in which your apps run. You can use this API to analyze devices that have installed your app.

## Architecture
The overall SafetyNet Attestation protocol, which appears in Figure 1, involves the following steps:

1. Your app makes a call to the SafetyNet Attestation API.
2. The API requests a signed response using its backend.
3. The backend sends the response to Google Play services.
4. The signed response is returned to your app.
5. Your app should forward the signed response to a server that you trust.
6. The server verifies the response and sends the result of the verification process back to your app.

<img src="./art/attestation-protocol.png" alt="Figure 1" style="width:500px;"/>

Figure 1. SafetyNet Attestation API protocol

7. After completing these steps, if the result indicates that the device has passed your app's risk-model evaluation, your app can resume its services.

# 事前に準備すること: API keyの発行と拡張

SafetyNetAPIを使用するためのAPIキーはデフォルトで1日1万件のリクエストまでの上限があります．しかし，これは申請することによって無料で上限の引き上げを行うことができます．

1. Go to the [Library page](https://console.developers.google.com/apis/library) in the Google APIs Console.
2. Search for the Android Device Verification API. When you've found the API, click on it. The Android Device Verification API dashboard screen appears.
3. If the API isn't already enabled, click Enable.
4. If the Create credentials button appears, click on it to generate an API key. Otherwise, click the All API credentials drop-down list and select the API key that is associated with the project for which the Android Device Verification API is enabled.
5. In the sidebar on the left, click Credentials. Copy the API key that appears.

6. Use this API key whenever you call the attest() method of the SafetyNetClient class.

7. After reviewing all the relevant documentation for this API—including best practices—estimate the number of calls your app might make to the API. If you need to make more than 10,000 requests per day across all API keys in your project, [fill out this quota request form](https://support.google.com/googleplay/android-developer/contact/safetynetqr).


# Liliumの推奨するシーケンス・ダイアグラム

<img src="./art/lilium-sequence.png" alt="Figure 1" style="width:500px;"/>

```plantuml
group Lilium(this library) sequence
"App" -> GameServer : Prepare Request(package_name, user_id)
GameServer -> "App" : api_key, nonce

"App" -> "PlayService" : SafetyNet API Call(api_key, nonce)

group SafetyNet sequence
"PlayService" -> GoogleServer : Black Box
GoogleServer -> "PlayService" : Black Box
end

"PlayService" -> "App" : attestation jwt report

"App" -> "App" : make report json
end

"App" -> GameServer : Report Request(json)
GameServer -> GameServer : attest judge
GameServer -> "App" : Login Response
```

# ライブラリの使い方

##### Java and Kotlin

Users of your library will need add the jitpack.io repository:

```gradle
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
```

and:

```gradle
dependencies {
    compile 'com.github.mofneko:Lilium:2.6.1'
}
```

Step1. Handling SafetyNetDelegate

```kotlin
            val attestCallback = object : DefaultAttestCallback() {
                override fun onResult(response: String) {
                    // Report json here.
                }
            }
```

Step2. Attest

```kotlin
            Lilium().attest(this, "BASE_URI_HERE", "USERID_HERE", attestCallback)
```

注意： SafetyNetはPlayServiceの機能の一部を利用しています．通常は端末にインストールされているPlayServiceアプリが何らかの形で実行の妨げになっている場合は，ユーザーに対して適切なガイドラインを提示する必要があります．Liliumライブラリは次の一行でユーザーに状況の説明とルーティングを提供するダイアログを表示することができます．

```kotlin
            Lilium().showErrorPlayService(this)
```

##### C# (Unity)
Create a folder with the structure Assets/Plugins/Android and put [*.aar](https://github.com/mofneko/Lilium/blob/master/aar/) in the Android folder.

and fact Delegate.

*注意： UnityのAndroid Pluginでコールバック関数を使うことはイベント発火後メインスレッドIDを変更されることを意味します． メインスレッドに戻す方法を検討してください．*

```C# (Unity)
　　public class AttestListener : AndroidJavaProxy
    {
        public AttestListener()
            : base("com.nekolaboratory.Lilium.DefaultAttestCallback")
        {
        }
        void onResult(string response){
                    // Report json here.
        }
    }
```

and execute Attest.

```C# (Unity)
    void Attest()
    {
        // Step1. Instantiate
        using (AndroidJavaObject Lilium = new AndroidJavaObject("com.nekolaboratory.Lilium.Lilium"))
        {
        // Step2. Attest
         Lilium.Call("attest", "BASE_URI_HERE", "USERID_HERE", new AttestListener());
        }
    }
```

注意： SafetyNetはPlayServiceの機能の一部を利用しています．通常は端末にインストールされているPlayServiceアプリが何らかの形で実行の妨げになっている場合は，ユーザーに対して適切なガイドラインを提示する必要があります．Liliumライブラリは次の一行でユーザーに状況の説明とルーティングを提供するダイアログを表示することができます．

```C# (Unity)
    void Attest()
    {
        using (AndroidJavaObject Lilium = new AndroidJavaObject("com.nekolaboratory.Lilium.Lilium"))
        {
         Lilium.Call("showErrorPlayService");
        }
    }
```

# サーバーサイドフロー
Liliumライブラリを使用するにはサーバーサイドに適切なAPIを実装する必要があります．多くのサービスは次の2つのAPIを簡単に実装することができるでしょう．

## prepare api(get API key, nonce)

### endpoint

- https://BASE_URI/prepare

注意： BASE_URIはスキーマを含め好きなようにカスタマイズすることができます．

### Request

```
POST
Content-Type: application/json

{
  "user_id": "通常は，APKの整合性を判定するためのリクエストを送信してきたユーザーを識別するための文字列をここで受け取ります，ここの値は任意のStringを受けとることができるので使用方法は前述の限りではありません．",
  "package_name": "アプリケーションのパッケージ名を受け取ります，これの理由はAPKファイルがテスト用と本番用で異なるパッケージ名で分割されている場合，APIキーをここで受け取るパッケージ名で振り分けることができるからです．"
}
```

### Response

#### on Success

```
200 OK
Content-Type: application/json

{
  "api_key": "事前準備の項目で発行したAPIキーを返却するようにしてください．",
  "nonce": "nonce(Base64 Encorded)"
}
```

#### on Faild

PlayServiceのサーバーが落ちているなどの例外がある場合にはここでステータスコードとして異常を示してください．それには400番台と500番台のレンジで自由に決定することができます．サーバーが，これらの異常コードを返却するとLiliumは後述のReportにエラー状況を含めます．その時，atn_error_msgにはbodyの内容が内包されるので補足事項がある場合はここに出力するようにしてください．

## attestation

Liliumの出力するReportの受け口を実装してください，

### endpoint

注意：以下のエンドポイントは一例です．LiliumはReportを作成して実行元のアプリケーションにReportを返却するのでサーバーへの送信は任意のタイミングで送信することができます．

- https://BASE_URI/report

### Request

```
POST
Content-Type: application/json

{
  "user_id": "prepareで返却したuser_idが含まれます．使用方法として，prepareをリクエストしてきたユーザーが正しいフローでReportを送信してきたかを判定するために利用することができます．",
  "package_name": "アプリケーションのパッケージ名が含まれます．",
  "ver": "Liliumライブラリのバージョンが含まれています．このライブラリにバグや脆弱性が含まれていた場合にアップデートを行うのでサーバーサイドでも組み込んだライブラリが最新のものであるかを確認することを推奨します．",
  "atn": "Safetynet attestation APIが生成したReportのJWTが文字列として含まれています．",
  "atn_error":  "SafetyNetでAPKのチェックを行うまでにエラーが発生した場合に，ここに次のエラーコードが含まれます．そのような状況の場合，atnプロパティに対応するJWTは空になります．",
  "atn_error_msg": "atn_errorにエラーコードが含まれる状況がある場合に，補足情報があればここに情報が含まれます．具体例としてPlayServiceに問題があった時の原因や，prepareリクエストでサーバーがエラーに該当するステータスコードを返却した場合のbodyの内容がここに含まれます．"  
}
```

#### About atn_error

- `PREPARE_RETURNS_400` prepare returned 400 group error.
- `PREPARE_RETURNS_500` prepare returned 500 group error.
- `PREPARE_UNEXPECTED_ERROR` prepare another error.
- `PLAY_SERVICE_UNAVAILABLE` Play Service disabled. (need dialog notice)
- `ATTEST_API_ERROR_%d` `attest` returned ApiException.`%d`equals`ApiException#getStatusCode`(See https://developers.google.com/android/reference/com/google/android/gms/common/api/ApiException.html#getStatusCode)
- `ATTEST_UNEXPECTED_ERROR` `attest` another error.
- `UNEXPECTED_ERROR` another error.

#### About atn_error_msg when PLAY_SERVICE_UNAVAILABLE

- `PLAY_SERVICE_ERROR_MISSING` PlayServiceが端末にインストールされていない．
- `PLAY_SERVICE_ERROR_UPDATING` SafetyNetを扱うための要求バージョンをクリアしていない．
- `PLAY_SERVICE_ERROR_VERSION_UPDATE_REQUIRED` SafetyNetを扱うための要求バージョンをクリアしていない．
- `PLAY_SERVICE_ERROR_DISABLED` PlayServiceがユーザーによって凍結されている．
- `PLAY_SERVICE_ERROR_INVALID` PlayServiceのモジュールが不正なものである．

# Development

```
$ git clone git@github.com:mofneko/Lilium.git
$ cd Lilium
$ ./gradlew assembleRelease
```

# License

```
MIT License

Copyright (c) 2019 Yusuke Arakawa

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
