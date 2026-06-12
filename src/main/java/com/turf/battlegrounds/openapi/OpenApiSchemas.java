package com.turf.battlegrounds.openapi;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * OpenAPI schema definitions with example payloads matching actual API responses.
 */
public final class OpenApiSchemas {

    private OpenApiSchemas() {
    }

    @Schema(
            name = "SignupResponse",
            description = "Response for POST /api/auth/signup",
            example = """
                    {
                      "statusCode": 201,
                      "status": "success",
                      "message": "User created",
                      "data": {
                        "id": 1,
                        "name": "johndoe",
                        "email": "john@example.com"
                      }
                    }
                    """
    )
    public static class SignupResponse {
    }

    @Schema(
            name = "LoginResponse",
            description = "Response for POST /api/auth/login",
            example = """
                    {
                      "statusCode": 200,
                      "status": "success",
                      "message": "Authenticated",
                      "data": {
                        "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNzgxMjYxOTUzLCJleHAiOjE3ODEyNjI4NTN9.example",
                        "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNzgxMjYxOTUzLCJleHAiOjE3ODE4NjY3NTMsInR5cGUiOiJyZWZyZXNoIn0.example"
                      }
                    }
                    """
    )
    public static class LoginResponse {
    }

    @Schema(
            name = "RefreshTokenResponse",
            description = "Response for POST /api/auth/refresh",
            example = """
                    {
                      "statusCode": 200,
                      "status": "success",
                      "message": "Token refreshed",
                      "data": {
                        "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNzgxMjYxOTUzLCJleHAiOjE3ODEyNjI4NTN9.example",
                        "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNzgxMjYxOTUzLCJleHAiOjE3ODE4NjY3NTMsInR5cGUiOiJyZWZyZXNoIn0.example"
                      }
                    }
                    """
    )
    public static class RefreshTokenResponse {
    }

    @Schema(
            name = "UserDetailResponse",
            description = "Response for GET /api/v1/users/{id}",
            example = """
                    {
                      "statusCode": 200,
                      "status": "success",
                      "message": "User Detail fetched successfully",
                      "data": {
                        "id": 1,
                        "name": "johndoe",
                        "email": "john@example.com"
                      }
                    }
                    """
    )
    public static class UserDetailResponse {
    }

    @Schema(
            name = "UserListResponse",
            description = "Response for GET /api/v1/users",
            example = """
                    {
                      "statusCode": 200,
                      "status": "success",
                      "message": "User Details Fetched Successfully",
                      "data": [
                        {
                          "id": 1,
                          "name": "johndoe",
                          "email": "john@example.com"
                        }
                      ]
                    }
                    """
    )
    public static class UserListResponse {
    }

    @Schema(
            name = "UserCreateResponse",
            description = "Response for POST /api/v1/users",
            example = """
                    {
                      "statusCode": 201,
                      "status": "success",
                      "message": "User Created successfully",
                      "data": {
                        "id": 2,
                        "name": "johndoe",
                        "email": "john@example.com"
                      }
                    }
                    """
    )
    public static class UserCreateResponse {
    }

    @Schema(
            name = "NotFoundResponse",
            description = "Resource not found",
            example = """
                    {
                      "statusCode": 404,
                      "status": "error",
                      "message": "User Not Found with id: 99",
                      "data": {
                        "timestamp": "2026-06-12T10:30:00.512345678Z",
                        "status": 404,
                        "error": "Not Found",
                        "path": "/api/v1/users/99"
                      }
                    }
                    """
    )
    public static class NotFoundResponse {
    }

    @Schema(
            name = "UnauthorizedResponse",
            description = "Missing or invalid authentication",
            example = """
                    {
                      "statusCode": 401,
                      "status": "error",
                      "message": "Full authentication is required to access this resource",
                      "data": {
                        "timestamp": "2026-06-12T10:30:00.512345678Z",
                        "status": 401,
                        "error": "Unauthorized",
                        "path": "/api/v1/users/1"
                      }
                    }
                    """
    )
    public static class UnauthorizedResponse {
    }

    @Schema(
            name = "ValidationErrorResponse",
            description = "Request validation failed",
            example = """
                    {
                      "statusCode": 422,
                      "status": "error",
                      "message": "username: Username must be between 3 and 20 characters; password: Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character",
                      "data": {
                        "timestamp": "2026-06-12T10:30:00.512345678Z",
                        "status": 422,
                        "error": "Unprocessable Entity",
                        "path": "/api/auth/signup"
                      }
                    }
                    """
    )
    public static class ValidationErrorResponse {
    }

    @Schema(
            name = "InvalidCredentialsResponse",
            description = "Invalid login credentials",
            example = """
                    {
                      "statusCode": 401,
                      "status": "error",
                      "message": "Bad credentials",
                      "data": {
                        "timestamp": "2026-06-12T10:30:00.512345678Z",
                        "status": 401,
                        "error": "Unauthorized",
                        "path": "/api/auth/login"
                      }
                    }
                    """
    )
    public static class InvalidCredentialsResponse {
    }

    @Schema(
            name = "InvalidRefreshTokenResponse",
            description = "Response for invalid refresh token",
            example = """
                    {
                      "statusCode": 401,
                      "status": "error",
                      "message": "Invalid refresh token",
                      "data": null
                    }
                    """
    )
    public static class InvalidRefreshTokenResponse {
    }
}
