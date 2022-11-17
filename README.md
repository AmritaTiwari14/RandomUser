<h1 align="center">Lydia - RandomUser API</h1>

<p align="center">
  <a href="https://android-arsenal.com/api?level=19"><img alt="API" src="https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat"/></a>
</p>

## Subject:

Build an app that fetch data from this service: https://api.randomuser.me (use https://randomuser.me/api/1.0/?seed=lydia&results=10&page=1 to get 10 contacts for each api call, and increase page param to load more results).

- The app must display result in a list of first names and last names, and the email under it.
- The app must handle connectivity issue, and display the last results received, if it can't retrieve one at launch.
- Touching an item of the list should make appear a details page listing every attributes.
- The app must be in Kotlin, any third-party libraries are allowed but you'll have to justify why you use them.
- Evaluate the time it should take before starting, and give it with your work, with the time it really took.


## Development estimation

Here is how I propose to develop the application: 

- First, search for inspiration for the look and feel of the application and try to implement it in a POC (2h)
- Then, after a brief API analysis, think about the conception of the model (which field is needed, and so on) (1h)
- Put in place a MVVM architecture and develop HomeActivity and fetching from the API (4h)
  - Install libraries (Retrofit/Gson, Glide, Coroutines, ...)
  - Develop HomeActivity (fetching with Retrofit and scrolling)
  - Unit tests the repository
- Add Room and handle switch between local & remote fetching depending on the connectivity (4h)
  - ConnectivityListener
  - Implementation in the repository
  - Unit tests switch between local & remote fetching
- Add DetailActivity on user click (3h) 
  - Pass id by Intent
  - Load data from DB
  - Unit tests the repository
- UI, UX & Performance (2h)
  - Instrumented tests
  - Design improvement
- Readme summary + "release" (tag & apk) (1h)


## Design

