package indi.wgx.controller;

import indi.wgx.service.ExplainService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sentAllData")
public class ExplainController {


    ExplainService explainService;


}
