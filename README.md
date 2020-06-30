Auth Service
----------------
This is a spring boot microservice which is powered by **Spring Security**.
  * This microservice registers and is discoverable via **Discover-Service (Eureka Server)**.
  * It uses **Spring Security** to issue JWT token to valid users and has another endpoint to verify the token is authenticated.
  * Calls User-Service using Feign Client, to fetch the logged-in User by username and password.
  * **AOP** is used for logging across application
