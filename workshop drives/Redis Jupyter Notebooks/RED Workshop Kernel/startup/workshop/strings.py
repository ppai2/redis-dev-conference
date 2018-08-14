
def item_vote_key(item_id):
    "Returns the Redis key for maintaining item vote counts"
    
    return "item:" + str(item_id) + ":vote-count"
    
def clear_item_vote(r, item_id):
    "Clears the item vote count"
    
    key = item_vote_key(item_id)
    return r.delete(key)
    
def get_item_vote(r, item_id):
    "Returns the item vote count"
    
    key = item_vote_key(item_id)
    res = r.get(key)
    if res is None:
        return "0"
    else:
        return res
    
def users_vote_key(user_id):
    "Returns the key for tracking a users votes"

    return "user:" + str(user_id) + ":votes"
    
def clear_users_vote(r, user_id):
    "Clears a users vote history"
    
    key = users_vote_key(user_id)
    return r.delete(key)
    