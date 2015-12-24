package net.alloyggp.griddle.grammar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java_cup.runtime.ComplexSymbolFactory.Location;
import net.alloyggp.griddle.Position;

public class Sentence implements GdlVisitable {
    private final String name;
    //Body is nullable; a null body indicates the sentence was not in parentheses.
    private final List<Term> body;

    private final Position namePosition;
    private final Position position;

    private Sentence(String name, int nameLeft, int nameRight, int nameLine, List<Term> body, int left, int right, int line) {
        if (name == null) {
            throw new NullPointerException();
        }
        this.name = name;
        this.body = body;

        this.namePosition = new Position(nameLeft, nameRight, nameLine);
        this.position = new Position(left, right, line);
    }

    public static Sentence create(String name, Location nameLeft, Location nameRight, List<Term> body, Location left, Location right) {
        if (body == null) {
            throw new NullPointerException();
        }
        return new Sentence(name, nameLeft.getOffset(), nameRight.getOffset(), nameLeft.getLine(),
                Collections.unmodifiableList(new ArrayList<Term>(body)),
                left.getOffset(), right.getOffset(), left.getLine());
    }

    public static Sentence create(String name, Location left, Location right) {
        return new Sentence(name, left.getOffset(), right.getOffset(), left.getLine(),
                null, left.getOffset(), right.getOffset(), left.getLine());
    }

    public String getName() {
        return name;
    }

    /**
     * If the sentence did not have parentheses, getBody() will return null.
     */
    public List<Term> getBodyNullable() {
        return body;
    }

    /**
     * Unlike getBodyNullable(), this returns an empty list if the body is null.
     */
    public List<Term> getBody() {
        if (body == null) {
            return Collections.emptyList();
        }
        return body;
    }

    public Position getNamePosition() {
        return namePosition;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((body == null) ? 0 : body.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((position == null) ? 0 : position.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Sentence other = (Sentence) obj;
        if (body == null) {
            if (other.body != null)
                return false;
        } else if (!body.equals(other.body))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Sentence [name=" + name + ", body=" + body + ", position="
                + position + "]";
    }

    @Override
    public void accept(GdlVisitor visitor) {
        visitor.visitSentence(this);
        visitor.visitConstant(name, namePosition);
        if (body != null) {
            for (Term term : body) {
                term.accept(visitor);
            }
        }
    }

}
