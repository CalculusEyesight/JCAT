package JCATPackage;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;



public class JCATComponents extends PanelComponents implements ActionListener{
   
    
    
    JFrame jframe=new JFrame("JCAT");

    Image icon=Toolkit.getDefaultToolkit().getImage(JCATComponents.class.getResource("/animal.png"));

    
    CardLayout cardLayout=new CardLayout();

    JPanel jcardPanel=new JPanel(cardLayout);
    
    JMenuBar jmb=new JMenuBar();

    JMenu jfile=new JMenu("File");
    JMenu joptions=new JMenu("Options");
    JMenu jentries=new JMenu("Entries");
    JMenu jhelp=new JMenu("Help");

    JMenuItem jopen=new JMenuItem("Open");
    JMenuItem jsave=new JMenuItem("Save");
    JMenuItem jInsertEntries=new JMenuItem("New Entry");
    JMenuItem jRemoveEntries=new JMenuItem("Remove Entry");
    JMenuItem jCopyEntry=new JMenuItem("Copy Entry");
    JMenuItem jPasteEntry=new JMenuItem("Paste Entry");
    JMenuItem zeroEntry=new JMenuItem("Entry 0");
    JMenuItem idlist=new JMenuItem("ID List");
    JMenuItem catManual=new JMenuItem("CAT Manual");

    boolean entryAdded=false;
    boolean entryRemoved=false;
    boolean copyEntryCheck=true;
    


    File fileOpenedDirectory=null;
    
    long n=0;
    long entryCount;

    DuplicateData clipBoard=new DuplicateData();

    int entryCopyContainer=0, entryPasteContainer;

    

