package net.alloyggp.griddle.grammar;

public interface GdlVisitable {
    void accept(GdlVisitor visitor);
}
