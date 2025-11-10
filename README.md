# BurgerStream
A side project to further my understanding of Angular + Spring boot full stack development

## Backend application
To start up the back end application.

Copy the content of "application-example.properties" in "../BurgerStream/burgerstream-backend/src/main/resources
into a application.properties file (or rename the example).

Fill out your MySQL username and password and port if it is different from the default one.

Then run BurgerstreamBackendApplication file.
The API is accessible at:
"http://localhost:8080/api/v1/BurgerStream/"  
Example of endpoints include: "/menu/items", "orders" and "/menu/sizes."



## Setup
Run the BurgerstreamBackendApplication file as mentioned.  
Open a terminal in the burgerstream-frontend folder and run "ng serve"
This should open a link to "http://localhost:4200/" which should redirect you to the main page.  
If not it is http://localhost:4200/BurgerStream/.

To access admin view of the application go to
"http://localhost:4200/BurgerStream/admin."  
(Admin view is accessible to any user with this link, admin or not, as this project does not focus on handling access.)

Here you are able to view menu items but with the ability to do all CRUD operations on them.

## Further development
I will continue to work on the admin side of the project, as CRUD for Orders and SizeOptions are still missing.  
Some QoL in the display is missing. fx SizeOption select bar in menu-list-item is different widths.
