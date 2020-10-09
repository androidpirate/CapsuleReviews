package com.github.androidpirate.capsulereviews.util.internal

class Constants {
    companion object {
        // General constants
        const val COMMA_SEPARATOR = ","
        // Fragment constants
        const val MOVIES_LIST_FRAG_TAG = "MoviesListFragment"
        const val PAGED_MOVIES_LIST_FRAG_TAG = "PagedMoviesListFragment"
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
        const val TV_TOP_RATED_TITLE = "Top Rated TV shows"
        const val TV_TRENDING_TITLE = "Trending TV Shows"
        const val TV_TRENDING_NETFLIX_TITLE = "Trending TV Shows on Netflix"
        const val TV_TRENDING_HULU_TITLE = "Trending TV Shows on Hulu"
        const val TV_TRENDING_DISNEY_TITLE = "Trending TV Shows on Disney Plus"
        // Favorite content constants
        const val EMPTY_POSTER_PATH = ""
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
        const val PAGER_MOVIE_TAB_POS = 0
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
        // Movie Genre Ids
        const val MOVIE_ACTION_ID = 28
        const val MOVIE_ADVENTURE_ID = 12
        const val MOVIE_ANIMATION_ID = 16
        const val MOVIE_COMEDY_ID = 35
        const val MOVIE_CRIME_ID = 80
        const val MOVIE_DOCUMENTARY_ID = 99
        const val MOVIE_DRAMA_ID = 18
        const val MOVIE_FAMILY_ID = 10751
        const val MOVIE_FANTASY_ID = 14
        const val MOVIE_HISTORY_ID = 36
        const val MOVIE_HORROR_ID = 27
        const val MOVIE_MUSIC_ID = 10402
        const val MOVIE_MYSTERY_ID = 9648
        const val MOVIE_ROMANCE_ID = 10749
        const val MOVIE_SCI_FI_ID = 878
        const val MOVIE_TV_MOVIE_ID = 10770
        const val MOVIE_THRILLER_ID = 53
        const val MOVIE_WAR_ID = 10752
        const val MOVIE_WESTERN_ID = 37
        // Tv Show Genre Ids
        const val TV_ACTION_ADVENTURE_ID = 10759
        const val TV_ANIMATION_ID = 16
        const val TV_COMEDY_ID = 35
        const val TV_CRIME_ID = 80
        const val TV_DOCUMENTARY_ID = 99
        const val TV_DRAMA_ID = 18
        const val TV_FAMILY_ID = 10751
        const val TV_KIDS_ID = 10762
        const val TV_MYSTERY_ID = 9648
        const val TV_NEWS_ID = 10763
        const val TV_REALITY_ID = 10764
        const val TV_SCI_FI_FANTASY_ID = 10756
        const val TV_SOAP_ID = 10766
        const val TV_TALK_ID = 10767
        const val TV_WAR_POLITICS_ID = 10768
        const val TV_WESTERN_ID = 37

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
            val genreTypes = GenreType.getAllTypesArray()
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
    }
}