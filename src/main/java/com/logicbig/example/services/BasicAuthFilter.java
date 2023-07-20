package com.logicbig.example.services;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;

@Provider
public class BasicAuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Basic ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"My Realm\"")
                    .build());
            return;
        }

        String[] credentials = extractCredentials(authorizationHeader);

        if (credentials == null || !isAuthenticated(credentials[0], credentials[1])) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"My Realm\"")
                    .build());
        }
    }

    private String[] extractCredentials(String authorizationHeader) {
        try {
            byte[] decodedBytes = Base64.getDecoder()
                    .decode(authorizationHeader.substring("Basic ".length()).trim());
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
            return decodedString.split(":", 2);
        } catch (Exception e) {
            return null;
        }
    }
    private boolean isAuthenticated(String username, String password) {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/authenticated", "root", "Shahrez7");
            System.out.println("Connected");
            // Prepare the SQL statement to query the users table
            String sql = "SELECT COUNT(*) FROM Users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);


            ResultSet resultSet = statement.executeQuery();


            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}
