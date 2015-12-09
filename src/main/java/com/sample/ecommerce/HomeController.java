package com.sample.ecommerce;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zols.datatore.exception.DataStoreException;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(Model model,
            HttpServletRequest request,
            Pageable pageable) throws IOException, DataStoreException {
        return "index";
    }

}
