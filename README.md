# GoMaterials - Landscaping Bid Platform

This platform demonstrates my ability to rapidly learn new technologies and deliver robust, scalable software solutions. It showcases my experience as a senior software engineer and my ability to produce production-ready prototypes under tight deadlines.

## Project Goals

* Demonstrate rapid learning of a new technology (Angular) from scratch.
* Showcase senior-level engineering skills, including system design, backend consistency, and scalability.
* Provide GoMaterials with a working prototype illustrating a basic but robust bid process.

## Development Notes

* Completed in **1 week** using only personal time.
* Zero prior experience in Angular; leveraged **15+ years of full-stack development** and AI-assisted design to speed up UI creation.
* Backend built with **robust state management** and horizontal scalability in mind.

## Key Features

### Bid Request & Bid Response Flow

* Focused on the state transitions between bid requests and bid responses.
* Ensured consistency with **Pessimistic Write locking** in MySQL, preventing concurrency issues.
* Designed to scale horizontally with multiple backend instances using **MySQL InnoDB Cluster**.

### User Actions

* **Landscaper**: Create bid requests, award bid responses, complete bid requests, cancel bid requests.
* **Supplier**: Submit bid responses, withdraw, or undo withdrawal.
* For the sake of this prototype, the following actions don't require authentication: create landscaper, create supplier, list bid requests.

### Prototype Scope

* The system prioritizes **robust backend and process flow** over rich UI forms.
* Provides enough functionality to simulate a real bid process for demonstration purposes.

## Architecture

* **Frontend**: Angular app served via Nginx.
* **Backend**: Spring Boot RESTful API.
* **Database**: MySQL InnoDB Cluster.
* **Containerization**: Docker Compose with three containers (frontend, backend, database).

## Running Instance

* [Live Prototype](https://gomaterials.danton.pro/bid-requests)
* User-friendly interface allows selecting a user (landscaper/supplier) to execute actions.

## API Documentation

* [Swagger UI](https://gomaterials.danton.pro/swagger-ui/index.html)

### Authentication

* `POST /api/auth/login` - Authenticate as a landscaper or supplier

### Bid Requests

* `GET /api/v1/bid-requests` - List all bid requests
* `GET /api/v1/bid-requests/{id}` - Retrieve bid request by ID
* `POST /api/v1/bid-requests` - Create new bid request (LANDSCAPER role)
* `POST /api/v1/bid-requests/{bidRequestId}/award/{bidResponseId}` - Award bid response (LANDSCAPER role)
* `POST /api/v1/bid-requests/{bidRequestId}/cancel` - Cancel bid request (LANDSCAPER role)
* `POST /api/v1/bid-requests/{bidRequestId}/complete` - Complete bid request (LANDSCAPER role)

### Bid Responses

* `POST /api/v1/bid-responses` - Submit bid response (SUPPLIER role, `bidRequestId` query)
* `POST /api/v1/bid-responses/{bidResponseId}/withdraw` - Withdraw bid response (SUPPLIER role)
* `POST /api/v1/bid-responses/{bidResponseId}/undo-withdraw` - Undo withdraw (SUPPLIER role)

## Instalation

```sh
# Clone gomaterials_bid git repository
git clone https://github.com/pintode/gomaterials_bid

# Access gomaterials_bid directory
cd gomaterials_bid

# Build docker images
docker compose build
```

## Excecute Docker Compose

```sh
# Start Docker Compose stack
docker compose up

# Stop Docker Compose stack
docker compose down
```
