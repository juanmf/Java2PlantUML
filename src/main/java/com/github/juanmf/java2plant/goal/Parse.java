package com.github.juanmf.java2plant.goal;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.github.juanmf.java2plant.render.filters.Filters;

/**
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
    @Parameter(property = "parse.filter", defaultValue = "com.github.juanmf.java2plant.structure")
    private String filter;

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

        try {
            URLClassLoader loader = getLoader();
            getLog().debug("loader URLs: " + Arrays.toString(loader.getURLs()));

            getLog().info("Following is the PlantUML src: \n" + Parser.parse(
                    thePackage, Filters.FILTER_ALLOW_ALL_RELATIONS, Filters.FILTER_FORBID_ANONIMOUS, loader,
                    Filters.FILTER_CHAIN_RELATION_STANDARD));

        } catch (DependencyResolutionRequiredException e) {
            throw new MojoExecutionException("Something went wrong", e);
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
