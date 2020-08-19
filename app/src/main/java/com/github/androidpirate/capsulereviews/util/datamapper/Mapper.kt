package com.github.androidpirate.capsulereviews.util.datamapper

interface Mapper<I, O> {

    fun map(input: I): O

}