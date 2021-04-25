package com.example.maxdoroassigment.core

interface Mapper<in T, out R> {
    fun map(input: T): R
}