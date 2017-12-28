package com.example.zhong.keeps

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.ZipInputStream


val addr: String = "http://10.109.34.210:8080/keepsserver-1.0-SNAPSHOT"
val goodResponse = "<status>0</status>"
val badResponse = "<status>1</status>"
val shitResponse = "<status>2</status>"
val fuckResponse = "<status>3</status>"

fun userLoginTest(username: String, password: String, activity: NetworkTestActivity) {
    Thread(Runnable {
        val url = URL("$addr/UserLogin?username=$username&password=$password")
        // val url = URL("http://www.baidu.com")

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val sb = StringBuilder()
        var line: String?
        while (true) {
            line = reader.readLine()
            if (line != null) {
                sb.appendln(line)
            } else {
                break
            }
        }
        reader.close()
        connection.disconnect()
        activity.onLoginResponse(sb.toString() == "$goodResponse\n")
    }).start()
}

fun userLogin(username: String, password: String, activity: LoginActivity) {
    Thread(Runnable {
        val url = URL("$addr/UserLogin?username=$username&password=$password")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val sb = StringBuilder()
        var line: String?
        while (true) {
            line = reader.readLine()
            if (line != null) {
                sb.appendln(line)
            } else {
                break
            }
        }
        reader.close()
        connection.disconnect()
        activity.onUserLoginReturn(sb.toString() == "$goodResponse\n")
    }).start()
}

fun userRegister(username: String, password: String, activity: RegisterActivity) {
    Thread(Runnable {
        val url = URL("$addr/UserRegister?username=$username&password=$password")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val sb = StringBuilder()
        var line: String?
        while (true) {
            line = reader.readLine()
            if (line != null) {
                sb.appendln(line)
            } else {
                break
            }
        }
        reader.close()
        connection.disconnect()
        activity.onUserRegisterReturn(sb.toString() == "$goodResponse\n")
    }).start()
}

fun initKnowledgePoints(username: String, password: String, activity: MainActivity) {
    Thread(Runnable {
        // get structure xml
        val url = URL("$addr/GetKPStructureXml?username=$username&password=$password")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val sb = StringBuilder()
        var line: String?
        while (true) {
            line = reader.readLine()
            if (line != null) {
                sb.appendln(line)
            } else {
                break
            }
        }
        reader.close()
        connection.disconnect()
        val response = sb.toString()

        Log.d("Xml", response)
        // if bad response
        if (response == "$badResponse\n") {
            activity.onInitKnowledgePointsReturn(false, null)
            return@Runnable
        }

        // save xml file
        val userDataDir = File(activity.filesDir, "userdata")
        userDataDir.mkdir()
        val userDir = File(userDataDir, username)
        userDir.mkdir()
        val userContentDir = File(userDir, "content")
        userContentDir.mkdir()
        val structureFile = File(userDir, "kp_structure.xml")
        val writer = PrintWriter(structureFile)
        writer.print(sb.toString())
        writer.close()

        // get user content
        val urlForContent = URL("$addr/GetKPContentZip?username=$username&password=$password")
        val connectionForContent = urlForContent.openConnection() as HttpURLConnection
        connectionForContent.requestMethod = "GET"
        val inputStream = connectionForContent.inputStream

        val buf = ByteArray(1024)
        val contentZipFile = File("${activity.filesDir}/userdata/$username/content.zip")
        var size: Int

        val fos = FileOutputStream(contentZipFile)
        while (true) {
            size = inputStream.read(buf)
            if (size >= 0) {
                fos.write(buf, 0, size)
            } else {
                break
            }
        }
        fos.close()

        // unzip file
        val buffer = ByteArray(1024)
        val zis = ZipInputStream(FileInputStream(contentZipFile))
        var zipEntry = zis.nextEntry
        while (zipEntry != null) {
            val fileName = zipEntry.name
            val extractedFile = File("${activity.filesDir}/userdata/$username/content/$fileName")
            val fos = FileOutputStream(extractedFile)
            while (true) {
                size = zis.read(buffer)
                if (size > 0) {
                    fos.write(buffer, 0, size)
                } else {
                    break;
                }
            }
            fos.close()
            zipEntry = zis.nextEntry
        }
        zis.closeEntry()
        zis.close()

        // construct from xml
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(StringReader(response))
        parser.next()
        val rootKnowledgePoint = constructKP(parser, null, username, activity)

        activity.onInitKnowledgePointsReturn(true, rootKnowledgePoint)
    }).start()
}


