package Implementations.WebServer;

import java.io.*;
import java.nio.charset.StandardCharsets;

import Implementations.ClassicalCiphers.CaesarCipherA.CaesarCipherA;
import Implementations.ClassicalCiphers.CaesarCipherB.CaesarCipherB;
import Implementations.ClassicalCiphers.Playfair.Playfair;
import Implementations.ClassicalCiphers.Vigenere.Vigenere;
import Implementations.Hashing.Database;
import com.sun.net.httpserver.*;

import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;

import static Implementations.WebServer.TwoFA.getTOTPCode;

public class WebServer {
    private static String secretKey = "NM7W4SSFMK3XOWQM72LJP3NGC3TBIBLF";
    private static boolean sessionLoggedIn = false;

    public static void main(String[] args) {
        try {
            Database.getData("basicusername1", "somepassword");
            Database.getData("basicusername2", "somepassword2");
            Database.getData("thisisconfusing", "ilovepasswords");

            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/login", new logIn());
            server.createContext("/logout", new logOut());
            server.createContext("/caesar1", new caesarA());
            server.createContext("/caesar2", new caesarB());
            server.createContext("/playfair" , new playfair());
            server.createContext("/vigenere", new vigenere());

            server.setExecutor(null);
            server.start();
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static class caesarA implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (sessionLoggedIn){
                if ("POST".equals(exchange.getRequestMethod())) {
                    String response = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                            .lines().collect(Collectors.joining("\n"));
                    String[] responseArray = response.split("&");
                    String plaintext = responseArray[0].split("=")[1];
                    String key = responseArray[1].split("=")[1];
                    int keyInt = Integer.parseInt(key);
                    CaesarCipherA caesarCipherA = new CaesarCipherA(keyInt);
                    String ciphertext = caesarCipherA.encryptMessage(plaintext);
                    String htmlResponse = "<html><body><h1>Caesar Cipher A</h1><p>Plaintext: " + plaintext + "</p><p>Key: " + key + "</p><p>Ciphertext: " + ciphertext + "</p></body></html>";
                    exchange.sendResponseHeaders(200, htmlResponse.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(htmlResponse.getBytes());
                    os.close();
                } else {
                    String htmlResponse = "<html><body><h1>Caesar Cipher A</h1><form method=\"post\" action=\"/caesar1\"><input type=\"text\" name=\"plaintext\" placeholder=\"Plaintext\"><input type=\"text\" name=\"key\" placeholder=\"Key\"><input type=\"submit\" value=\"Encrypt\"></form></body></html>";
                    exchange.sendResponseHeaders(200, htmlResponse.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(htmlResponse.getBytes());
                    os.close();
                }
            } else {
                byte[] response = ("<h1>Not logged in</h1>").getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(302, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
            }
        }
    }

    public static class caesarB implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (sessionLoggedIn){
                if ("POST".equals(exchange.getRequestMethod())) {
                    String response = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                            .lines().collect(Collectors.joining("\n"));
                    String[] responseArray = response.split("&");
                    String plaintext = responseArray[0].split("=")[1];
                    String key = responseArray[1].split("=")[1];
                    String permKey = responseArray[2].split("=")[1];
                    int keyInt = Integer.parseInt(key);
                    CaesarCipherB caesarCipherB = new CaesarCipherB(keyInt, permKey);
                    String ciphertext = caesarCipherB.encryptMessage(plaintext);
                    String htmlResponse = "<html><body><h1>Caesar Cipher B</h1><p>Plaintext: " + plaintext + "</p><p>Key: " + key + "</p><p>Permutation Key: " + permKey + "</p><p>Ciphertext: " + ciphertext + "</p></body></html>";
                    exchange.sendResponseHeaders(200, htmlResponse.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(htmlResponse.getBytes());
                    os.close();
                } else {
                    String htmlResponse = "<html><body><h1>Caesar Cipher B</h1><form method=\"post\" action=\"/caesar2\"><input type=\"text\" name=\"plaintext\" placeholder=\"Plaintext\"><input type=\"text\" name=\"key\" placeholder=\"Key\"><input type=\"text\" name=\"permKey\" placeholder=\"Permutation Key\"><input type=\"submit\" value=\"Encrypt\"></form></body></html>";
                    exchange.sendResponseHeaders(200, htmlResponse.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(htmlResponse.getBytes());
                    os.close();
                }
            } else {
                byte[] response = ("<h1>Not logged in</h1>").getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(302, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
            }
        }
    }

    public static class playfair implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (sessionLoggedIn){
                if ("POST".equals(exchange.getRequestMethod())){
                    String response = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                            .lines().collect(Collectors.joining("\n"));
                    String[] responseArray = response.split("&");
                    String plaintext = responseArray[0].split("=")[1];
                    String permkey = responseArray[1].split("=")[1];
                    Playfair playfair = new Playfair(permkey);
                    String ciphertext = playfair.encryptMessage(plaintext);
                    String htmlResponse = "<html><body><h1>Playfair Cipher</h1><p>Plaintext: " + plaintext + "</p><p>Permutation Key: " + permkey + "</p><p>Ciphertext: " + ciphertext + "</p></body></html>";
                    exchange.sendResponseHeaders(200, htmlResponse.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(htmlResponse.getBytes());
                    os.close();
                } else {
                    String htmlResponse = "<html><body><h1>Playfair Cipher</h1><form method=\"post\" action=\"/playfair\"><input type=\"text\" name=\"plaintext\" placeholder=\"Plaintext\"><input type=\"text\" name=\"permkey\" placeholder=\"Permutation Key\"><input type=\"submit\" value=\"Encrypt\"></form></body></html>";
                    exchange.sendResponseHeaders(200, htmlResponse.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(htmlResponse.getBytes());
                    os.close();
                }
            } else {
                byte[] response = ("<h1>Not logged in</h1>").getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(302, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
            }
        }
    }

    public static class vigenere implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (sessionLoggedIn){
                if ("POST".equals(exchange.getRequestMethod())){
                    String response = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                            .lines().collect(Collectors.joining("\n"));
                    String[] responseArray = response.split("&");
                    String plaintext = responseArray[0].split("=")[1];
                    String permKey = responseArray[1].split("=")[1];
                    Vigenere vigenere = new Vigenere(permKey);
                    vigenere.extendKey(plaintext);
                    String ciphertext = vigenere.encryptMessage(plaintext);
                    String htmlResponse = "<html><body><h1>Vigenere Cipher</h1><p>Plaintext: " + plaintext + "</p><p>Permutation Key: " + permKey + "</p><p>Ciphertext: " + ciphertext + "</p></body></html>";
                    exchange.sendResponseHeaders(200, htmlResponse.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(htmlResponse.getBytes());
                    os.close();
                } else {
                    String htmlResponse = "<html><body><h1>Vigenere Cipher</h1><form method=\"post\" action=\"/vigenere\"><input type=\"text\" name=\"plaintext\" placeholder=\"Plaintext\"><input type=\"text\" name=\"permKey\" placeholder=\"Permutation Key\"><input type=\"submit\" value=\"Encrypt\"></form></body></html>";
                    exchange.sendResponseHeaders(200, htmlResponse.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(htmlResponse.getBytes());
                    os.close();
                }
            } else {
                byte[] response = ("<h1>Not logged in</h1>").getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(302, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
            }
        }
    }


    public static class logOut implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if(sessionLoggedIn){
                byte[] response = ("<html><body><h1>You have been logged out!</h1></body></html>").getBytes(StandardCharsets.UTF_8);
                sessionLoggedIn = false;

                exchange.sendResponseHeaders(200, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();

            } else {
                byte[] response =  ("<html><body><h1>You are not logged in!</h1></body></html>").getBytes(StandardCharsets.UTF_8);

                exchange.sendResponseHeaders(200, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
            }
        }
    }

    public static class logIn implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!sessionLoggedIn) {
                byte[] response = ("<html><body><form action=\"/login\" method=\"post\">"
                        + "Username: <input type=\"text\" name=\"username\"><br>"
                        + "Password: <input type=\"password\" name=\"password\"><br>"
                        + "2FA Code: <input type=\"text\" name=\"2fa\"><br>"
                        + "<input type=\"submit\" value=\"Submit\">"
                        + "</form></body></html>").getBytes(StandardCharsets.UTF_8);

                if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                    String[] input = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                            .lines().collect(Collectors.joining("\n")).split("&");

                    String username = input[0].split("=")[1];
                    String password = input[1].split("=")[1];
                    String twoFACode = input[2].split("=")[1];

                    System.out.println("Username: " + username + ", Password: " + password + ", 2FA Code: " + twoFACode);

                    try {
                        if (Database.authenticate(username, password)) {
                            if (getTOTPCode(secretKey).equals(twoFACode)) {
                                sessionLoggedIn = true;
                                response = ("<html><body><h1>Logged in!</h1></body></html>").getBytes(StandardCharsets.UTF_8);
                            } else {
                                response = ("<html><body><h1>2FA code is incorrect!</h1></body></html>").getBytes(StandardCharsets.UTF_8);
                            }
                        } else {
                            response = ("<html><body><h1>Username or password is incorrect!</h1></body></html>").getBytes(StandardCharsets.UTF_8);
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
                exchange.sendResponseHeaders(200, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
            } else {
                byte[] response = ("<html><body><h1>Already logged in!</h1></body></html>").getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
            }
        }
    }
}