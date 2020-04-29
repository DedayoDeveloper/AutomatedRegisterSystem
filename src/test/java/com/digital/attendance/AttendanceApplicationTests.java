//package com.digital.attendance;
//
//import com.digital.attendance.model.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.web.server.LocalServerPort;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.runner.RunWith;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class AttendanceApplicationTests {
//    
//    
//
//      @LocalServerPort
//      private int port;
//
//    TestRestTemplate restTemplate = new TestRestTemplate();
//    
//     HttpHeaders headers = new HttpHeaders();
//    
//        @Test
//          public void testLogin() throws Exception{
//
//        HttpEntity<User> entity = new HttpEntity<>(null, headers);
//
//        ResponseEntity<User> response;
//          response = restTemplate.exchange(
//                  
//                  createURLWithPort("/login"), HttpMethod.POST, entity, User.class);
//
//        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
//
//        assertTrue(actual.contains("/login"));
//
//    }    
//          
//     private String createURLWithPort(String uri) {
//       return "http://localhost:" + port + uri;
//    }
//     
//     
//     
//
//}
