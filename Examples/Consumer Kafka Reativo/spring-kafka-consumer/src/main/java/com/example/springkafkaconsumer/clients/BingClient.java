package com.example.springkafkaconsumer.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "https://api.bing.microsoft.com/v7.0/", name = "bing")
public interface BingClient {

    @GetMapping(value = "search?q=animal {word}", headers = {"Ocp-Apim-Subscription-Key=822efdff97a949c6b6b676f397ebb3ac"})
    String searchWord(@PathVariable("word") String word);
}
