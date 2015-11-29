# WiFiDetective

## 功能和实现
1 WiFi扫描：获取你的android设备能够扫描到的所有的WiFi设备，并返回基本信息：SSID、WiFi信号强度、信道以及加密方式  
2 设备扫描：尽可能的获取所有你android设备所在的WiFi下的所有连接该WiFi的设备及其信心：IPMAC、名称或者厂商

## 实现原理 
### WiFi扫描
注册android的广播，实现监听即可。
### 设备扫描
关键技术
* android下如何进行ping操作
* android下如何判断端口是否打开
* android如何读取arp表
* android下如何通过ip获取mac地址
* android下如何通过ip获取部分设备的名称（NetBios service)
* android下如何通过mac地址获取设备的厂商     
以上的这些你通过阅读代码就可以得到。请关注……谢谢……    

##效果图
![image](https://github.com/gpfduoduo/WiFiDetective/blob/master/gif/wifidetective.gif "效果图")
##引用库
* 进度条来自代码家  (暂时没有实际作用，只是界面）      
https://github.com/daimajia/NumberProgressBar
* android设备实现沉浸式菜单   来自鸿洋  
https://github.com/hongyangAndroid/ColorfulStatusBar
