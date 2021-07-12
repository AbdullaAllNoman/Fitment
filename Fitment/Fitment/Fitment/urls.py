#Import settings
from django.conf import settings
#import admin
from django.contrib import admin
#import path
from django.urls import path
#import views from view
from . import views
from django.conf.urls.static import static
urlpatterns = [
    path('admin/', admin.site.urls),
    #home url
    path('home/', views.home,name='home'),
    path('signin/',views.signin,name='signin'),
    path('postsign/', views.postsign),
    path('logout', views.logout,name='log'),
    path('signup/',views.signup,name='signup'),
    path('postsignup/', views.postsignup,name='postsignup'),
    path('products/', views.products,name='products'),
    path('products/details/<slug:name>/', views.productsDetails,name='detail'),
    path('productsform/', views.productsform,name='productsform'),
    path('usersignup/', views.usersignup,name='usersignup'),
    path('postusersignup/', views.postusersignup,name='postusersignup'),
    path('productcreate/', views.productcreate,name='productcreate'),
    path('aaaa/', views.aaa, name='abc'),
    path('viewCart/', views.viewCart, name='viewCart'),
    path('order/<slug:foo>', views.order, name='order'),
    path('search/', views.search, name='search'),
    path('adminsignup/', views.adminsignup, name='adminsignup'),
    path('adminsign/', views.adminsign, name='adminsign'),
    path('vieworder/', views.vieworder, name="vieworder"),




    #path('products/details/<slug:name>/', views.Products_detail.as_view(),name='detail'),

]
if settings.DEBUG:
    urlpatterns = urlpatterns + static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
    urlpatterns = urlpatterns + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
