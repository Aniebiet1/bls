package util;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DBInitDialog extends JDialog {
    public DBInitDialog(Frame owner, String errorMessage) {
        super(owner, "Database initialization failed", true);
        setSize(600, 320);
        setLocationRelativeTo(owner);
        JTextArea area = new JTextArea();
        area.setEditable(false);
        StringBuilder sb = new StringBuilder();
        sb.append("Failed to initialize database tables:\n");
        sb.append(errorMessage == null ? "(no details)" : errorMessage);
        sb.append(
                "\n\nYou can run the following SQL as a Postgres superuser or grant CREATE on schema public to your DB user:\n\n");
        sb.append(DBInit.CREATE_SQL);
        area.setText(sb.toString());
        area.setCaretPosition(0);

        JScrollPane sp = new JScrollPane(area);
        add(sp, BorderLayout.CENTER);

        JPanel btns = new JPanel();
        JButton copy = new JButton(new AbstractAction("Copy SQL") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(DBInit.CREATE_SQL),
                        null);
                JOptionPane.showMessageDialog(DBInitDialog.this, "SQL copied to clipboard");
            }
        });
        JButton close = new JButton(new AbstractAction("Close") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btns.add(copy);
        btns.add(close);
        add(btns, BorderLayout.SOUTH);
    }
}
