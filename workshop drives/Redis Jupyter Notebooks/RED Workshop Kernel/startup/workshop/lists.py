import json

sample_history_data = [
    {
        "date": 1488573871,
         "title": "Unveiling the New Redis Enterprise Cloud UI",
         "url": "https://redislabs.com/blog/unveiling-new-redis-enterprise-cloud-ui/"
    },
    {
        "date": 1488573971,
         "title": "First Ever Redis Modules Hackaton",
         "url": "https://redislabs.com/blog/first-ever-redis-modules-hackathon/"
    },
    {
        "date": 1488574071,
         "title": "Redis Pack v4.4 Release",
         "url": "https://redislabs.com/blog/redis-pack-v4-4-release/"
    },
    {
        "date": 1488574271,
         "title": "Count Min Sketch: The Art and Science of Estimating Stuff",
         "url": "https://redislabs.com/blog/count-min-sketch-the-art-and-science-of-estimating-stuff/"
    },
    {
        "date": 1488574471,
         "title": "Redis Cloud Integrates with Databricks Spark",
         "url": "https://redislabs.com/blog/redis-cloud-integrates-with-databricks-spark/"
    },
    {
        "date": 1488574571,
         "title": "ZeroBrane Studio Plugin for Redis Lua Scripts",
         "url": "https://redislabs.com/blog/zerobrane-studio-plugin-for-redis-lua-scripts/"
    },
    {
        "date": 1488574771,
         "title": "Connecting Spark and Redis: A Detailed Look",
         "url": "https://redislabs.com/blog/connecting-spark-and-redis-a-detailed-look/"
    },
    {
        "date": 1488574871,
         "title": "Top Redis Headaches for DevOps - Client Buffers",
         "url": "https://redislabs.com/blog/top-redis-headaches-for-devops-client-buffers/"
    },
    {
        "date": 1488575071,
         "title": "5^H 6^H 7 Methods for Tracing and Debuging Redis Lua Scripts",
         "url": "https://redislabs.com/blog/5-6-7-methods-for-tracing-and-debugging-redis-lua-scripts/"
    },
    {
        "date": 1488575871,
         "title": "Redis is Beautiful: A Vizualization of Redis Commands",
         "url": "https://redislabs.com/blog/red-is-beautiful-a-visualization-of-redis-commands/"
    }
]

class ViewedItem(object):
    
    def __init__(self, **kwargs):
        self.date = None
        self.url = None
        self.title = None
         
        for key in kwargs:
            setattr(self, key, kwargs[key])
             
    def __str__(self):
        return "%s: %s" % (self.id, self.name)
        
    def __repr__(self):
        from pprint import pformat
        return pformat(vars(self))
        
        
    def to_serialized_json(self):
        return json.dumps(self.__dict__)
       
    @staticmethod 
    def from_serialized_json(s):
        return ViewedItem(**json.loads(s))



def user_history_key(user_id):
    "Generates the appropriate Redis key for the last viewed items of the supplied user_id"
    
    return "user:history:" + str(user_id)    

def clear_user_history(r, user_id):
    "Clears the previous viewed history for a user from Redis"
    r.delete(user_history_key(user_id))

def add_history(r, user_id):
    "Adds some user history data to Redis for a given user"
    
    sample_history_data.sort(lambda x, y: cmp(x['date'], y['date']))

    values = [ ]
    for hist in sample_history_data:
        
        item = ViewedItem(**hist)
        serialized_json = item.to_serialized_json()
        values.append(serialized_json)


    r.lpush(user_history_key(user_id), *values)


