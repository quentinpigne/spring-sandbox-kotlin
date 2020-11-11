package fr.quentinpigne.springsandboxkotlin.utils.cache.cacheablelist

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.cache.interceptor.SimpleKey
import org.springframework.cache.interceptor.SimpleKeyGenerator
import org.springframework.stereotype.Component
import java.lang.reflect.Method
import javax.cache.Cache
import javax.cache.CacheManager

@Aspect
@Component
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class CacheableListProcessorAspect(private val cacheManager: CacheManager) {

    @Around("@annotation(CacheableList)")
    fun cacheableList(joinPoint: ProceedingJoinPoint): List<Any?> {

        // Getting method, annotation and cache
        val method: Method = getCurrentMethod(joinPoint)
        val cacheableList: CacheableList = method.getAnnotation(CacheableList::class.java)
        val cache: Cache<Any, Any> = cacheManager.getCache(cacheableList.value, Any::class.java, Any::class.java)

        // List to spread for caching must be the first argument
        val listParameter = joinPoint.args[0] as List<Any>

        // Getting all items of the list already stored in the cache
        val cachedItemByCacheKey = cache.getAll(listParameter.map { item: Any -> getCacheKey(method, item) as SimpleKey }.toSet())

        // Computing list of parameters not found in cache
        val remainingParameters = listParameter.filterNot { item: Any -> cachedItemByCacheKey.containsKey(getCacheKey(method, item)) }.distinct()

        // No need to call initial method if everything was found in the cache
        if (remainingParameters.isEmpty()) {
            return listParameter.map { item: Any -> cachedItemByCacheKey[getCacheKey(method, item)] }
        }

        // Getting non cached items by calling initial method
        val nonCachedItems = joinPoint.proceed(arrayOf<Any>(remainingParameters)) as List<Any?>

        // Grouping non cached item by corresponding parameter and caching it
        val nonCachedItemByParameter = remainingParameters.zip(nonCachedItems).toMap()
        cache.putAll(nonCachedItemByParameter.filter { entry -> entry.value != null }.mapKeys { entry -> getCacheKey(method, entry.key) })

        return listParameter.map { item: Any ->
            if (cachedItemByCacheKey.containsKey(getCacheKey(method, item))) cachedItemByCacheKey[getCacheKey(method, item)] else nonCachedItemByParameter[item]
        }
    }

    private fun getCurrentMethod(joinPoint: JoinPoint): Method {
        val signature: MethodSignature = joinPoint.signature as MethodSignature
        return signature.method
    }

    private fun getCacheKey(method: Method, item: Any): Any {
        return SimpleKeyGenerator.generateKey(method.name, item)
    }
}
