package com.github.juanmf.java2plant.goals;

import com.github.juanmf.java2plant.Parser;
import com.github.juanmf.java2plant.render.Filters;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author juanmf@gmail.com
 */
@Mojo(name = "parse")
public class Parse extends AbstractMojo {

    /**
     * The package to parse for Types and Associations
     */
    @Parameter(property = "parse.thePackage", defaultValue = "com.github.juanmf.java2plant.structure")
    private String thePackage;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            System.out.println(Parser.parse(thePackage, Filters.FILTER_ALLOW_ALL));
        } catch (ClassNotFoundException e) {
            throw new MojoExecutionException("Something went wrong", e);
        }
    }
}
