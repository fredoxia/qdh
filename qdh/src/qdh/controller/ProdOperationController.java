package qdh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prodOptController")
public class ProdOperationController {
	
	@RequestMapping("/HQProdMgmt")
	public String HQProdMgmt() {
		
		return"/jsp/hq/hqAdmin/ProdMgmt.jsp";
	}
}
