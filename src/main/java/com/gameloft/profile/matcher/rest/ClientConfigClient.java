package com.gameloft.profile.matcher.rest;

import com.gameloft.profile.matcher.api.GetClientConfigApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "clientConfigClient", url = "${client.config.api.url}")
public interface ClientConfigClient extends GetClientConfigApi {
}
