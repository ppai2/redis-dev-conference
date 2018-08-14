
sample_user_recommendations = [
    (3001, [ (201, -1), (202, -.8), (203, .4), (204, .9), (205, .8), (206, .7)]),
    (3002, [ (201, -1), (202,  .7), (204, .3) ]),
    (3003, [ (201, -1), (202, -.2), (203, .4) ]),
    (3004, [ (201, -1), (202, -.3), (203, .2) ]),
    (3005, [ (201, -1), (202,  .8), (203, .2), (204, .9) ]),
    (3006, [ (201, -1), (202,  .8), (203, .1), (204, .9) ]),
    (3007, [ (201, -1), (202,  .2), (203, .1) ]),
    (3008, [ (201, -1), (202,  .1), (203, .6) ]),
    (3009, [ (201, -1), (202,  .7), (203, .6), (204, .9) ]),
    (3010, [ (201, -1), (202,  .2) ])
]

def user_recommendation_key(user_id):
    "Returns the appropriate key for user recommendations"
    
    return "user:" + str(user_id) + ":recommendations"
    

def clear_user_recommendations(r, user_id):
    "Clears the recommendations for the given user"
    
    key = user_recommendation_key(user_id)
    r.delete(key)
    
def get_user_recommendations(r, user_id):
    "Returns the recommendations for the specified user"
    
    key = user_recommendation_key(user_id)
    return r.zrange(key, 0, -1, withscores=True)