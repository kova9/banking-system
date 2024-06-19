package com.bankingSystem.bankingSystem.resource;

import com.bankingSystem.bankingSystem.provider.MainProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MainResource {
    private final MainProvider provider;


}
