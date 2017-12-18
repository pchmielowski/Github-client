package net.chmielowski.github;

/**
 * Field annotated with this annotation has to be repositoryFromCache in Realm.
 * TODO: implement engine to detect fields breaking this rule.
 */
public @interface Cached {
}
