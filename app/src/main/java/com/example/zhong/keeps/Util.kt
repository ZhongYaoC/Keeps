package com.example.zhong.keeps

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

var ip = "10.103.17.137"
val goodResponse = "<status>0</status>"
val badResponse = "<status>1</status>"
val shitResponse = "<status>2</status>"
val fuckResponse = "<status>3</status>"

private fun getAddr():String {
    return "http://$ip:8080/keepsserver-1.0-SNAPSHOT"
}

fun addOfflineUser(username: String, password: String, activity: MainActivity) {
    val userDataDir = File(activity.filesDir, "userdata")
    userDataDir.mkdir()
    val userDir = File(userDataDir, username)
    userDir.mkdir()
    val userContentDir = File(userDir, "content")
    userContentDir.mkdir()
}

fun userLogin(username: String, password: String, activity: LoginActivity) {
    Thread(Runnable {
        val url = URL("${getAddr()}/UserLogin?username=$username&password=$password")
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

fun userLoginTest(username: String, password: String, activity: TestActivity) {
    Thread(Runnable {
        val url = URL("${getAddr()}/UserLogin?username=$username&password=$password")
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

fun userRegister(username: String, password: String, activity: RegisterActivity) {
    Thread(Runnable {
        val url = URL("${getAddr()}/UserRegister?username=$username&password=$password")
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
        var rootKnowledgePoint: KnowledgePoint
        try{
            // get structure xml
            val url = URL("${getAddr()}/GetKPStructureXml?username=$username&password=$password")
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
            val urlForContent = URL("${getAddr()}/GetKPContentZip?username=$username&password=$password")
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
                val extractedFile = File("${activity.filesDir}/userdata/$username/$fileName")
                if (extractedFile.isDirectory) {
                    extractedFile.mkdir()
                } else {
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
                }
                zipEntry = zis.nextEntry
            }
            zis.closeEntry()
            zis.close()

            // construct from xml
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(StringReader(response))
            parser.next()
            rootKnowledgePoint = constructKP(parser, null, username, activity)
        }
        catch (e: Exception) {
            rootKnowledgePoint = KnowledgePoint("root", "", null, mutableListOf())
        }
        activity.onInitKnowledgePointsReturn(true, rootKnowledgePoint)
    }).start()
}


fun initKnowledgePointsTest(username: String, password: String, activity: TestActivity) {
    Thread(Runnable {
        // get structure xml
        val url = URL("${getAddr()}/GetKPStructureXml?username=$username&password=$password")
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
        val urlForContent = URL("${getAddr()}/GetKPContentZip?username=$username&password=$password")
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
            val extractedFile = File("${activity.filesDir}/userdata/$username/$fileName")
            if (extractedFile.isDirectory) {
                extractedFile.mkdir()
            } else {
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
            }
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
        val childKPs = mutableListOf<KnowledgePoint>()
        val kp = KnowledgePoint(name, markdownContent, parentKP, childKPs)
        var eventType = parser.next()
        if (eventType == XmlPullParser.TEXT) {
           eventType = parser.next()
        }
        while (eventType == XmlPullParser.START_TAG) {
            childKPs.add(constructKP(parser, kp, username, context))
            eventType = parser.next()
            if (eventType == XmlPullParser.TEXT) {
                eventType = parser.next()
            }
        }
        if (eventType == XmlPullParser.END_TAG) {
            return kp
        }
    }
    throw Exception()
}

fun saveDataChanges(username: String, password: String, context: Context,
                    root: KnowledgePoint) {
    // update structure xml
    val kpStructureXmlString = constructXmlFromKP(root)
    val printWriter = PrintWriter("${context.filesDir}/userdata/$username/kp_structure.xml")
    printWriter.print(kpStructureXmlString)
    printWriter.close()

    // update markdown content
    updateContent(username, root, context)
}

private fun constructXmlFromKP(currentKP: KnowledgePoint): String {
    var entrySB = StringBuilder()
    entrySB.append("<kp name=\"${currentKP.name}\">")
    for (child in currentKP.childKPList) {
        entrySB.append(constructXmlFromKP(child))
    }
    entrySB.append("</kp>")
    return entrySB.toString()
}

private fun updateContent(username: String, currentKP: KnowledgePoint, context: Context) {
    // update markdown
    val printWriter = PrintWriter("${context.filesDir}/userdata/$username/content/" +
            "${currentKP.name}.md")
    printWriter.print(currentKP.markdownContent)
    printWriter.close()
    for (child in currentKP.childKPList) {
        updateContent(username, child, context)
    }
}

fun syncDataToServer(username: String, password: String, activity: SettingActivity) {

    // generate zip file
    val fos = FileOutputStream("${activity.filesDir}/userdata/$username/content.zip")
    val zos = ZipOutputStream(fos)

    val dirToZip = File("${activity.filesDir}/userdata/$username/content")
    zipFile(dirToZip, "content", zos)

    zos.close()

    //  upload
    Thread(Runnable {
        val resp1 = uploadStructureXml(username, password, activity)
        val resp2 = uploadContentZip(username, password, activity)
        activity.onSyncDataToServerReturn(resp1 && resp2)
    }).start()
}

fun syncDataToServerTest(username: String, password: String, activity: TestActivity) {

    // generate zip file
    val fos = FileOutputStream("${activity.filesDir}/userdata/$username/content.zip")
    val zos = ZipOutputStream(fos)

    val dirToZip = File("${activity.filesDir}/userdata/$username/content")
    zipFile(dirToZip, "content", zos)

    zos.close()

    //  upload
    Thread(Runnable {
        val resp1 = uploadStructureXml(username, password, activity)
        val resp2 = uploadContentZip(username, password, activity)
        activity.onSyncDataToServerReturn(resp1 && resp2)
    }).start()
}


private fun zipFile(fileToZip: File, fileName: String, zos: ZipOutputStream) {
    if (fileToZip.isHidden) {
        return
    }
    if (fileToZip.isDirectory) {
        val children = fileToZip.listFiles()
        for (childFile in children!!) {
            zipFile(childFile, fileName + "/" + childFile.name, zos)
        }
        return
    }
    val fis = FileInputStream(fileToZip)
    val zipEntry = ZipEntry(fileName)
    zos.putNextEntry(zipEntry)
    val bytes = ByteArray(1024)
    var length: Int
    while (true) {
        length = fis.read(bytes)
        if (length >= 0) {
            zos.write(bytes, 0, length)
        } else {
            break
        }
    }
    fis.close()
}

private fun uploadStructureXml(username: String, password: String, context: Context): Boolean {
    /* content zip*/
    // get connection
    val url = URL("${getAddr()}/UpdateKPStructureXml?username=$username&password=$password")
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "POST"

    // upload
    val os = connection.outputStream
    val fis = FileInputStream("${context.filesDir}/userdata/kp_structure.xml")
    val buf = ByteArray(1024)
    var length: Int
    while (true) {
        length = fis.read(buf)
        if (length >= 0) {
            os.write(buf)
        } else {
            break
        }
    }
    fis.close()
    os.close()

    // get response
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

    return sb.toString() == goodResponse
}

private fun uploadContentZip(username: String, password: String, context: Context): Boolean {
    /* content zip*/
    // get connection
    val contentZipUrl = URL("${getAddr()}/UpdateKPContentZip?username=$username&password=$password")
    val contentZipConnection = contentZipUrl.openConnection() as HttpURLConnection
    contentZipConnection.requestMethod = "POST"

    // upload
    val os = contentZipConnection.outputStream
    val fis = FileInputStream("${context.filesDir}/userdata/content.zip")
    val buf = ByteArray(1024)
    var length: Int
    while (true) {
        length = fis.read(buf)
        if (length >= 0) {
            os.write(buf)
        } else {
            break
        }
    }
    fis.close()
    os.close()

    // get response
    val reader = BufferedReader(InputStreamReader(contentZipConnection.inputStream))
    val contentZipResponseSb = StringBuilder()
    var line: String?
    while (true) {
        line = reader.readLine()
        if (line != null) {
            contentZipResponseSb.appendln(line)
        } else {
            break
        }
    }
    reader.close()

    contentZipConnection.disconnect()

    return contentZipResponseSb.toString() == goodResponse
}