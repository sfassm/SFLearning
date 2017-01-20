 Java Program for Testing WIoTP Client Connections
===================================================
Last Edited: 2017-01-20


1) Introduction
----------------
- Java program allows to configure either an WIoTP DEVICE or APPLICATION client to connect through
 MQTT
- Input was taken from IBM developerWorks: 
https://developer.ibm.com/recipes/tutorials/use-the-java-client-library-to-connect-to-the-ibm-internet-of-things-iot-foundation/
- Can be configured to run as DEVICE OR APPLICATION client (console mode: keyboard hit stops program)


A) Use of external packages:
   * commons-net-3.3.jar (Apache Commons)
   * gson-2.2.jar (Google/Apache v2)
   * org.eclipse.paho.client.mqttv3-1.1.1-20161114.052252-83.jar (Eclipse)
   * watson-iot-0.2.2.jar (Eclipse IBM)
   * httpclient-4.3.6.jar (Apache v2)
   * httpcore-4.2.2.jar (Apache v2)
 (OSS Check!!! IBM approved: https://w3-connections.ibm.com/wikis/home?lang=en-us#!/wiki/W783ba5fa6c1a_40b3_945a_07d0eb0115bd/page/All%20Usage%20Approved%20(AUA)%20List)
   
   
- MQTT Paho client libs and IBM WIoTP DeviceClient libs:
  * Maven directory: pom.xml
    <dependency>
	 <groupId>com.ibm.messaging</groupId>
	 <artifactId>watson-iot</artifactId>
	 <version>0.1.0</version>
	</dependency>
  * LIBs: Download watson-iot-0.2.2-jar from https://repo1.maven.org/maven2/com/ibm/messaging/watson-iot/0.2.2/
- MQTT Paho client libs downloaded from https://repo.eclipse.org/content/repositories/paho-snapshots/org/eclipse/paho/org.eclipse.paho.client.mqttv3/1.1.1-SNAPSHOT/
org.eclipse.paho.client.mqttv3-1.1.1-20161107.052322-79.jar
- JsonObject class (from Google GSON, download from https://github.com/google/gson)
(IBM approved: https://w3-connections.ibm.com/wikis/home?lang=en-us#!/wiki/W783ba5fa6c1a_40b3_945a_07d0eb0115bd/page/All%20Usage%20Approved%20(AUA)%20List)

2) Change History and features
- 20170118: initial creation
- 20170120: WIoT DeviceClient successfully tested with pvcdemo@de.ibm.com account in IoT US-South: 
  	space IoT_Tests | IoTFSsoTest  ORG=r344n3 (https://r344n3.internetofthings.ibmcloud.com)