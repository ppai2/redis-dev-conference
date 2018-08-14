import cgi
import cStringIO

from IPython.display import HTML


def redis_key_to_repr(r, key):
    
    the_type = r.type(key)
    if the_type == 'string':
        #todo? - hyperloglog
        val = r.get(key)
    elif the_type == 'list':
        val = r.lrange(key, 0, -1)
    elif the_type == 'hash':
        val = r.hgetall(key)
    elif the_type == 'set':
        val = r.smembers(key)
    elif the_type == 'zset':
        val = r.zrange(key, 0, -1)
        
    return repr(val)


def show_database_state(r):
        
    
    s = cStringIO.StringIO()
    print >>s, '<table class="redis-database">\n'
     
    for key in r.keys():
        print >>s, "<tr>\n"
        print >>s, "<td>"
        print >>s, cgi.escape(key)
        print >>s, "</td>"
        print >>s, "<td>"
        print >>s, cgi.escape(redis_key_to_repr(r, key))
        print >>s, "</td>\n"
        print >>s, "</tr>\n"

    print >>s, "</table>\n"
        
    return HTML(s.getvalue())
