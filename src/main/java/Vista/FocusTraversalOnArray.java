/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

/**
 *
 * @author Nico
 */
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class FocusTraversalOnArray extends FocusTraversalPolicy {

    private final List<Component> order;

    public FocusTraversalOnArray(Component[] components) {
        this.order = Arrays.asList(components);
    }

    // Método auxiliar para encontrar el componente con foco en la lista
    private Component getComponentInOrder(Container aContainer, Component aComponent, boolean goForward) {
        if (order.isEmpty()) {
            return null;
        }

        int index = order.indexOf(aComponent);
        int newIndex;

        if (index == -1) { // El componente actual no está en nuestra lista, buscar el primero/último
            return goForward ? getFirstComponent(aContainer) : getLastComponent(aContainer);
        }

        if (goForward) {
            newIndex = (index + 1) % order.size(); // Siguiente componente, cicla al principio
        } else {
            newIndex = (index - 1 + order.size()) % order.size(); // Anterior componente, cicla al final
        }

        // Buscar el siguiente componente focusable en el orden
        Component nextComponent = order.get(newIndex);
        int attempts = 0;
        while (!accept(nextComponent) && attempts < order.size()) {
            if (goForward) {
                newIndex = (newIndex + 1) % order.size();
            } else {
                newIndex = (newIndex - 1 + order.size()) % order.size();
            }
            nextComponent = order.get(newIndex);
            attempts++;
        }

        // Si después de intentar todos, no encontramos uno focusable, retorna null o el actual si es el único
        if (!accept(nextComponent)) {
            return null; // O aComponent, dependiendo de tu lógica deseada
        }

        return nextComponent;
    }

    @Override
    public Component getComponentAfter(Container aContainer, Component aComponent) {
        return getComponentInOrder(aContainer, aComponent, true);
    }

    @Override
    public Component getComponentBefore(Container aContainer, Component aComponent) {
        return getComponentInOrder(aContainer, aComponent, false);
    }

    @Override
    public Component getFirstComponent(Container aContainer) {
        if (order.isEmpty()) return null;
        for (Component c : order) {
            if (accept(c)) {
                return c;
            }
        }
        return null; // No hay componentes focusable en la lista
    }

    @Override
    public Component getLastComponent(Container aContainer) {
        if (order.isEmpty()) return null;
        for (int i = order.size() - 1; i >= 0; i--) {
            Component c = order.get(i);
            if (accept(c)) {
                return c;
            }
        }
        return null; // No hay componentes focusable en la lista
    }

    @Override
    public Component getDefaultComponent(Container aContainer) {
        return getFirstComponent(aContainer);
    }

    // Método para determinar si un componente es un candidato válido para el foco
    private boolean accept(Component aComponent) {
        return aComponent != null && aComponent.isVisible() && aComponent.isDisplayable() && aComponent.isEnabled() && aComponent.isFocusable();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Custom Array Focus Traversal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 250);
            frame.setLayout(new FlowLayout()); // Usamos FlowLayout por simplicidad

            JTextField txtField1 = new JTextField("Campo 1");
            JTextField txtField2 = new JTextField("Campo 2");
            JButton btnButton1 = new JButton("Botón 1");
            JTextField txtField3 = new JTextField("Campo 3");
            JButton btnButton2 = new JButton("Botón 2");

            frame.add(txtField3); // Agregado fuera del orden deseado
            frame.add(btnButton2); // Agregado fuera del orden deseado
            frame.add(txtField1);
            frame.add(txtField2);
            frame.add(btnButton1);


            // Crear y establecer la política de recorrido de foco
            FocusTraversalPolicy customPolicy = new FocusTraversalOnArray(
                new Component[]{txtField1, txtField2, btnButton1, txtField3, btnButton2}
            );
            frame.setFocusTraversalPolicy(customPolicy);
            // frame.setFocusTraversalPolicyProvider(true); // Solo si el contenedor no es un FocusCycleRoot

            frame.setVisible(true);

            // Opcional: Solicitar el foco al primer componente en tu orden
            SwingUtilities.invokeLater(() -> txtField1.requestFocusInWindow());
        });
    }
}