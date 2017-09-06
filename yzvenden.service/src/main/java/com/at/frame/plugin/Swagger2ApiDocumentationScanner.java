package com.at.frame.plugin;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.PathProvider;
import springfox.documentation.builders.DocumentationBuilder;
import springfox.documentation.builders.ResourceListingBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.RequestHandlerProvider;
import springfox.documentation.spi.service.contexts.Defaults;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.paths.PathMappingAdjuster;
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.scanners.*;

import javax.servlet.ServletContext;
import java.util.*;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Sets.newTreeSet;
import static springfox.documentation.service.Tags.toTags;
import static springfox.documentation.spi.service.contexts.Orderings.listingReferencePathComparator;

/**
 * Created by Administrator on 2017/6/22.
 */
@Primary
@Component
@ConditionalOnProperty(prefix = "at.frame" ,name = "swagger" ,havingValue = "true")
public class Swagger2ApiDocumentationScanner extends ApiDocumentationScanner {

    private ApiListingReferenceScanner apiListingReferenceScanner;
    private ApiListingScanner apiListingScanner;
    public static final List<String[]> GROUPS = new ArrayList<>();

    @Autowired
    public Swagger2ApiDocumentationScanner(ApiListingReferenceScanner apiListingReferenceScanner, ApiListingScanner apiListingScanner) {
        super(apiListingReferenceScanner, apiListingScanner);
        this.apiListingReferenceScanner = apiListingReferenceScanner;
        this.apiListingScanner = apiListingScanner;
    }

    public void scanSplit(DocumentationContext context,DocumentationCache cached) {
        ApiListingReferenceScanResult result = apiListingReferenceScanner.scan(context);
        ApiListingScanningContext listingContext = new ApiListingScanningContext(context,
                result.getResourceGroupRequestMappings());

        Multimap<String, ApiListing> apiListings = apiListingScanner.scan(listingContext);
        Set<Tag> tags = toTags(apiListings);
        tags.addAll(context.getTags());

        Set<ApiListingReference> apiReferenceSet = newTreeSet(listingReferencePathComparator());
        apiReferenceSet.addAll(apiListingReferences(apiListings, context));

        ImmutableList<ApiListingReference> apiListingReferences = from(apiReferenceSet).toSortedList(context.getListingReferenceOrdering());

        Map<String, Collection<ApiListing>> apisMap = apiListings.asMap();
        apisMap.forEach((groupName,apis)->{
            Multimap<String, ApiListing> map = LinkedListMultimap.create();
            for(ApiListing api : apis){
                map.put(groupName,api);
                GROUPS.add(new String[] {groupName,api.getDescription()});
            }
            DocumentationBuilder group = new DocumentationBuilder()
                    .name(groupName)
                    .apiListingsByResourceGroupName(map)
                    .produces(context.getProduces())
                    .consumes(context.getConsumes())
                    .host(context.getHost())
                    .schemes(context.getProtocols())
                    .basePath(context.getPathProvider().getApplicationBasePath())
                    .tags(tags);

            for(int i = 0, s = apiListingReferences.size(); i < s; i++){
                ApiListingReference ref = apiListingReferences.get(i);
                if(ref.getPath().indexOf(groupName) != -1){
                    List list = new ArrayList<>(1);
                    list.add(ref);
                    ResourceListing resourceListing = new ResourceListingBuilder()
                            .apiVersion(context.getApiInfo().getVersion())
                            .apis(list)
                            .securitySchemes(context.getSecuritySchemes())
                            .info(context.getApiInfo())
                            .build();
                    group.resourceListing(resourceListing);
                    cached.addDocumentation(group.build());
                    break;
                }
            }
        });
    }

    private Collection<? extends ApiListingReference> apiListingReferences(Multimap<String, ApiListing> apiListings,
                                                                           DocumentationContext context) {
        Map<String, Collection<ApiListing>> grouped = Multimaps.asMap(apiListings);
        return FluentIterable.from(grouped.entrySet()).transform(toApiListingReference(context)).toSet();
    }
    private Function<Map.Entry<String, Collection<ApiListing>>, ApiListingReference> toApiListingReference(final DocumentationContext context) {
        return new Function<Map.Entry<String, Collection<ApiListing>>, ApiListingReference>() {
            @Override
            public ApiListingReference apply(Map.Entry<String, Collection<ApiListing>> input) {
                String description = Joiner.on(System.getProperty("line.separator"))
                        .join(descriptions(input.getValue()));
                PathAdjuster adjuster = new PathMappingAdjuster(context);
                PathProvider pathProvider = context.getPathProvider();
                String path = pathProvider.getResourceListingPath(context.getGroupName(), input.getKey());
                return new ApiListingReference(adjuster.adjustedPath(path), description, 0);
            }
        };
    }

    private Iterable<String> descriptions(Collection<ApiListing> apiListings) {
        return FluentIterable.from(apiListings).transform(toDescription());
    }

    private Function<ApiListing, String> toDescription() {
        return new Function<ApiListing, String>() {
            @Override
            public String apply(ApiListing input) {
                return input.getDescription();
            }
        };
    }

}
