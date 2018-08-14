
class User(object):

    def __init__(self, **kwargs):
        self.id = None
        self.username = None
        self.fname = None
        self.lname = None
        self.email = None
        
        for key in kwargs:
            setattr(self, key, kwargs[key])
        
    def __str__(self):
        return str(self.__dict__)
               
    def get_key(self):
        if self.id is not None:
            return "user:" + str(self.id)
        else:
            return None

