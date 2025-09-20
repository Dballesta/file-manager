# Business Case

There's a business need that requires offering an asset upload feature for several of our internal tools which basically implies: 

    1. Managing the file upload process, which implies receiving the file and delegating the upload to the internal publisher. 
    2. Allowing the user to search the uploaded assets (against the persisted information in this system).

## Considerations
- See the attached openapi.yml for the API spec.
- The upload process must be done in an async manner.
- This implementation is not the "final resting place" of the uploaded assets, just a proxy that keeps information of it and acts as a middleman for the upload.

## Requirements
- Java 17/21.
- Spring Boot.
- HTTP/REST.
- Unit & integration tests.
- Fully functional (compiles, starts locally).
- Hexagonal design (You can go modular or package based, no worries on that). 
- Implement whichever defined filter you see fit, no need to implement them all but sorting is a must.
- Persistence.
- Reactive (WebFlux).
- Calling database

## Optional
- Implement all filters.
- Dockerized.
- Reactive (WebFlux). 
- Virtual Threads.
- Resilient.
- Postman collection.
- Security.

## To be covered during the interview
- Which improvements would you do to the API definition?
- How would you include paging?

## Consideraciones
Llevo cerca de dos años realizando mantenimiento y estoy un poco oxidado en cuanto a nuevas implementaciones.
Hace mucho que no toco la arquitectura hexagonal, dejo las fuentes en las que me he basado para hacer la implementación.

Se modifica el request param sort para ser autogenerado por el plugin de openapi.
https://swagger.io/docs/specification/v3_0/data-models/enums/
https://chatgpt.com/share/68cc9c4e-dda0-800a-a378-0cd930490378

## Fuentes
https://dev.to/rock_win_c053fa5fb2399067/hexagonal-architecture-in-spring-boot-microservices-a-complete-guide-with-folder-structure-1jld
https://www.youtube.com/watch?v=BKyiJaYdtSg
https://www.youtube.com/watch?v=PcoeGzGomqs
https://www.baeldung.com/spring-boot-return-errors-problemdetail
https://itnext.io/an-introduction-to-spring-problem-details-support-38850dcf81e1