    public JCATComponents() {
       

        //jframe section
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(500,560);
        jframe.setLocationRelativeTo(null);
        jframe.add(jcardPanel);
        jframe.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        jframe.setJMenuBar(jmb);
        jframe.setIconImage(icon);
        jframe.setVisible(true);
        
        
        //jpanel section
        PanelComponents jpe0=new PanelComponents();
        entryPanels.add(jpe0);
        jcardPanel.add(jpe0,"Entry 0");
        cardLayout.show(jcardPanel,"Entry 0");

    
        //jmb section
        jmb.add(jfile);
        jmb.add(joptions);
        jmb.add(jentries);
        jmb.add(jhelp);
        
        
        //jfile section
        jfile.add(jopen);
        jfile.add(jsave);

        //joptions section
        joptions.add(jInsertEntries);
        joptions.add(jRemoveEntries);
        joptions.add(jCopyEntry);
        joptions.add(jPasteEntry);
        

        //jentry section
        jentries.add(zeroEntry);

        //jhelp section
        jhelp.add(idlist);
        jhelp.add(catManual);

        //addactionlistener section
        jopen.addActionListener(this);
        jsave.addActionListener(this);
        jInsertEntries.addActionListener(this);
        jRemoveEntries.addActionListener(this);
        zeroEntry.addActionListener(this);
        idlist.addActionListener(this);
        jCopyEntry.addActionListener(this);
        jPasteEntry.addActionListener(this);
        catManual.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent ae) { 
        String detect= ae.getActionCommand();
        
        if(detect.equals("Open")){
            resetEntries();
            JFileChooser jfc=new JFileChooser("C:\\Program Files (x86)\\Steam\\steamapps\\common\\DB Xenoverse 2");
            FileNameExtensionFilter filter=new FileNameExtensionFilter(".cat", "cat");
            jfc.setFileFilter(filter);
            if(jfc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
                Path path=Path.of(jfc.getSelectedFile().getAbsolutePath());
                File selectedFile = jfc.getSelectedFile();
                fileOpenedDirectory=selectedFile;
                PanelComponents panel= entryPanels.get(0);
                
                
                try(FileChannel channel = FileChannel.open(path, 
                StandardOpenOption.READ, 
                StandardOpenOption.WRITE)) {
                    
                    
                    ByteBuffer mBuf=ByteBuffer.allocate(2);
                    ByteBuffer mBuf2=ByteBuffer.allocate(4);
                    ByteBuffer mBuf3=ByteBuffer.allocate(3);
                    ByteBuffer mBuf4=ByteBuffer.allocate(1);
                    entryCount=(selectedFile.length()-12)/24;
                    
                    
                    mBuf.order(ByteOrder.LITTLE_ENDIAN);
                    mBuf2.order(ByteOrder.LITTLE_ENDIAN);
                    mBuf3.order(ByteOrder.LITTLE_ENDIAN);
                    mBuf4.order(ByteOrder.LITTLE_ENDIAN);
                    

                    //reading charaid
                    channel.position(12);
                    channel.read(mBuf);
                    mBuf.rewind();
                    int charaidRead=mBuf.getShort() & 0xffff;
                    panel.charaId.setText(String.valueOf(charaidRead));

                    //readingcostume
                    channel.position(14);
                    mBuf.rewind();
                    channel.read(mBuf);
                    mBuf.rewind();
                    int costumeRead=mBuf.getShort() &0xffff;
                    panel.costume.setText(String.valueOf(costumeRead));

                    //reding I_04
                    channel.position(16);
                    mBuf.rewind();
                    channel.read(mBuf);
                    mBuf.rewind();
                    int readI_04=mBuf.getShort() & 0xffff;
                    panel.i04.setText(String.valueOf(readI_04));

                    //reading skillid2
                    channel.position(18);
                    mBuf.rewind();
                    channel.read(mBuf);
                    mBuf.rewind();
                    int readSkillid2=mBuf.getShort() & 0xffff;
                    panel.skillId2.setText(String.valueOf(readSkillid2));

                    //reading characode
                    channel.position(20);
                    channel.read(mBuf3);
                    mBuf3.rewind();
                    panel.charaCode.setText(StandardCharsets.UTF_8.decode(mBuf3).toString());

                    //reading I_12
                    channel.position(24);
                    mBuf2.rewind();
                    channel.read(mBuf2);
                    mBuf2.rewind();
                    panel.i12.setText(String.valueOf(mBuf2.getInt()));

                    //reading I_16
                    channel.position(28);
                    mBuf2.rewind();
                    channel.read(mBuf2);
                    mBuf2.rewind();
                    panel.i16.setText(String.valueOf(mBuf2.getInt()));

                    //reading I_20
                    channel.position(32);
                    mBuf4.rewind();
                    channel.read(mBuf4);
                    mBuf4.rewind();
                    int i20read=mBuf4.get() & 0xff;
                    panel.i20.setText(String.valueOf(i20read));

                    //reading transformationEntry
                    channel.position(33);
                    mBuf4.rewind();
                    channel.read(mBuf4);
                    mBuf4.rewind();
                    int transformationEntryread=mBuf4.get() & 0xff;
                    panel.TransformationEntrty.setText(String.valueOf(transformationEntryread));

                    //reading I_22
                    channel.position(34);
                    mBuf.rewind();
                    channel.read(mBuf);
                    mBuf.rewind();
                    int I_22=mBuf.get() & 0xffff;
                    panel.i22.setText(String.valueOf(I_22));
                    
                    


                    
                    for(long i=1;i<entryCount;i++){
                    if(entryCount>1){
                    if(n>entryCount-1|| n==entryCount-1){
                        break;
                    }
                    mBuf.rewind();
                    JMenuItem nEntry=new JMenuItem("Entry "+(n+1));
                    jentries.add(nEntry);
                    nEntry.addActionListener(this);

                    PanelComponents newEntryPanel = new PanelComponents();
                    entryPanels.add(newEntryPanel);
                    jcardPanel.add(newEntryPanel, "Entry " + (n+1));
                    
                    n++;
                    
                    //writing charaid
                    channel.position(EntryOffsets(i)[0]);
                    channel.read(mBuf);
                    mBuf.rewind();
                    int LoopCharaidRead=mBuf.getShort() & 0xffff;
                    newEntryPanel.charaId.setText(String.valueOf(LoopCharaidRead));
                    
                    
                    

                    //readingcostume
                    channel.position(EntryOffsets(i)[1]);
                    mBuf.rewind();
                    channel.read(mBuf);
                    mBuf.rewind();
                    int LoopCostumeRead=mBuf.getShort() & 0xffff;
                    newEntryPanel.costume.setText(String.valueOf(LoopCostumeRead));

                    //reding I_04
                    channel.position(EntryOffsets(i)[2]);
                    mBuf.rewind();
                    channel.read(mBuf);
                    mBuf.rewind();
                    int LoopI_04Read=mBuf.getShort() & 0xffff;
                    newEntryPanel.i04.setText(String.valueOf(LoopI_04Read));

                    //reading skillid2
                    channel.position(EntryOffsets(i)[3]);
                    mBuf.rewind();
                    channel.read(mBuf);
                    mBuf.rewind();
                    int LoopSkillid2Read=mBuf.getShort() & 0xffff;
                    newEntryPanel.skillId2.setText(String.valueOf(LoopSkillid2Read));

                    //reading characode
                    mBuf3.rewind();
                    channel.position(EntryOffsets(i)[4]);
                    channel.read(mBuf3);
                    mBuf3.rewind();
                    newEntryPanel.charaCode.setText(StandardCharsets.UTF_8.decode(mBuf3).toString());

                    //reading I_12
                    channel.position(EntryOffsets(i)[5]);
                    mBuf2.rewind();
                    channel.read(mBuf2);
                    mBuf2.rewind();
                    newEntryPanel.i12.setText(String.valueOf(mBuf2.getInt()));

                    //reading I_16
                    channel.position(EntryOffsets(i)[6]);
                    mBuf2.rewind();
                    channel.read(mBuf2);
                    mBuf2.rewind();
                    newEntryPanel.i16.setText(String.valueOf(mBuf2.getInt()));

                    //reading I_20
                    channel.position(EntryOffsets(i)[7]);
                    mBuf4.rewind();
                    channel.read(mBuf4);
                    mBuf4.rewind();
                    int i20readloop=mBuf4.get() & 0xff;
                    newEntryPanel.i20.setText(String.valueOf(i20readloop));

                    //reading Transformation Entry
                    channel.position(EntryOffsets(i)[8]);
                    mBuf4.rewind();
                    channel.read(mBuf4);
                    mBuf4.rewind();
                    int TransformEntryloop=mBuf4.get() & 0xff;
                    newEntryPanel.TransformationEntrty.setText(String.valueOf(TransformEntryloop));


                    //reading I_22
                    channel.position(EntryOffsets(i)[9]);
                    mBuf.rewind();
                    channel.read(mBuf);
                    mBuf.rewind();
                    int I_22loop=mBuf.getShort() & 0xffff;
                    newEntryPanel.i22.setText(String.valueOf(I_22loop));
                    mBuf.rewind();
                    
                    }
                        
                        

                    }
                    
                } catch (IOException e) {
                JOptionPane.showMessageDialog(jframe, "Can't read file");
                }

        }
            
        }
        if(detect.equals("Save")){
            entryCount=0;
            JFileChooser jfc=new JFileChooser(fileOpenedDirectory);
            FileNameExtensionFilter filter=new FileNameExtensionFilter(".cat", "cat");
            jfc.setFileFilter(filter);
            if(jfc.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
                File selected = jfc.getSelectedFile();

                    if (!selected.getName().toLowerCase().endsWith(".cat")) {
                            selected = new File(selected.getAbsolutePath() + ".cat");
                    }

                Path path = selected.toPath();
                
                PanelComponents panel= entryPanels.get(0);
                
                try(FileChannel channel = FileChannel.open(path,
                StandardOpenOption.WRITE,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)) {
                    byte[] magic=new byte[]{0x23,0x43,0x41,0x54};
                    byte[] endiannes=new byte[]{(byte)0xFE,(byte)0xFF};
                    byte[] entryOffset=new byte[]{0x0C,0x00,0x00,0x00};
                    ByteBuffer mBuf=ByteBuffer.allocate(2);
                    ByteBuffer mBuf2=ByteBuffer.allocate(4);
                    ByteBuffer mBuf3=ByteBuffer.allocate(1);
                    mBuf.order(ByteOrder.LITTLE_ENDIAN);
                    mBuf2.order(ByteOrder.LITTLE_ENDIAN);
                    mBuf3.order(ByteOrder.LITTLE_ENDIAN);
                    try{
                    
                    //write magic
                    channel.position(0);
                    mBuf2=ByteBuffer.wrap(magic);
                    channel.write(mBuf2);
                    mBuf2.order(ByteOrder.LITTLE_ENDIAN);
                    mBuf2.rewind();
                    
                    
                    //write endiannes
                    channel.position(4);
                    mBuf=ByteBuffer.wrap(endiannes);
                    channel.write(mBuf);
                    mBuf.order(ByteOrder.LITTLE_ENDIAN);
                    mBuf.rewind();


                    //write entry
                    channel.position(6);
                    mBuf.putShort((short)(n+1));
                    mBuf.flip();
                    channel.write(mBuf);

                    //write entry offset
                    channel.position(8);
                    mBuf2=ByteBuffer.wrap(entryOffset);
                    channel.write(mBuf2);
                    mBuf2.order(ByteOrder.LITTLE_ENDIAN);
                    mBuf2.rewind();

                    

                    //writing charaid
                    mBuf.rewind();
                    channel.position(12);
                    int charaidU=Integer.parseInt(panel.charaId.getText());
                    mBuf.putShort((short) (charaidU & 0xffff));
                    mBuf.flip();
                    channel.write(mBuf);

                    //writing costume
                    channel.position(14);
                    mBuf.rewind();
                    int costumeU=Integer.parseInt(panel.costume.getText());
                    mBuf.putShort((short) (costumeU & 0xffff));
                    mBuf.flip();
                    channel.write(mBuf);

                    //writing i04
                    channel.position(16);
                    mBuf.rewind();
                    int I_04U=Integer.parseInt(panel.i04.getText());
                    mBuf.putShort((short) (I_04U & 0xffff));
                    mBuf.flip();
                    channel.write(mBuf);

                     //writing skillid2
                    channel.position(18);
                    mBuf.rewind();
                    int skillid2U=Integer.parseInt(panel.skillId2.getText());
                    mBuf.putShort((short) (skillid2U& 0xffff));
                    mBuf.flip();
                    channel.write(mBuf);
                    mBuf.rewind();

                    //writing characode
                    channel.position(20);
                    mBuf2.position(0);
                    mBuf2.putChar((panel.charaCode.getText().charAt(0)));
                    mBuf2.position(1);
                    mBuf2.putChar((panel.charaCode.getText().charAt(1)));
                    mBuf2.position(2);
                    mBuf2.putChar((panel.charaCode.getText().charAt(2)));
                    mBuf2.flip();
                    channel.write(mBuf2);

                     //writing I_12
                    channel.position(24);
                    mBuf2.rewind();
                    mBuf2.putInt(Integer.parseInt(panel.i12.getText()));
                    mBuf2.flip();
                    channel.write(mBuf2);

                    //writing I_16
                    channel.position(28);
                    mBuf2.rewind();
                    mBuf2.putInt(Integer.parseInt(panel.i16.getText()));
                    mBuf2.flip();
                    channel.write(mBuf2);

                    //writing I_20
                    channel.position(32);
                    mBuf3.rewind();
                    int I_20U=Integer.parseInt(panel.i20.getText());
                    mBuf3.put((byte) (I_20U & 0xff));
                    mBuf3.flip();
                    channel.write(mBuf3);

                    //writing TransformationEntry
                    channel.position(33);
                    mBuf3.rewind();
                    int TransformationEntryU=Integer.parseInt(panel.TransformationEntrty.getText());
                    mBuf3.put((byte) (TransformationEntryU & 0xff));
                    mBuf3.flip();
                    channel.write(mBuf3);

                    //writing I_22
                    channel.position(34);
                    mBuf.rewind();
                    int I_22U=Integer.parseInt(panel.i22.getText());
                    mBuf.putShort((short) (I_22U & 0xffff));
                    mBuf.flip();
                    channel.write(mBuf);
                    mBuf.rewind();

                    if(entryAdded){
                     channel.truncate((n+1)*24+12);
                    }
                    entryAdded=false;
                    if(entryRemoved){
                        channel.truncate((n+1)*24+12);

                    }
                    entryRemoved=false;

                    for(long i=1;i<n+1;i++){
                    mBuf.rewind();
                    PanelComponents panelr = entryPanels.get((int)i);

                     //writing charaid
                    mBuf.rewind();
                    channel.position(EntryOffsets(i)[0]);
                    int charaidLoopU=Integer.parseInt(panelr.charaId.getText());
                    mBuf.putShort((short) (charaidLoopU & 0xffff));
                    mBuf.flip();
                    channel.write(mBuf);

                    //writing costume
                    channel.position(EntryOffsets(i)[1]);
                    mBuf.rewind();
                    int costumeLoopU=Integer.parseInt(panelr.costume.getText());
                    mBuf.putShort((short) (costumeLoopU & 0xffff));
                    mBuf.flip();
                    channel.write(mBuf);

                    //writing I_04
                    channel.position(EntryOffsets(i)[2]);
                    mBuf.rewind();
                    int I_04LoopU=Integer.parseInt(panelr.i04.getText());
                    mBuf.putShort((short) (I_04LoopU & 0xffff));
                    mBuf.flip();
                    channel.write(mBuf);

                    //writing skillid2
                    channel.position(EntryOffsets(i)[3]);
                    mBuf.rewind();
                    int skillid2LoopU=Integer.parseInt(panelr.skillId2.getText());
                    mBuf.putShort((short) (skillid2LoopU & 0xffff));
                    mBuf.flip();
                    channel.write(mBuf);
                    

                    //writing characode
                    mBuf2.rewind();
                    channel.position(EntryOffsets(i)[4]);
                    mBuf2.rewind();
                    mBuf2.position(0);
                    mBuf2.putChar((panelr.charaCode.getText().charAt(0)));
                    mBuf2.position(1);
                    mBuf2.putChar((panelr.charaCode.getText().charAt(1)));
                    mBuf2.position(2);
                    mBuf2.putChar((panelr.charaCode.getText().charAt(2)));
                    mBuf2.flip();
                    channel.write(mBuf2);
                    

                    //writing I_12
                    channel.position(EntryOffsets(i)[5]);
                    mBuf2.rewind();
                    mBuf2.putInt(Integer.parseInt(panelr.i12.getText()));
                    mBuf2.flip();
                    channel.write(mBuf2);
                    

                    //writing I_16
                    channel.position(EntryOffsets(i)[6]);
                    mBuf2.rewind();
                    mBuf2.putInt(Integer.parseInt(panelr.i16.getText()));
                    mBuf2.flip();
                    channel.write(mBuf2);
                   

                    //writing I_20
                    channel.position(EntryOffsets(i)[7]);
                    mBuf3.rewind();
                    int I_20loopU=Integer.parseInt(panelr.i20.getText());
                    mBuf3.put((byte) (I_20loopU & 0xff));
                    mBuf3.flip();
                    channel.write(mBuf3);

                    //writing TransformationEntry
                    channel.position(EntryOffsets(i)[8]);
                    mBuf3.rewind();
                    int TranformationEntryloopU=Integer.parseInt(panelr.TransformationEntrty.getText());
                    mBuf3.put((byte) (TranformationEntryloopU & 0xff));
                    mBuf3.flip();
                    channel.write(mBuf3);

                    //writing I_22
                    channel.position(EntryOffsets(i)[9]);
                    mBuf.rewind();
                    int I_22LoopU=Integer.parseInt(panelr.i22.getText());
                    mBuf.putShort((short) (I_22LoopU & 0xffff));
                    mBuf.flip();
                    channel.write(mBuf);
                    mBuf.rewind();
                    
                }
                }catch(NumberFormatException a){
                    JOptionPane.showMessageDialog(jframe, "Text field value not read");

                }
                    

                }catch(IOException e){
                    JOptionPane.showMessageDialog(jframe, "Can't save file");
                }
                
            }
            

        }
        if(detect.equals("New Entry")){
            n+=1;
            JMenuItem nEntry=new JMenuItem("Entry "+n);
            jentries.add(nEntry);
            nEntry.addActionListener(this);

            PanelComponents newEntryPanel = new PanelComponents();
            entryPanels.add(newEntryPanel);
            jcardPanel.add(newEntryPanel, "Entry " + n);

            entryAdded=true;

        }
        if(detect.startsWith("Entry ")){
            cardLayout.show(jcardPanel, detect);
            if(copyEntryCheck){ 
                entryCopyContainer=Integer.parseInt(detect.substring(6)); 
                
            } 
            
            entryPasteContainer=Integer.parseInt(detect.substring(6)); 
            
            
            
        }
        if(detect.equals("ID List")){
            try {
                Desktop.getDesktop().browse(new URI("https://docs.google.com/spreadsheets/d/1SyHP2fns9w_ovq96eiLejxZBngo2cYbsNxC9fo9YH5w/edit?gid=511700634#gid=511700634"));
                
                
                
            } catch (URISyntaxException |IOException e) {
            }
            
             

        }
        if(detect.equals("Remove Entry")){
            if(n!=0){
            jentries.remove((int)n);
            entryPanels.remove((int)n);
            jcardPanel.remove((int)n);
            n-=1;
            entryRemoved=true;
            
            }

        }
        if(detect.equals("Copy Entry")){
            try {
                clipBoard = clipBoard.CopyData(entryPanels.get(entryCopyContainer));
            copyEntryCheck=false;
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(jframe, "Can't copy empty fields");

            }
            
            
        }
        if(detect.equals("Paste Entry")){
            if(clipBoard==null){
                JOptionPane.showMessageDialog(jframe, "Nothing Copied");
            }
            clipBoard.PasteData(entryPanels.get(entryPasteContainer), clipBoard);
            entryCopyContainer=entryPasteContainer;
            copyEntryCheck=true;
            
            
        }
        if(detect.equals("CAT Manual")){
            try {
                Desktop.getDesktop().browse(new URI("https://docs.google.com/document/d/e/2PACX-1vREvZmm3PrBMt58EGxf6uhh46O-evXx-11ynzjXa-JsZxfQyK_vwJ4a5ZOmY3OgYl-f8HW9lmCXtqPk/pub"));
                
                
                
            } catch (URISyntaxException |IOException e) {
            }

        }
        
    }
    
