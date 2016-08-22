package com.github.juanmf.java2plant.goal;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.juanmf.java2plant.render.filters.ChainFilter;
import com.github.juanmf.java2plant.render.filters.Filter;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import com.github.juanmf.java2plant.Parser;

import static com.github.juanmf.java2plant.render.filters.Filters.CHAIN_CLASSES_CUSTOM_NAME;
import static com.github.juanmf.java2plant.render.filters.Filters.CHAIN_RELATIONS_CUSTOM_NAME;
import static com.github.juanmf.java2plant.render.filters.Filters.CHAIN_RELATION_TYPE_CUSTOM_NAME;
import static com.github.juanmf.java2plant.render.filters.Filters.FILTERS;

/**
 * Usages:
 * <pre>
 * mvn   -Dparse.thePackage="com.my.package" clean compile java2PlantUML:parse
 * mvn   -Dparse.thePackage="com.my.package, com.other.package" clean compile java2PlantUML:parse
 * mvn   -Dparse.thePackage="com.my.package, com.other.Class" clean compile java2PlantUML:parse
 * </pre>
 * 
 * Usage of Filters:
 * Default Filters are:
 * <pre>
 *     "parse.relationTypeFilter" = "FILTER_CHAIN_RELATION_TYPE_STANDARD"
 *     "parse.classesFilter" = "FILTER_CHAIN_CLASSES_STANDARD"
 *     "parse.relationsFilter" = "FILTER_CHAIN_RELATION_STANDARD"
 * </pre>
 * To change them use:
 * <pre>
 * mvn   -Dparse.thePackage="p1" -Dparse.classesFilter="FILTER_FORBID_ANONIMOUS" clean compile java2PlantUML:parse
 * mvn   -Dparse.thePackage="p1" -Dparse.relationTypeFilter="FILTER_FORBID_USES" clean compile java2PlantUML:parse
 * mvn   -Dparse.thePackage="p1" -Dparse.relationsFilter="FILTER_RELATION_FORBID_TO_PRIMITIVE" clean compile java2PlantUML:parse
 * </pre>
 * 
 * Available filter names and types are defined in {@link com.github.juanmf.java2plant.render.filters.Filters#FILTERS} Map
 * Read {@link com.github.juanmf.java2plant.render.filters.Filters} javadoc to understand the three types of filters
 * yuio can use.
 * 
 * Custom filters usage:
 * You can use a custom chain filter for each of the three types of filters, combining any of the exisitng filters
 * without the need to code.
 * <pre>
 *  mvn -Dparse.thePackage="p1" \
 *      -Dparse.classesFilter="FILTER_CHAIN_CLASSES_CUSTOM" \
 *      -Dparse.customClassesFilter="FILTER_FORBID_ANONIMOUS,FILTER_FORBID_PRIMITIVES" \
 *      clean compile java2PlantUML:parse
 *
 *  mvn -Dparse.thePackage="p1" \
 *      -Dparse.relationTypeFilter="FILTER_CHAIN_RELATION_TYPE_CUSTOM" \
 *      -Dparse.customRelationTypeFilters="FILTER_FORBID_USES,FILTER_FORBID_AGGREGATION" \
 *      clean compile java2PlantUML:parse
 *
 *  mvn -Dparse.thePackage="p1" \
 *      -Dparse.relationsFilter="FILTER_CHAIN_RELATION_TYPE_CUSTOM" \
 *      -Dparse.customRelationsFilter="FILTER_RELATION_FORBID_TO_PRIMITIVE,FILTER_RELATION_FORBID_FROM_ANONIMOUS" \
 *      clean compile java2PlantUML:parse
 * </pre>
 *
 * Of course you can use all three cusom chain filters un a single run.
 *
 * @author juanmf@gmail.com
 */
@Mojo(name = "parse",
        requiresDependencyResolution = ResolutionScope.COMPILE)
public class Parse extends AbstractMojo {

    /**
     * The package to parse for Types and Associations
     */
    @Parameter(property = "parse.thePackage", defaultValue = "com.github.juanmf.java2plant.structure")
    private String thePackage;

    /**
     * The package to parse for Types and Associations
     */
    @Parameter(property = "parse.relationTypeFilter", defaultValue = "FILTER_CHAIN_RELATION_TYPE_STANDARD")
    private String relationTypeFilter;

    @Parameter(property = "parse.classesFilter", defaultValue = "FILTER_CHAIN_CLASSES_STANDARD")
    private String classesFilter;

    @Parameter(property = "parse.relationsFilter", defaultValue = "FILTER_CHAIN_RELATION_STANDARD")
    private String relationsFilter;

    /**
     * The package to parse for Types and Associations
     */
    @Parameter(property = "parse.customRelationTypeFilter")
    private String customRelationTypeFilter;