Reflexion about the listing page:
- [Millet-Chat Application](https://www.behance.net/gallery/103978981/Millet-Chat-Application?tracking_source=search_projects_recommended%7Cchat%20application%20user%20listing)
- [Tandem App Redesign](https://www.behance.net/gallery/103406949/Tandem-App-Redesign?tracking_source=search_projects_recommended%7Cchat%20application)
- [IM - Chat](https://www.behance.net/gallery/68932769/IM-Chat?tracking_source=search_projects_recommended%7Cchat%20application)

Reflexion about the detail page:
- [LINC - Social Messenger App](https://www.behance.net/gallery/69090631/LINC-Social-Messenger-App)
- [User profile](https://www.behance.net/gallery/55595531/User-profile?tracking_source=search_projects_recommended%7CEdit%20profile%20screen)
- [Chat App UI Kit](https://www.behance.net/gallery/103192515/Chat-App-UI-Kit?tracking_source=search_projects_recommended%7Cchat%20application)

Chosen colors:
- For the listing I will use a white background for the item of the adapter (#F0F0F0) and a darker white for the background of the activity (#E8E8E8).
- For the icons I will use a grey color (#9A9CA4)

For the icon use in the project, I will be using [the website iconmonstr](https://iconmonstr.com/) and the vector assets of Android Studio.

It took me approximately 3 hours to find a relevant design which pleases me and to try make a first implementation.


## Used libraries & MVVM Architecture

The project target [Android 4.4](https://en.wikipedia.org/wiki/Android_KitKat) (API 19) to be able to operate on a maximum of available devices. Thanks to this 
minSDK, we can esperate provide our application on 98% of devices, according to Android Studio 4.0.1.

Here are the list of external dependencies I used for the project : 

- I used [Retrofit](https://github.com/square/retrofit) for the API call and the gson converter with it. I preferred [Gson](https://github.com/google/gson) over [Moshi](https://github.com/square/moshi) or [Jackson](https://github.com/FasterXML/jackson) to the concision of the produced code.
- I also added OkHttp 3.12.12 and force it for every others libraries. Indeed, since the latest versions [they don't support Android 4.4](https://medium.com/square-corner-blog/okhttp-3-13-requires-android-5-818bb78d07ce) so to be able to support it we need to use this specific version. 
- To handle database, I chosen [Room](https://developer.android.com/topic/libraries/architecture/room) for the efficiency to handle entities and database access.
- I used [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) to handle and dispatch result of heavy operations easily. I like the code readability produced with it.
- I used [Glide](https://github.com/bumptech/glide) to handle image loading efficiently and have access to great caching strategy.
- Last but not least [Koin](https://github.com/InsertKoinIO/koin) helped me to do dependency injection on target modules like network & database.

I am used to MVP architecture but less to MVVM architecture. So I needed to [read documentation](https://developer.android.com/jetpack/guide) about it, to refresh my 
knowledge of the subject. 

![MVVM architecture](https://github.com/1ud0v1c/lydia-random-user/raw/master/data/mvvm-architecture.png)

We can check the HomeActivity implementation :
- HomeActivity, it is the View himself, which can interact with the user
- HomeViewModel, the man-in-the middle classes which make the linking between the View and the Model (the repository)
- HomeRepository, last but not least the class which does all the business logic. Generally speaking, this class handle the call to API or database.

To put the architecture in place and to have a working HomeActivity fetching data from the server thanks to Retrofit. It took me 6 hours, mainly because I reworked 
the handling of the ViewModel. For example, near the end of the project I used [Databinding](https://developer.android.com/topic/libraries/data-binding) to simplify 
the DetailActivity interface update. I also took metime to clean the access of the context thanks to Koin. I was able to avoid using [AndroidViewModel](https://developer.android.com/reference/android/arch/lifecycle/AndroidViewModel) thanks to it.


## HomeActivity

After that, I added Room and make the switch between local and remote provider depending on the network connectivity. That was faster than I expected. I finished it 
in 3 hours.

Let's see, how my final HomeActivity looks like. First, I handled the case when you open the application for the first time and you have no internet connection. In this case, I display a Snackbar and let the user click on a retry button, while being connected to the internet.

![HomeActivity first launch no network](https://github.com/1ud0v1c/lydia-random-user/raw/master/data/home_activity_first_launch_no_network.png)

As soon as, you have fetched the result of the API, we display the list of the users and insert the data into Room database. And thus, if you lose the connection 
you will still be able to browse the list of users. 

![HomeActivity](https://github.com/1ud0v1c/lydia-random-user/raw/master/data/home_activity.png)

Finally, when a user click on a profile, we pass by intent the email of the user (which serve as primary key in the user table) and open the DetailActivity. 


## DetailActivity

This part, was fast to implement, but it took me some time to have a pleasant look and feel for this Activity. It took me 2h30 to finish it. 

Here is the result, after receiving the email from a user, from the HomeActivity, we can request the database and display all attributes of the selected user.

![DetailActivity](https://github.com/1ud0v1c/lydia-random-user/raw/master/data/detail_activity.png)

I also added intents on specific views:
- While clicking on the user cell or phone, it opens the dialer, ready to call. 
- While clicking on the user location, it opens google maps on the address of the user.

![DetailActivity dial intent](https://github.com/1ud0v1c/lydia-random-user/raw/master/data/dial_intent.png)
![DetailActivity location intent](https://github.com/1ud0v1c/lydia-random-user/raw/master/data/map_intent.png)


## Unit tests & Performance

To polish the application I aimed for 2 hours, but it took me more time than that. I mainly developed the application with a OnePlus 5T under Android 10. But I didn't 
tested on Android 4.4 and after running an emulator I saw they were TLS issues on Retrofit requests. To fix it, I made a workaround which only authorizes requests made 
to the API domain name. The best solution would be to use [the ProviderInstaller](https://medium.com/tech-quizlet/working-with-tls-1-2-on-android-4-4-and-lower-f4f5205629a) proposed by google. 

Because of that, It took me 4 hours, to have a satisfying application.

For the unit tests I used [Robolectric](https://github.com/robolectric/robolectric) and I used [Espresso](https://developer.android.com/training/testing/espresso) for my instrumented tests.


## In short 

I expected to finished the application in 17 hours but I finished it in 19 hours. Thanks to this test I saw that I can progress on my understanding of the MVVM 
architecture & the new libraries offered with [Android Jetpack](https://developer.android.com/jetpack).
