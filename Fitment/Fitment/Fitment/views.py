from django.shortcuts import render, redirect
from pyrebase import pyrebase
from django.contrib import auth
from django.http import HttpResponse
from django.views.generic import TemplateView
from datetime import datetime as dt

#configuration of firebase

Config = {

    'apiKey': "AIzaSyBnHFYVViTRipbrF8nMbKBN43ONyEeyAMY",
    'authDomain': "fitment-c1432.firebaseapp.com",
    'databaseURL': "https://fitment-c1432.firebaseio.com",
    'projectId': "fitment-c1432",
    'storageBucket': "fitment-c1432.appspot.com",
    'messagingSenderId': "200152667905",
    'appId': "1:200152667905:web:0599b3810037e20c872d00",
    'measurementId': "G-HYEGZC7YE6"
}
#initialize firebase configuration
firebase=pyrebase.initialize_app(Config)
#authentiacte with fireabase
authe=firebase.auth()
#call fireabse database
database=firebase.database()
#This is demo view just for checking
def aaa(request):

    return render(request,'aaaa.html', {'user':request.session['user']})
"""
this view is for home page .get product from database
and check for user.In context products and user are sent.
"""
def home(request):
    products= database.child("Products").get()
    try:
        user = request.session['idToken']
#current user
    except:
        user = None
    return render(request,'home.html',{"prod":products, "user":user})
#sign in view
def signin(request):
    return render(request,'signin.html')
"""
In this view declare how sign in form
behave after submit the form.User authenticate with built in
sign_in_with_email_and_password() method by giving email and password_validation
"""
def postsign(request):
    email=request.POST.get("email")
    passw=request.POST.get("pass")
    try:
        user=authe.sign_in_with_email_and_password(email,passw)
        #authenticate user

    except:
        #exception message
        message="invalid email or password"
        return render(request,'signin.html',{'messg':message})

    print(user['idToken'])
    print(authe.get_account_info(user['idToken']))
    session_id=user['idToken']
    request.session['idToken'] = str(session_id)
#    request.session['user']= str(user['uid'])
    return render(request,'home.html',{'e':email})

#logout view method
#it delete the session after call the logout method
def logout(request):
    try:
        del request.session['idToken']
    except KeyError:
        pass
    return render(request,'signin.html')
#signup view method
def signup(request):
    return render(request,'signup.html')
"""
This method is responsible for postsignup
behaviour.By built in method it authenticate user
After that push all the form data to database under
Users table
"""
def postsignup(request):
    name=request.POST.get('fname')
    email=request.POST.get('email')
    passw=request.POST.get('pass')
    adress=request.POST.get('adress')
    phone=request.POST.get('phone')
    try:
        user=authe.create_user_with_email_and_password(email,passw)
        #authenticate user
        uid = user['localId']

    except:
        message="Unable to create account try again"
        return render(request,"signup.html",{"messg":message})

    data = {
         "Phone":phone,
         "address":adress,
         "name":name,
         "password":passw,
         "email":email
    }
    #store the form data
    database.child("Users").child(uid).set(data)
    return render(request,"signIn.html")
#show all the products in database
def products(request):
    products= database.child("Products").get()
    #get the products from database
    try:
        user = request.session['idToken']
        #current user

    except:
        user = None

    for pro in products.each():
        print(pro.key()[0])
        print(pro.val())

        #print(pro.val())
    return render(request,"products.html",{"prod":products,"user":user})
#products details
def productsDetails(request, name):
    products= database.child("Products").get()
    quantity=request.POST.get('quantity')
    try:
        user = request.session['idToken']
#current user
    except:
        user = None
        #if the method is get then
    if request.method == 'GET':
        for pro in products.each():
            if pro.val()['pname'] == name:
                product = pro
                return render(request,"productsDetails.html",{"prod":pro,"user":user})
        return HttpResponse("Product Not Found")
 #this is to show the details of product
 #if the method is post then
 #it will store the product data to cart list
    if request.method == 'POST':
       products= database.child("Products").get()
       try:
           user_session = request.session['idToken']

       except:
           return redirect('signin')

       info = authe.get_account_info(request.session['idToken'])

       uid = info['users'][0]['localId']
#localId of user
       for pro in products.each():
           if pro.val()['pname'] == name:
               product = pro
               break
       if product:
           pid = product.val()['pid']
           data = {
            'date' : dt.today().strftime("%b %d, %Y"),
            'discount' : '',
            'pid' : pid,
            'pname': product.val()['pname'],
            'price': product.val()['price'],
            'quantity':int(quantity),
            'time': dt.today().strftime("%H:%M:%S")
           }
         #push the data to cart list user view
           database.child("Cart List").child('User View').child(uid).child('Products').push(data)
           #push the data to cart list admin view
           database.child("Cart List").child('Admin View').child(uid).child('Products').push(data)
           return redirect('products')




      # except KeyError:
