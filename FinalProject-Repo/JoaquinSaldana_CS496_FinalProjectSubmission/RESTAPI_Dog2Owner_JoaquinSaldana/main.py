'''
Student: Joaquin Saldana 

Description: This is my REST API for the Final Project 

In this API, we are creating two objects and Dog and Human and the purpose 
of this API is for the Dog to Human app.  This will help fellow dog lovers/walkers to create 
a dictionary/library of other dogs and their owners so when they meet on the walking trails 
they save themselves the embarrassment of not knowing the names of either the dog and it's owner
'''


from google.appengine.ext import ndb 
import webapp2
import json 


# =========================================
# ENTITY MODELS 
# =========================================


# entity model for the Dog object
class Dog(ndb.Model): 
    name = ndb.StringProperty(required=True)
    breed = ndb.StringProperty(required=True)
    age = ndb.IntegerProperty(required=True) 
    owner = ndb.StringProperty() 
    alive = ndb.BooleanProperty(default=True)
    
    
# entity model used to register the dogs owned by 
# the human w/ the dogs id and link and can be a 
# list which is a property of the human entity 
class DogsOwned(ndb.Model):
    dog_id = ndb.StringProperty(required=True)
    dog_link = ndb.StringProperty(required=True)


# entity model for the Human object 
class Human (ndb.Model):
    name = ndb.StringProperty(required=True)
    age = ndb.IntegerProperty()
    favDogPark = ndb.StringProperty() 
    dogsOwned = ndb.StructuredProperty(DogsOwned, repeated=True)
    

# =========================================
# REQUEST HANDLERS  
# =========================================

# DogHandler that allows you to create, modify and delete data for the 
# dog entity

class DogHandler(webapp2.RequestHandler):
    def post(self):
        self.response.content_type = 'application/json'
        dogPostData=json.loads(self.request.body)
        
        new_Dog = Dog(name=dogPostData['name'], 
                      breed=dogPostData['breed'], 
                      age=dogPostData['age'])
        
        new_Dog.put()
        dog_dict=new_Dog.to_dict()
        dog_dict['self'] = '/dogs/' + new_Dog.key.urlsafe() 
        dog_dict['dog_id'] = str(new_Dog.key.urlsafe())
        
        self.response.write(json.dumps(dog_dict))
        self.response.status_int = 200
        self.response.status_message='Dog Successfully Created'


    def get(self, dog_id=None):
        self.response.content_type='application/json'
        
        # if the user provided a key/id to a dog 
        # then we GET it and return it by searching via that key 
        # else, we just return the entire list of dogs in our datastore 
        if dog_id: 
            dogGetData=ndb.Key(urlsafe=dog_id).get() 
            dog_dict=dogGetData.to_dict()
            dog_dict['self']='/dogs/' + dog_id
            dog_dict['dog_id'] = str(dog_id)
            
            self.response.write(json.dumps(dog_dict))
            self.response.status_int = 201
        else: 
            dogs=Dog.query()
            dog_list=[]
            for dog in dogs:    
                dog_dict2=dog.to_dict()
                dog_dict2['self']='/dogs/' + dog.key.urlsafe()
                dog_dict2['dog_id'] = str(dog.key.urlsafe())
                
                dog_list.append(dog_dict2)
                
            self.response.write(json.dumps(dog_list))
            self.response.status_int = 201
            
    def patch(self, dog_id=None):
        self.response.content_type = 'application/json'
        dogPatchData = json.loads(self.request.body)
        dog = ndb.Key(urlsafe=dog_id).get() 
        
        if dog: 
            if 'name' in dogPatchData: 
                dog.name=dogPatchData['name']
            if 'breed' in dogPatchData: 
                dog.breed=dogPatchData['breed']
            if 'age' in dogPatchData: 
                dog.age=dogPatchData['age']
                
            dog.put() 
            
            dog_dict=dog.to_dict() 
            dog_dict['self'] = '/dogs/' + dog.key.urlsafe()
            dog_dict['dog_id'] = str(dog.key.urlsafe())
            
            self.response.status_int = 201
            self.response.write(json.dumps(dog_dict))
            
        else: 
            self.response.status_int = 505 
            self.response.status_message ="Dog was not found.  Invalid information.  Please check your submission."
            self.response.out.write('Dog was not found.  Invalid information.  Please check your submission.')

            
    def put(self, dog_id=None):
        self.response.content_type='application/json'
        dogPutData=json.loads(self.request.body)
        dog = ndb.Key(urlsafe=dog_id).get() 
        
        if dog: 
            dog.alive=dogPutData['alive']
            
            dog.put() 
            
            dog_dict=dog.to_dict() 
            dog_dict['self'] = '/dogs/' + dog.key.urlsafe()
            dog_dict['dog_id'] = str(dog.key.urlsafe())
            
            self.response.status_int = 201
            self.response.write(json.dumps(dog_dict))
        else: 
            self.response.status_int = 505 
            self.response.status_message ="Dog was not found.  Invalid key"
            self.response.out.write('Dog was not found.  Invalid key')
            
            
            
    def delete(self, dog_id=None):
        self.response.content_type='application/json'
        dog = ndb.Key(urlsafe=dog_id).get() 
        
        if dog: 
            if dog.owner: 
                human=ndb.Key(urlsafe=dog.owner).get()
                
                dogsList = human.dogsOwned 
                pos = 0            
            
                for dogInList in dogsList: 
                    if(dogInList.dog_id == str(dog_id)): 
                        break
                    else:  
                        pos = pos + 1 
                
                 
                del dogsList[pos]
                human.dogsOwned = dogsList
                dog.owner = None 
                
                dog.put() 
                human.put() 
                
            dog_key = dog.key 
            dog_key.delete() 
            self.response.status_int = 201
            self.response.status_message='Dog Successfully Deleted'
            self.response.out.write('Dog Successfully Deleted')                
        else: 
            self.response.status_int = 505
            self.response.status_message ="Dog not found. Invalid key."
            self.response.out.write('Dog not found. Invalid key.')
            
                
                
