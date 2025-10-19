package com.mvc.framework.servlet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour FrontServlet
 * 
 * @author MVC Framework Team
 * @version 1.0.0
 */
@RunWith(JUnit4.class)
public class FrontServletTest {
    
    private FrontServlet frontServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private HttpSession mockSession;
    private ServletConfig mockServletConfig;
    private ServletContext mockServletContext;
    private StringWriter responseWriter;
    private PrintWriter printWriter;
    
    @Before
    public void setUp() throws IOException, ServletException {
        frontServlet = new FrontServlet();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockSession = mock(HttpSession.class);
        mockServletConfig = mock(ServletConfig.class);
        mockServletContext = mock(ServletContext.class);
        
        responseWriter = new StringWriter();
        printWriter = new PrintWriter(responseWriter);
        
        when(mockResponse.getWriter()).thenReturn(printWriter);
        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockServletConfig.getServletContext()).thenReturn(mockServletContext);
        when(mockServletConfig.getServletName()).thenReturn("FrontServlet");
        
        // Initialiser le servlet avec la configuration mock
        frontServlet.init(mockServletConfig);
    }
    
    @Test
    public void testDoGet() throws ServletException, IOException {
        // Arrange
        setupMockRequest("GET", "/test", "param1=value1");
        
        // Act
        frontServlet.doGet(mockRequest, mockResponse);
        
        // Assert
        verify(mockResponse).setContentType("text/html;charset=UTF-8");
        verify(mockResponse).setCharacterEncoding("UTF-8");
        
        String response = responseWriter.toString();
        assertTrue("La réponse doit contenir le titre du framework", 
                   response.contains("MVC Framework"));
        assertTrue("La réponse doit afficher la méthode GET", 
                   response.contains("GET"));
    }
    
    @Test
    public void testDoPost() throws ServletException, IOException {
        // Arrange
        setupMockRequest("POST", "/submit", null);
        
        // Act
        frontServlet.doPost(mockRequest, mockResponse);
        
        // Assert
        verify(mockResponse).setContentType("text/html;charset=UTF-8");
        
        String response = responseWriter.toString();
        assertTrue("La réponse doit afficher la méthode POST", 
                   response.contains("POST"));
    }
    
    @Test
    public void testDoPut() throws ServletException, IOException {
        // Arrange
        setupMockRequest("PUT", "/update", null);
        
        // Act
        frontServlet.doPut(mockRequest, mockResponse);
        
        // Assert
        String response = responseWriter.toString();
        assertTrue("La réponse doit afficher la méthode PUT", 
                   response.contains("PUT"));
    }
    
    @Test
    public void testDoDelete() throws ServletException, IOException {
        // Arrange
        setupMockRequest("DELETE", "/delete", null);
        
        // Act
        frontServlet.doDelete(mockRequest, mockResponse);
        
        // Assert
        String response = responseWriter.toString();
        assertTrue("La réponse doit afficher la méthode DELETE", 
                   response.contains("DELETE"));
    }
    
    @Test
    public void testRequestWithParameters() throws ServletException, IOException {
        // Arrange
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put("name", new String[]{"John"});
        parameters.put("age", new String[]{"25"});
        
        when(mockRequest.getParameterMap()).thenReturn(parameters);
        setupMockRequest("GET", "/test", "name=John&age=25");
        
        // Act
        frontServlet.doGet(mockRequest, mockResponse);
        
        // Assert
        String response = responseWriter.toString();
        assertTrue("La réponse doit contenir le paramètre name", 
                   response.contains("name"));
        assertTrue("La réponse doit contenir la valeur John", 
                   response.contains("John"));
    }
    
    @Test
    public void testRequestWithHeaders() throws ServletException, IOException {
        // Arrange
        setupMockRequest("GET", "/test", null);
        
        Enumeration<String> headerNames = Collections.enumeration(
            java.util.Arrays.asList("User-Agent", "Accept")
        );
        when(mockRequest.getHeaderNames()).thenReturn(headerNames);
        when(mockRequest.getHeader("User-Agent")).thenReturn("Mozilla/5.0");
        when(mockRequest.getHeader("Accept")).thenReturn("text/html");
        
        // Act
        frontServlet.doGet(mockRequest, mockResponse);
        
        // Assert
        String response = responseWriter.toString();
        assertTrue("La réponse doit contenir les headers", 
                   response.contains("User-Agent"));
        assertTrue("La réponse doit contenir la valeur du User-Agent", 
                   response.contains("Mozilla/5.0"));
    }
    
    private void setupMockRequest(String method, String uri, String queryString) {
        when(mockRequest.getMethod()).thenReturn(method);
        when(mockRequest.getRequestURI()).thenReturn(uri);
        when(mockRequest.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080" + uri));
        when(mockRequest.getQueryString()).thenReturn(queryString);
        when(mockRequest.getProtocol()).thenReturn("HTTP/1.1");
        when(mockRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(mockRequest.getParameterMap()).thenReturn(new HashMap<>());
        when(mockRequest.getHeaderNames()).thenReturn(Collections.enumeration(Collections.emptyList()));
        
        when(mockSession.getId()).thenReturn("TEST_SESSION_ID");
        when(mockSession.isNew()).thenReturn(true);
    }
}