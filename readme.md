
```http://localhost:8081/v1/calendar```

```docker run -d --name rso-bookingsdb -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=bookings -p 5435:5432 --network rso2021 postgres:13```

https://examples.javacodegeeks.com/enterprise-java/jpa-named-query-example/

PUT doesn't need id, INDENTITY strategy will assign auto increment

CREATE TABLE bookings (
	id serial PRIMARY KEY,
	customerid integer NOT NULL,
	workerid integer NOT NULL,
	t_from bigint NOT NULL,
	t_to bigint NOT NULL
);
