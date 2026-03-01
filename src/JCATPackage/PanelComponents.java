package JCATPackage;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
public class PanelComponents extends  JPanel {
    List<PanelComponents> entryPanels =new ArrayList<>();
    JTextField charaId = new JTextField(6);
    JTextField costume = new JTextField(6);
    JTextField i04 = new JTextField(6);
    JTextField skillId2 = new JTextField(6);
    JTextField charaCode = new JTextField(6);
    JTextField i12 = new JTextField(6);
    JTextField i16 = new JTextField(6);
    JTextField i20 = new JTextField(6);
    JTextField TransformationEntrty = new JTextField(6);
    JTextField i22 = new JTextField(6);

    
    

    PanelComponents() {
        setLayout(new GridLayout(10,1,2,20));
        setBorder(BorderFactory.createEmptyBorder(10,10,0,0));

        add(row("Chara ID:", charaId));
        add(row("Costume:", costume));
        add(row("I_04:", i04));
        add(row("Skill ID2:", skillId2));
        add(row("Chara Code:", charaCode));
        add(row("I_12:", i12));
        add(row("Loading Screen Value?:", i16));
        add(row("I_20:", i20));
        add(row("Transformation Entry:", TransformationEntrty));
        add(row("I_22", i22));
    }

    private JPanel row(String label, JTextField field) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel(label));
        p.add(field);
        return p;
    }
    public boolean hasEmptyFields(PanelComponents panel) {
    return panel.charaId.getText().isBlank()
        && panel.costume.getText().isBlank()
        && panel.i04.getText().isBlank()
        && panel.skillId2.getText().isBlank()
        && panel.charaCode.getText().isBlank()
        && panel.i12.getText().isBlank()
        && panel.i16.getText().isBlank()
        && panel.i20.getText().isBlank()
        && panel.TransformationEntrty.getText().isBlank()
        && panel.i22.getText().isBlank();
    
    }
    class DuplicateData {
    int charaId, costume, i04, skillId2, i12, i16, i20, TransformationEntry, i22;
    String charaCode;
   
    public DuplicateData CopyData(PanelComponents p) {
    DuplicateData d = new DuplicateData();
    d.charaId  = Integer.parseInt(p.charaId.getText());
    d.costume  = Integer.parseInt(p.costume.getText());
    d.i04      = Integer.parseInt(p.i04.getText());
    d.skillId2 = Integer.parseInt(p.skillId2.getText());
    d.charaCode= p.charaCode.getText();
    d.i12      = Integer.parseInt(p.i12.getText());
    d.i16      = Integer.parseInt(p.i16.getText());
    d.i20      = Integer.parseInt(p.i20.getText());
    d.TransformationEntry=Integer.parseInt(p.TransformationEntrty.getText());
    d.i22=Integer.parseInt(p.i22.getText());
    return d;
}
    public void PasteData(PanelComponents p, DuplicateData d) {
    p.charaId.setText(String.valueOf(d.charaId));
    p.costume.setText(String.valueOf(d.costume));
    p.i04.setText(String.valueOf(d.i04));
    p.skillId2.setText(String.valueOf(d.skillId2));
    p.charaCode.setText(d.charaCode);
    p.i12.setText(String.valueOf(d.i12));
    p.i16.setText(String.valueOf(d.i16));
    p.i20.setText(String.valueOf(d.i20));
    p.TransformationEntrty.setText(String.valueOf(d.TransformationEntry));
    p.i22.setText(String.valueOf(d.i22));
}
    }

}


