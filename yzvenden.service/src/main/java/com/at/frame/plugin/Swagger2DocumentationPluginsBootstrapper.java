package com.at.frame.plugin;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.PathProvider;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.DocumentationBuilder;
import springfox.documentation.builders.ResourceListingBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.DocumentationPlugin;
import springfox.documentation.spi.service.RequestHandlerProvider;
import springfox.documentation.spi.service.contexts.Defaults;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.DocumentationContextBuilder;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.paths.PathMappingAdjuster;
import springfox.documentation.spring.web.plugins.DefaultConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.scanners.*;

import javax.servlet.ServletContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Sets.newTreeSet;
import static springfox.documentation.service.Tags.toTags;
import static springfox.documentation.spi.service.contexts.Orderings.listingReferencePathComparator;
import static springfox.documentation.spi.service.contexts.Orderings.pluginOrdering;

/**
 * Created by Administrator on 2017/6/22.
 */
@Primary
@Component
@ConditionalOnProperty(prefix = "at.frame" ,name = "swagger" ,havingValue = "true")
public class Swagger2DocumentationPluginsBootstrapper extends DocumentationPluginsBootstrapper {

    private static final Logger log = LoggerFactory.getLogger(DocumentationPluginsBootstrapper.class);
    private final DocumentationPluginsManager documentationPluginsManager;
    private final List<RequestHandlerProvider> handlerProviders;
    private final DocumentationCache scanned;
    private final ApiDocumentationScanner resourceListing;
    private final DefaultConfiguration defaultConfiguration;

    private AtomicBoolean initialized = new AtomicBoolean(false);

    @Autowired
    public Swagger2DocumentationPluginsBootstrapper(
            DocumentationPluginsManager documentationPluginsManager,
            List<RequestHandlerProvider> handlerProviders,
            DocumentationCache scanned,
            ApiDocumentationScanner resourceListing,
            TypeResolver typeResolver,
            Defaults defaults,
            ServletContext servletContext) {
        super(documentationPluginsManager,handlerProviders,scanned,resourceListing,typeResolver,defaults,servletContext);
        this.documentationPluginsManager = documentationPluginsManager;
        this.handlerProviders = handlerProviders;
        this.scanned = scanned;
        this.resourceListing = resourceListing;
        this.defaultConfiguration = new DefaultConfiguration(defaults, typeResolver, servletContext);
    }

    private DocumentationContext buildContext(DocumentationPlugin each) {
        return each.configure(defaultContextBuilder(each));
    }

    private void scanDocumentation(DocumentationContext context) {
        scanned.addDocumentation(resourceListing.scan(context));
    }

    private DocumentationContextBuilder defaultContextBuilder(DocumentationPlugin each) {
        DocumentationType documentationType = each.getDocumentationType();
        List<RequestHandler> requestHandlers = from(handlerProviders)
                .transformAndConcat(handlers())
                .toList();
        return documentationPluginsManager
                .createContextBuilder(documentationType, defaultConfiguration)
                .requestHandlers(requestHandlers);
    }

    private Function<RequestHandlerProvider, ? extends Iterable<RequestHandler>> handlers() {
        return new Function<RequestHandlerProvider, Iterable<RequestHandler>>() {
            @Override
            public Iterable<RequestHandler> apply(RequestHandlerProvider input) {
                return input.requestHandlers();
            }
        };
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        callback.run();
    }

    @Override
    public void start() {
        if (initialized.compareAndSet(false, true)) {
            log.info("Context refreshed");
            List<DocumentationPlugin> plugins = pluginOrdering()
                    .sortedCopy(documentationPluginsManager.documentationPlugins());
            log.info("Found {} custom documentation plugin(s)", plugins.size());
            for (DocumentationPlugin each : plugins) {
                DocumentationType documentationType = each.getDocumentationType();
                if (each.isEnabled()) {
                    scanDocumentation(buildContext(each));
                    if(Docket.DEFAULT_GROUP_NAME.equals(each.getGroupName())){
                        ((Swagger2ApiDocumentationScanner)resourceListing).scanSplit(buildContext(each),scanned);
                    }
                } else {
                    log.info("Skipping initializing disabled plugin bean {} v{}",
                            documentationType.getName(), documentationType.getVersion());
                }
            }
        }
    }

    @Override
    public void stop() {
        initialized.getAndSet(false);
        scanned.clear();
    }

    @Override
    public boolean isRunning() {
        return initialized.get();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
