/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Nico
 */
public class TableErrorRenderer extends DefaultTableCellRenderer {
    
    public Border errorBorder = BorderFactory.createLineBorder(Color.RED, 2);
    public Border normalBorder = new EmptyBorder(1, 1, 1, 1);
    
    private int column = 2;
    
    private int[] rowsWithError;
    
    public TableErrorRenderer(int maxRows) {
        super();
        
        this.rowsWithError = new int[maxRows];
        
        for (int i = 0; i < maxRows; i++)
            this.rowsWithError[i] = 0;
    }
    
    public void addErrorToRow(int row) {
        this.rowsWithError[row] = 1;
    }
    
    public void clearErrors() {
        for (int i = 0; i < this.rowsWithError.length; i++)
            this.rowsWithError[i] = 0;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = null;
        
        c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, this.column);
        
        if (this.rowsWithError[row] == 0){
            ((JComponent) c).setBorder(this.normalBorder);
        } else {
            ((JComponent) c).setBorder(this.errorBorder);
        }
        
        return c;
    }
}
