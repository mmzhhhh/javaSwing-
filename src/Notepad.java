import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Notepad extends JFrame implements ActionListener {
    //文本框
    JTextArea jTextArea;

    //滚动条
    JScrollPane jScrollPane;

    //构建菜单栏
    JMenuBar jMenuBar;

    //文件目录
    JMenu file;
    JMenu edit;
    JMenu help;

    //文件三个子目录
    JMenuItem save;
    JMenuItem open;
    JMenuItem exit;
    JMenuItem newFile;
    JMenuItem saveAnother;


    //追踪当前文件编辑路径，如果相同则直接保存，不相同则实现另保存
    String currentFilePath;

    //构建默认函数
    Notepad(){
        jTextArea=new JTextArea();

        //为文本框放置滚动条
        jScrollPane=new JScrollPane(jTextArea);
        jMenuBar=new JMenuBar();

        //初始化目录和五个子目录
        file=new JMenu("文件(F)");
        edit=new JMenu("编辑(E)");
        help=new JMenu("帮助(H)");
        save=new JMenuItem("保存(S)");
        open=new JMenuItem("打开(O)");
        exit=new JMenuItem("退出(X)");
        newFile=new JMenuItem("新建(N)");
        saveAnother=new JMenuItem("另存为(A)");

        //设立快捷键助记符
        file.setMnemonic('F');
        save.setMnemonic('S');
        open.setMnemonic('O');
        exit.setMnemonic('X');
        newFile.setMnemonic('N');
        saveAnother.setMnemonic('A');
        //为窗体添加菜单
        this.setJMenuBar(jMenuBar);
        jMenuBar.add(file);
        jMenuBar.add(edit);
        jMenuBar.add(help);
        file.add(newFile);
        file.add(save);
        file.add(saveAnother);
        file.add(open);
        file.add(exit);


        //为子目录添加监听器
        //关联字符串命令
        save.setActionCommand("save");
        save.addActionListener(this);//为当前对象添加动作事件监听器
        open.setActionCommand("open");
        open.addActionListener(this);
        exit.setActionCommand("exit");
        exit.addActionListener(this);
        newFile.setActionCommand("newFile");
        newFile.addActionListener(this);
        saveAnother.setActionCommand("saveAnother");
        saveAnother.addActionListener(this);

        this.add(jScrollPane);
        this.setBounds(300,200,400,300);
        this.setTitle("记事本");
        //设置点击关闭窗口时终止程序
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

    }

    //监听动作事件命令
    @Override
    public void actionPerformed(ActionEvent e) {
       if(e.getActionCommand().equals("open")){
           openFile();
       }else if(e.getActionCommand().equals("save")){
           saveFile();
       }else if(e.getActionCommand().equals("exit")){
           System.exit(0);
       }else if(e.getActionCommand().equals("newFile")){
           newFile();
       }else if(e.getActionCommand().equals("saveAnother")){
           saveAnother();
       }
    }

    //save保存方法
    private void saveFile(){
        //通过判断当前追踪路径是否为空来判断此文件是否在打开的状态下编辑，如果是，则不会弹出保存文件选择框，反之亦然
        if(currentFilePath!=null&&!currentFilePath.isEmpty()){
            saveToFile(currentFilePath);
        }else{
            //创建文件选择框并指定选择文件类型
            JFileChooser fileChooser=jFileChooser();

            //独立窗口显示文件选择框并返回返回值
            int result=fileChooser.showSaveDialog(null);

            //判断用户是否选择了保存操作
            if(result==JFileChooser.APPROVE_OPTION){

                //获取用户在文件选择框中选择的文件，并赋值给fileSave
                File fileSave=fileChooser.getSelectedFile();

                //更新当前文件路径
                currentFilePath=fileSave.getAbsolutePath();

                saveToFile(currentFilePath);
            }
        }
    }

    //新建记事本方法
    private void newFile(){
        jTextArea.setText("");
        //追踪目录路径设为空
        currentFilePath=null;
    }

    //文件另存为方法
    private void saveAnother() {

        //创建文件选择框，并指定选择类型
        JFileChooser jFileChooser = new JFileChooser();

        //独立窗口显示文件选择框并返回返回值
        int result = jFileChooser.showOpenDialog(null);
        //检查用户是否在文件选择对话框中执行了确定操作
        if (result == JFileChooser.APPROVE_OPTION) {
            //追踪目录路径设为空
            currentFilePath = null;
            saveFile();
        }
    }

    //打开文件方法
    private void openFile(){

        //创建文件选择框并指定只能选择的文件类型
        JFileChooser jFileChooser=jFileChooser();

        //显示文件选择框并等待用户下一步操作
        int result=jFileChooser.showOpenDialog(null);

        //判断用户是否选择了打开操作
        if(result==JFileChooser.APPROVE_OPTION){
            //创建文件
            File fileOpen=jFileChooser.getSelectedFile();
            //更新追踪路径
            currentFilePath=fileOpen.getAbsolutePath();
            try{
                //创建从FileReader对象打开文件
                FileReader reader=new FileReader(currentFilePath);
                //创建文件读取类读取对象文本数据
                BufferedReader bufferedReader=new BufferedReader(reader);
                String line;

                //清空当前文本框
                jTextArea.setText("");

                while ((line=bufferedReader.readLine())!=null){
                    //添加读取到的行和换行符
                    jTextArea.append(line+System.getProperty("line.separator"));
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    //文件保存
    private void saveToFile(String currentFilePath){
        try{
            //创建文件输出流,并指定默认的平台编码UTF-8
            FileOutputStream fileOutputStream=new FileOutputStream(currentFilePath);
            OutputStreamWriter ow=new OutputStreamWriter(fileOutputStream);

            //获取文本框的内容写入文件
            ow.write(jTextArea.getText());
            ow.flush();
            ow.close();
        }catch (IOException e){ //处理IO异常
            e.printStackTrace();
        }
    }


    //创建文件选择类型器
    private JFileChooser jFileChooser(){
        //追踪目录路径设为空
        //创建文件选择框，并指定选择类型
        JFileChooser jFileChooser=new JFileChooser();
        FileNameExtensionFilter fileNameExtensionFilter=new FileNameExtensionFilter("文本文件 (*.txt)","txt");
        jFileChooser.setFileFilter(fileNameExtensionFilter);
        jFileChooser.setDialogTitle("选择文件");

        return jFileChooser;
    }

    public static void main(String[] args) {
        new Notepad();
    }
}
