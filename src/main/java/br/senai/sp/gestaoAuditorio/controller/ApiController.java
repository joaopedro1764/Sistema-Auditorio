package br.senai.sp.gestaoAuditorio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApiController {

	@RequestMapping("apiInsta")
	public String apiInsta() {
		return "Interface/apiInsta";
	}
}
