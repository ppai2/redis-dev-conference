
sample_page_view_events = [
    ("1ef81361-0071-11e7-bf3a-4c3275922049", 2017, 3, 4, 3001, 201),
    ("1ef81554-0071-11e7-b11b-4c3275922049", 2017, 3, 4, 3001, 202),
    ("1ef8164f-0071-11e7-80ec-4c3275922049", 2017, 3, 4, 3002, 201),
    ("1ef81717-0071-11e7-9791-4c3275922049", 2017, 3, 4, 3001, 202),
    ("1ef8188a-0071-11e7-a448-4c3275922049", 2017, 3, 4, 3001, 201),
    ("1ef81917-0071-11e7-9215-4c3275922049", 2017, 3, 4, 3003, 201),
    ("1ef81997-0071-11e7-ac0b-4c3275922049", 2017, 3, 4, 3004, 201),
    ("1ef81a2e-0071-11e7-a560-4c3275922049", 2017, 3, 4, 3003, 201),
    ("1ef81ac2-0071-11e7-9ffe-4c3275922049", 2017, 3, 4, 3001, 205),
    ("1ef81b59-0071-11e7-967a-4c3275922049", 2017, 3, 4, 3003, 202),
    ("4eb72d75-0072-11e7-b160-4c3275922049", 2017, 3, 5, 3001, 201),
    ("4eb72f57-0072-11e7-aa82-4c3275922049", 2017, 3, 5, 3002, 202),
    ("4eb732b0-0072-11e7-9153-4c3275922049", 2017, 3, 5, 3002, 201),
    ("4eb733c0-0072-11e7-b177-4c3275922049", 2017, 3, 5, 3001, 202),
    ("4eb734e1-0072-11e7-aeb5-4c3275922049", 2017, 3, 5, 3003, 204),
    ("4eb7358a-0072-11e7-a629-4c3275922049", 2017, 3, 5, 3003, 204),
    ("4eb7364a-0072-11e7-b999-4c3275922049", 2017, 3, 5, 3001, 204),
    ("4eb73780-0072-11e7-b7c1-4c3275922049", 2017, 3, 5, 3001, 202),
    ("4eb7385c-0072-11e7-a8c5-4c3275922049", 2017, 3, 5, 3003, 201),
    ("4eb73907-0072-11e7-9caf-4c3275922049", 2017, 3, 5, 3001, 202)  
]

def daily_page_view_key(page_id, year, month, day):
    """Builds a structured key of the form page:{page_id}:unique:{year}:{month}:{day} to track
    unique page views
    """
    
    return  "page:" + str(page_id) + ":unique:" + str(year) + ":" + str(month) + ":" + str(day)
    
def secondary_page_index():
    "Returns the index key for the given date"
    
    return "index:unique-page"

def log_page_view_event(eid, pid, uid, year, month, day):
    "Utility function to log page view event for set exercises"
    
    print "%s PAGE_VIEW: %04d user %04d %04d-%02d-%02d" % (eid, pid, uid, year, month, day)

def log_page_view(pid, year, month, day, views):
    "Utility function to print page views for set exercises"
    
    print "Unique Page Views %04d %04d-%02d-%02d: %03d" % (pid, year, month, day, views)
    
def convert_key_to_components(key):
    "Returns a (pid, year, month, day) tuple from a key"

    comps = key.split(":")
    return (int(comps[1]), int(comps[3]), int(comps[4]), int(comps[5]))
    
def hint_idx_record_user_page_view(r, pid, year, month, day, uid):
    "Records a page view in Redis to generate unique viewers"

    # modify this function to create a secondary index
    idx_name = workshop.sets.secondary_page_index()  
    r.sadd(idx_name, workshop.sets.daily_page_view_key(pid, year, month, day))   

    key = workshop.sets.daily_page_view_key(pid, year, month, day)
    return r.sadd(key, uid)

def hint_idx_report_unique_page_views(r):
    "Implements a basic report of unique page views for the data in Redis"
    
    #keys = scan_keys('page:*:unique:*')
    #keys.sort()
    idx_name = workshop.sets.secondary_page_index()  
    keys = r.smembers(idx_name)
    
    for key in keys:
        pid, year, month, day = workshop.sets.convert_key_to_components(key)
        
        views = get_unique_views(pid, year, month, day)
        workshop.sets.log_page_view(pid, year, month, day, views)
        
def get_keys_from_secondary_index(r):
    "Returns the keys from the secondary index"
    
    idx_name = secondary_page_index()
    return r.smembers(idx_name)
    

def mau_key(year, month):
    "Returns the key for the MAU storage"
    
    return "site:metrics:" + str(year) + ":" + str(month)
    
def all_user_key():
    "Returns the key for all users in the system"

    return "site:users:all_users"
    
def create_all_users_set(r):
    "Creates a sample all user set"
    
    users = set()
    for event in sample_page_view_events:
        users.add(event[4])
        
    users.add(1001)
    users.add(1002)
    users.add(1003)
    
    r.sadd(all_user_key(), *users)

def absent_user_key(year, month):
    "Returns the absent user key for the given year, month combo"
    
    return "site:users:absent_users:" + str(year) + ":" + str(month)
    

    
    