    long[] EntryOffsets(long n){
        long [] Offsets=new long[10];
        long CharaId=24*(n)+12;
        long costume=24*(n)+14;
        long I_04=24*(n)+16;
        long skillId2=24*(n)+18;
        long charaCode=24*(n)+20;
        long I_12=24*(n)+24;
        long I_16=24*(n)+28;
        long I_20=24*(n)+32;
        long transformationEntry=24*(n)+33;
        long I_22=24*(n)+34;
        Offsets[0]=CharaId;
        Offsets[1]=costume;
        Offsets[2]=I_04;
        Offsets[3]=skillId2;
        Offsets[4]=charaCode;
        Offsets[5]=I_12;
        Offsets[6]=I_16;
        Offsets[7]=I_20;
        Offsets[8]=transformationEntry;
        Offsets[9]=I_22;
        return Offsets;

    }

    void resetEntries() {
    // reset counters
    n = 0;
    entryCount = 0;

    // clear menu entries (keep Entry 0)
    jentries.removeAll();
    jentries.add(zeroEntry);

    // clear panels
    entryPanels.clear();
    jcardPanel.removeAll();

    // recreate Entry 0
    PanelComponents base = new PanelComponents();
    entryPanels.add(base);
    jcardPanel.add(base, "Entry 0");
    cardLayout.show(jcardPanel, "Entry 0");

    // refresh UI
    jentries.revalidate();
    jentries.repaint();
    jcardPanel.revalidate();
    jcardPanel.repaint();
    }
}





    
    
    

