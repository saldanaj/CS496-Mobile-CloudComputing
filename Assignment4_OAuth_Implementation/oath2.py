'''
Student Name: Joaquin Saldana 
Assignment 4 - OAuth2.0 Implementation  

Description: This is the google app that implements oauth2.0 
''' 

from google.appengine.ext import ndb
from google.appengine.ext.webapp import template 
import logging
import webapp2
import json
import os 
import urllib
import urllib2
import random 
from google.appengine.api import urlfetch


LOGIN_URI = 'https://accounts.google.com/o/oauth2/v2/auth'
tokenEndpoint = 'https://www.googleapis.com/oauth2/v4/token'
STATE = str(random.randint(100000,999999))

# project id: saldanaj-oauth2-imp
CLIENT_ID = '306731382932-1qqvvds1icsaf9s1f1n6rh41iadsq06q.apps.googleusercontent.com'
CLIENT_SECRET = 'uE5rXYDb4kgxFBaKyVu224B3'
REDIRECT_URI = 'https://saldanaj-oauth2-imp.appspot.com/oauth' 
# REDIRECT_URI = 'http://localhost:8080/oauth' 
REMOTE_URL = "https://accounts.google.com/o/oauth2/v2/auth"
SCOPE = "https://www.googleapis.com/auth/userinfo.email"


# Handler that redirects user to enter their credentials for authorization 
# to view their Google + information.  As in postman, it contains multiple values 
# that includes the authorization url
class LoginHandler(webapp2.RequestHandler):
    def get(self):
        
        url = 'https://accounts.google.com/o/oauth2/auth'
        values = {
            'response_type':'code',
            'state': STATE,
            'client_id':CLIENT_ID,
            'redirect_uri':REDIRECT_URI,
            'scope':'email',
            'access_type':'offline',
            'validate_certificate': 'true'
        }
        data = urllib.urlencode(values)
        address = url + '?' + data
        self.redirect(address)
        
        
        
# Handler that handles the exchange of the authorization code 
# for the access token.         
class AuthHandler(webapp2.RequestHandler):
    def get(self):
        state=self.request.get("state")
        
        # checking the state variable returned is the same variable we sent in the 
        # LoginHandler                 
        if state != STATE: 
                # response = make_response(json.dumps('Invalid state parameter.'), 401)
                # response.headers['Content-Type'] = 'application/json'
                self.response.status_int=402
                self.response.out.write(json.dumps('Invalid state parameter.'))  
                return 
        
        code=self.request.get("code")
        
        # POST to use authorization code to get access token
        # If done correctly, it returns the access token and the token type 
        data_to_post = {
          'code': code,
          'client_id': CLIENT_ID,
          'client_secret': CLIENT_SECRET,
          'redirect_uri': REDIRECT_URI,
          'grant_type': 'authorization_code'
        }
        
        encoded_data = urllib.urlencode(data_to_post)
        
        # Send encoded application-2 response to application-3
        headers={'Content-Type':'application/x-www-form-urlencoded'}
        result = urlfetch.fetch(tokenEndpoint, headers=headers, payload=encoded_data, method=urlfetch.POST)
        json_result=json.loads(result.content)
        accessToken=json_result['access_token']
        token_type=json_result['token_type']
        
        
        # Output response of application-3 to screen
        # section of code that now does a GET report to the 
        # Google + API w/ the access token to get the users 
        # information which is the displayName and the URL to the 
        # user's Google + account.  
        url='https://www.googleapis.com/plus/v1/people/me'
        
        headers = {
            'Authorization': token_type +' '+accessToken
        }
        final_result = urlfetch.fetch(url, headers=headers,  method=urlfetch.GET)
        parse_result=json.loads(final_result.content)
        name=parse_result['displayName']
        displayUrl=parse_result['url']
        path = os.path.join(os.path.dirname(__file__), 'result.html') 
        self.response.out.write(template.render(path, {}))
        self.response.write('Name: '+name+'\n'+'Google+ URL: '+displayUrl+'\n'+'State Variable: '+STATE)

        
        
# Main page that will have a link to the authorization link       
class MainPage(webapp2.RequestHandler):
    def get(self):
        path = os.path.join(os.path.dirname(__file__), 'index.html') 
        self.response.out.write(template.render(path, {}))    
        
            

# As per video the source of the code for the PATH handler is the following: 
# https://stackoverflow.com/questions/16280496/patch-method-handler-on-google-appengine-webapp2
allowed_methods = webapp2.WSGIApplication.allowed_methods
new_allowed_methods = allowed_methods.union(('PATCH',))
webapp2.WSGIApplication.allowed_methods = new_allowed_methods



app = webapp2.WSGIApplication([
    ('/', MainPage),
    ('/login',LoginHandler), 
    ('/oauth',AuthHandler)
], debug=True)


