# Backend API test

In order to run the tests:

1. Pull the project from GitHub
2. Open it using any IDE, I've used Intellij to code it
3. Using maven pull all dependencies
4. Run the code


NOTES: For PostCustomers class, a lot of tests are failing because I built the tests around what I thought would be expected from the API itself, since Post has no validation or checks, and all requests pass with a 201 no matter what is sent. Testing that scenario would be short so I opted to have some general expectations from the API.

I didn't finish the Frontent test I'm afraid because I didn't have the time. I wrote automation tests only for APIs while working in my previous company and didn't find the time to learn more for frontend ones as well. 
