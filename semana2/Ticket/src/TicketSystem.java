import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

public class TicketSystem {
    public static void main(String[] args) {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket(1, "Login falla", Priority.CRITICAL));
        tickets.add(new Ticket(2, "Boton desalineado", Priority.LOW));
        tickets.add(new Ticket(3, "Error en pagos", Priority.HIGH));
        tickets.add(new Ticket(4, "Mejorar docs", Priority.MEDIUM));

        System.out.println("=== Todos los Tickets ===");
        tickets.forEach(System.out::println);

        // Transiciones
        System.out.println("\n=== Transiciones ===");
        tickets.get(0).transitionTo(TicketStatus.IN_PROGRESS);
        tickets.get(2).transitionTo(TicketStatus.IN_PROGRESS);
        tickets.get(2).transitionTo(TicketStatus.RESOLVED);

        // Transicion invalida
        tickets.get(2).transitionTo(TicketStatus.OPEN);

        System.out.println("\n=== Estado Actualizado ===");
        tickets.forEach(System.out::println);

        // TODO: EnumMap para contar tickets por status
        System.out.println("\n=== Dashboard (EnumMap) ===");
        EnumMap<TicketStatus, Integer> conteo = new EnumMap<>(TicketStatus.class);
        for (TicketStatus s : TicketStatus.values()) conteo.put(s, 0);
        for (Ticket t : tickets) {
            conteo.merge(t.getStatus(), 1, Integer::sum);
        }//si la categoria de ticket no existe le suma 1, si nol epone 1
        // TODO: contar tickets por status

        conteo.forEach((status, count) ->
                System.out.printf("  %s: %d%n", status, count));

        // TODO: EnumSet para filtrar tickets urgentes (HIGH + CRITICAL)
        System.out.println("\n=== Tickets Urgentes (EnumSet) ===");
        EnumSet<Priority> urgentes = EnumSet.of(Priority.HIGH, Priority.CRITICAL);
        // TODO: filtrar e imprimir tickets con prioridad urgente
        tickets.stream().filter(t -> urgentes.contains(t.getPriority()))
                .forEach(System.out::println);
    }
}