# HumanHandler that allows you to create, modify and delete data for the 
# human entity

class HumanHandler(webapp2.RequestHandler):
    def post(self):
        self.response.content_type = 'application/json'
        humanPostData=json.loads(self.request.body)
        
        new_Human = Human(name=humanPostData['name'],
                          age=humanPostData['age'],
                          favDogPark=humanPostData['favorite_park'])
        
        new_Human.put()
        human_dict=new_Human.to_dict()
        human_dict['self'] = '/humans/' + new_Human.key.urlsafe() 
        human_dict['human_id'] = str(new_Human.key.urlsafe())
        
        self.response.write(json.dumps(human_dict))
        self.response.status_int = 200
        self.response.status_message='Human Successfully Created'
    
    
    def get(self, human_id=None):
        self.response.content_type='application/json'
        
        # if the user provided a key/id to a human 
        # then we GET it and return it by searching via that key 
        # else, we just return the entire list of humans in our datastore 
        if human_id: 
            humanGetData=ndb.Key(urlsafe=human_id).get() 
            human_dict=humanGetData.to_dict()
            human_dict['self']='/humans/' + human_id
            human_dict['human_id'] = str(human_id)
            
            self.response.write(json.dumps(human_dict))
            self.response.status_int = 201
        else: 
            humans=Human.query()
            human_list=[]
            for human in humans:    
                human_dict2=human.to_dict()
                human_dict2['self']='/humans/' + human.key.urlsafe()
                human_dict2['human_id'] = str(human.key.urlsafe())
                
                human_list.append(human_dict2)
                
            self.response.write(json.dumps(human_list))
            self.response.status_int = 201
        
        
    def patch(self, human_id=None):
        self.response.content_type = 'application/json'
        humanPatchData = json.loads(self.request.body)
        human = ndb.Key(urlsafe=human_id).get() 
        
        if human: 
            if 'name' in humanPatchData: 
                human.name=humanPatchData['name']
            if 'age' in humanPatchData: 
                human.age=humanPatchData['age']
            if 'favorite_park' in humanPatchData: 
                human.favDogPark=humanPatchData['favorite_park']
                
            human.put() 
            
            human_dict=human.to_dict() 
            human_dict['self'] = '/humans/' + human.key.urlsafe()
            human_dict['human_id'] = str(human.key.urlsafe())
            
            self.response.status_int = 201
            self.response.write(json.dumps(human_dict))
            
        else: 
            self.response.status_int = 505 
            self.response.status_message ="Human was not found.  Invalid information.  Please check your submission."
            self.response.out.write('Human was not found.  Invalid information.  Please check your submission.')
            
            
    def delete(self, human_id=None):
        self.response.content_type='application/json'
        human = ndb.Key(urlsafe=human_id).get()  
        
        if human: 
            dogsList = human.dogsOwned 

            for dog in dogsList: 
                dogEntity = ndb.Key(urlsafe=dog.dog_id).get() 
                dogEntity.owner = None 
                dogEntity.put() 
                
            human_key = human.key
            human_key.delete()
            
            self.response.status_int = 201
            self.response.status_message='Human Successfully Deleted'
            self.response.out.write('Human Successfully Deleted')
                
        else: 
            self.response.status_int = 505
            self.response.status_message ="Human not found. Invalid key."
            self.response.out.write('Human not found. Invalid key.')
        

# Handler that's used to start associating and dis-associating 
# a dog and human entity  

