enum Priority {
    LOW(1, 48),
    MEDIUM(2, 24),
    HIGH(3, 8),
    CRITICAL(4, 1);

    private final int level;
    private final int responseTimeHours;

    // TODO: constructor
    Priority(int level, int responseTimeHours) {
        this.level = level;
        this.responseTimeHours = responseTimeHours;
    }

    public int getLevel() { return level; }
    public int getResponseTimeHours() { return responseTimeHours; }

    public String getLabel() {
        // TODO: retornar "NOMBRE (Nivel X, Respuesta: Yh)"
        return String.format("%s (Nivel %d, Respuesta: %dh)",
                name(), getLevel(), getResponseTimeHours());
    }
}

enum TicketStatus {
    OPEN, IN_PROGRESS, RESOLVED, CLOSED;

    public boolean canTransitionTo(TicketStatus target) {
        // TODO: definir transiciones validas
        // OPEN -> IN_PROGRESS
        // IN_PROGRESS -> RESOLVED o OPEN (reabrir)
        // RESOLVED -> CLOSED o IN_PROGRESS (reabrir)
        // CLOSED -> ninguno
        return switch (this) {
            case OPEN -> target == IN_PROGRESS;
            case IN_PROGRESS ->target == RESOLVED || target == OPEN ;
            case RESOLVED -> target == CLOSED || target == IN_PROGRESS;
            case CLOSED -> false;
        };
    }
}

class Ticket {
    private final int id;
    private final String description;
    private final Priority priority;
    private TicketStatus status;

    public Ticket(int id, String description, Priority priority) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.status = TicketStatus.OPEN;
    }

    public void transitionTo(TicketStatus newStatus) {
        // TODO: validar con canTransitionTo, cambiar o imprimir error
        if(getStatus().canTransitionTo(newStatus)) {
            System.out.printf("Ticket %d: %s -> %s%n", id, status, newStatus);
            this.status = newStatus;
        }
        else {
            this.status = TicketStatus.RESOLVED;
            System.out.printf("Error no se puede transicionar de %s a %s",TicketStatus.RESOLVED, newStatus);
        }
    }

    // TODO: getters
    public int getId() { return id; }
    public Priority getPriority() { return priority; }
    public TicketStatus getStatus() { return status; }

    @Override
    public String toString() {
        return String.format("Ticket{id=%d, desc='%s', priority=%s, status=%s}",
                id, description, priority.getLabel(), status);
    }
}

