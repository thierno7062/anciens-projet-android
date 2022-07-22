package com.gtemate.petiteannoncekmer.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by admin on 29/01/2017.
 */
@Controller
public class AngularJsForwardController {
    @RequestMapping(value = "/**/{[path:[^\\.]*}")
    public String redirect() {
        return "forward:/";
    }
}
