package fr.quentinpigne.springsandboxkotlin.utils.cache.cacheablelist

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
annotation class CacheableList(val value: String = "")
