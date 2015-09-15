# idea-jflex

A Plugin for IntelliJ IDEA to enable JFlex support.

This project was previously hosted on <http://code.google.com/p/idea-jflex>

To build, follow the instructions on <http://www.jetbrains.org/intellij/sdk/docs/index.html> 
to get and configure the IntelliJ IDEA Community Edition source.  Checkout the "nika" branch,
which corresponds to build number 111, from which IntelliJ IDEA 11.0 was released.
  
Open the project in IntelliJ.  Set the project JDK to 1.6, not 1.8 as mentioned in the
above-linked docs.

Import idea-jflex as a module using existing sources.

Make/rebuild the project, then right click on the root of the idea-jflex module and select
"Prepare Plugin Module 'idea-jflex' For Deployment".  The plugin archive (named either 
"idea-jflex.jar" or "idea-jflex.zip", depending on whether there are any bundled dependencies)
will be built and then saved in the module's root directory.

To load the plugin into IntelliJ IDEA, copy the built archive to the config/plugins/
directory under your $HOME/.IntelliJIDEA<X>/ directory (where <X> is the IntelliJ IDEA
major version), and then restart IntelliJ IDEA. 

See <http://www.jetbrains.org/intellij/sdk/docs/basics/getting_started/publishing_plugin.html>
for instructions on publishing the plugin to the IntelliJ IDEA plugin repository.