package io.github.fort4.bootweb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

	@GetMapping("/api/test")
	public ResponseEntity<String> testConnection() {
	    return ResponseEntity.ok("React 연결 OK!");
	}

	
}
