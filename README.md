# Github-client

[![CircleCI](https://circleci.com/gh/pchmielowski/Github-client.svg?style=svg)](https://circleci.com/gh/pchmielowski/Github-client)

[![codecov](https://codecov.io/gh/pchmielowski/Github-client/branch/master/graph/badge.svg)](https://codecov.io/gh/pchmielowski/Github-client)


Simple Android Github client with the ability to search for repository, view some of its details and with local list of favourite ones.

App can be used anonymously or with logged in user (with the Githhub credentials) in order to be able to send more requests (Github REST API limitation).

<img src=screencast.gif />

## Libraries
* architecture:
  * RxJava with RxBinding and RxAndroid
  * Dagger
  * Data Binding
  
* data: 
  * Realm
  * Retrofit with Gson

* other
  * Picasso
  * Lombok
  * Stetho
