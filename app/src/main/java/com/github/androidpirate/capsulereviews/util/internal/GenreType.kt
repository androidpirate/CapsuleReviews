package com.github.androidpirate.capsulereviews.util.internal

enum class GenreType(val id: Int) {
    ALL (0),
    ACTION(28),
    ADVENTURE(12),
    ANIMATION(16),
    COMEDY(35),
    CRIME(80),
    DOCUMENTARY(99),
    DRAMA(18),
    FAMILY(10751),
    FANTASY(14),
    HISTORY(36),
    HORROR(27),
    MUSIC(10402),
    MYSTERY(9648),
    ROMANCE(10749),
    SCI_FI(878),
    TV_MOVIE(10770),
    THRILLER(53),
    WAR(10752),
    WESTERN(37);

    companion object {
        fun getAllTypesArray(): Array<GenreType> {
            return arrayOf(
                ALL, ACTION, ADVENTURE, ANIMATION, COMEDY, CRIME, DOCUMENTARY,
                DRAMA, FAMILY, FANTASY, HISTORY, HORROR, MUSIC, MYSTERY, ROMANCE,
                SCI_FI, TV_MOVIE, THRILLER, WAR, WESTERN)
        }
    }

}