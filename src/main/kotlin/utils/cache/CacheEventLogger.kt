package utils.cache

import org.ehcache.event.CacheEvent
import org.ehcache.event.CacheEventListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CacheEventLogger : CacheEventListener<Any, Any> {

    private val logger: Logger = LoggerFactory.getLogger(CacheEventLogger::class.java)

    override fun onEvent(cacheEvent: CacheEvent<out Any, out Any>?) {
        logger.info("Cache event = {}, Key = {},  Old value = {}, New value = {}", cacheEvent?.type, cacheEvent?.key,
            cacheEvent?.oldValue, cacheEvent?.newValue)
    }
}
