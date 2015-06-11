/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.TestConfiguration;
import com.sample.ecommerce.domain.Term;
import java.util.List;
import static junit.framework.TestCase.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfiguration.class)
public class TermServiceTest {

    @Autowired
    private TermService termService;

    @Test
    public void test_suggestByMatch() {
        List<Term> terms = termService.suggest("ipod");
        assertTrue("Only One Tewrm", terms.size() == 1);
    }

    @Test
    public void test_suggestByPrefix() {
        List<Term> terms = termService.suggest("i");
        assertTrue("Multiple Terns", terms.size() > 1);
    }

}
