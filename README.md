# gdl-validation
Standalone GDL validation library

This is a GDL parsing and validation library with minimal dependencies. It is
intended to be a complete solution for static-analysis-based error detection for
GDL games.

This was originally part of the Griddle project, but was split out into its
own project to make publication of the validation code easier, i.e. it is no
longer necessarily tied to a Griddle plugin release.

Errors and warnings are based on the 2008 version of the original Game Description Language specification, as well as my own experiences with writing games and general game players. Direct contradictions of the specification and problems that will reliably cause players to break are generally errors, while gray areas and issues that less commonly cause problems are left as warnings.

Note that several requirements for well-formed games, such as termination and having well-formed goal values, cannot be checked by examination of the game description alone. Files with no reported errors or warnings may still be invalid games or contain bugs. The Validator application in the GGP-Base library can check for these errors, though by their nature these types of errors can escape detection.

Authorship
==========

The gdl-validation library is written and maintained by Alex Landau. Email may be directed to griddle@alloyggp.net, but consider first if the "Issues" section of the Github repository is more applicable.