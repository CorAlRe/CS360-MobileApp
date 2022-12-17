# CS 360 – Mobile App Android
### Briefly summarize the requirements and goals of the app you developed. What user needs was this app designed to address?
The requirement was to design a mobile application on Android that allowed the user to schedule events and provide reminders. 

### What screens and features were necessary to support user needs and produce a user-centered UI for the app? How did your UI designs keep users in mind? Why were your designs successful? 
Three screens (and some ancillary UI) were necessary for the application. One is a login screen that establishes the identity and authorization of the user. Second a home screen that lists upcoming events and options to add new events. Lastly, an event creation/edit screen for setting the properties of an event. The design was successful in emphasizing the most relevant information first and moving less essential functions to either lower down or out of direct view through gestures or menus. 

### How did you approach the process of coding your app? What techniques or strategies did you use? How could those be applied in the future?
I worked the coding from two directions: designing the UI and then creating the data access layer that would support the UI elements. Lastly, I tried to glue the two layers together through a controlling layer. 

### How did you test to ensure your code was functional? Why is this process important, and what did it reveal?
I tested by exercising the features as they were added. I wanted to add unit tests but didn't have enough time to complete the assignment. 

### Considering the full app design and development process, from initial planning to finalization, where did you have to innovate to overcome a challenge?
I added asynchronous tasks that increased the complexity of the controlling layer, but that wasn't the main problem. The main problem I encountered was sending data between activities through bundles and intents. The method illustrated in the book was deprecated, and I had to learn and implement a new way of launching activities, but I was not getting the return value from the called activity. The activity lifecycle was not behaving as expected. When an activity launches another activity and expects a return value, I would expect the first activity to pause and resume. It behaved as if it was destroyed and created again. No matter the lifecycle stage, I could not find the return value from the called activity. I could not complete the project in time due to the time it took to find the cause.

### In what specific component from your mobile app were you particularly successful in demonstrating your knowledge, skills, and experience?
My strength was in the data design and asynchronous tasks the application used to access the data layer. I need to focus more on learning the user interface to feel like I'm approaching proficiency.