fun initKnowledgePointsTest(username: String, password: String, activity: NetworkTestActivity) {
    Thread(Runnable {
        // get structure xml
        val url = URL("$addr/GetKPStructureXml?username=$username&password=$password")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val sb = StringBuilder()
        var line: String?
        while (true) {
            line = reader.readLine()
            if (line != null) {
                sb.appendln(line)
            } else {
                break
            }
        }
        reader.close()
        connection.disconnect()
        val response = sb.toString()

        Log.d("Xml", response)
        // if bad response
        if (response == "$badResponse\n") {
            activity.onInitKnowledgePointsReturn(false, null)
            return@Runnable
        }

        // save xml file
        val userDataDir = File(activity.filesDir, "userdata")
        userDataDir.mkdir()
        val userDir = File(userDataDir, username)
        userDir.mkdir()
        val userContentDir = File(userDir, "content")
        userContentDir.mkdir()
        val structureFile = File(userDir, "kp_structure.xml")
        val writer = PrintWriter(structureFile)
        writer.print(sb.toString())
        writer.close()

        // get user content
        val urlForContent = URL("$addr/GetKPContentZip?username=$username&password=$password")
        val connectionForContent = urlForContent.openConnection() as HttpURLConnection
        connectionForContent.requestMethod = "GET"
        val inputStream = connectionForContent.inputStream

        val buf = ByteArray(1024)
        val contentZipFile = File("${activity.filesDir}/userdata/$username/content.zip")
        var size: Int

        val fos = FileOutputStream(contentZipFile)
        while (true) {
            size = inputStream.read(buf)
            if (size >= 0) {
                fos.write(buf, 0, size)
            } else {
                break
            }
        }
        fos.close()

        // unzip file
        val buffer = ByteArray(1024)
        val zis = ZipInputStream(FileInputStream(contentZipFile))
        var zipEntry = zis.nextEntry
        while (zipEntry != null) {
            val fileName = zipEntry.name
            val extractedFile = File("${activity.filesDir}/userdata/$username/content/$fileName")
            val fos = FileOutputStream(extractedFile)
            while (true) {
                size = zis.read(buffer)
                if (size > 0) {
                    fos.write(buffer, 0, size)
                } else {
                    break;
                }
            }
            fos.close()
            zipEntry = zis.nextEntry
        }
        zis.closeEntry()
        zis.close()

        // construct from xml
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(StringReader(response))
        parser.next()
        val rootKnowledgePoint = constructKP(parser, null, username, activity)

        activity.onInitKnowledgePointsReturn(true, rootKnowledgePoint)
    }).start()
}




private fun constructKP(parser: XmlPullParser, parentKP: KnowledgePoint?, username: String,
                        context: Context): KnowledgePoint {
    if (parser.eventType == XmlPullParser.START_TAG) {
        val name = parser.getAttributeValue(0)

        var markdownContent = ""
        val markdownFile = File("${context.filesDir}/userdata/$username/content/$name.md")
        if (markdownFile.exists()) {
            val reader = BufferedReader(FileReader("${context.filesDir}/userdata/$username/content/$name.md"))
            val sb = StringBuilder()
            var line: String?
            while (true) {
                line = reader.readLine()
                if (line != null) {
                    sb.appendln(line)
                } else {
                    break
                }
            }
            markdownContent = sb.toString()
        }

        val kp = KnowledgePoint(name, markdownContent, parentKP, null)
        var eventType = parser.next()
        if (eventType == XmlPullParser.TEXT) {
           eventType = parser.next()
        }
        val childKPs = mutableListOf<KnowledgePoint>()
        while (eventType == XmlPullParser.START_TAG) {
            childKPs.add(constructKP(parser, kp, username, context))
            eventType = parser.next()
            if (eventType == XmlPullParser.TEXT) {
                eventType = parser.next()
            }
        }
        if (childKPs.isNotEmpty()) {
            kp.childKPList = childKPs
        }
        if (eventType == XmlPullParser.END_TAG) {
            return kp
        }
    }
    throw Exception()
}