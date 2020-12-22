package ru.android.exn.feature.quotes.di

interface QuotesFragmentDependency {

    interface DependencyProvider {

        fun getQuotesFragmentDependency(): QuotesFragmentDependency
    }
}