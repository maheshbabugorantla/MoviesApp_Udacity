# Movie/TVShow Reviews App 

## About
<p>This Android Application is made as a Project for "Developing Android Apps" Course on Udacity.<br> This app allows the user to see the <em><b>reviews and video trailers</b></em> for a <b><em>Movie or a TV Show</em></b> of his/her Choice.<br>User can go to settings window to change the choice of Video and also the rating type as shown in the images below</p>

## How to Use
<p> To use this application. You will need your own API KEY.<br> This can be obtained from <a href="https://www.themoviedb.org/" target="_blank">MovieDB</a>. After clicking the link, create an account(if you don't have one) and follow the necessary steps provided on the website to obtain the API Key.<br></p>
<p> Once you obtain the API Key, go to build.gradle file in the 'app' folder of this repository. And place your API Key in the section that looks as follows <br><br><em>buildTypes.each</em><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;it.buildConfigField&nbsp;'String',&nbsp;'THE_MOVIE_DB_API_KEY',&nbsp;"\"THE_MOVIE_DB_API\""<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br><br>In Place of "THE_MOVIE_DB_API" insert your API Key and run the App.</p>

## Below are the screenshots of the Current Status of Application

### MainScreen

![mainfragment](https://cloud.githubusercontent.com/assets/3885116/16654790/611df8dc-4425-11e6-90d4-9ee8fe69a35a.png)

### MainScreen LandScape
![mainfragment_land](https://cloud.githubusercontent.com/assets/3885116/16655065/b14a0c78-4426-11e6-89f0-427cf61ac99d.png)

### MovieSummary
<img src="https://cloud.githubusercontent.com/assets/3885116/16993782/cc199202-4e72-11e6-87a1-11f2c0ed1d24.png" height="800" width="450" align="left">

<img src="https://cloud.githubusercontent.com/assets/3885116/16994137/16d51c5c-4e74-11e6-9555-e5f41426b36f.png" height="800" width="450" align="right">

### Settings Menu
![settings_1](https://cloud.githubusercontent.com/assets/3885116/16655166/1fdffa12-4427-11e6-959c-e525183d92db.png)
![settings_2](https://cloud.githubusercontent.com/assets/3885116/16655181/2e38eaba-4427-11e6-8d68-7fd2d811d3b8.png)


### APIs Used
<ol>
  <li><a href="http://square.github.io/picasso/">Picasso</a></li>
</ol>

### Future Goals
<ol>
  <li>Implement a SQLite Database to store the Web data locally. </li>
  <li>Implement Content Providers and SyncAdapters to achieve efficient data transfers from Web to UI</li>
</ol>

<p><br><br><b><em>&nbsp;&nbsp;&nbsp; ***** THIS APPLICATION IS STILL CURRENTLY UNDER DEVELOPMENT WILL BE UPDATED REGULARLY *****</b></em></p>
