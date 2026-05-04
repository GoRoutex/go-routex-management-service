package vn.com.routex.hub.management.service.infrastructure.cache.redisson;

public interface RedisDistributedService {

    RedisDistributedLocker getDistributedLock(String lockKey);
}
