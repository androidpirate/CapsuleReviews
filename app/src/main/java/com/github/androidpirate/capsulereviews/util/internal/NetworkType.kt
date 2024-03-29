package com.github.androidpirate.capsulereviews.util.internal

enum class NetworkType(val id: Int) {
    ALL(0),
    AMAZON_PRIME(1024),
    APPLE_TV(2552),
    DISNEY_PLUS(2739),
    HBO_NOW(49),
    HBO_MAX(3186),
    HULU(453),
    NETFLIX(213),
    PEACOCK(3353);

    companion object {
        fun getNetworkTypes(): Array<NetworkType> {
            return arrayOf(
                ALL,
                AMAZON_PRIME,
                APPLE_TV,
                DISNEY_PLUS,
                HBO_NOW,
                HBO_MAX,
                HULU,
                NETFLIX,
                PEACOCK
            )
        }
    }
}