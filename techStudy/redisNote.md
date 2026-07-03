# Redis

## 1.Common Commands

### 1.keys

keys *查询所有键

keys a* 查询a开头所有键

### 2.DEL

DEL name 删除 name键

### 3.EXISTS

EXISTS age 判断age键是否存在-返回1

### 4.EXIPRE

设定键的TTL

SET name sht

EXPIRE name 20

GET name

先初始化key-value EXPIRE设定有效期TTL

### 5.TTL

TTL name 查询name键的TTL

### 6.SET & SETNX & SETEX

set key value

MSET k1 v1 k2 v2 k3 v3 批量初始化

setnx:只有key不存在时才能创建

setex name 20 sht  == set name sht ex 20  //setex key TTL value

### 7.GET

get key

MGET k1,k2 批量获取 一次性返回

### 8.INCR &	INCRBY&INCRBYFLOAT

对整型String自增

set age 18

incr age (incrby age 2/incrby age -2==decrby age 1)

get age   "19"

set money 11.4

incrbyfloat money 0.5

get money  "11.9"
