# MoviesApp_Udacity

<p>This an Android Application is made as a part of Udacity Android Development Project.<br> This app allows the user to see the <em><b>reviews and video trailers</b></em> for a <b><em>Movie or a TV Show</em></b> of his/her Choice.<br>User can go to settings window to change the choice of Video and also the rating type</p>

<p> To use this application. You will need your own API KEY.<br> This can be obtained from <a href="https://www.themoviedb.org/" target="_blank">MovieDB</a>. After clicking the link, create an account and follow the necessary steps provided on the website to obtain the API Key.</p>
<p> Once you obtain the API Key, go to build.gradle file in the 'app' folder of this repository. And place your API Key in the section that looks as follows <br><br><em>buildTypes.each</em><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;it.buildConfigField&nbsp;'String',&nbsp;'THE_MOVIE_DB_API_KEY',&nbsp;"\"THE_MOVIE_DB_API\""<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br><br>In Place of "THE_MOVIE_DB_API" insert your API Key and run the App.</p>