    @Parameter(property = "parse.customClassesFilter")
    private String customClassesFilter;

    @Parameter(property = "parse.customRelationsFilter")
    private String customRelationsFilter;

    @Component
    private MavenProject project;

    /**
     * Send thePackage and a convenient fixed set of filters to Parser and outputs Parser's result
     *
     * Todo: allow filters to be set from CLI
     *
     * <code>
     * // A very permisive filter configuration:
     * getLog().info("Following is the PlantUML src: \n" + Parser.parse(
     *         thePackage, Filters.FILTER_ALLOW_ALL_RELATIONS, Filters.FILTER_ALLOW_ALL_CLASSES, loader,
     *         Filters.FILTER_RELATION_ALLOW_ALL));

     * </code>
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    @SuppressWarnings("unchecked")
    public void execute() throws MojoExecutionException, MojoFailureException {
        filterParamsSanityCheck();
        prepareCustomFilters();
        try {
            URLClassLoader loader = getLoader();
            getLog().debug("loader URLs: " + Arrays.toString(loader.getURLs()));

            getLog().info("Following is the PlantUML src: \n" + Parser.parse(
                    thePackage, FILTERS.get(this.relationTypeFilter), FILTERS.get(this.classesFilter),
                    FILTERS.get(this.relationsFilter), loader));

        } catch (DependencyResolutionRequiredException e) {
            throw new MojoExecutionException("Something went wrong", e);
        }
    }

    private void prepareCustomFilters() throws MojoExecutionException {
        if (CHAIN_RELATION_TYPE_CUSTOM_NAME.equals(relationTypeFilter)) {
            if (null == customRelationTypeFilter) {
                throw new MojoExecutionException("Need to set parse.customRelationTypeFilter!");
            }
            prepareRelationTypeFilter();
        }
        if (CHAIN_RELATIONS_CUSTOM_NAME.equals(relationsFilter)) {
            if (null == customRelationsFilter) {
                throw new MojoExecutionException("Need to set parse.customRelationsFilter!");
            }
            prepareRelationsFilter();
        }
        if (CHAIN_CLASSES_CUSTOM_NAME.equals(classesFilter)) {
            if (null == customClassesFilter) {
                throw new MojoExecutionException("Need to set parse.customClassesFilter!");
            }
            prepareClassesFilter();
        }
    }

    private void prepareClassesFilter()  throws MojoExecutionException {
        ChainFilter classesFilter = (ChainFilter) FILTERS.get(CHAIN_CLASSES_CUSTOM_NAME);
        loadFilters(classesFilter, customClassesFilter);
    }

    private void prepareRelationsFilter() throws MojoExecutionException {
        ChainFilter relationFilter = (ChainFilter) FILTERS.get(CHAIN_RELATIONS_CUSTOM_NAME);
        loadFilters(relationFilter, customRelationsFilter);

    }

    private void prepareRelationTypeFilter() throws MojoExecutionException {
        ChainFilter relTypeFilter = (ChainFilter) FILTERS.get(CHAIN_RELATION_TYPE_CUSTOM_NAME);
        loadFilters(relTypeFilter, customRelationTypeFilter);
    }

    private void loadFilters(ChainFilter filter, String FilterNames) throws MojoExecutionException {
        for (String filterName : customClassesFilter.split("\\s*,\\s*")) {
            filterExists(filterName);
            // Could explode if wrong filterType
            filter.addFilter(FILTERS.get(filterName));
        }
    }

    private void filterParamsSanityCheck() throws MojoExecutionException {
        filterExists(this.classesFilter);
        filterExists(this.relationsFilter);
        filterExists(this.relationTypeFilter);
    }

    void filterExists(String filterName) throws MojoExecutionException {
        String message = "Non existent Filter name: %s. Available Filter names: " + FILTERS.toString();
        if (! FILTERS.containsKey(filterName)) {
            throw new MojoExecutionException(String.format(message, filterName));
        }
    }

    /**
     * Fetches all project's class path elements and creates a URLClassLoader to be used by
     * Reflections.
     *
     * @return All class path's URLs in a ClassLoader
     * @throws DependencyResolutionRequiredException
     * @throws MojoExecutionException
     */
    private URLClassLoader getLoader() throws DependencyResolutionRequiredException, MojoExecutionException {
        List<String> classpathElements = null;
        classpathElements = project.getCompileClasspathElements();
        List<URL> projectClasspathList = new ArrayList<>();
        for (String element : classpathElements) {
            try {
                projectClasspathList.add(new File(element).toURI().toURL());
            } catch (MalformedURLException e) {
                throw new MojoExecutionException(element + " is an invalid classpath element", e);
            }
        }
        URLClassLoader loader = new URLClassLoader(projectClasspathList.toArray(new URL[0]));
        return loader;
    }
}
