# Popular-Movies-Stage-1
Udacity Android Nanodegree part 1 Stage 1
You have to supply your own API key for themoviedb.org before this app will compile. 
The app to help users discover popular and recent movies, building a clean UI, synchronizing to a server, and presenting information to the user.

After obtaining the key, paste it into the gradle file of the app instead of the letters "xxxx"

buildTypes.each {
        it.buildConfigField 'String', 'OPEN_MOVIES_MAP_API_KEY', "\"xxxxxxxx\""
    }

**License**

The content of this repository is licensed under a [Creative Commons Attribution](creativecommons.org/licenses/by/3.0/us/).
