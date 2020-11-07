package com.github.androidpirate.capsulereviews.util.internal

class Constants {
    companion object {
        // Fragment constants
        const val MOVIES_LIST_FRAG_TAG = "MoviesListFragment"
        const val TV_SHOWS_LIST_FRAG_TAG = "TvShowsListFragment"
        const val PAGED_MOVIES_LIST_FRAG_TAG = "PagedMoviesListFragment"
        const val PAGED_TV_SHOWS_LIST_FRAG_TAG = "PagedTvShowsListFragment"
        // Video constants
        const val VIDEO_SITE = "YouTube"
        const val VIDEO_TYPE = "Trailer"
        // Adapter constants
        const val ADAPTER_UNKNOWN_FRAGMENT_MESSAGE = "Unknown fragment type"
        const val ADAPTER_UNKNOWN_VIEW_TYPE_MESSAGE = "Unknown view type"
        const val ADAPTER_POSTER_WIDTH = "w185/"
        // Item constants
        const val SHOWCASE_POSTER_WIDTH = "w342/"
        const val EMPTY_VIDEO_KEY = ""
        // Data source constants
        const val FIRST_PAGE = 1
        const val PAGE_LOAD_INCREMENT = 1
        const val PAGE_SIZE = 20
        // Toolbar title constants
        const val MOVIE_POPULAR_TITLE = "Popular Movies"
        const val MOVIE_TOP_RATED_TITLE = "Top Rated Movies"
        const val MOVIE_NOW_PLAYING_TITLE = "Now Playing Movies"
        const val MOVIE_UPCOMING_TITLE = "Upcoming Movies"
        const val MOVIE_TRENDING_TITLE = "Trending Movies"
        const val TV_POPULAR_TITLE = "Popular TV Shows"
        const val TV_TOP_RATED_TITLE = "Top Rated TV Shows"
        const val TV_TRENDING_TITLE = "Trending TV Shows"
        // Favorite content constants
        const val EMPTY_FIELD_STRING = ""
        const val EMPTY_FIELD_INT = 0
        const val FAVORITE_MOVIE_TYPE = "Movie"
        const val FAVORITE_TV_SHOW_TYPE = "Tv Show"
        const val DEFAULT_BINGE_STATUS = "Not Started Yet"
        const val BINGING_BINGE_STATUS = "Binging"
        const val SEEN_BINGE_STATUS = "Seen"
        // FavoritePagerAdapter constants
        const val MOVIE_TAB_TITLE = "Movies"
        const val TV_SHOWS_TAB_TITLE = "TV Shows"
        const val PAGER_PAGE_COUNT = 2
        // Binge status Alert Dialog constants
        const val ARG_ITEM_ID = "List Item Id"
        const val BINGE_STATUS_ALERT_DIALOG_TITLE = "Select Binge Status"
        const val ALERT_DIALOG_CANCEL = "CANCEL"
        // ContentFormatter constants
        const val NO_DATA_AVAILABLE = "Data not available."
        const val RELEASE_DATE_DELIMITER = "-"
        const val RELEASE_DATE_YEAR = 0
        const val RELEASE_DATE_MONTH = 1
        const val RELEASE_DATE_DAY = 2
        const val MOVIE_RUNTIME_HOUR_UNIT = "h"
        const val MOVIE_RUNTIME_MINUTE_UNIT = "m"
        const val TV_SHOW_RUNTIME_UNIT = "mins"
        // MovieGenresDialogFragment constants
        const val DEFAULT_SELECTED_POSITION = 0
        // Genre Titles
        const val GENRE_ALL = "All"
        const val GENRE_ACTION = "Action"
        const val GENRE_ADVENTURE = "Adventure"
        const val GENRE_ANIMATION = "Animation"
        const val GENRE_COMEDY = "Comedy"
        const val GENRE_CRIME = "Crime"
        const val GENRE_DOCUMENTARY = "Documentary"
        const val GENRE_DRAMA = "Drama"
        const val GENRE_FAMILY = "Family"
        const val GENRE_FANTASY = "Fantasy"
        const val GENRE_HISTORY = "History"
        const val GENRE_HORROR = "Horror"
        const val GENRE_MUSIC = "Music"
        const val GENRE_MYSTERY = "Mystery"
        const val GENRE_ROMANCE = "Romance"
        const val GENRE_SCI_FI = "Science Fiction"
        const val GENRE_TV_MOVIE = "TV Movie"
        const val GENRE_THRILLER = "Thriller"
        const val GENRE_WAR = "War"
        const val GENRE_WESTERN = "Western"
        const val GENRE_ACTION_ADVENTURE= "Action & Adventure"
        const val GENRE_KIDS = "Kids"
        const val GENRE_NEWS = "News"
        const val GENRE_REALITY = "Reality"
        const val GENRE_SCI_FI_FANTASY = "Sci-Fi & Fantasy"
        const val GENRE_SOAP = "Soap"
        const val GENRE_TALK = "Talk"
        const val GENRE_WAR_POLITICS = "War & Politics"
        const val GENRE_TYPE_ERROR_MESSAGE = "No such genre type."
        // Network Titles
        const val NETWORK_ALL = "All Networks"
        const val NETWORK_PRIME = "PrimeVideo"
        const val NETWORK_APPLE = "AppleTV"
        const val NETWORK_DISNEY = "Disney+"
        const val NETWORK_HBO_NOW = "HBONow"
        const val NETWORK_HBO_MAX = "HBOMax"
        const val NETWORK_HULU = "Hulu"
        const val NETWORK_NETFLIX = "Netflix"
        const val NETWORK_PEACOCK = "Peacock"
        const val NETWORK_TYPE_ERROR_MESSAGE = "No such network type."
        // Search Fragment constants
        const val MEDIA_TYPE_MOVIE = "movie"
        const val MEDIA_TYPE_TV = "tv"
        // Intent constants
        const val INTENT_MOVIE_SUBJECT = "Check out this movie:"
        const val INTENT_TV_SUBJECT = "Check out this tv show:"
        const val INTENT_TYPE_TEXT = "text/plain"
        // Exception constants
        const val ILLEGAL_FRAGMENT_TYPE_EXCEPTION = "No such FragmentType"

        fun getBingeStatusArray(): Array<String> {
            return arrayOf(
                DEFAULT_BINGE_STATUS,
                BINGING_BINGE_STATUS,
                SEEN_BINGE_STATUS
            )
        }

        fun getGenericSortArray(): Array<String> {
            return arrayOf(
                MOVIE_POPULAR_TITLE,
                MOVIE_TOP_RATED_TITLE,
                MOVIE_NOW_PLAYING_TITLE,
                MOVIE_UPCOMING_TITLE,
                MOVIE_TRENDING_TITLE
            )
        }

        fun getGenericSortMap(): Map<String, GenericSortType> {
            val genericSortArray = getGenericSortArray()
            val genericSortTypes = GenericSortType.getAllTypesArray()
            return genericSortArray.zip(genericSortTypes).toMap()
        }

        fun getGenericSortKey(value: GenericSortType): String {
            val genericSortMap = getGenericSortMap()
            return genericSortMap.keys.first { value == genericSortMap[it] }
        }

        fun getMovieGenresArray(): Array<String> {
            return arrayOf(
                GENRE_ALL,
                GENRE_ACTION,
                GENRE_ADVENTURE,
                GENRE_ANIMATION,
                GENRE_COMEDY,
                GENRE_CRIME,
                GENRE_DOCUMENTARY,
                GENRE_DRAMA,
                GENRE_FAMILY,
                GENRE_FANTASY,
                GENRE_HISTORY,
                GENRE_HORROR,
                GENRE_MUSIC,
                GENRE_MYSTERY,
                GENRE_ROMANCE,
                GENRE_SCI_FI,
                GENRE_TV_MOVIE,
                GENRE_THRILLER,
                GENRE_WAR,
                GENRE_WESTERN
            )
        }

        fun getMovieGenresMap(): Map<String, GenreType> {
            val movieGenresArray = getMovieGenresArray()
            val genreTypes = GenreType.getMovieGenreTypes()
            return movieGenresArray.zip(genreTypes).toMap()
        }

        fun getMovieGenresKey(value: GenreType) : String {
            val movieGenresMap = getMovieGenresMap()
            return movieGenresMap.keys.first { value == movieGenresMap[it] }
        }

        fun getMovieGenrePosition(genre: GenreType): Int {
            val key = getMovieGenresKey(genre)
            return getMovieGenresArray().indexOf(key)
        }

        fun getTvGenresArray(): Array<String> {
            return arrayOf(
                GENRE_ALL,
                GENRE_ACTION_ADVENTURE,
                GENRE_ANIMATION,
                GENRE_COMEDY,
                GENRE_CRIME,
                GENRE_DOCUMENTARY,
                GENRE_DRAMA,
                GENRE_FAMILY,
                GENRE_KIDS,
                GENRE_MYSTERY,
                GENRE_NEWS,
                GENRE_REALITY,
                GENRE_SCI_FI_FANTASY,
                GENRE_SOAP,
                GENRE_TALK,
                GENRE_WAR_POLITICS,
                GENRE_WESTERN
            )
        }

        fun getTvGenresMap(): Map<String, GenreType> {
            val tvShowsGenresArray = getTvGenresArray()
            val genreTypes = GenreType.getTvGenreTypes()
            return tvShowsGenresArray.zip(genreTypes).toMap()
        }

        fun getTvGenresKey(value: GenreType) : String {
            val tvShowGenresMap = getTvGenresMap()
            return tvShowGenresMap.keys.first { value == tvShowGenresMap[it] }
        }

        fun getTvGenrePosition(genre: GenreType): Int {
            val key = getTvGenresKey(genre)
            return getTvGenresArray().indexOf(key)
        }

        fun getTvNetworksArray(): Array<String> {
            return arrayOf(
                NETWORK_ALL,
                NETWORK_PRIME,
                NETWORK_APPLE,
                NETWORK_DISNEY,
                NETWORK_HBO_NOW,
                NETWORK_HBO_MAX,
                NETWORK_HULU,
                NETWORK_NETFLIX,
                NETWORK_PEACOCK
            )
        }

        fun getTvNetworksMap(): Map<String, NetworkType> {
            val tvNetworksArray = getTvNetworksArray()
            val networkTypes = NetworkType.getNetworkTypes()
            return tvNetworksArray.zip(networkTypes).toMap()
        }

        fun getTvNetworksKey(value: NetworkType) : String {
            val tvNetworksMap = getTvNetworksMap()
            return tvNetworksMap.keys.first { value == tvNetworksMap[it] }
        }

        fun getTvNetworkPosition(network: NetworkType): Int {
            val key = getTvNetworksKey(network)
            return getTvNetworksArray().indexOf(key)
        }
    }
}