class SetOwnerHandler(webapp2.RequestHandler):
    def put(self, dog_id=None, human_id=None):
        self.response.content_type='application/json'
        
        dog = ndb.Key(urlsafe=dog_id).get() 
        human = ndb.Key(urlsafe=human_id).get() 
        
        
        if not dog: 
            self.response.status_int=405
            self.response.status_message="Invalid Dog ID"
            self.response.out.write("Invalid Dog ID")
        elif not human: 
            self.response.status_int=404 
            self.response.status_message="Invalid Human ID"
            self.response.out.write("Invalid Human ID") 
        elif dog.owner: 
            self.response.status_int=403 
            self.response.status_message="Dog Already Has Owner. Must Remove Current Owner Before Proceeding"
            self.response.out.write("Dog Already Has Owner.  Must Remove Current Owner Before Proceeding") 
        else: 
            
            dogsList = [] 
            
            for dogInList in human.dogsOwned: 
                if(dogInList.dog_id == str(dog_id)): 
                    self.response.status_int=408
                    self.response.status_message="Dog Already In List"
                    self.response.out.write("Dog Already In List")
                    return 
                else: 
                    dogsList.append(dogInList)
            
            dogsList.append(DogsOwned(dog_id=str(dog.key.urlsafe()), 
                                      dog_link='/dogs/'+ dog.key.urlsafe()))
            
            human.dogsOwned = dogsList

            dog.owner = human.key.urlsafe()
            
            dog.put() 
            human.put() 
            
            resp = [] 
            
            human_dict=human.to_dict()
            human_dict['self']= '/humans/' + human.key.urlsafe()
            human_dict['human_id'] = str(human.key.urlsafe())
            
            resp.append(human_dict)
            
            self.response.write(json.dumps(resp))

            
            self.response.status_int=201
            self.response.status_message="Owner set successfully"
            self.response.out.write("Owner set successfully")     
    
    
    def delete(self, dog_id=None, human_id=None):
        self.response.content_type='application/json'
        
        dog = ndb.Key(urlsafe=dog_id).get() 
        human = ndb.Key(urlsafe=human_id).get() 
        
        
        if not dog: 
            self.response.status_int=405
            self.response.status_message="Invalid Dog ID"
            self.response.out.write("Invalid Dog ID")
        elif not human: 
            self.response.status_int=404 
            self.response.status_message="Invalid Human ID"
            self.response.out.write("Invalid Human ID")    
        elif dog.owner == None: 
            self.response.status_int=403
            self.response.status_message="Error: Dog Does Not Have Owner"
            self.response.out.write("Error: Dog Does Not Have Owner")
        else:
            dogsList = human.dogsOwned 
            pos = 0            
            dogFound = False
            
            for dogInList in dogsList: 
                if(dogInList.dog_id == str(dog_id)): 
                    dogFound = True
                    break
                else:  
                    pos = pos + 1 
                    
            if dogFound == False: 
                self.response.status_int=408
                self.response.status_message="Error: Dog Is Not Present In The Humans List of Dogs Owned"
                self.response.out.write("Error: Dog Is Not Present In The Humans List of Dogs Owned")
                return 
            else: 
                dog.owner = None 
                
                del dogsList[pos]
                
                human.dogsOwned = dogsList
                
                dog.put() 
                human.put() 
                
                resp = [] 
            
                human_dict=human.to_dict()
                human_dict['self']= '/humans/' + human.key.urlsafe()
                human_dict['human_id'] = str(human.key.urlsafe())
                
                resp.append(human_dict)
                
                dog_dict=dog.to_dict() 
                dog_dict['self'] = '/dogs/' + dog.key.urlsafe()
                dog_dict['dog_id'] = str(dog.key.urlsafe())
                
                resp.append(dog_dict)
                
                self.response.write(json.dumps(resp))
                
                self.response.status_int=201
                self.response.status_message="Owner Removed Successfully"
                self.response.out.write("Owner Removed Successfully")  
                
            
class MainPage(webapp2.RequestHandler):
    def get(self):
        self.response.headers['Content-Type'] = 'text/plain'
        self.response.out.write('Joaquin Saldana - Final Project - Hybrid / Dog and Human App')


# =========================================
# APPLICATION
# =========================================


# As per video the source of the code for the PATH handler is the following: 
# https://stackoverflow.com/questions/16280496/patch-method-handler-on-google-appengine-webapp2
allowed_methods = webapp2.WSGIApplication.allowed_methods
new_allowed_methods = allowed_methods.union(('PATCH',))
webapp2.WSGIApplication.allowed_methods = new_allowed_methods


# dont forget to add the commas here after each URL path and handler tuple else 
# the console will throw an error 
app = webapp2.WSGIApplication([
    webapp2.Route(r'/', handler=MainPage), 
    webapp2.Route(r'/dogs', handler=DogHandler), 
    webapp2.Route(r'/dogs/<dog_id>', handler=DogHandler),
    webapp2.Route(r'/humans', handler=HumanHandler), 
    webapp2.Route(r'/humans/<human_id>', handler=HumanHandler),
    webapp2.Route(r'/dogs/<dog_id>/owner/<human_id>', handler=SetOwnerHandler)
    ], debug=True) 





