package com.elvis.webDemo.core.redis;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.SortingParams;

@Service
public class JedisClient {

	@Autowired
	private JedisPool pool;
	private final Long RELEASE_SUCCESS;

	public JedisClient() {
		RELEASE_SUCCESS = 1L;
	}

	//注："nil"不可作为key使用。
	
	//存储字符串，值为String类型
	public String set(String key, String value) {
		Jedis jedis = pool.getResource();	//Jedis是操作数据库的类
		String set = jedis.set(key, value);
		jedis.close();
		return set;
	}

	/**
	 * 利用set实现分布式锁
	 * @param lockKey
	 * @param lockValue	
	 * @param NXXX	只能取NX或者XX，如果取NX，则只有当key不存在是才进行set，如果取XX，则只有当key已经存在时才进行set
	 * @param EXPX	只能取EX或者PX，代表数据过期时间的单位，EX代表秒，PX代表毫秒。
	 * @param time	过期时间，单位是expx所代表的单位。
	 * @return	操作成功返回true；操作失败返回false；
	 */
	public boolean setDistributedLock(String lockKey, String lockValue, String NXXX, String EXPX, int time) {
		Jedis jedis = pool.getResource();	//Jedis是操作数据库的类
		String set = jedis.set(lockKey, lockValue, NXXX, EXPX, time);
		jedis.close();
		String LOCK_SUCCESS = "OK";
		if (LOCK_SUCCESS.equals(set)) {
			return true;
		}
        return false;
	}
    /**
     * 释放分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseDistributedLock(String lockKey, String lockValue) {
    	Jedis jedis = pool.getResource();	//Jedis是操作数据库的类
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(lockValue));
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }
	//获取指定key的value，值为String类型
	public String get(String key) {
		Jedis jedis = pool.getResource();
		String get = jedis.get(key);
		jedis.close();
		return get;
	}
	
	//递增指定的key对应的value，值为String类型
	public long incr(String key) {
		Jedis jedis = pool.getResource();
		Long incr = jedis.incr(key);
		jedis.close();
		return incr;
	}
	
	//存储hash散列数据
	//例：hset user1 name xiaoming age 20 sex 1;
	public long hset(String hkey, String key, String value) {
		Jedis jedis = pool.getResource();
		Long hset = jedis.hset(hkey,key,value);
		jedis.close();
		return hset;
	}
	
	//存储hash散列数据
	//例：hset user1 name xiaoming age 20 sex 1;
	public long hset(String hkey, String key, String value,int seconds) {
		Jedis jedis = pool.getResource();
		Long hset = jedis.hset(hkey,key,value);
		if (seconds>0) {
			jedis.expire(key, seconds);
		}
		jedis.close();
		return hset;
	}
	
	//获取hash散列数据的值
	//例：hget user1 name;	结果为：xiaoming
	public String hget(String hkey, String key) {
		Jedis jedis = pool.getResource();
		String str = jedis.hget(hkey, key);
		jedis.close();
		return str;
	}

	//存储list（如果集合已存在，追加到表头，如果集合不存在，创建）
	public long lpush(String key, String... strings) {
		Jedis jedis = pool.getResource();
		Long hset = jedis.lpush(key, strings);
		jedis.close();
		return hset;
	}
	
	//存储list（如果集合已存在，追加到表头，如果集合不存在，不执行任何操作）
	public Long lpushx(String key, String... strings) {
		Jedis jedis = pool.getResource();
		Long hset = jedis.lpushx(key, strings);
		jedis.close();
		return hset;
	}

	//存储list（如果集合已存在，追加到表尾，如果集合不存在，创建）
	public long rpush(String key, String... strings) {
		Jedis jedis = pool.getResource();
		Long hset = jedis.rpush(key, strings);
		jedis.close();
		return hset;
	}
	
	//存储list（如果集合已存在，追加到表尾，如果集合不存在，不执行任何操作）
	public Long rpushx(String key, String... strings) {
		Jedis jedis = pool.getResource();
		Long hset = jedis.rpushx(key, strings);
		jedis.close();
		return hset;
	}

	//返回list集合中下标为index的元素（0,1,2。。。）
	public String lindex(String key, long index) {
		Jedis jedis = pool.getResource();
		String hset = jedis.lindex(key, index);
		jedis.close();
		return hset;
	}
	
	//返回list集合中下标从start到end的元素（0,1,2。。。）
	public List<String> lrange(String key, long start, long end) {
		Jedis jedis = pool.getResource();
		List<String> hset = jedis.lrange(key, start, end);
		jedis.close();
		return hset;
	}
	
	//删除键值对，值为String类型
	//返回值为删除key的数量
	public long del(String... keys) {
		Jedis jedis = pool.getResource();
		Long l = jedis.del(keys);
		jedis.close();
		return l;
	}
	
	//删除键值对，值为hash类型
	public long hdel(String hkey, String key) {
		Jedis jedis = pool.getResource();
		Long l = jedis.hdel(hkey, key);
		jedis.close();
		return l;
	}
	
	//为key设置生命周期（秒）
	//设置成功返回1，key不存在返回0，不能为key设置周期返回0
	public long expire(String key, int seconds) {
		Jedis jedis = pool.getResource();
		Long e = jedis.expire(key, seconds);
		jedis.close();
		return e;
	}
	
	//为key设置生命周期（毫秒）
	//设置成功返回1，key不存在返回0，不能为key设置周期返回0
	public long pexpire(String key, long milliseconds) {
		Jedis jedis = pool.getResource();
		Long e = jedis.pexpire(key, milliseconds);
		jedis.close();
		return e;
	}
	
	//为key设置生命周期（某个时间点以秒为单位的uuix时间戳）
	//设置成功返回1，key不存在返回0，不能为key设置周期返回0
	public long expireAt(String key, long unixTime) {
		Jedis jedis = pool.getResource();
		Long e = jedis.expireAt(key, unixTime);
		jedis.close();
		return e;
	}

	//为key设置生命周期（某个时间点以毫秒为单位的Timestamp时间戳）
	//设置成功返回1，key不存在返回0，不能为key设置周期返回0
	public long pexpireAt(String key, long millisecondsTimestamp) {
		Jedis jedis = pool.getResource();
		Long e = jedis.pexpireAt(key, millisecondsTimestamp);
		jedis.close();
		return e;
	}
	
	//为key移除生命周期，永不过期
	//成功时返回1。如果key不存在或key没有设置生存时间，返回0 。
	public long persist(String key) {
		Jedis jedis = pool.getResource();
		Long e = jedis.persist(key);
		jedis.close();
		return e;
	}
	
	//查询key的剩余生命周期，（秒）
	public long ttl(String key) {
		Jedis jedis = pool.getResource();
		Long e = jedis.ttl(key);
		jedis.close();
		return e;
	}

	//查询key的剩余生命周期，（毫秒）
	public long pttl(String key) {
		Jedis jedis = pool.getResource();
		Long e = jedis.pttl(key);
		jedis.close();
		return e;
	}
	
	//检查给定key 是否存在
	//存在返回1，不存在返回0
	public long exists(String... keys) {
		Jedis jedis = pool.getResource();
		Long e = jedis.exists(keys);
		jedis.close();
		return e;
	}
	
	//模糊查询符合条件的key的set集合
	//* 匹配所有key	*
	//占位符? 匹配单个字符	ab?de
	//占位符* 匹配0个或多个字符	ab*ef
	//[abc] 匹配a或者b或者c	ab[cde]fg
	public Set<String> keys(String pattern) {
		Jedis jedis = pool.getResource();
		Set<String> set = jedis.keys(pattern);
		jedis.close();
		return set;
	}
	
	//将当前实例中的键值对剪切到另一个实例中
	//返回值OK：成功，其他：错误
	public String migrate(String host,int port,String key,int destinationDb,int timeout) {//timeout 毫秒
		Jedis jedis = pool.getResource();
		String s = jedis.migrate(host, port, key, destinationDb, timeout);
		jedis.close();
		return s;
	}
	
	//将当前库中的键值对剪切到另一个库中
	//成功返回1，失败返回0
	public long move(String key,int dbIndex) {
		Jedis jedis = pool.getResource();
		Long s = jedis.move(key, dbIndex);
		jedis.close();
		return s;
	}
	
	//随机返回当前库中一个key，如果不存在，返回nil
	public String randomKey() {
		Jedis jedis = pool.getResource();
		String key = jedis.randomKey();
		jedis.close();
		return key;
	}
	
	//更换key名
	//成功返回OK，其余为失败，newkey 已存在时， RENAME 会覆盖旧newkey
	public String rename(String oldkey,String newkey) {
		Jedis jedis = pool.getResource();
		String ok = jedis.rename(oldkey, newkey);
		jedis.close();
		return ok;
	}
	
	//当且仅当newkey 不存在时，将key 改名为newkey
	//成功返回1，失败返回0
	public long renamenx(String oldkey,String newkey) {
		Jedis jedis = pool.getResource();
		Long ok = jedis.renamenx(oldkey, newkey);
		jedis.close();
		return ok;
	}
	
	//返回集合中的值排序的结果
	public List<String> sort(String key,SortingParams sortingParameters) {
		Jedis jedis = pool.getResource();
		if (sortingParameters == null) {
			sortingParameters = sortingParameters.asc();
		}
		List<String> list = jedis.sort(key, sortingParameters);
		jedis.close();
		return list;
	}
	
	//将key的value中元素排序，保存到dstkey中
	//成功返回1，失败返回0
	public long sort(String key,SortingParams sortingParameters,String dstkey) {
		Jedis jedis = pool.getResource();
		if (sortingParameters == null) {
			sortingParameters = sortingParameters.asc();
		}
		Long l = jedis.sort(key, sortingParameters, dstkey);
		jedis.close();
		return l;
	}
	
	//返回key 所储存的值的类型。
	public String type(String key) {
		Jedis jedis = pool.getResource();
		String type = jedis.type(key);
		jedis.close();
		return type;
	}
	
	//拼接字符串
	//成功返回1，失败返回0
	public long append(String key,String value) {
		Jedis jedis = pool.getResource();
		Long type = jedis.append(key, value);
		jedis.close();
		return type;
	}
}
