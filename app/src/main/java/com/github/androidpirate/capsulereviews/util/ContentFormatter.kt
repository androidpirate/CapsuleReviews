package com.github.androidpirate.capsulereviews.util

import com.github.androidpirate.capsulereviews.data.network.response.genre.NetworkGenre
import com.github.androidpirate.capsulereviews.data.network.response.tvShow.NetworkCreatedBy
import com.github.androidpirate.capsulereviews.data.network.response.tvShow.NetworkNetworkInfo
import java.text.DecimalFormat

class ContentFormatter {

    companion object {
        private const val NO_DATA_AVAILABLE = "No data available."
        private const val RELEASE_DATE_DELIMITER = "-"
        private const val RELEASE_DATE_YEAR = 0
        private const val RELEASE_DATE_MONTH = 1
        private const val RELEASE_DATE_DAY = 2
        private const val MOVIE_RUNTIME_HOUR_UNIT = "h"
        private const val MOVIE_RUNTIME_MINUTE_UNIT = "m"
        private const val TV_SHOW_RUNTIME_UNIT = "mins"

        fun formatReleaseDate(releaseDate: String): String {
            val dateArray = releaseDate.split(RELEASE_DATE_DELIMITER)
            val year = dateArray[RELEASE_DATE_YEAR]
            val month = dateArray[RELEASE_DATE_MONTH]
            val day = dateArray[RELEASE_DATE_DAY]
            return "$day/$month/$year"
        }

        fun formatMovieRunTime(runTime: Int): String {
            val hour = runTime / 60
            val minute = runTime % 60
            return "$hour $MOVIE_RUNTIME_HOUR_UNIT $minute $MOVIE_RUNTIME_MINUTE_UNIT"
        }

        fun formatTvShowRunTime(runTime: Int): String {
            return "$runTime $TV_SHOW_RUNTIME_UNIT"
        }

        fun formatGenres(networkGenres: List<NetworkGenre>): String {
            var movieGenres: String = ""
            for(i in networkGenres.indices) {
                if(i == networkGenres.size - 1) {
                    movieGenres += networkGenres[i].name
                    break
                }
                movieGenres += "${networkGenres[i].name}, "
            }
            return movieGenres
        }

        fun formatBudget(budget: Long): String {
            return if(budget != 0L) {
                val formatter = DecimalFormat("#,###")
                "$ ${formatter.format(budget)}"
            } else {
                NO_DATA_AVAILABLE
            }
        }

        fun formatRevenue(revenue: Long): String {
            return if(revenue != 0L) {
                val formatter = DecimalFormat("#,###")
                "$ ${formatter.format(revenue)}"
            } else {
                NO_DATA_AVAILABLE
            }
        }

        fun formatCreatedBy(createdBy: List<NetworkCreatedBy>): String {
            var creators = ""
            for(i in createdBy.indices) {
                if(i == createdBy.size - 1) {
                    creators += createdBy[i].name
                    break
                }
                creators += "${createdBy[i].name}, "
            }
            return creators
        }

        fun formatNetworks(networkInfo: List<NetworkNetworkInfo>): String {
            var network = ""
            for(i in networkInfo.indices) {
                if(i == networkInfo.size - 1) {
                    network += networkInfo[i].name
                    break
                }
                network += "${networkInfo[i].name}, "
            }
            return network
        }
    }
}