# Ship Service

## Overview
Component Service is a Spring Boot microservice responsible for managing component templates.  
Each component is owned by a user, and all operations are locked to that user using Oauth 2.0 authorization.

## API
The service exposes a REST API to create, obtain or change component data.

### Component-controller
| Method | Path           | Description                                                 |
|--------|----------------|-------------------------------------------------------------|
| GET    | /              | Get the list of all components owned by the user.           |
| POST   | /              | Create a new component.                                     |
| GET    | /{componentId} | Get the component with the matching given {componentId}.    |
| PUT    | /{componentId} | Update the component with the matching given {componentId}. |
| DELETE | /{componentId} | Delete the component with the matching given {componentId}. |

## Authentication
User authentication is handled using a JSON Web Token (JWT). The authenticated user ID is obtained form the JWT and used to enforce ownership. Requests without a valid JWT are rejected.

## Database
The database is a PostgreSQL which schema is managed automatically by Hibernate.

## Swagger / OpenAPI
Swagger UI is available at: /swagger-ui/index.html.

## Deployment
The service is part of a cloud-native microservices system deployed on Azure Kubernetes Service (AKS) and integrated via Ingress-NGINX.

### Docker
The service is containerized and published to the GitHub Container Registry (GHCR).

### CI/CD
Continuous integration and continuous deployment (CI/CD) is implemented using GitHub Actions.
#### GitHub Actions pipeline:
* Build
* Test
* Build Docker image
* Push to GHCR
* Trigger infrastructure deployment