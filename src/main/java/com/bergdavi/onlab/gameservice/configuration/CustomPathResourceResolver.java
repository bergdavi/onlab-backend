package com.bergdavi.onlab.gameservice.configuration;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * CustomPathResourceResolver
 */
public class CustomPathResourceResolver extends PathResourceResolver {

    @Override
	protected Resource getResource(String resourcePath, Resource location) throws IOException {
        Resource resource = location.createRelative(resourcePath);
        Resource res = verfiyResource(resource, location);
		if(res == null) {
            resource = location.createRelative(resourcePath + ".html");
            res = verfiyResource(resource, location);
        }
        return res;
    }
    
    private Resource verfiyResource(Resource resource, Resource location) throws IOException {
        if (resource.isReadable()) {
			if (checkResource(resource, location)) {
				return resource;
			}
			else if (logger.isWarnEnabled()) {
				Resource[] allowedLocations = getAllowedLocations();
				logger.warn("Resource path was successfully resolved " +
						"but resource \"" +	resource.getURL() + "\" is neither under the " +
						"current location \"" + location.getURL() + "\" nor under any of the " +
						"allowed locations " + (allowedLocations != null ? Arrays.asList(allowedLocations) : "[]"));
			}
		}
		return null;
    }
    
}