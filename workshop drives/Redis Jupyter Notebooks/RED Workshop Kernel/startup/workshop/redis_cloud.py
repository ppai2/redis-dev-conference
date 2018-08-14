import os
import redis

## 
## Load configuration - should be in 00-config
##


def load_redis_cloud_config():
    "Loads the Redis Cloud configuration from the users home directory"
    
    home_dir = os.environ['HOME']
    config_path = os.path.join(home_dir, '.redis_cloud', 'config')

    with open(config_path, 'r') as inf:
        config = eval(inf.read())

    return config

def reset_redis_connection(config):
    try:
        r = redis.StrictRedis(**config)
        # empty 
        for key in r.keys():
            r.delete(key)
        
        return r

    except Exception, e:
        print "Unable to initialize iPython Redis Workshop prfile -- Redis connection failed"
        raise e

def is_connected(r):
    
    try:
        r.info()
        print "Connected to DB-%d@%s:%d" % (r.connection_pool.connection_kwargs['db'], r.connection_pool.connection_kwargs['host'], r.connection_pool.connection_kwargs['port'])
    except Exception, e:
        print "No connection to Redis"
        raise e

