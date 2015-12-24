package net.alloyggp.griddle;


public class GdlProblem {
    private final Level level;
    private final Position position;
    private final String message;

    public static enum Level {
        WARNING,
        ERROR
    }

    private GdlProblem(Level level, Position position, String message) {
        this.level = level;
        this.position = position;
        this.message = message;
    }

    public static GdlProblem create(Level level, String message,
            Position position) {
        return new GdlProblem(level, position, message);
    }

    public static GdlProblem createError(String message, Position position) {
        return new GdlProblem(Level.ERROR, position, message);
    }

    public static GdlProblem createWarning(String message, Position position) {
        return new GdlProblem(Level.WARNING, position, message);
    }

    public Level getLevel() {
        return level;
    }

    /**
     * This may return null, if the error is not associated with a particular section of code.
     */
    public Position getPosition() {
        return position;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return level == Level.ERROR;
    }

    public boolean isWarning() {
        return level == Level.WARNING;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((level == null) ? 0 : level.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
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
        GdlProblem other = (GdlProblem) obj;
        if (level != other.level)
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
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
        return "GdlProblem [level=" + level + ", position=" + position
                + ", message=" + message + "]";
    }

}
