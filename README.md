Nothing specific to setup just a normal clone method used java 17


Endpoints:
1)post http://localhost:8080/api/v1/posts
{
    "userId":1220,
    "content":"Hi hello how are you"
}

Assuming we are already having a user service thats why i am passing random user id and content and then it will generate a unique post id for every hit and send in a response

2)http://localhost:8080/api/v1/posts/{user_id}/analysis
Fetching all the post details using user id

http://localhost:8080/api/v1/post/{post_id}/analysis
Fetching particular post details using post id

Cache: Used inbuilt sprinboot cache to handle get requests
Rate limiter: Used inbuilt ratelimiter which allows 1 request per second 

Used synchronized method to handle race conditions if any


Database: postgres
Created a simple schema which contains postid(pk), userid, content and table name is post data. postid is PK because post id can be unique but userid might be same for few posts.


