package org.example.Domain.Dtos;

public class RequestDto {
    private String controller;  // Ej: "Auth", "Product"
    private String request;     // Ej: "login", "register"
    private String data;        // JSON de un DTO específico
    private String token;       // Opcional: token de sesión

    public RequestDto() {}

    public RequestDto(String controller, String request, String data, String token) {
        this.controller = controller;
        this.request = request;
        this.data = data;
        this.token = token;
    }

    public String getController() { return controller; }
    public void setController(String controller) { this.controller = controller; }

    public String getRequest() { return request; }
    public void setRequest(String request) { this.request = request; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
