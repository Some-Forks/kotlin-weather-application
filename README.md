# Kolor Weather Application
This application was developed to practice the basics of the Kotlin programming language

### Specs

Here the process of implementing the application is detailed:

* [Volley] - Volley is an HTTP library that makes networking for Android apps easier and most importantly, faster. Volley is available on GitHub.
* [Dark Sky] - Dark Sky API is a rest application to obtain the weather information, this application is no longer commercially available so it is recommended to look for other alternatives.
* [Google GPS Location] - To develop an app using the Google Play services APIs, you need to set up your project with the Google Play services SDK, which is available from the Google maven repository.

### Installation

This application requires two implementations in the **build.gradle** file


```sh
 implementation 'com.android.volley:volley:1.1.1'
 implementation 'com.google.android.gms:play-services-location:17.1.0'
```

### Resources

This application is developed based on a Kotlin course, to improve skills in Android application development.

* [Curso Kotlin] - You will learn how to make several applications, including a weather application, an application to take photos and videos, an application to display a list of marvel heroes, etc.

### To Do
Improve the implementation of current time, as well as the location by means of GPS, implement coroutines to improve the performance of the application and presentation of data in real time.
    
   [Volley]:<https://developer.android.com/training/volley>
   [Dark Sky]:<https://darksky.net/dev>
   [Google GPS Location]:<https://developers.google.com/android/guides/setup>
   [Curso Kotlin]: <https://www.udemy.com/share/102sYAAkYScltRQnw=/>
