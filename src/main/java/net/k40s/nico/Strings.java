package net.k40s.nico;

/**
 * Error / Info - messages and constants.
 *
 * @author Nicolai Süper (nico@k40s.net)
 */
class Strings {
    static final String EMPTY_DIR = "Omfg, directory is empty: ";
    static final String UNABLE_TO_BUILD = "So it seems like you were unable to build stuff." +
            " Or something strange happened and the files are gone." +
            " I don't know if this message is satisfying, but I don't really care about it either.";
    static final String CAN_T_FIND_PROJECT_ROOT = "Couldn't determine Dart project root.";
    static final String ERROR_CLEANING_UP = "Error cleaning up target directory." +
            " Do you have the permissions to write to it?";
    static final String MULTIPLE_PUBSPECS = "Unable to determine project root, because more than one pubspec.yaml was found." +
            " Please specify your project root dir.";
    static final String DEFAULT_TARGET = "src/main/resources/static";
}
