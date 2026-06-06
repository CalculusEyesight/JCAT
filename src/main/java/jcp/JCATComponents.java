package jcp;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import static jcp.BitUtilities.*;

import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class JCATComponents extends PanelComponents implements ActionListener,KeyListener{
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
    JMenuItem jInsertEntries=new JMenuItem("New Entry    Crtl+I");
    JMenuItem jRemoveEntries=new JMenuItem("Remove Entry    Delete");
    JMenuItem jCopyEntry=new JMenuItem("Copy Entry    Ctrl+C");
    JMenuItem jPasteEntry=new JMenuItem("Paste Entry    Ctrl+V");
    JMenuItem zeroEntry=new JMenuItem("Entry 0");
    JMenuItem idlist=new JMenuItem("ID List");
    JMenuItem catManual=new JMenuItem("CAT Manual");

    boolean entryAdded=false;
    boolean entryRemoved=false;
    boolean copyEntryCheck=true;
    
    
    long n=0;
    long entryCount;

    DuplicateData clipBoard=new DuplicateData();

    int entryCopyContainer=0, entryPasteContainer;

    File chosenDirectory;


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

        //addkeylistener section
        jInsertEntries.addKeyListener(this);
        jRemoveEntries.addKeyListener(this);
        jcardPanel.addKeyListener(this);
        addKeyListener(this);
        jframe.addKeyListener(this);
        jpe0.addKeyListener(this);
        jframe.setFocusable(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) { 
        String detect= ae.getActionCommand();

        if(detect.equals("Open")){
            Platform.runLater(()->{
                FileChooser fc=new FileChooser();
                fc.getExtensionFilters().addAll(new ExtensionFilter("cat flies","*.cat")); 
                File selectedFile = fc.showOpenDialog(null);
                setOpenFileLocation(selectedFile);
                
                if(selectedFile==null){
                    return;
                }
                else{
                    resetEntries();
                }
                Path path=Path.of(selectedFile.getAbsolutePath());;
               
                PanelComponents panel= entryPanels.get(0);
                
                try(FileChannel channel = FileChannel.open(path, 
                StandardOpenOption.READ)) {
                    ByteBuffer byteBuffer=ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
                    ByteBuffer shortBuffer=ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
                    ByteBuffer stringBuffer=ByteBuffer.allocate(3).order(ByteOrder.LITTLE_ENDIAN);
                    ByteBuffer intBuffer=ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                    
                    entryCount=(selectedFile.length()-12)/24;
                    
                    //reading charaid
                    channel.position(12);
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    panel.charaId.setText(String.valueOf(ToUShort(shortBuffer.getShort())));

                    //readingcostume
                    channel.position(14);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    panel.costume.setText(String.valueOf(ToUShort(shortBuffer.getShort())));
                    
                    //reading I_04
                    channel.position(16);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    panel.i04.setText(String.valueOf(ToUShort(shortBuffer.getShort())));

                    //reading skillid2
                    channel.position(18);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    panel.skillId2.setText(String.valueOf(ToUShort(shortBuffer.getShort())));

                    //reading characode
                    channel.position(20);
                    channel.read(stringBuffer);
                    stringBuffer.flip();
                    panel.charaCode.setText(StandardCharsets.UTF_8.decode(stringBuffer).toString());

                    //reading I_12
                    channel.position(24);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    panel.i12.setText(String.valueOf(intBuffer.getInt()));

                    //reading I_16
                    channel.position(28);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    panel.i16.setText(String.valueOf(intBuffer.getInt()));

                    //reading I_20
                    channel.position(32);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    panel.i20.setText(String.valueOf(ToUByte(byteBuffer.get())));

                    //reading transformationEntry
                    channel.position(33);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    panel.TransformationEntrty.setText(String.valueOf(ToUByte(byteBuffer.get())));

                    //reading I_22
                    channel.position(34);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    panel.i22.setText(String.valueOf(ToUShort(shortBuffer.getShort())));
                
                    for(long i=1;i<entryCount;i++){
                        if(entryCount>1){
                            if(n>entryCount-1|| n==entryCount-1){
                                break;
                            }
                        
                            JMenuItem nEntry=new JMenuItem("Entry "+(n+1));
                            jentries.add(nEntry);
                            nEntry.addActionListener(this);

                            PanelComponents newEntryPanel = new PanelComponents();
                            entryPanels.add(newEntryPanel);
                            jcardPanel.add(newEntryPanel, "Entry " + (n+1));
                            
                            n++;
                            
                            //reading charaid
                            channel.position(24*i+12);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            newEntryPanel.charaId.setText(String.valueOf(ToUShort(shortBuffer.getShort())));
                            
                            //readingcostume
                            channel.position(24*i+14);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            newEntryPanel.costume.setText(String.valueOf(ToUShort(shortBuffer.getShort())));

                            //reding I_04
                            channel.position(24*i+16);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            newEntryPanel.i04.setText(String.valueOf(ToUShort(shortBuffer.getShort())));

                            //reading skillid2
                            channel.position(24*i+18);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            newEntryPanel.skillId2.setText(String.valueOf(ToUShort(shortBuffer.getShort())));

                            //reading characode
                            channel.position(24*i+20);
                            stringBuffer.clear();
                            channel.read(stringBuffer);
                            stringBuffer.flip();
                            newEntryPanel.charaCode.setText(StandardCharsets.UTF_8.decode(stringBuffer).toString());

                            //reading I_12
                            channel.position(24*i+24);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            newEntryPanel.i12.setText(String.valueOf(intBuffer.getInt()));

                            //reading I_16
                            channel.position(24*i+28);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            newEntryPanel.i16.setText(String.valueOf(intBuffer.getInt()));

                            //reading I_20
                            channel.position(24*i+32);
                            byteBuffer.clear();
                            channel.read(byteBuffer);
                            byteBuffer.flip();
                            newEntryPanel.i20.setText(String.valueOf(ToUByte(byteBuffer.get())));

                            //reading Transformation Entry
                            channel.position(24*i+33);
                            byteBuffer.clear();
                            channel.read(byteBuffer);
                            byteBuffer.flip();
                            newEntryPanel.TransformationEntrty.setText(String.valueOf(ToUByte(byteBuffer.get())));

                            //reading I_22
                            channel.position(24*i+34);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            newEntryPanel.i22.setText(String.valueOf(ToUShort(shortBuffer.getShort())));
                        }
                    }    
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(jframe, "Can't read file");
                }
            });
            
        }
        if(detect.equals("Save")){
            entryCount=0;
            Platform.runLater(()->{
                FileChooser fc=new FileChooser();
                fc.setInitialDirectory(chosenDirectory);
                
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("cat files", "*.cat") );
                File selectedFile = fc.showSaveDialog(null);
               
                if(selectedFile==null){
                    return;
                }
                
                
                
                

                    if (!selectedFile.getName().toLowerCase().endsWith(".cat")) {
                        selectedFile = new File(selectedFile.getAbsolutePath() + ".cat");
                    }

                Path path = selectedFile.toPath();
                
                PanelComponents panel= entryPanels.get(0);
                
                try(FileChannel channel = FileChannel.open(path,
                StandardOpenOption.WRITE,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)) {
                
                    ByteBuffer byteBuffer=ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
                    ByteBuffer shortBuffer=ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
                    ByteBuffer intBuffer=ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                    
                    try{
                    
                        //write magic
                        channel.position(0);
                        channel.write(ByteBuffer.wrap(new byte[]{0x23,0x43,0x41,0x54}));
                        
                        //write endiannes
                        channel.position(4);
                        channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE,(byte)0xFF}));
                    
                        //write entry
                        channel.position(6);
                        shortBuffer.putShort((short)(n+1));
                        shortBuffer.flip();
                        channel.write(shortBuffer);

                        //write entry offset
                        channel.position(8);
                        channel.write(ByteBuffer.wrap(new byte[]{0x0C,0x00,0x00,0x00}));

                        //writing charaid
                        channel.position(12);
                        shortBuffer.clear();
                        shortBuffer.putShort((short)Integer.parseInt(panel.charaId.getText()));
                        shortBuffer.flip();
                        channel.write(shortBuffer);

                        //writing costume
                        channel.position(14);
                        shortBuffer.clear();
                        shortBuffer.putShort((short)Integer.parseInt(panel.costume.getText()));
                        shortBuffer.flip();
                        channel.write(shortBuffer);

                        //writing i04
                        channel.position(16);
                        shortBuffer.clear();
                        shortBuffer.putShort((short)Integer.parseInt(panel.i04.getText()));
                        shortBuffer.flip();
                        channel.write(shortBuffer);

                        //writing skillid2
                        channel.position(18);
                        shortBuffer.clear();
                        shortBuffer.putShort((short)Integer.parseInt(panel.skillId2.getText()));
                        shortBuffer.flip();
                        channel.write(shortBuffer);

                        //writing characode
                        channel.position(20);
                        intBuffer.clear();
                        intBuffer.position(0);
                        intBuffer.putChar((panel.charaCode.getText().charAt(0)));
                        intBuffer.position(1);
                        intBuffer.putChar((panel.charaCode.getText().charAt(1)));
                        intBuffer.position(2);
                        intBuffer.putChar((panel.charaCode.getText().charAt(2)));
                        intBuffer.flip();
                        channel.write(intBuffer);

                        //writing I_12
                        channel.position(24);
                        intBuffer.clear();
                        intBuffer.putInt(Integer.parseInt(panel.i12.getText()));
                        intBuffer.flip();
                        channel.write(intBuffer);

                        //writing I_16
                        channel.position(28);
                        intBuffer.clear();
                        intBuffer.putInt(Integer.parseInt(panel.i16.getText()));
                        intBuffer.flip();
                        channel.write(intBuffer);

                        //writing I_20
                        channel.position(32);
                        byteBuffer.clear();
                        byteBuffer.put((byte)Integer.parseInt((panel.i20.getText())));
                        byteBuffer.flip();
                        channel.write(byteBuffer);

                        //writing TransformationEntry
                        channel.position(33);
                        byteBuffer.clear();
                        byteBuffer.put((byte)Integer.parseInt((panel.TransformationEntrty.getText())));
                        byteBuffer.flip();
                        channel.write(byteBuffer);

                        //writing I_22
                        channel.position(34);
                        shortBuffer.clear();
                        shortBuffer.putShort((short)Integer.parseInt(panel.skillId2.getText()));
                        shortBuffer.flip();
                        channel.write(shortBuffer);

                        if(entryAdded){
                            channel.truncate((n+1)*24+12);
                        }
                        entryAdded=false;
                        if(entryRemoved){
                            channel.truncate((n+1)*24+12);
                        }
                        entryRemoved=false;

                        for(long i=1;i<n+1;i++){
                            PanelComponents panelr = entryPanels.get((int)i);
            
                            //writing charaid
                            channel.position(24*i+12);
                            shortBuffer.clear();
                            shortBuffer.putShort((short)Integer.parseInt(panelr.charaId.getText()));
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            //writing costume
                            channel.position(24*i+14);
                            shortBuffer.clear();
                            shortBuffer.putShort((short)Integer.parseInt(panelr.costume.getText()));
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            //writing I_04
                            channel.position(24*i+16);
                            shortBuffer.clear();
                            shortBuffer.putShort((short)Integer.parseInt(panelr.i04.getText()));
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            //writing skillid2
                            channel.position(24*i+18);
                            shortBuffer.clear();
                            shortBuffer.putShort((short)Integer.parseInt(panelr.skillId2.getText()));
                            shortBuffer.flip();
                            channel.write(shortBuffer);
                            
                            //writing characode
                            channel.position(24*i+20);
                            intBuffer.clear();
                            intBuffer.position(0);
                            intBuffer.putChar((panelr.charaCode.getText().charAt(0)));
                            intBuffer.position(1);
                            intBuffer.putChar((panelr.charaCode.getText().charAt(1)));
                            intBuffer.position(2);
                            intBuffer.putChar((panelr.charaCode.getText().charAt(2)));
                            intBuffer.flip();
                            channel.write(intBuffer);
                    
                            //writing I_12
                            channel.position(24*i+24);
                            intBuffer.rewind();
                            intBuffer.putInt(Integer.parseInt(panelr.i12.getText()));
                            intBuffer.flip();
                            channel.write(intBuffer);
                        
                            //writing I_16
                            channel.position(24*i+28);
                            intBuffer.rewind();
                            intBuffer.putInt(Integer.parseInt(panelr.i16.getText()));
                            intBuffer.flip();
                            channel.write(intBuffer);
                    
                            //writing I_20
                            channel.position(24*i+32);
                            byteBuffer.clear();
                            byteBuffer.put((byte)Integer.parseInt((panelr.i20.getText())));
                            byteBuffer.flip();
                            channel.write(byteBuffer);

                            //writing TransformationEntry
                            channel.position(24*i+33);
                            byteBuffer.clear();
                            byteBuffer.put((byte)Integer.parseInt((panelr.TransformationEntrty.getText())));
                            byteBuffer.flip();
                            channel.write(byteBuffer);

                            //writing I_22
                            channel.position(24*i+34);
                            shortBuffer.clear();
                            shortBuffer.putShort((short)Integer.parseInt(panelr.i22.getText()));
                            shortBuffer.flip();
                            channel.write(shortBuffer);
                        }
                    }catch(NumberFormatException a){
                        JOptionPane.showMessageDialog(jframe, "Text field value not read");
                    }
                }catch(IOException e){
                    JOptionPane.showMessageDialog(jframe, "Can't save file");
                } 
                
            });
           
        }
        if(detect.equals("New Entry    Crtl+I")){
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
        if(detect.equals("Remove Entry    Delete")){
            if(n!=0){
                jentries.remove(entryPasteContainer);
                entryPanels.remove(entryPasteContainer);
                jcardPanel.remove(entryPasteContainer);
                
                n-=1;
                for(int i=0;i<n+1;i++){
                    jentries.getItem(i).setText("Entry "+i);
                    jcardPanel.add(entryPanels.get(i),"Entry "+i); 
                }
                entryRemoved=true;
            }
        }
        if(detect.equals("Copy Entry    Ctrl+C")){
            try {
                clipBoard = clipBoard.CopyData(entryPanels.get(entryCopyContainer));
                copyEntryCheck=false;
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(jframe, "Can't copy empty fields");
            }   
        }
        if(detect.equals("Paste Entry    Ctrl+V")){
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
  
    private void setOpenFileLocation(File file){
        if(file!=null)
        this.chosenDirectory=file.getParentFile();
    }
    private void resetEntries() {
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
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int key=e.getKeyCode();
        switch (key) {
            case 73 :
                n+=1;
                JMenuItem nEntry=new JMenuItem("Entry "+n);
                jentries.add(nEntry);
                nEntry.addActionListener(this);

                PanelComponents newEntryPanel = new PanelComponents();
                entryPanels.add(newEntryPanel);
                jcardPanel.add(newEntryPanel, "Entry " + n);

                entryAdded=true;
                break;
            case 127:
                if(n!=0){
                    jentries.remove(entryPasteContainer);
                    entryPanels.remove(entryPasteContainer);
                    jcardPanel.remove(entryPasteContainer);
                    
                    n-=1;
                    for(int i=0;i<n+1;i++){
                        jentries.getItem(i).setText("Entry "+i);
                        jcardPanel.add(entryPanels.get(i),"Entry "+i); 
                    }
                    entryRemoved=true;
                }
                break;
            case 67:
                try {
                    clipBoard = clipBoard.CopyData(entryPanels.get(entryCopyContainer));
                    copyEntryCheck=false;
                    
                } catch (NumberFormatException ev) {
                    JOptionPane.showMessageDialog(jframe, "Can't copy empty fields");
                }  
                break; 
            case 86:
                if(clipBoard==null){
                    JOptionPane.showMessageDialog(jframe, "Nothing Copied");
                }
                clipBoard.PasteData(entryPanels.get(entryPasteContainer), clipBoard);
                entryCopyContainer=entryPasteContainer;
                copyEntryCheck=true; 
                break;
            default:
                break;
        }
        
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
}