#message="Opps user logout please signin"
#return render(request,'signin.html',{'messg':message})


       #return redirect('home')



#show the product form page

def productsform(request):
    return render(request,'productform.html')

# class Products_detail(TemplateView):
#     template_name = "productsDetails.html"
#
#     def get_object(self, *args, **kwargs):
#         request = self.request
#         slug = self.kwargs.get('name')
#         products= database.child("Products").get()
#         for pro in products.each():
#
#             if pro.val()['pname'] == slug:
#                 prod = pro
#                 return projectId
#         return HttpResponse("notfound")
#show the signup page
def usersignup(request):
    return render(request,'usersignup.html')
#this is postsignup method.After signup the data
#will store to Users database table
def postusersignup(request):
    uname=request.POST.get('uname')
    phone=request.POST.get('phone')
    adress=request.POST.get('adress')
    password=request.POST.get('pass')
    user=request.POST.get('username')
    data = {
         "Phone":phone,
         "adress":adress,
         "name":uname,
         "password":password,
         "userName":user
    }
    database.child("Users").push(data)
    #push the data to user
    return render(request,'home.html')
#product create method
#BY filling the form product will add to products databse table
def productcreate(request):
    category=request.POST.get('category')
    date=request.POST.get('date')
    description=request.POST.get('description')
    url=request.POST.get('url')
    modelurl=request.POST.get('modelurl')
    pid=request.POST.get('pid')
    pname=request.POST.get('pname')
    price=request.POST.get('price')
    time=request.POST.get('time')
    data = {
         "category":category,
         "date":date,
         "description":description,
         "image":url,
         "model3dUrl":modelurl,
         "pid":pid,
         "pname":pname,
         "price":price,
         "time":time,
    }
    #push the created product data to database
    database.child("Products").push(data)
    return render(request,'products.html')
#view current user cart to the user
def viewCart(request):
    cart = database.child('Cart List')
    try:
        user_session = request.session['idToken']

    except:
        return redirect('signin')

    info = authe.get_account_info(request.session['idToken'])
    #get information of current user

    uid = info['users'][0]['localId']

    cart_user = cart.child('User View').child(uid)
    user_prod = cart_user.child('Products').get()
    if not user_prod.each():
        return render(request,'emptycart.html')
        #return HttpResponse("Cart is empty")
    total = 0

    for p in user_prod.each():
        total += int(p.val()['price']) * p.val()['quantity']
    return render(request,'viewCart.html', {'prod': user_prod,"user":user_session, 'total':str(total)})

#total of all products
#show order to the admin
def order(request, foo):
    try:
        user_session = request.session['idToken']

    except:
        return redirect('signin')

    info = authe.get_account_info(request.session['idToken'])

    uid = info['users'][0]['localId']
    print(uid)
    database.child("Orders").push(uid)
    database.child("Cart List").child('User View').child(uid).remove()

    users = database.child('Users').get()


    user_name = None
    user_phone = None
    user_address = None
    for u in users.each():
        if uid == u.key():
            user_name = u.val()['name']
            user_phone = u.val()['Phone']
            user_address = u.val()['address']
            print(user_name)

    if user_name:

        data = {
         'date' : dt.today().strftime("%b %d, %Y"),
         'address' : user_address,
         'phone' : user_phone,
         'state': 'not shipped',
         'totalAmount': foo,
         'time': dt.today().strftime("%H:%M:%S"),
         'uid': uid,
        }

        database.child("Orders").push(data)
        return render(request,'order.html')
    return render(request, 'home.html')
#search method to get the products
def search(request):
    search=request.POST.get('search')
    products= database.child("Products").get()
    for pro in products.each():
        if pro.val()['pname'] == search:
            product = pro
            return render(request,"search.html",{"prod":pro})
    return HttpResponse("Product Not Found")
#admin signup form
def adminsignup(request):
    return render(request, 'adminsign.html')
#admin post signin
def adminsign(request):
    name=request.POST.get('name')
    admin= database.child("Admins").get()
    for add in admin.each():
        if add.val()['userName'] == name:

            return render(request,"vieworder.html")
    return HttpResponse("Please input correct admin username and password")
#view order to admin
def vieworder(request):
    orders= database.child("Orders").get()
    return render(request,"vieworder.html",{"order":orders})
