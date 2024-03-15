package com.code.orientation.utils;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.code.orientation.constants.CodeEnum;
import com.code.orientation.constants.RedisConstants;
import com.code.orientation.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@SuppressWarnings("all")
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * 给一个指定的 key 值附加过期时间
     *
     * @param key  键
     * @param time 值
     * @return 是否设置成功
     */
    public boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键
     * @return 过期时间
     */
    public long getTime(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键
     * @return 是否已经过期
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 移除指定key 的过期时间
     *
     * @param key 键
     * @return 是否移除成功
     */
    public boolean persist(String key) {
        return redisTemplate.boundValueOps(key).persist();
    }

    //- - - - - - - - - - - - - - - - - - - - -  String类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 根据key获取值
     *
     * @param key 键
     * @return 值
     */
    public Object getObject(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取
     *
     * @param key 键
     * @return {@link String}
     */
    public String getString(String key){
        return redisTemplate.opsForValue().get(key) == null ? null : JSONUtil.toJsonStr(redisTemplate.opsForValue().get(key));
    }

    /**
     * 按类型获取值
     *
     * @param key  键
     * @param type 类型
     * @return {@link E}
     */
    public <E> E get(String key,Class<E> type){
        String json = JSONUtil.toJsonStr(redisTemplate.opsForValue().get(key));
        E value = getValue(type,json);
        return value;
    }

    /**
     * 获取值
     *
     * @param type 类型
     * @param json json格式
     * @return {@link E}
     */
    public static <E> E getValue(Class<E> type, String json) {
        if (type == String.class) {
            // 对于字符串，直接返回 JSON 字符串
            return (E) json;
        } else if (type == Integer.class) {
            // 对于整数，使用 parseInteger 方法
            return (E) Integer.valueOf(JSONUtil.parseObj(json).getInt("value"));
        } else if (type == Long.class) {
            // 对于长整数，使用 parseLong 方法
            return (E) Long.valueOf(JSONUtil.parseObj(json).getLong("value"));
        } else if (type == Double.class) {
            // 对于双精度浮点数，使用 parseDouble 方法
            return (E) Double.valueOf(JSONUtil.parseObj(json).getDouble("value"));
        } else if (type == Float.class) {
            // 对于浮点数，使用 parseFloat 方法
            return (E) Float.valueOf(JSONUtil.parseObj(json).getFloat("value"));
        } else if (type == Boolean.class) {
            // 对于布尔值，使用 parseBoolean 方法
            return (E) Boolean.valueOf(JSONUtil.parseObj(json).getBool("value"));
        } else if (type == Character.class) {
            // 对于字符，使用字符串的第一个字符
            String strValue = JSONUtil.parseObj(json).getStr("value");
            if (strValue != null && strValue.length() > 0) {
                return (E) Character.valueOf(strValue.charAt(0));
            }
        } else if (type == Byte.class) {
            // 对于字节，使用 parseByte 方法
            return (E) Byte.valueOf(JSONUtil.parseObj(json).getByte("value"));
        } else if (type.isArray()) {
            // 对于数组，使用 parseArray 方法
            Class<?> componentType = type.getComponentType();
            JSONArray jsonArray = JSONUtil.parseArray(json);
            Object array = Array.newInstance(componentType, jsonArray.size());
            for (int i = 0; i < jsonArray.size(); i++) {
                Array.set(array, i, getValue(componentType, jsonArray.getStr(i)));
            }
            return (E) array;
        } else {
            // 对于其他对象类型，使用 toBean 方法
            return JSONUtil.toBean(json, type);
        }
        throw new CustomException(CodeEnum.UNSUPPORT_TYPE);
    }

    /**
     * 将值放入缓存
     *
     * @param key   键   键
     * @param value 值
     * @return true成功 false 失败
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 将值放入缓存并设置时间及时间单位
     *
     * @param key      键   键
     * @param value    值
     * @param time     时间(秒) -1为无期限
     * @param timeUnit 时间单位
     */
    public void set(String key, Object value, long time, TimeUnit timeUnit) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, timeUnit);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 设置
     *
     * @param redisConstants Redis 常量
     * @param key            键
     * @param value          价值
     */
    public void set(RedisConstants redisConstants, String key, Object value) {
        Long ttl = redisConstants.getTtl();
        if (ttl > 0) {
            redisTemplate.opsForValue().set(redisConstants.getKey() + key, value, ttl, redisConstants.getTimeUnit());
        } else {
            redisTemplate.opsForValue().set(redisConstants.getKey() + key, value);
        }
    }

    /**
     * 集
     *
     * @param redisConstants Redis 常量
     * @param value          值
     */
    public void set(RedisConstants redisConstants,Object value) {
        Long ttl = redisConstants.getTtl();
        if (ttl > 0) {
            redisTemplate.opsForValue().set(redisConstants.getKey(), value, ttl, redisConstants.getTimeUnit());
        } else {
            redisTemplate.opsForValue().set(redisConstants.getKey(), value);
        }
    }

    /**
     * 清理缓存
     *
     * @param
     */
    public boolean remove(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量添加 key (重复的键会覆盖)
     *
     * @param key      键
     * @param AndValue 键值对集合
     */
    public void batchSet(Map<String, String> keyAndValue) {
        redisTemplate.opsForValue().multiSet(keyAndValue);
    }

    /**
     * 批量添加 key-value 只有在键不存在时,才添加
     * map 中只要有一个key存在,则全部不添加
     *
     * @param key 键AndValue 键值对集合
     */
    public void batchSetIfAbsent(Map<String, String> keyAndValue) {
        redisTemplate.opsForValue().multiSetIfAbsent(keyAndValue);
    }

    /**
     * 对一个 key-value 的值进行加减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是长整型 ,将报错
     *
     * @param key    键
     * @param number
     */
    public Long increment(String key, long number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    /**
     * 对一个 key-value 的值进行加减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是 纯数字 ,将报错
     *
     * @param key    键
     * @param number
     */
    public Double increment(String key, double number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    //- - - - - - - - - - - - - - - - - - - - -  set类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 将数据放入set缓存
     *
     * @param key 键
     */
    public void sSet(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 获取变量中的值
     *
     * @param key 键
     * @return
     */
    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 随机获取变量中指定个数的元素
     *
     * @param key   键   键
     * @param count 值
     */
    public void randomMembers(String key, long count) {
        redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 随机获取变量中的元素
     *
     * @param key 键
     * @return object
     */
    public Object randomMember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 弹出变量中的元素
     *
     * @param key 键
     * @return
     */
    public Object pop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 获取变量中值的长度
     *
     * @param key 键
     * @return
     */
    public long size(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 检查给定的元素是否在变量中。
     *
     * @param key 键
     * @param obj 元素对象
     * @return 是否在变量中
     */
    public boolean isMember(String key, Object obj) {
        return redisTemplate.opsForSet().isMember(key, obj);
    }

    /**
     * 转移变量的元素值到目的变量。
     *
     * @param key     键     键
     * @param value   元素对象
     * @param destKey 元素对象
     * @return 是否转移成功
     */
    public boolean move(String key, Object value, String destKey) {
        return redisTemplate.opsForSet().move(key, value, destKey);
    }

    /**
     * 批量移除set缓存中元素
     *
     * @param key    键    键
     * @param values 值
     */
    public void remove(String key, Object... values) {
        redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 通过给定的key求2个set变量的差值
     *
     * @param key     键     键
     * @param destKey 键
     * @return 计算出的差值
     */
    public Set<Set> difference(String key, String destKey) {
        return redisTemplate.opsForSet().difference(key, destKey);
    }


    //- - - - - - - - - - - - - - - - - - - - -  hash类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 加入缓存
     *
     * @param key 键
     * @param map 键
     */
    public void add(String key, Map<String, String> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 获取 key 下的 所有  hashkey 和 value
     *
     * @param key 键
     * @return 键值对
     */
    public Map<Object, Object> getHashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 验证指定 key 下 有没有指定的 hashkey
     *
     * @param key     键
     * @param hashKey
     * @return 是否有指定的键
     */
    public boolean hashKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 获取指定key的值string
     *
     * @param key 键  键
     * @param key 键2 键
     * @return 字符串
     */
    public String getMapString(String key, String key2) {
        return redisTemplate.opsForHash().get("map1", "key1").toString();
    }

    /**
     * 获取指定的值Int
     *
     * @param key 键  键
     * @param key 键2 键
     * @return 整形变量int
     */
    public Integer getMapInt(String key, String key2) {
        return (Integer) redisTemplate.opsForHash().get("map1", "key1");
    }

    /**
     * 弹出元素并删除
     *
     * @param key 键
     * @return 弹出的元素
     */
    public String popValue(String key) {
        return redisTemplate.opsForSet().pop(key).toString();
    }

    /**
     * 删除指定 hash 的 HashKey
     *
     * @param key      键
     * @param hashKeys
     * @return 删除成功的 数量
     */
    public Long delete(String key, String[] hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 给指定 hash 的 hashkey 做增减操作
     *
     * @param key     键
     * @param hashKey
     * @param number
     * @return
     */
    public Long increment(String key, String hashKey, long number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 增加
     * 给指定 hash 的 hashkey 做增减操作
     *
     * @param key     键
     * @param hashKey 哈希键
     * @param number
     * @return {@link Double}
     */
    public Double increment(String key, String hashKey, Double number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 哈希键
     * 获取 key 下的 所有 hashkey 字段
     *
     * @param key 键
     * @return hashkey 字段的set集合
     */
    public Set<Object> hashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 哈希大小
     * 获取指定 hash 下面的 键值对 数量
     *
     * @param key 键
     * @return 键值对数量 long
     */
    public Long hashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    //- - - - - - - - - - - - - - - - - - - - -  list类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 在变量左边添加元素值
     *
     * @param key   键
     * @param value
     */
    public void leftPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 获取集合指定位置的值。
     *
     * @param key   键
     * @param index
     * @return 值
     */
    public Object index(String key, long index) {
        return redisTemplate.opsForList().index("list", 1);
    }

    /**
     * 获取指定区间的值。
     *
     * @param key   键
     * @param start 区间开始标志
     * @param end   区间结束标志
     * @return 指定区间的List集合
     */
    public List<Object> range(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 把最后一个参数值放到指定集合的第一个出现中间参数的前面，
     * 如果中间参数值存在的话。
     *
     * @param key   键
     * @param pivot
     * @param value 值
     */
    public void leftPush(String key, String pivot, Object value) {
        redisTemplate.opsForList().leftPush(key, pivot, value);
    }

    /**
     * 向左边批量添加参数元素。
     *
     * @param key    键
     * @param values 数量可变值元素
     */
    public void leftPushAll(String key, List<String> values) {
//        redisTemplate.opsForList().leftPushAll(key,"w","x","y");
        redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 向集合最右边添加元素。
     *
     * @param key   键
     * @param value 值
     */
    public void leftPushAll(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 向左边批量添加参数元素。
     *
     * @param key    键
     * @param values 值
     */
    public void rightPushAll(String key, String[] values) {
        //redisTemplate.opsForList().leftPushAll(key,"w","x","y");
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 向已存在的集合中添加元素。
     *
     * @param key   键
     * @param value 值
     */
    public void rightPushIfPresent(String key, Object value) {
        redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 获取存储在key处的列表的大小。
     *
     * @param key 键
     * @return 列表大小
     */
    public long listLength(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 移除集合中的左边第一个元素。
     *
     * @param key 键
     */
    public void leftPop(String key) {
        redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 移除集合中左边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
     *
     * @param key 键
     */
    public void leftPop(String key, long timeout, TimeUnit unit) {
        redisTemplate.opsForList().leftPop(key, timeout, unit);
    }

    /**
     * 移除集合中右边的元素。
     *
     * @param key 键
     */
    public void rightPop(String key) {
        redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 移除集合中右边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
     *
     * @param key 键
     */
    public void rightPop(String key, long timeout, TimeUnit unit) {
        redisTemplate.opsForList().rightPop(key, timeout, unit);
    }
}

